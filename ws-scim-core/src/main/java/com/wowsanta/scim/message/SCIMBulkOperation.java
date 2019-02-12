package com.wowsanta.scim.message;

import com.google.gson.JsonObject;
import com.wowsanta.scim.obj.JsonUtil;

public class SCIMBulkOperation extends SCIMOperation{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2290817678960481644L;
	
	private String bulkId;
	
	public SCIMBulkOperation() {}
	public SCIMBulkOperation(SCIMBulkOperation operation) {
		setMethod(operation.getMethod());
		setBulkId(operation.getBulkId());
	}
	public String getBulkId() {
		return bulkId;
	}
	public void setBulkId(String bulkId) {
		this.bulkId = bulkId;
	}
	
	@Override
	public JsonObject parse(String json_str) {
		JsonObject json_obj = super.parse(json_str);
		this.bulkId = JsonUtil.toString(json_obj.get("bulkId"));
		return json_obj;
	}

	@Override
	public JsonObject encode() {
		JsonObject json_obj = super.encode();
		json_obj.addProperty("bulkId",	this.bulkId);
		return json_obj ;
	}
}
