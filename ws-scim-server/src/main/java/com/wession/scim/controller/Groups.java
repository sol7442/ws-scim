package com.wession.scim.controller;

import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import org.slf4j.Logger;

import com.wession.common.ScimUtils;
import com.wession.common.WessionLog;
import com.wession.scim.Const;
import com.wession.scim.exception.ScimFilterException;
import com.wession.scim.intf.schemas_name;
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

public class Groups implements schemas_name {
	private MemRepository repo = new MemRepository().getInstance();
	private Logger processLog = new WessionLog().getProcessLog();
	
	public String getGroup(Request request, Response response) {
		
		JSONObject group = repo.getGroup(request.params(":id"));
		response.status(200);
		return group.toJSONString();
	}
	
	public JSONObject getGroup(String id) {
		
		JSONObject group = repo.getGroup(id);
		return group;
	}
	
	public String getGroupParam(Request request, Response response) {
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
				JSONArray filtered = getGroupFilter(request, response);
				resp = ScimUtils.getAttributes(filtered.iterator(), keys);
			} else {
				resp = ScimUtils.getAttributes(repo.iterator("Groups"), keys);
			}
		} else if (query.hasKey("filter")) {
			resp.setResources(getGroupFilter(request, response));
		} 
		response.status(200);
		return resp.toJSONString(startIdx, count);
	}
	
	public String createGroup(Request request, Response response) {
		JSONObject json = (JSONObject) JSONValue.parse(request.body());
		JSONArray schemas = (JSONArray) json.get(_group_schemas);

		if (schemas == null) {
			response.status(400);
			Error err = new Error(400, "no schemas.");
			return err.toJSONString();
		}

		if (!ScimUtils.checkSchemas(schemas, Const.schemas_v20_group)) {
			response.status(400);
			Error err = new Error(400, "Wrong schemas. Schemas must contains " + Const.schemas_v20_group);
			return err.toJSONString();
		}
		
		json.remove("schemas");
		json.remove("id");
		json.remove("meta");
		
		Group group = new Group();
		
		// displayName은 required 사항
		if (!json.containsKey(_group_name_display)) {
			Error err = new Error(400, "displayName is required.");
			return err.toJSONString();
		}
		String groupName = json.getAsString(_group_name_display).trim();
		if ("".equals(groupName)) {
			Error err = new Error(400, "empty displayName is not allowed.");
			return err.toJSONString();
		}

		// 기존에 동일한 이름이 있는지 확인함
		{
			Iterator itor = repo.iterator("Groups");
			while (itor.hasNext()) {
				JSONObject itor_group = (JSONObject) itor.next();
				String itor_group_displayname = itor_group.getAsString(_group_name_display);
				if (itor_group_displayname.equals(groupName)) {
					Error err = new Error(200,
							"same displayname(" + groupName + ") found. displayname must be unique.");
					return err.toJSONString();
				}
			}
		}

		group.put(_group_name_display, groupName);
		repo.addGroup(group.toJSONObject());

		processLog.info("CREATE GROUP : " + group.getAsString("id") + " / " + groupName);
		
		// members가 있다면 같이 추가함
		if (json.containsKey(_group_members)) {
			JSONArray arry = (JSONArray) json.get(_group_members);
			Iterator itor = arry.iterator();
			while (itor.hasNext()) {
				try {
					JSONObject member = (JSONObject) itor.next();
					String uid = member.getAsString(_group_member_id);
					JSONObject user_json = repo.getUser(uid);
					User user = new User(user_json);
					user.addGroup(group);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			processLog.info("ADD GROUP [" + groupName + "] Member(s) " + ((JSONArray) group.get(_group_members)).size());
		}
		
		// 기타 다른 내용이 있는지 확인하여 추가함
		Set<String> keys = json.keySet();
		for (String key : keys) {
			if (!key.equals(_group_name_display) && !key.equals(_group_members))
				group.put(key, json.get(key));
		}
		
		group.updateModifyTime();
		repo.save();
		
		response.status(200);

		return group.toJSONString();
	}

	public String updateGroup(Request request, Response response) {
		String sysid = request.params(":id");
		JSONObject target = repo.getGroup(sysid);
		JSONObject json = (JSONObject) JSONValue.parse(request.body());
		// displayName 변경
		if (json.containsKey(_group_name_display)) {
			String old_value = (String) target.getOrDefault(_group_name_display, "");
			target.put(_group_name_display, json.getAsString(_group_name_display));
			processLog.info("Update Group[" + sysid + "] - from old [" + old_value + "] to new [" +  json.getAsString(_group_name_display) + "]");

			//TODO member에 대한 처리 필요
			Group group = new Group(target);
			group.updateModifyTime();
			repo.save();
		}
		

		return target.toJSONString();
	}
	
	public String deleteGroup(Request request, Response response) {
		String sysid = request.params(":id");
		Iterator itor = repo.iterator("Groups");
		String groupName = "";
		boolean isDelete = false;
		while (itor.hasNext()) {
			JSONObject obj = (JSONObject) itor.next();
			String id = obj.getAsString("id");
			if (id.equals(sysid)) {
				// 해당되는 사용자도 Group 항목에서 모두 제외해야 함
				JSONArray members = (JSONArray) obj.get(_group_members);
				if (members != null) {
					Iterator itor_member = members.iterator();
					while (itor_member.hasNext()) {
						JSONObject member = (JSONObject) itor_member.next();
						String uid = member.getAsString(_group_member_id);
						User user = new User(repo.getUser(uid));
						user.removeGroup(sysid);
					}
				}
				groupName = obj.getAsString(_group_name_display);
				itor.remove();
				isDelete = true;
				break;
			}
		}
		if (isDelete) {
			processLog.info("DELETE GROUP : " + sysid + " / " + groupName);
			repo.save();
			response.status(204);
		} else {
			response.status(200);
			return new Error(404, "Not found " + sysid).toJSONString();
		}
		return "";
	}
	
	private JSONArray getGroupFilter(Request request, Response response) {
		String params = request.queryParams("filter");
		JSONArray result = null;
		
		try {
			result = new Filter().doFilter(params, "Groups");
		} catch (ScimFilterException e) {
			e.printStackTrace();
		}
		
		return result;
	}
}
