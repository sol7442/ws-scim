package com.wowsanta.scim.obj;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;

public class SCIMSystem  {
	static transient Logger logger = LoggerFactory.getLogger(SCIMSystem.class);

	private String systemId;
	private String systemName;
	private String systemDesc;
	private String systemUrl;
	private String systemType;
	

	public static SCIMSystem parse(LinkedTreeMap system_object) {
		SCIMSystem system = new SCIMSystem();
		system.systemId   = (String) system_object.get("systemId");
		system.systemName = (String) system_object.get("systemName");
		system.systemDesc = (String) system_object.get("systemDesc");
		system.systemUrl  = (String) system_object.get("systemUrl");
		system.systemType = (String) system_object.get("systemType");
		
		return system;
	}
	public static SCIMSystem parse(String result_string) {
		SCIMSystem system = null;
		try {
			Gson gson = new GsonBuilder().disableHtmlEscaping().create();
			system = gson.fromJson(result_string, SCIMSystem.class);
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return system;
	}
	
	public String toString() {
		return toString(false);
	}
	public String toString(boolean pretty) {
		try {
			GsonBuilder builder = new GsonBuilder().disableHtmlEscaping();
			if (pretty) {
				builder.setPrettyPrinting();
			}
			Gson gson = builder.create();
			return gson.toJson(this);
		} catch (Exception e) {
			logger.error(e.getMessage() + " : ",  e);
		}
		return null;
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
