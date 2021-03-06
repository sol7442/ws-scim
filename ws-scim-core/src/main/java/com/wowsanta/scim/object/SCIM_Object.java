package com.wowsanta.scim.object;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class SCIM_Object {
	private static transient Logger logger = LoggerFactory.getLogger(SCIM_Object.class);
	
	protected String id;
	protected List<String> schemas = new ArrayList<String>();
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<String> getSchemas() {
		return schemas;
	}
	public void addSchema(String schema) {
		this.schemas.add(schema);
	}
	public void setSchemas(List<String> schemas) {
		this.schemas = schemas;
	}
	
	public String toString() {
		return toString(false);
	}
	public String toString(boolean pretty) {
		try {
			GsonBuilder builder = new GsonBuilder().disableHtmlEscaping();
			if(pretty) {
				builder.setPrettyPrinting();
			}
			Gson gson = builder.create();
			return gson.toJson(this);
		}catch (Exception e) {
			logger.error("OBJECT ERRORR : {} ",e.getMessage(),e);
		}
		return this.toString();
	}
}

