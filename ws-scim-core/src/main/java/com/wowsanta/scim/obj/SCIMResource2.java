package com.wowsanta.scim.obj;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.wowsanta.scim.json.SCIMJsonObject;

public class SCIMResource2 extends SCIMJsonObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4506364327193996264L;
	
	private String id;
	
	public SCIMResource2(){}
	
	public String getId() {
		return this.id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
}
