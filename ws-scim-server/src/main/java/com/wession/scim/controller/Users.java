package com.wession.scim.controller;

import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import org.omg.PortableInterceptor.USER_EXCEPTION;
import org.slf4j.Logger;

import com.wession.common.ScimUtils;
import com.wession.common.WessionLog;
import com.wession.scim.Const;
import com.wession.scim.exception.ScimFilterException;
import com.wession.scim.intf.schemas_name;
import com.wession.scim.policy.Restrict;
import com.wession.scim.resource.Group;
import com.wession.scim.resource.User;
import com.wession.scim.schema.Error;
import com.wession.scim.schema.ListResponse;
import com.wession.scim.simple.MemRepository;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;

public class Users implements schemas_name {
	private MemRepository repo = new MemRepository().getInstance();
	private Logger processLog = new WessionLog().getProcessLog();
	private Logger auditLog = new WessionLog().getAuditLog();
	
	public String getUser(Request request, Response response) {
		JSONObject user = repo.getUser(request.params(":id"));

		response.status(200);
		return user.toJSONString();
	}
	
	public String getUserParam(Request request, Response response) {
		QueryParamsMap query = request.queryMap();
		if (query == null) {
			response.status(400);
			Error err = new Error(400, "no paramertes.");
			return err.toJSONString();
		}

		ListResponse resp = new ListResponse();
		// page count
		int count = ScimUtils.getInteger(query, "count", 200);
		// offset
		int startIdx = ScimUtils.getInteger(query, "startIndex", 1);

		if (query.hasKey("attributes")) {
			String att_key = query.value("attributes");
			Vector<String> keys = ScimUtils.toVector(att_key, ",");
			if (query.hasKey("filter")) {
				JSONArray filtered = getUserFilter(request, response);
				resp = ScimUtils.getAttributes(filtered.iterator(), keys);
			} else {
				resp = ScimUtils.getAttributes(repo.iterator("Users"), keys);
			}
		} else if (query.hasKey("filter")) {
			resp.setResources(getUserFilter(request, response));
		} 
		response.status(200);
		return resp.toJSONString(startIdx, count);
	}

	public JSONArray getUserFilter(Request request, Response response) {
		String params = request.queryParams("filter");
//		if (params == null || "".equals(params.trim())) {
//			response.status(400);
//			Error err = new Error(400, "filter param is not allowed empty.");
//			return err;
//		}
		JSONArray result = null;
		
		try {
			result = new Filter().doFilter(params, "Users");
		} catch (ScimFilterException e) {
			e.printStackTrace();
		}
		
		return result;
	}

	public String createUser(Request request, Response response) {
		JSONObject json = (JSONObject) JSONValue.parse(request.body());
		//System.out.println(json.toJSONString());

		response.status(200);
		
		JSONArray schemas = (JSONArray) json.get("schemas");
		if (schemas == null) {
			response.status(400);
			Error err = new Error(400, "no schemas.");
			return err.toJSONString();
		}

		if (!ScimUtils.checkSchemas(schemas, Const.schemas_v20_user)) {
			response.status(400);
			Error err = new Error(400, "Wrong schemas. Schemas must contains " + Const.schemas_v20_user);
			return err.toJSONString();
		}

		json.remove("schemas");
		json.remove("id");
		json.remove("meta");

		String json_user_userName = json.getAsString(_user_username);
		String json_user_externalId = json.getAsString(_user_external_id);

		if (json_user_userName == null || "".equals(json_user_userName)) {
			response.status(400);
			Error err = new Error(400, "userName is required.");
			processLog.error(err.toJSONString());
			return err.toJSONString();
		}

		if (json_user_externalId == null || "".equals(json_user_externalId) ) {
			response.status(400);
			Error err = new Error(400, "externalId is required.");
			processLog.error(err.toJSONString());
			return err.toJSONString();
		}

		if (!Restrict.checkExternalId(json_user_externalId)) {
			response.status(400);
			Error err = new Error(400, "externalId was in violation of the policy. " + json_user_externalId);
			processLog.error(err.toJSONString());
			return err.toJSONString();
		}
		
		// 기존 externalId 및 userName은 unique해야 함
		{
			Iterator itor = repo.iterator("Users");

			while (itor.hasNext()) {
				JSONObject itor_user = (JSONObject) itor.next();
				String itor_user_userName = itor_user.getAsString(_user_username);
				String itor_user_externalId = itor_user.getAsString(_user_external_id);

				if (json_user_userName.equals(itor_user_userName)) {
					response.status(400);
					Error err = new Error(400, "displayName must be unique. " + json_user_userName);
					processLog.error(err.toJSONString());
					return err.toJSONString();
				}

				if (json_user_externalId.equals(itor_user_externalId)) {
					response.status(400);
					Error err = new Error(400, "externalId must be unique. " + json_user_externalId);
					processLog.error(err.toJSONString());
					return err.toJSONString();
				}
			}
		}

		User user = new User(json_user_externalId, json_user_userName);

		// 전체를 돌려서 입력함
		Set<String> keys = json.keySet();
		for (String key : keys) {
			if (!key.equals(_user_groups))
				user.put(key, json.get(key));
		}

		repo.addUser(user.toJSONObject());
		processLog.info(
				"CREATE User : " + user.getAsString("id") + " / " + json_user_userName + " / " + json_user_externalId + 
				"\n\t" + json.toJSONString());
		
		auditLog.info("user[{}][{}][{}] added from IP[{}]",user.getAsString("id"), json_user_externalId, json_user_userName, request.ip());

		// Group정보가 있다면 입력함
		JSONArray arry = (JSONArray) json.get(_user_groups);
		if (arry != null) {
			Iterator itor = arry.iterator();
			while (itor.hasNext()) {
				JSONObject group = ((JSONObject) itor.next());
				String group_id = group.getAsString(_user_groups_id);
				user.addGroup(new Group(repo.getGroup(group_id)));
				processLog.info("ADD GROUP Member [" + json_user_userName + "] to GROUP[" + group_id + "]");
				auditLog.info("user[{}][{}] assigned to group[{}] from IP[{}]",user.getAsString("id"), user.getAsString(_user_external_id), group_id, request.ip());
			}
			
			
		}

		user.updateModifyTime();
		repo.save();

		return user.toJSONString();
	}

