package com.wowsanta.scim.resource;

import java.lang.instrument.Instrumentation;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class User {
	
	private String id;
	private Date created;
	private Date modified;
	private Map<String,String> attributes = new HashMap<String,String>();
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public Date getModified() {
		return modified;
	}
	public void setModified(Date modified) {
		this.modified = modified;
	}
	public Map<String, String> getAttributes() {
		return attributes;
	}
	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}
	public void setAttribute(String key, String value) {
		this.attributes.put(key, value);
	}
	public String getAttribute(String key) {
		return this.attributes.get(key);
	}
}
