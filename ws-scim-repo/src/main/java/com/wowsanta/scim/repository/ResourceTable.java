package com.wowsanta.scim.repository;

import java.util.HashMap;
import java.util.Map;

import com.wowsanta.scim.object.SCIM_Object;

public class ResourceTable extends SCIM_Object {
	private String name;
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
	public String getName() {
		return name;
	}
	public void setName(String tableName) {
		this.name = tableName;
	}
}
