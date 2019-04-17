package com.wowsanta.scim.object;

import java.util.HashMap;
import java.util.Map;

public class SCIM_Resource extends SCIM_Object {
	private Map<String,Object> attributes = new HashMap<String, Object>();

	public Map<String, Object> getAttributes() {
		return attributes;
	}
	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}
	
	public Object getAttribute(String key) {
		return this.attributes.get(key);
	}
	public void addAttribute(String key,Object value) {
		this.attributes.put(key,value);
	}
}
