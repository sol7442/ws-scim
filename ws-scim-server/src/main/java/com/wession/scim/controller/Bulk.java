package com.wession.scim.controller;

import java.util.Iterator;
import java.util.Set;

import com.wession.common.RESTClient;
import com.wession.common.ScimUtils;
import com.wession.scim.Const;
import com.wession.scim.resource.User;
import com.wession.scim.schema.BulkRequest;
import com.wession.scim.schema.BulkResponse;
import com.wession.scim.schema.Error;
import com.wession.scim.simple.MemRepository;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import spark.Request;
import spark.Response;

public class Bulk {
	private static ServiceProviderConfig conf = ServiceProviderConfig.getInstance();
	
	public String setBulk(Request request, Response response) {
		MemRepository repo = MemRepository.getInstance();
		JSONObject server_config = conf.getServerConfig();
		JSONObject bulk_config = (JSONObject) conf.read("bulk");
		
		int maxCount = (int) bulk_config.getAsNumber("maxOperations");
		int maxPayloadSize = (int) bulk_config.getAsNumber("maxPayloadSize");
		
		int payload = request.contentLength();
		if (payload > maxPayloadSize) {
			response.status(413);
			Error err = new Error(413, "The counts of the bulk operation exceeds the maxPayloadSize " + maxPayloadSize + ".");
			return err.toJSONString();
		}
		
		JSONObject json = new JSONObject();
		try {
			json = (JSONObject) JSONValue.parse(request.body());
		} catch (Exception e) {
			response.status(400);
			return new Error(400, "invalidSyntax", "The request body message structure was invalid or did not conform to the request schema.").toJSONString();
		}
		String base_URL = server_config.getAsString("baseURL");
		
		// schema check
		JSONArray schemas = (JSONArray) json.get("schemas");
		if (schemas == null) {
			response.status(400);
			Error err = new Error(400, "no schemas.");
			return err.toJSONString();
		}

		if (!ScimUtils.checkSchemas(schemas, Const.schemas_v20_bulk_request)) {
			response.status(400);
			Error err = new Error(400, "Wrong schemas. Schemas must contains " + Const.schemas_v20_bulk_request);
			return err.toJSONString();
		}
		
		BulkRequest bulk = new BulkRequest(json);
		BulkResponse bulk_resp = new BulkResponse();
		
		int count = bulk.size();
		
		if (count > maxCount) {
			response.status(413);
			Error err = new Error(413, "The counts of the bulk operation exceeds the maxPayloadCounts " + maxCount + ".");
			return err.toJSONString();
		}
		
		for (int idx=0; idx<count; idx++) {
			JSONObject bulk_op = bulk.getBulk(idx);
			String method = bulk_op.getAsString("method").toLowerCase();
			String path = bulk_op.getAsString("path");
			String bulkId = bulk_op.getAsString("bulkId");
			JSONObject data = (JSONObject) bulk_op.get("data");
//			
//			System.out.println(method);
//			System.out.println(path);
//			System.out.println(bulkId);
//			System.out.println(data);
			
			if (path.startsWith("/Users")) {
				JSONArray schema = (JSONArray) data.get("schemas");
				if (!schema.contains(Const.schemas_v20_user)) {
					//TODO add log
					bulk_resp.addError(bulkId, method, "400", null, "invalidSyntax", "Request is unparsable, syntactically incorrect, or violates schema.");
					continue;
				}
								
				if ("post".equals(method)) {
					// 필수입력내역이 있는지 확인
					// externalId, userName
					String externalId = data.getAsString("externalId");
					String userName = data.getAsString("userName");
					
					if (externalId == null || "".equals(externalId) || userName == null || "".equals(userName)) {
						//TODO add log
						bulk_resp.addError(bulkId, method, "400", null , "invalidValue", "externalId and userName are required.");
						continue;
					}
					
					// 기존에 있는 externalId인지 확인
					if (repo.existUser(externalId, "externalId")) {
						//TODO add log
						bulk_resp.addError(bulkId, method, "409", null , "uniqueness", "externalId must be unique.");
						continue;
					}
					
					User user = new User(externalId, userName);
					user.put("schemas", new JSONArray());
					user.merge(data);
					user.updateModifyTime();
					repo.addUser(user);
					//TODO add log
					
					// 성공한 경우
					bulk_resp.addResult(bulkId, method, "201", user.getLocation(), user.getVersion());
					
				} else if ("put".equals(method)) {
					// 필수입력내역이 있는지 확인
					
					// id
					String id = data.getAsString("id");
					if (id == null) {
						String [] paths = path.split("/");
						if (paths.length == 2) {
							id = paths[1].trim();
						} else {
							//TODO add log
							bulk_resp.addError(bulkId, method, "400", null , "invalidValue", "externalId and userName are required.");
							continue;
						}
					}
					
					JSONObject json_user = repo.getUser(id);
					if (json_user == null) {
						// 찾는 사용자가 없는 경우
						//TODO add log
						bulk_resp.addError(bulkId, method, "404", null , null, "Not found user. " + id);
						continue;
					}
					
					User user = new User(json_user);
					Set <String> keys = data.keySet();
					for (String key: keys) {
						if (key.equals("schemas") || key.equals("id")) {
							// id와 schemas는 대상이 아님
						} else {
							user.put(key, data.get(key));
						}
					}
					
					user.updateModifyTime();
					//TODO addLog
					
					// 성공한 경우
					bulk_resp.addResult(bulkId, method, "200", user.getLocation(), user.getVersion());
					
				} else if ("delete".equals(method)) {
					// 필수입력내역이 있는지 확인
					// id
					String id = data.getAsString("id");
					if (id == null) {
						String [] paths = path.split("/");
						if (paths.length == 2) {
							id = paths[1].trim();
						} else {
							bulk_resp.addError(bulkId, method, "400", null , "invalidValue", "externalId and userName are required.");
							continue;
						}
					}
					
					JSONObject json_user = repo.getUser(id);
					if (json_user == null) {
						// 찾는 사용자가 없는 경우
						bulk_resp.addError(bulkId, method, "404", null , null, "Not found user. " + id);
						continue;
					}
					
					User user = new User(json_user);
					
					String location = user.getLocation();
					
					repo.deleteUser(id);
					//TODO addLog
					
					bulk_resp.addResult(bulkId, method, "204", location, null);
					
				} else if ("patch".equals(method)) {
					// 필수입력내역이 있는지 확인
					// id
					String id = data.getAsString("id");
					if (id == null) {
						String [] paths = path.split("/");
						if (paths.length == 2) {
							id = paths[1].trim();
						} else {
							bulk_resp.addError(bulkId, method, "400", null , "invalidValue", "externalId and userName are required.");
							continue;
						}
					}
					
					JSONObject json_user = repo.getUser(id);
					if (json_user == null) {
						// 찾는 사용자가 없는 경우
						bulk_resp.addError(bulkId, method, "404", null , null, "Not found user. " + id);
						continue;
					}
					
					User user = new User(json_user);
					
					JSONArray Operations = (JSONArray) data.get("Operations");
					for (int i=0; i<Operations.size(); i++) {
						JSONObject patch_json = (JSONObject) Operations.get(i);
						String s_op = patch_json.getAsString("op");
						String s_path = patch_json.getAsString("path");
						Object s_value = patch_json.get("value");
						String type = ScimUtils.getJSONType(s_value);
						
						if ("add".equals(s_op)) {
							if ("array".equals(type)) {
								// 원데이터 Type은 기존에 잘 맞아줘야 함
								JSONArray path_arry = (JSONArray) user.get(s_path);
								path_arry.merge(s_value);
							} else {
								Object older = user.get(s_path);
								if (older != null) {
									bulk_resp.addError(bulkId, method, "400", null , "mutability", "Attribute(" + s_path + ") is not empty.");
									continue;
								}
								user.put(s_path, s_value);
								//TODO Log 생성 
							}
						} else if ("remove".equals(s_op)) {
							user.remove(s_path);
							//TODO Log 생성 
							
						} else if ("replace".equals(s_op)) {
							Object older = user.get(s_path);
							String older_type = ScimUtils.getJSONType(older);

							if ("array".equals(older_type)) {
								JSONArray news_arry =  (JSONArray) s_value;
								JSONObject news_target = (JSONObject) news_arry.get(0);
								
								JSONArray older_arry = (JSONArray) older;
								for (int o1 = 0; o1<older_arry.size(); o1++) {
									JSONObject o1_so = (JSONObject) older_arry.get(o1);
									if (news_target.equals(o1_so)) {
										//System.out.println("WOW~~~~~" + o1_so.toJSONString() + " / " + news_target.toJSONString());
										older_arry.remove(o1);
										older_arry.add((JSONObject) news_arry.get(1));
										//TODO Log 생성 
									} 
								}
							} else if ("json".equals(older_type)) {
								
							} else {
								JSONArray news_arry =  (JSONArray) s_value;
								
								if (news_arry.size() == 1) {
									user.put(s_path, news_arry.get(0));
								} else if (news_arry.size() == 2) {
									// 1번 오브젝트와 비교해야함
									Object news_target = news_arry.get(0);
									if (news_target.equals(user.get(s_path))) {
										//System.out.println("WOW~~~~~" + news_target + " / " + user.get(s_path));
										user.put(s_path, news_arry.get(1));
										//TODO Log 생성
									}
								}
							}
						}
					}
					
					user.updateModifyTime();
							
					bulk_resp.addResult(bulkId, method, "200", user.getLocation(), user.getVersion());

					
				}
			} else if ("/Groups".equals(path)) {
				System.out.println(method + " " + path);
				
				if ("post".equals(method)) {
					
				} else if ("put".equals(method)) {
					
				} else if ("delete".equals(method)) {

				} else if ("patch".equals(method)) {
				
				}
			}
		}
		
		repo.save();
		
		return bulk_resp.toJSONString();
	}

}
