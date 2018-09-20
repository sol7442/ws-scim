package com.wession.scim.test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Iterator;
import java.util.Set;

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

public class BulkTest {
	private static String getValue(Object obj) {
		String type = ScimUtils.getJSONType(obj);
		String key_value = "";
		if ("array".equals(type)) {
			
		} else if ("json".equals(type)) {
			
		} else {
			
		}
		return key_value;
	}
	
	public static void main(String [] args) throws FileNotFoundException {
		JSONObject bulk_request = (JSONObject) JSONValue.parse(new FileReader("./sample/BulkRequest.json"));
		MemRepository repo = MemRepository.getInstance();
		repo.load();

		BulkRequest bulk = new BulkRequest(bulk_request);
		BulkResponse bulk_resp = new BulkResponse();
		
		System.out.println(bulk_request.toJSONString());
		
		// 비교처리용
		{
			Set <String> keys = bulk_request.keySet();
			for (String key: keys) {
				Object obj = bulk_request.get(key);
				String type = ScimUtils.getJSONType(obj);
				if ("array".equals(type)) {
					System.out.println(type + ":" + key);
				} else if ("json".equals(type)) {
					System.out.println(type + ":" + key);
				} else {
					System.out.println(type + ":" + key);
				}
			}
		}
		
		int bulk_size = bulk.size();

//		if (bulk_size > 1000) {
//			response.status(413);
//			Error err = new Error(413, "The counts of the bulk operation exceeds the maxPayloadCounts " + 1000);
//			return err.toJSONString();
//		}

		
		for (int idx=0; idx<bulk_size; idx++) {
			JSONObject bulk_op = bulk.getBulk(idx);
			String method = bulk_op.getAsString("method").toLowerCase();
			String path = bulk_op.getAsString("path");
			String bulkId = bulk_op.getAsString("bulkId");
			JSONObject data = (JSONObject) bulk_op.get("data");
			
			System.out.println(method);
			System.out.println(path);
			System.out.println(bulkId);
			System.out.println(data);
			
			if (path.startsWith("/Users")) {
				System.out.println(method + " " + path);
				JSONArray schema = (JSONArray) data.get("schemas");
				if (!schema.contains(Const.schemas_v20_user)) {
					System.out.println("ERROR SCHEMA");
					bulk_resp.addError(bulkId, method, "400", null, "invalidSyntax", "Request is unparsable, syntactically incorrect, or violates schema.");
					continue;
				}
								
				if ("post".equals(method)) {
					// 필수입력내역이 있는지 확인
					// externalId, userName
					String externalId = data.getAsString("externalId");
					String userName = data.getAsString("userName");
					
					if (externalId == null || "".equals(externalId) || userName == null || "".equals(userName)) {
						bulk_resp.addError(bulkId, method, "400", null , "invalidValue", "externalId and userName are required.");
						continue;
					}
					
					// 기존에 있는 externalId인지 확인
					if (repo.existUser(externalId, "externalId")) {
						bulk_resp.addError(bulkId, method, "409", null , "uniqueness", "externalId must be unique.");
						continue;
					}
					
					User user = new User(externalId, userName);
					user.put("schemas", new JSONArray());
					user.merge(data);
					user.updateModifyTime();
					repo.addUser(user);
					
					System.out.println("work add : " + user.toJSONString());
					
					// 성공한 경우
					bulk_resp.addResult(bulkId, method, "201", user.getLocation(), user.getVersion());
					
					//TODO 로그 생성
					
					
				} else if ("put".equals(method)) {
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
					Set <String> keys = data.keySet();
					for (String key: keys) {
						if (key.equals("schemas") || key.equals("id")) {
							// id와 schemas는 대상이 아님
						} else {
							user.put(key, data.get(key));
						}
					}
					
					user.updateModifyTime();
					System.out.println("work put : " + user.toJSONString());
					
					// 성공한 경우
					bulk_resp.addResult(bulkId, method, "200", user.getLocation(), user.getVersion());
					
					// 로그 생성
					
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
					System.out.println("work delete : " + id);
					
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
						
						System.out.println(s_op + ":" + s_path + ":" + s_value + ":" + type);
						
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
							}
						} else if ("remove".equals(s_op)) {
							user.remove(s_path);
							
						} else if ("replace".equals(s_op)) {
							Object older = user.get(s_path);
							String older_type = ScimUtils.getJSONType(older);
							System.out.println(">> " + s_op + ":" + s_path + ":type-"+older_type);
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
					System.out.println("work patch : " + user.toJSONString());
							
					bulk_resp.addResult(bulkId, method, "200", user.getLocation(), user.getVersion());

					
				}
			} else if ("/Groups".equals(path)) {
				System.out.println(method + " " + path);
				
				if ("post".equals(method)) {
					// 그룹을 생성함
				} else if ("put".equals(method)) {
					// 그룹을 대체함, 또는 그룹내용 중 일부를 교체함
				} else if ("delete".equals(method)) {
					// 그룹을 삭제함
				} else if ("patch".equals(method)) {
					// 그룹 내용 중 일부교체함, 특히 사용자 변경
				
				}
			}
		}
		
		Iterator itor = repo.iterator("Users");
		while (itor.hasNext()) {
			JSONObject user = (JSONObject) itor.next();
			
//			String userName = user.getAsString("userName");
//			if ("Alice".equals(userName))  {
//				String id = user.getAsString("id");
//				System.out.println("delete id " + id);
//				repo.deleteUser(id);
//			}
			
			
			if (null != user.get("test"))
			System.out.println(user.toJSONString());
		}
		
		System.out.println(bulk_resp.toJSONString());
		// 다 끝났으면 저장
//		repo.save();
	}
}
