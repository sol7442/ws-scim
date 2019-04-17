package com.wowsanta.scim.repository;

import java.util.HashMap;
import java.util.Map;

import com.wowsanta.scim.object.SCIM_Object;

public class ResourceColumn extends SCIM_Object {
	private Map<String,Object> attributes = new HashMap<String, Object>();
	private DataMapper mapper;
	
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
	public DataMapper getMapper() {
		return mapper;
	}
	public void setMapper(DataMapper mapper) {
		this.mapper = mapper;
	}
}
