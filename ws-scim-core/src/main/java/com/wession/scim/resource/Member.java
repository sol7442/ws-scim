package com.wession.scim.resource;

import java.io.Serializable;

import com.wession.common.ScimUtils;

import net.minidev.json.JSONObject;

public class Member extends JsonModel implements Serializable {
	public Member() {
		obj = new JSONObject();
		obj.put("value", "UUID");
		obj.put("$ref", "http://yoursite.com/scim/v2.0/Users/UUID");
		obj.put("display", "john doe");
	}
	
	public Member(String id, String resourceType, String display) {
		obj = new JSONObject();
		obj.put("value", id);
		obj.put("$ref", ScimUtils.makeRef(getRefer(), id, resourceType));
		obj.put("display", display);
	}
}
