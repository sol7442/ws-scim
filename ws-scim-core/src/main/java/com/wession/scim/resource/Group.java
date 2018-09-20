package com.wession.scim.resource;

import java.io.Serializable;
import java.util.Iterator;

import com.wession.common.ScimUtils;
import com.wession.scim.Const;
import com.wession.scim.intf.schemas_name;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

public class Group extends JsonModel implements Serializable, schemas_name {
	public Group() {
		obj = new JSONObject();
		JSONArray schemas = new JSONArray();
		{
			schemas.add(Const.schemas_v20_group);
		}
		obj.put(_group_schemas, schemas);
		obj.put(_group_id, ScimUtils.makeId());
		obj.put("displayName", "Empty Group");
		obj.put("meta", new Meta("Group").toJSONObject());
		obj.put("members", new JSONArray());
		
	}
	
	public Group(String json) {
		obj = (JSONObject) JSONValue.parse(json);
	}
	
	public Group(JSONObject o) {
		obj = o;
	}
	
	public Group(String display, Meta meta) {
		obj = new JSONObject();
		JSONArray schemas = new JSONArray();
		{
			schemas.add(Const.schemas_v20_group);
		}
		obj.put("schemas", schemas);
		obj.put("id", ScimUtils.makeId());
		obj.put("displayName", display);
		obj.put("meta", meta.toJSONObject());
		obj.put("members", new JSONArray());
	}
	
	public void addMember(Member member) {
		JSONArray members = (JSONArray) obj.get("members");
		if (members == null) {
			members = new JSONArray();
			obj.put("members", members);
		}
		
		String id = member.getAsString("value");
		Iterator itor = members.iterator();
		while (itor.hasNext()) {
			JSONObject jo = (JSONObject) itor.next();
			if (jo.getAsString("value").equals(id)) itor.remove();
		}
		members.add(member.toJSONObject());
	}
	
	public void removeMember(String id) {
		JSONArray members = (JSONArray) obj.get("members");
		Iterator itor = members.iterator();
		while (itor.hasNext()) {
			JSONObject jo = (JSONObject) itor.next();
			if (jo.getAsString("value").equals(id)) itor.remove();
		}
	}
	
	public void removeMember(Member member) {
		removeMember(member.getAsString("value"));
	}
	
	public Member toMember() {
		String display = obj.getAsString("displayName");
		return new Member(obj.getAsString("id"), "Group", display);
	}
	
	public JSONObject getGroup() {
		Meta meta = new Meta((JSONObject) obj.get("meta"));
		
		JSONObject groups = new JSONObject();
		groups.put("value", obj.getAsString("id"));
		groups.put("$ref", meta.getAsString("location"));
		groups.put("display", obj.getAsString("displayName"));
		return groups;
	}
	
}
