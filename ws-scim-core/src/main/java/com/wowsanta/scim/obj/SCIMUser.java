package com.wowsanta.scim.obj;


import com.google.gson.JsonObject;
import com.wowsanta.scim.schema.SCIMConstants;

public class SCIMUser extends SCIMResource {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5408692784468420918L;
	
	
	
	private String userName;
	private boolean active;
	private String password;
	
	private SCIMUserMeta meta;
	
	public SCIMUser() {
		addSchema(SCIMConstants.USER_CORE_SCHEMA_URI);
	}
	
	@Override
	public JsonObject parse(String json_str) {
		JsonObject json_obj = super.parse(json_str);
		this.userName 	= JsonUtil.toString(json_obj.get("userName"));
		this.active  	= JsonUtil.toBoolean(json_obj.get("active"));
		this.password   = JsonUtil.toString(json_obj.get("password"));
		
		if(json_obj.get("meta") != null){
			this.meta       = new SCIMUserMeta();
			this.meta.parse(json_obj.get("meta").toString());
		}
		return json_obj;
	}

	@Override
	public JsonObject encode() {
		JsonObject json_obj = super.encode();

		json_obj.addProperty("userName",this.userName);
		json_obj.addProperty("active", this.active);
		json_obj.addProperty("password", this.password);
		
		if(this.meta != null){
			json_obj.add("meta", this.meta.encode());
		}
		
		return json_obj ;
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
	public int getActive() {
		if(active) {return 1;}else {return 0;}
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
