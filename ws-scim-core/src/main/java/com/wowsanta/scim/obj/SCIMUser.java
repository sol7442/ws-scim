package com.wowsanta.scim.obj;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.wowsanta.scim.json.SCIMJsonObject;
import com.wowsanta.scim.schema.SCIMConstants;

public class SCIMUser extends SCIMResource {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5408692784468420918L;
	
	
	
	private String id;
	private String userName;
	private boolean active;
	
	private SCIMUserMeta meta;
	
	public SCIMUser() {
		addSchema(SCIMConstants.USER_CORE_SCHEMA_URI);
	}
	
	@Override
	public JsonObject parse(String json_str) {
		JsonObject json_obj = super.parse(json_str);
		
		this.id 		= JsonUtil.toString(json_obj.get("id"));
		this.userName 	= JsonUtil.toString(json_obj.get("userName"));
		this.active  	= JsonUtil.toBoolean(json_obj.get("active"));
		
		if(json_obj.get("meta") != null){
			this.meta       = new SCIMUserMeta();
			this.meta.parse(json_obj.get("meta").toString());
		}
		return json_obj;
	}

	@Override
	public JsonObject encode() {
		JsonObject json_obj = super.encode();

		json_obj.addProperty("id",this.id);
		json_obj.addProperty("userName",this.userName);
		json_obj.addProperty("active", this.active);
		
		if(this.meta != null){
			json_obj.add("meta", this.meta.encode());
		}
		
		return json_obj ;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public SCIMUserMeta getMeta() {
		return meta;
	}

	public void setMeta(SCIMUserMeta meta) {
		this.meta = meta;
	}
}
