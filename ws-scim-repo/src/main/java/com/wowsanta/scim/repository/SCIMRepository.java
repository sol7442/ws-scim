package com.wowsanta.scim.repository;

import com.google.gson.JsonObject;
import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.json.AbstractJsonObject;
import com.wowsata.util.json.WowsantaJson;

public abstract class SCIMRepository extends WowsantaJson {
	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	
	public static int RESULT_DUPLICATE_ENTRY = 1;
	public static int RESULT_IS_NULL = 2;
	
	
	private String type;
	public abstract void initialize() throws SCIMException;
	public abstract void close();
	
	public void setType(String type) {
		this.type = type;
	};
	public String getType() {
		return this.type;
	}

	public abstract boolean validate()throws SCIMException;
	//public abstract void fromJson(JsonObject jsonObject)throws SCIMException;

}
