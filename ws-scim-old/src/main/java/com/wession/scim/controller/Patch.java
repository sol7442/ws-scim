package com.wession.scim.controller;

import java.util.ArrayList;
import java.util.Iterator;

import org.slf4j.Logger;

import com.wession.common.ScimUtils;
import com.wession.common.WessionLog;
import com.wession.scim.Const;
import com.wession.scim.Operater;
import com.wession.scim.exception.ScimSchemaException;
import com.wession.scim.schema.Error;
import com.wession.scim.schema.PatchOp;
import com.wession.scim.simple.MemRepository;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import net.minidev.json.parser.ParseException;
import spark.Request;
import spark.Response;

public class Patch {
	private static Logger processLog;
	private static Logger auditLog;
	private static WessionLog wlog = new WessionLog();
	
	private MemRepository repo = new MemRepository().getInstance();
	
	public Patch() {
		processLog = wlog.getProcessLog();
		auditLog = wlog.getAuditLog();
	}
	
	// 개별 그룹에만 적용이 됨 
	public String patchGroup(Request request, Response response) {
		return patch(request, response, "Groups");
	}
	
	public String patchUser(Request request, Response response) {
		return patch(request, response, "Users");
	}
	
	public String patch(Request request, Response response, String resource) {
		MemRepository repo = MemRepository.getInstance();
		String id = request.params(":id");
		JSONObject json = new JSONObject();
		JSONObject patch = new JSONObject();
		try {
			json = (JSONObject) JSONValue.parse(request.body());
			if (json == null) {
				Error err = new Error(400, "JSON parser error on request body");
				return err.toJSONString();
			}
			patch = process(resource, id, json);
		} catch (ScimSchemaException sse) {
			response.status(400);
			Error err = new Error(400, sse.getMessage());
			return err.toJSONString();
		}
		
		return patch.toJSONString();
	}
	public JSONObject process2(String resource, String scim_id, JSONObject json) throws ScimSchemaException {
		return process(resource, scim_id, json);
	}
	
	private JSONObject process(String resource, String scim_id, JSONObject json) throws ScimSchemaException {
		// schema check
				JSONArray schemas = (JSONArray) json.get("schemas");
				if (schemas == null) {
					throw new ScimSchemaException("no schemas.");
				} else if (!ScimUtils.checkSchemas(schemas, Const.schemas_v20_patch)) {
					throw new ScimSchemaException("Wrong schemas. Schemas must contains " + Const.schemas_v20_patch);
				}
				
				JSONObject obj = repo.getResource(resource, scim_id);
				if (obj == null) {
					return new Error(404, "not find " + resource + " - " + scim_id);
				}
				
				PatchOp patchs = new PatchOp(json);
				int size = patchs.size();

				for (int idx = 0; idx < size; idx++) {
					JSONObject patch = patchs.getPatchOne(idx);
					processLog.debug("PatchOp[" + idx + "] : " + patch.toJSONString());
					// op : add, replace, remove
					// path : "name.familyName 도 되고, members[value eq \"2819c223-7f76-453a-919d-413861904646\"] 도 된다.
					// value : -> 있을 수도 있고, 없을 수도 있음
					
					String op = patch.getAsString("op").toLowerCase();
					String path = patch.getAsString("path"); // Resource의 attribute name
					Object data = obj.get(path);
					Object value = patch.get("value"); // String 이나 JSONArray 등 다양한 것이 될 수 있음
					
					String data_type = ScimUtils.getJSONType(data);
					String value_type = ScimUtils.getJSONType(value);

//					System.out.println("data_type : " + data_type);
					
					if ("new_one".equals(data_type)) data_type = value_type;
					
					// JSONArray인 경우에 한하여 진행
					if ("add".equals(op)) {
						if ("array".equals(data_type)) {
							if ("array".equals(value_type)) { // array라면 2개의 array를 머지해야 함, 단 동일한 데이터가 있으면 안됨
								Iterator itor_value = ((JSONArray) value).iterator();
								while (itor_value.hasNext()) {
									Object io_value = itor_value.next();
									addArray((JSONArray)data, "value", io_value);
								}
							} else {
								obj.put(path, value);
							}
						} else { // 기존데이터가 있으면 오류처리
							if (data != null) {
								obj.put(path, value);
								processLog.debug("PatchOp[{}] : add attribute[{}] to value[{}] ", idx, path, value);
							} else {
								processLog.debug("PatchOp[{}] : Already attribute[{}] set value[{}]. ", idx, path, value);
							}
						}
					} else if ("replace".equals(op)) {
						if ("array".equals(data_type)) {
							if ("array".equals(value_type)) { // array라면 2개의 array를 머지해야 함, 단 동일한 데이터가 있으면 안됨
								Iterator itor_value = ((JSONArray) value).iterator();
								while (itor_value.hasNext()) {
									Object io_value = itor_value.next();
									// 삭제후 다시 만들기
									String replace_key = ((JSONObject) io_value).getAsString("value");
									changeArray((JSONArray)data, "value", io_value);
									processLog.debug("PatchOp[" + idx + "] : change key : " + replace_key);
								}
							} else {
								obj.put(path, value);
							}
						} else if ("string".equals(data_type)) {
							// replace는 new_data, old_data가 배열형식으로 입력됨, 순서는 new가 1번째, old가 2번째
							// 만약 value가 1개라면 바로 바꿔치기를 함
							if ("array".equals(value_type)) {
								int value_size = ((JSONArray) value).size();
								if (value_size == 1) {
									obj.put(path, ((JSONArray) value).get(0));
									processLog.debug("PatchOp[" + idx + "] : replace " + path + "[" +  ((JSONArray) value).get(0) + "]");
								} else if (value_size == 2) {
									// 이전데이터와 비교하여 입력
									Object new_value = ((JSONArray) value).get(0);
									Object old_value = ((JSONArray) value).get(1);
									System.out.println(old_value + " -> " + new_value);
								}
							} else {
								obj.put(path, value);
								processLog.debug("PatchOp[" + idx + "] : replace " + path + "[" +  value+ "]");
							}
						}
					} else if ("remove".equals(op)) {
						if ("array".equals(data_type)) {
							if ("array".equals(value_type)) { // array라면 2개의 array를 머지해야 함, 단 동일한 데이터가 있으면 안됨
								Iterator itor_value = ((JSONArray) value).iterator();
								while (itor_value.hasNext()) {
									Object io_value = itor_value.next();
									// 전체에서 삭제함
									String remove_key = ((JSONObject) io_value).getAsString("value");
									processLog.debug("PatchOp[" + idx + "] : remove key : " + remove_key);
									deleteArray((JSONArray) data, "value", remove_key);
								}
							} else {
								obj.remove(path);
							}
						} else { // 지우는 것은 데이터 타입과 상관없음
							obj.remove(path);
						}
					} else { // 적절한 op가 아님
						obj.remove(path);
					}
				}

				// Patch 결과는 path만 돌려준다?
				
		return obj;
	}
	
