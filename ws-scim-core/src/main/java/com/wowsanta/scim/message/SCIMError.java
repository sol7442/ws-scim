package com.wowsanta.scim.message;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.wowsanta.scim.json.SCIMJsonObject;
import com.wowsanta.scim.obj.JsonUtil;
import com.wowsanta.scim.schema.SCIMConstants;

public class SCIMError extends SCIMJsonObject{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8535241767230109023L;
	
	
	private String status;
	private String scimType;
	private String detail;
	
	private List<String> schemas = new ArrayList<String>();
	
	public SCIMError() {
		addSchema(SCIMConstants.ERROR_SCHEMA_URI);
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
		
		this.status 	= JsonUtil.toString(json_obj.get("status"));
		this.scimType 	= JsonUtil.toString(json_obj.get("scimType"));
		this.detail 	= JsonUtil.toString(json_obj.get("detail"));
		
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
		
		json_obj.addProperty("status",this.status);
		json_obj.addProperty("scimType",this.scimType);
		json_obj.addProperty("detail",this.detail);		
		
		return json_obj ;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getScimType() {
		return scimType;
	}
	public void setScimType(String scimType) {
		this.scimType = scimType;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}


}
