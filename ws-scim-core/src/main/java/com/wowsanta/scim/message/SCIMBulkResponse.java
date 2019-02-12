package com.wowsanta.scim.message;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.wowsanta.scim.schema.SCIMConstants;

public class SCIMBulkResponse extends SCIMMessage{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1979838842129374790L;
	
	private List<SCIMBulkOperation> operations = new ArrayList<SCIMBulkOperation>();
	
	public SCIMBulkResponse() {
		addSchema(SCIMConstants.BULK_RESPONSE_URI);
	}

	public List<SCIMBulkOperation> getOperations() {
		return operations;
	}

	public void setOperations(List<SCIMBulkOperation> operations) {
		this.operations = operations;
	}
	
	public void addOperation(SCIMBulkOperation operation) {
		this.operations.add(operation);
	}

	@Override
	public JsonObject parse(String json_str) {
		
		System.out.println(json_str);
		
		JsonObject json_obj = super.parse(json_str);
		
		JsonArray json_operations = json_obj.get("Operations").getAsJsonArray();
		for (JsonElement json_opperation : json_operations) {
			SCIMBulkOperation operation = new SCIMBulkOperation();
			operation.parse(json_opperation.toString());
			this.operations.add(operation);
		}
		
		return json_obj;
	}

	@Override
	public JsonObject encode() {
		JsonObject super_json = super.encode();
		
		JsonArray json_operations = new JsonArray();
		for (SCIMBulkOperation operation : operations) {
			json_operations.add(operation.encode());
		}
		super_json.add("Operations", json_operations);
		
		return super_json;
	}


}