	private void deleteArray(JSONArray target, String key, Object value) {
		String new_one = "";
		String value_type = ScimUtils.getJSONType(value);
		
		if ("json".equals(value_type)) {
			new_one = ((JSONObject) value).getAsString(key);
		} else if ("string".equals(value_type)) {
			new_one = (String) value;
		}
		
		Iterator itor = target.iterator();
		while (itor.hasNext()) {
			Object obj = itor.next();
			String obj_type = ScimUtils.getJSONType(obj);
			if ("json".equals(obj_type)) {
				String compare_value = ((JSONObject) obj).getAsString(key);
				if (new_one.equals(compare_value)) {
					itor.remove();
					break;
				}
			} else if ("string".equals(obj_type)) {
				String compare_value = (String) obj;
				if (new_one.equals(compare_value)) {
					itor.remove();
					break;
				}
			} else if ("number".equals(obj_type)) {
				String compare_value = obj+"";
				if (new_one.equals(compare_value)) {
					itor.remove();
					break;
				}
			}
			
		}
	}
	
	private void addArray(JSONArray target, String key, Object value) {
		String new_one = "";
		String value_type = ScimUtils.getJSONType(value);
		
		if ("json".equals(value_type)) {
			new_one = ((JSONObject) value).getAsString(key);
		} else if ("string".equals(value_type)) {
			new_one = (String) value;
		}
		
		Iterator itor = target.iterator();
		boolean absense = false;
		while (itor.hasNext()) {
			Object obj = itor.next();
			String obj_type = ScimUtils.getJSONType(obj);
			if ("json".equals(obj_type)) {
				String compare_value = ((JSONObject) obj).getAsString(key);
				if (new_one.equals(compare_value)) {
					absense = true;
					break;
				}
			} else if ("string".equals(obj_type)) {
				String compare_value = (String) obj;
				if (new_one.equals(compare_value)) {
					absense = true;
					break;
				}
			} else if ("number".equals(obj_type)) {
				String compare_value = obj+"";
				if (new_one.equals(compare_value)) {
					absense = true;
					break;
				}
			}
		}
		if (!absense) ((JSONArray) target).add(value);
	}
	
	private void changeArray(JSONArray target, String key, Object value) {
		String new_one = "";
		String value_type = ScimUtils.getJSONType(value);
		
		if ("json".equals(value_type)) {
			new_one = ((JSONObject) value).getAsString(key);
		} else if ("string".equals(value_type)) {
			new_one = (String) value;
		}
		
		Iterator itor = target.iterator();
		boolean present = false;
		
		while (itor.hasNext()) {
			Object obj = itor.next();
			String obj_type = ScimUtils.getJSONType(obj);
			if ("json".equals(obj_type)) {
				String compare_value = ((JSONObject) obj).getAsString(key);
				if (new_one.equals(compare_value)) {
					itor.remove();
					present = true;
					break;
				}
			} else if ("string".equals(obj_type)) {
				String compare_value = (String) obj;
				if (new_one.equals(compare_value)) {
					itor.remove();
					present = true;
					break;
				}
			} else if ("number".equals(obj_type)) {
				String compare_value = obj+"";
				if (new_one.equals(compare_value)) {
					itor.remove();
					present = true;
					break;
				}
			}
			
		}
		if (present) ((JSONArray) target).add(value);
	}

}
