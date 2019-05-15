package com.wowsanta.scim.object;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Resource_Object {
	transient Logger logger = LoggerFactory.getLogger(Resource_Object.class);
	
	private Map<String,Object> attributes = new HashMap<String,Object>();

	private String id;
	public String getId() {
		return this.id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public Map<String,Object> getAttributes() {
		return attributes;
	}
	public void setAttributes(Map<String,Object> attributes) {
		this.attributes = attributes;
	}
	
	public void put(String key, Object value) {
		this.attributes.put(key, value);
	}
	public Object get(String key) {
		return this.attributes.get(key);
	}
	
	public String getString(String key) {
		String attribute = null;
		try {
			attribute = (String) this.attributes.get(key);
		}catch (Exception e) {
			logger.info(e.getMessage() + "{}", key );
		}
		return attribute;
	}
	public int getInteger(String key) {
		int attribute = 0;
		try {
			attribute = (Integer) this.attributes.get(key);
		}catch (Exception e) {
			attribute = 0;
		}
		return attribute;
	}
	public Timestamp getTimestamp(String key) {
		Timestamp attribute = null;
		try {
			String str_value = (String) this.attributes.get(key);
			attribute = java.sql.Timestamp.valueOf(str_value); 
		}catch (Exception e) {
			logger.info(e.getMessage() + "{}", key );
		}
		return attribute;
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
}
