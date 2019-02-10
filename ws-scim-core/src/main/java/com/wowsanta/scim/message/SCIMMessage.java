package com.wowsanta.scim.message;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public abstract class SCIMMessage implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<String> schemas = new ArrayList<String>();
	
	public void addSchema(String schema) {
		this.schemas.add(schema);
	}
	public List<String> getSchemas() {
		return schemas;
	}
	public void setSchemas(List<String> schemas) {
		this.schemas = schemas;
	}
}
