package com.wowsanta.scim.protocol;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ClientRequest {
	
	static transient Logger logger = LoggerFactory.getLogger(ClientRequest.class);
	
	private String method;
	private Map<String,Object> params = new HashMap<String, Object>();
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public Map<String,Object> getParams() {
		return params;
	}
	public void putParam(String key, Object value) {
		this.params.put(key, value);
	}
	public Object getParam(String key) {
		return this.params.get(key);
	}
	public void setParams(Map<String,Object> params) {
		this.params = params;
	}
	
	public static ClientRequest parse(String result_string) {
		ClientRequest request = null;
		try {
			Gson gson = new GsonBuilder().disableHtmlEscaping().create();
			request = gson.fromJson(result_string, ClientRequest.class);
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return request;
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
