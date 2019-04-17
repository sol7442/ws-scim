package com.wowsanta.scim.protocol;

import java.util.HashMap;
import java.util.Map;

public class FrontReqeust {
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
	public void setParams(Map<String,Object> params) {
		this.params = params;
	}
	
}
