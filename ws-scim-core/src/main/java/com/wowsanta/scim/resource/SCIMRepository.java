package com.wowsanta.scim.resource;

import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.json.AbstractJsonObject;

public abstract class SCIMRepository extends AbstractJsonObject {
	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	
	private String type;
	public abstract void initialize() throws SCIMException;
	public abstract void close();
	
	public void setType(String type) {
		this.type = type;
	};
	public String getType() {
		return this.type;
	}
	
}
