package com.wowsanta.scim.obj;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.wowsanta.scim.attribute.Attribute;


public class SCIMObject implements Serializable{
	private static final long serialVersionUID = -7757000188475144311L;
	protected  Map<String, Attribute> attributes = new HashMap<String, Attribute>();

	public Map<String, Attribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, Attribute> attributes) {
		this.attributes = attributes;
	}
	
	public void addAttribute(String key, Attribute attribute) {
		this.attributes.put(key, attribute);
	}
	public Attribute getAttribute(String key) {
		return this.attributes.get(key);
	}
	
	public Attribute removeAttribute(String key) {
		return this.attributes.remove(key);
	}
}
