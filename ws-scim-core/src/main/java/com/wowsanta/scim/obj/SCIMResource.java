package com.wowsanta.scim.obj;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.wowsanta.scim.json.SCIMJsonObject;

public class SCIMResource extends SCIMJsonObject{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4506364327193996264L;
	private List<String> schemas = new ArrayList<String>();
	
	public SCIMResource(){}
	public SCIMResource(JsonObject json_obj) {
		JsonArray json_schemas = json_obj.get("schemas").getAsJsonArray();
		for (JsonElement jsonElement : json_schemas) {
			this.schemas.add(jsonElement.getAsString());
		}
	}
	public void addSchema(String schema) {
		this.schemas.add(schema);
	}
	public List<String> getSchemas() {
		return schemas;
	}
	public void setSchemas(List<String> schemas) {
		this.schemas = schemas;
	}
	
	@Override
	public JsonObject parse(String json_str) {
		this.schemas.clear();

		JsonObject json_obj = super.parse(json_str);
		JsonArray json_schemas = json_obj.get("schemas").getAsJsonArray();
		for (JsonElement jsonElement : json_schemas) {
			this.schemas.add(jsonElement.getAsString());
		}
		
		return json_obj;
	}

	@Override
	public JsonObject encode() {
		JsonObject json_obj = super.encode();
		
		JsonArray json_schemas = new JsonArray();
		for (String schema : getSchemas()) {
			json_schemas.add(schema);
		}
		json_obj.add("schemas",json_schemas);
		
		return json_obj ;
	}
}
