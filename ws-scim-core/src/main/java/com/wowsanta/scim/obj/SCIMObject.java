package com.wowsanta.scim.obj;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public abstract class SCIMObject implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3608570793684619034L;
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
	
	public abstract void parse(String json_str);
	public abstract JsonObject encode();
	
	public String toString() {
		return encode().toString();
	}
	public String toString(boolean pretty) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(encode());
	}
}