	public String updateUser(Request request, Response response) {
		response.status(200);
		StringBuilder sb = new StringBuilder();
		
		User user = new User(repo.getUser(request.params(":id")));
		JSONObject data = (JSONObject) JSONValue.parse(request.body());

		Iterator<String> itor = data.keySet().iterator();
		while (itor.hasNext()) {
			String key = itor.next();
			if (!"schemas".equals(key) && !"id".equals(key) && !"groups".equals(key)) {
				Object value = data.get(key);
				if (value != null && value instanceof JSONArray) {
					JSONArray arry = (JSONArray) value;
					if (arry.size() == 0) {
//						user.remove(key);
						user.put(key, null);
						sb.append("\tremove attribute : ").append(key).append("\n");
					} else {
						user.put(key, value);
						sb.append("\tupdate attribute : ").append(key).append(":").append(arry.toJSONString()).append("\n");
					}
				} else {
					user.put(key, value);
					sb.append("\tupdate attribute : ").append(key).append(":").append(value).append("\n");
				}
			}
		}
		
		// Group정보가 있다면 입력함
		JSONArray arry = (JSONArray) data.get("groups");
		if (arry == null) arry = new JSONArray();
		// 싹 밀고 다시 시작
		Vector <String> org_gid = new Vector <String>();
		
		JSONArray user_group = (JSONArray) user.get("groups");
		if (user_group == null) user_group = new JSONArray();
		Iterator itor_ugr = user_group.iterator();
		while (itor_ugr.hasNext()) {
			String gid = ((JSONObject) itor_ugr.next()).getAsString("value");
			org_gid.add(gid);
			Group tgr = new Group(repo.getGroup(gid));
			tgr.removeMember(request.params(":id"));
		}
		
		
		if (arry != null) {
			if (arry.size() > 0) {
				//System.out.println(arry.toJSONString());
				Iterator itor2 = arry.iterator();
				while (itor2.hasNext()) {
					JSONObject obj2 = (JSONObject) itor2.next();
					String group_id = obj2.getAsString(_group_member_id);
					if (group_id != null && !"".equals(group_id)) {						//System.out.println("Add Group : " + group_id + " / " + user.getAsString(_user_username));
						user.addGroup(new Group(repo.getGroup(group_id)));
						if (org_gid.contains(group_id)) {
							// 기존에 있었으므로 필요없음
							org_gid.remove(group_id);
						} else {
							auditLog.info("user[{}][{}] assigned to group[{}] from IP[{}]",user.getAsString("id"), user.getAsString(_user_external_id), group_id, request.ip());
							org_gid.remove(group_id);
						}

					} else {
						itor2.remove();
					}
				}
			} else { // 이건 데이터가 지워져서 들어온것일까?
				
			}
		}
		
		// 없어진 그룹 정보(Vector) 중 남은 것은 실제 지워진 groupid 임
		for (String remove_gid: org_gid) {
			auditLog.info("user[{}][{}] deassigned to group[{}] from IP[{}]",user.getAsString("id"), user.getAsString(_user_external_id), remove_gid, request.ip());
		}
		
		user.put("groups", arry);
		processLog.info("Update User[" + user.getAsString(_user_external_id)+ "] \n\t" + user.toJSONString());
		auditLog.info("user[{}][{}] updated from IP[{}]",user.getAsString("id"), user.getAsString(_user_external_id), request.ip());
		auditLog.info("user[{}][{}] updated details\n{}",user.getAsString("id"), user.getAsString(_user_external_id), sb.toString());
		
		user.updateModifyTime();
		repo.save();

		return user.toJSONString();
	}

	public String patchUser(Request request, Response response) {
		return "";
	}

	public String deleteUser(Request request, Response response) {
		String sysid = request.params(":id");
		Iterator itor = repo.iterator("Users");
		String externalId = "";
		String username = "";
		boolean isDelete = false;
		while (itor.hasNext()) {
			Object obj = itor.next();
			String id = ((JSONObject) obj).getAsString("id");
			if (id.equals(sysid)) {
				// groups를 정리함
				JSONArray groups = (JSONArray) ((JSONObject) obj).get("groups");
				if (groups != null) {
					Iterator itors = groups.iterator();
					while (itors.hasNext()) {
						JSONObject grp = (JSONObject) itors.next();
						String gid = grp.getAsString("value");
						if (gid != null) { 
							Group group = new Group(repo.getGroup(gid));
							group.removeMember(sysid);
						}
					}
				}
				externalId = ((JSONObject) obj).getAsString(_user_external_id);
				username = ((JSONObject) obj).getAsString(_user_username);
				itor.remove();
				isDelete = true;
				break;
			}
		}
		if (isDelete) {
			processLog.info("DELETE User : " + externalId + " / " + sysid);
			auditLog.info("user[{}][{}][{}] deleted from IP[{}]", sysid, externalId, username, request.ip());
			
			repo.save();
			response.status(204);
			return ("DELETE User : " + externalId + " / " + sysid);
		} else {
			response.status(200);
			return new Error(404, "Not found " + sysid).toJSONString();
		}
	}
}
