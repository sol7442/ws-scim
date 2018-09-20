package com.wession.scim.schema;

import java.util.ArrayList;

import com.wession.scim.Const;
import com.wession.scim.schema.BulkRequest.BulkOperation;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

public class BulkRequest extends JSONObject {
	private ArrayList<BulkOperation> bo_arry = new ArrayList<BulkOperation>();
	
	public BulkRequest() {
		JSONArray schema = new JSONArray();
		schema.add(Const.schemas_v20_bulk_request);
		this.put("schemas", schema);
		this.put("failOnErrors", 1);
		this.put("Operations", new JSONArray());
	}
	
	public BulkRequest(JSONObject obj) {
		this.merge(obj);
	}
	
	public JSONObject getBulk(int idx) {
		JSONArray arry = (JSONArray) this.get("Operations");
		return (JSONObject) arry.get(idx);
	}
	
	public void setOperations() {
		for (int idx=0; idx<size(); idx++) {
			BulkOperation bo = new BulkOperation();
			bo.setBulkOperation(getBulk(idx));
			bo_arry.add(bo);
		}
	}
	
	public int size() {
		JSONArray arry = (JSONArray) this.get("Operations");
		return arry.size();
	}
	
	class BulkOperation {
		String method;
		String path;
		String bulkId;
		JSONObject data;
		
		protected BulkOperation() {}
		
		protected void setBulkOperation(JSONObject json) {
			method = json.getAsString("method"); if (method != null) method = method.toLowerCase();
			path = json.getAsString("path");
			bulkId = json.getAsString("bulkId");
			
			data = (JSONObject) json.get("data");
		}
	}
}
