package com.wession.scim.schema;

import com.wession.scim.Const;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

public class BulkResponse extends JSONObject {
	public BulkResponse() {
		JSONArray schema = new JSONArray();
		schema.add(Const.schemas_v20_bulk_response);
		this.put("schemas", schema);
		this.put("Operations", new JSONArray());
		// location
		// mehtod
		// bulkId
		// version
		// status {"code-http", "scimType", "detail"}
	}
	
	public void addResult(JSONObject json) {
		((JSONArray) this.get("Operations")).add(json);
	}
	
	public void addResult(String bulkId, String method, String status, String location, String version) {
		JSONObject result = new JSONObject();
		result.put("bulkId", bulkId);
		result.put("method", method);
		result.put("status", status);
		if (location != null) result.put("location", location);
		if (version != null) result.put("version", version);
		addResult(result);
	}
	
	public void addError(String bulkId, String method, String status, String location, String errScimType, String errDetail) {
		JSONObject result = new JSONObject();
		result.put("bulkId", bulkId);
		result.put("method", method);
		result.put("status", status);
		if (location != null) result.put("location", location);
		Error bulkErr = new Error(Integer.parseInt(status), errScimType, errDetail);
		result.put("response", bulkErr);
		
		addResult(result);
	}
}
