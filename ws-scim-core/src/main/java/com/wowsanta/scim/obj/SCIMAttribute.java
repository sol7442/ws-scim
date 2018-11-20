package com.wowsanta.scim.obj;

import java.util.HashMap;
import java.util.Map;

import com.wowsanta.scim.attribute.Attribute;


public class SCIMAttribute {
	private  Map<String, Attribute> attributes = new HashMap<String, Attribute>();

	public Map<String, Attribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, Attribute> attributes) {
		this.attributes = attributes;
	}
	
}
