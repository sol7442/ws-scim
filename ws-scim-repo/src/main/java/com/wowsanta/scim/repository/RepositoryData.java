package com.wowsanta.scim.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class RepositoryData {
	static transient Logger logger = LoggerFactory.getLogger(RepositoryData.class);
	
	private String key;
	private Object value;
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	
	public static RepositoryData parse(String result_string) {
		RepositoryData data = null;
		try {
			Gson gson = new GsonBuilder().disableHtmlEscaping().create();
			data = gson.fromJson(result_string, RepositoryData.class);
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return data;
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
