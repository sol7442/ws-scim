package com.wowsanta.scim.obj;

import com.google.gson.JsonObject;
import com.wowsanta.scim.json.SCIMJsonObject;

public class SCIMSystem extends SCIMJsonObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9167714568504724185L;
	
	private String systemId;
	private String systemName;
	private String systemDesc;
	private String systemUrl;
	private String systemType;
	
	public JsonObject encode(){
		JsonObject json_object = super.encode();
	
		json_object.addProperty("systemId"		, systemId);
		json_object.addProperty("systemName"	, systemName);
		json_object.addProperty("systemDesc"	, systemDesc);
		json_object.addProperty("systemUrl"		, systemUrl);
		json_object.addProperty("systemType"	, systemType);
		
		return json_object;
	}
	
	public String getSystemId() {
		return systemId;
	}
	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}
	public String getSystemName() {
		return systemName;
	}
	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}
	public String getSystemDesc() {
		return systemDesc;
	}
	public void setSystemDesc(String description) {
		this.systemDesc = description;
	}
	public String getSystemUrl() {
		return systemUrl;
	}
	public void setSystemUrl(String systemUrl) {
		this.systemUrl = systemUrl;
	}

	public void setSystemType(String systemType) {
		this.systemType = systemType;
	}
	public String getSystemTyp() {
		return this.systemType;
	}
}
