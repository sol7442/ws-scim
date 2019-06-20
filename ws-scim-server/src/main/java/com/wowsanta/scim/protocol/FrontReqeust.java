package com.wowsanta.scim.protocol;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class FrontReqeust {
	static transient Logger logger = LoggerFactory.getLogger(FrontReqeust.class);
	private String method;
	private Map<String,String> params = new HashMap<String, String>();
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public Map<String,String> getParams() {
		return params;
	}
	public void setParams(Map<String,String> params) {
		this.params = params;
	}
	
	public static FrontReqeust parse(String result_string) {
		FrontReqeust response = null;
		try {
			Gson gson = new GsonBuilder().disableHtmlEscaping().create();
			response = gson.fromJson(result_string, FrontReqeust.class);
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return response;
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
