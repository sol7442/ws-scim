package com.wession.scim.resource;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

import com.wession.common.ScimUtils;
import com.wession.common.WessionLog;
import com.wession.scim.Const;

public class User extends JsonModel implements Serializable {
	private Logger processLog = new WessionLog().getProcessLog();
	
	public User() {
		obj = new JSONObject();
		JSONArray schemas = new JSONArray();
		{
			schemas.add(Const.schemas_v20_user);
		}
		obj.put("schemas", schemas);
		obj.put("id", ScimUtils.makeId());
		obj.put("userName", "john doe");
		obj.put("meta", new Meta("User").toJSONObject());
	}
	
	public User(String json) {
		obj = (JSONObject) JSONValue.parse(json);
	}
	
	public User(JSONObject o) {
		obj = o;
	}
	
	public User(String externalId, String userName, Meta meta) {
		obj = new JSONObject();
		JSONArray schemas = new JSONArray();{schemas.add(Const.schemas_v20_user);}obj.put("schemas", schemas);
		String id = ScimUtils.makeId();
		obj.put("id", id);
		obj.put("externalId", externalId);
		obj.put("userName", userName);
		obj.put("meta", meta.toJSONObject());
		meta.setLocation(id);
	}
	
	public User(String externalId, String userName) {
		obj = new JSONObject();
		JSONArray schemas = new JSONArray();{schemas.add(Const.schemas_v20_user);}obj.put("schemas", schemas);
		Meta meta = new Meta("User");
		String id = ScimUtils.makeId();
		obj.put("id", id);
		obj.put("externalId", externalId);
		obj.put("userName", userName);
		obj.put("meta", meta.toJSONObject());
		meta.setLocation(id);
	}

	public void setExternalId(String exid) {
		if (obj.getAsString("externalId") == null) {
			obj.put("externalId", exid);
		}
	}
	
	public void addSchema(String string) {
		JSONArray arry = (JSONArray) obj.get("schemas");
		arry.add(string);
	}

	public void removeSchema(String string) {
		JSONArray arry = (JSONArray) obj.get("schemas");
		Iterator itor = arry.iterator();
		while (itor.hasNext()) {
			String arry_string = (String) itor.next();
			if (arry_string.equals(string)) itor.remove();
		}
	}
	
	public String getLocation() {
		Meta meta = new Meta((JSONObject) obj.get("meta"));
		return meta.getAsString("location");
	}
	
	public String getVersion() {
		Meta meta = new Meta((JSONObject) obj.get("meta"));
		return meta.getAsString("version");
	}
	
	public String increaseVersion() {
		Meta meta = new Meta((JSONObject) obj.get("meta"));
		meta.setLastModify(obj);
		return meta.getAsString("version");
	}
	
	public String getLastModified() {
		Meta meta = new Meta((JSONObject) obj.get("meta"));
		return meta.getAsString("lastModified");
	}
	
	public List<String> getEmails(){
		return getArrayValue((JSONArray) obj.get("emails"));
	}
	
	public void resetEmail(String emailaddr, String type, boolean primary) {
		JSONArray arry = (JSONArray) obj.get("emails");
		if (arry == null) {
			arry = new JSONArray();
			obj.put("emails", arry);
		}
		resetArrayValue(arry, emailaddr, type, null, primary);
	}
	
	public void setEmail(String emailaddr, String type, boolean primary) {
		setEmail(emailaddr, type, null, primary);
	}
	
	public void setEmail(String emailaddr, String type, String display, boolean primary) {
		// "type": ["work","home","other"]
		setVTDP("emails", emailaddr, type, display, primary);
	}
	
	public boolean removeEmail(String emailaddr) {
		JSONArray arry = (JSONArray) obj.get("emails");
		boolean result = removeArrayValue(arry, emailaddr);
		return result;
	}

	public List<String> getPhoneNumbers(){
		return getArrayValue((JSONArray) obj.get("phoneNumbers"));
	}
	
	public void resetPhoneNumber(String number, String type, boolean primary) {
		JSONArray arry = (JSONArray) obj.get("phoneNumbers");
		if (arry == null) {
			arry = new JSONArray();
			obj.put("phoneNumbers", arry);
		}
		resetArrayValue(arry, number, type, null, primary);
	}
	
