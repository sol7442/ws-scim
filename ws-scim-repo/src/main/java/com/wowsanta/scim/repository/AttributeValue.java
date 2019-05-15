package com.wowsanta.scim.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class AttributeValue {
	private static Logger logger = LoggerFactory.getLogger(AttributeValue.class);
	
	private String name;
	private Object value;
	
	public AttributeValue(String name, Object value) {
		this.name = name;
		this.value = value;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
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
			Gson gson  = builder.create();
			return gson.toJson(this);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
}