	public void setPhoneNumber(String number, String type, boolean primary) {
		setPhoneNumber(number, type, null, primary);
	}
	
	public void setPhoneNumber(String number, String type, String display, boolean primary) {
		// "type": ["work","home","mobile","fax","pager","other"]
		setVTDP("phoneNumbers", number, type, display, primary);
	}
	
	public boolean removePhoneNumber(String number) {
		JSONArray arry = (JSONArray) obj.get("phoneNumbers");
		boolean result = removeArrayValue(arry, number);
		return result;
	}
	
	
	public List<String> getIMS(){
		return getArrayValue((JSONArray) obj.get("ims"));
	}
	
	public void resetIMS(String ims, String type, boolean primary) {
		JSONArray arry = (JSONArray) obj.get("ims");
		if (arry == null) {
			arry = new JSONArray();
			obj.put("ims", arry);
		}
		resetArrayValue(arry, ims, type, null, primary);
	}
	
	public void setIMS(String ims, String type, boolean primary) {
		setIMS(ims, type, null, primary);
	}
	
	public void setIMS(String ims, String type, String display, boolean primary) {
		// "type": ["work","home","other"]
		setVTDP("ims", ims, type, display, primary);
	}
	
	public boolean removeIMS(String ims) {
		JSONArray arry = (JSONArray) obj.get("ims");
		boolean result = removeArrayValue(arry, ims);
		return result;
	}
	
	private void setVTDP(String attrName, String value, String type, String display, boolean primary) {
		JSONArray arry = (JSONArray) obj.get(attrName);
		if (setArrayValue(arry, value, type, display, primary)) {
			
		} else { // 현재 arry가 null 인경우 
			JSONObject vtdp = new JSONObject();
			vtdp.put("value", value);
			vtdp.put("type", type);
			if (display != null) vtdp.put("display", display);
			if (primary) vtdp.put("primary", primary);
			
			JSONArray new_arry = new JSONArray();
			new_arry.add(vtdp);
			obj.put(attrName, new_arry);
		}
	}
	
	public Member toMember() {
		String display = obj.getAsString("display");
		if (display == null) display = obj.getAsString("userName");
		
		return new Member(obj.getAsString("id"), "User", display);
	}
	
	public void addGroup(JSONObject group_value_ref_display) { // 이부분은 Group 객체에는 member를 넣지 못함, client 전용
		JSONArray members = (JSONArray) obj.getOrDefault("groups", new JSONArray());
		members.add(group_value_ref_display);
		obj.put("groups", members);

	}
	
	public void addGroup(Group group) {
		Member group_member = group.toMember();
		
		JSONArray members = (JSONArray) obj.get("groups");
		if (members == null) {
			members = new JSONArray();
			obj.put("groups", members);
		}
		
		obj.put("groups", members);
		
		String id = group_member.getAsString("value");
		Iterator itor = members.iterator();
		while (itor.hasNext()) {
			JSONObject jo = (JSONObject) itor.next();
			if (jo != null && jo.getAsString("value").equals(id)) itor.remove();
		}
		members.add(group_member.toJSONObject());
		group.addMember(this.toMember());
	}
	
	public void removeGroup(Group group) {
		Member group_member = group.toMember();
		JSONArray members = (JSONArray) obj.get("groups");
		if (members == null) {
			members = new JSONArray();
			obj.put("groups", members);
		}
		
		String id = group_member.getAsString("value");
		Iterator itor = members.iterator();
		while (itor.hasNext()) {
			JSONObject jo = (JSONObject) itor.next();
			if (jo.getAsString("value").equals(id)) itor.remove();
		}
		
		group.removeMember(this.toMember());
	}
	
	
	public void removeGroup(String group_id) {
		/*
		 * 해당 User에서만 group을 삭제함 
		 */
		JSONArray members = (JSONArray) obj.get("groups");
		if (members == null) {
			members = new JSONArray();
			obj.put("groups", members);
		}
		
		Iterator itor = members.iterator();
		while (itor.hasNext()) {
			JSONObject jo = (JSONObject) itor.next();
			if (jo.getAsString("value").equals(group_id)) itor.remove();
		}
	}

	
}
