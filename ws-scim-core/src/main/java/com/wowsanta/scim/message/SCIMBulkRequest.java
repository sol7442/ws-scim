package com.wowsanta.scim.message;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.wowsanta.scim.schema.SCIMConstants;

public class SCIMBulkRequest extends SCIMMessage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1432540498690136463L;
	
	private int failOnErrors;
	private List<SCIMBulkOperastion> operations = new ArrayList<SCIMBulkOperastion>();
	
	public SCIMBulkRequest(){
		addSchema(SCIMConstants.BULK_REQUEST_URI);
	}

	public int getFailOnErrors() {
		return failOnErrors;
	}

	public void setFailOnErrors(int failOnErrors) {
		this.failOnErrors = failOnErrors;
	}

	public List<SCIMBulkOperastion> getOperations() {
		return operations;
	}

	public void setOperations(List<SCIMBulkOperastion> operations) {
		this.operations = operations;
	}
	
	public void AddOperation(SCIMBulkOperastion operation){
		this.operations.add(operation);
	}
	
	
	@Override
	public JsonObject parse(String json_str) {
		JsonObject json_obj = super.parse(json_str);
		
		JsonArray json_operations = json_obj.get("Operations").getAsJsonArray();
		for (JsonElement json_opperation : json_operations) {
			SCIMBulkOperastion operation = new SCIMBulkOperastion();
			operation.parse(json_opperation.toString());
			this.operations.add(operation);
		}
		
		return json_obj;
	}

	@Override
	public JsonObject encode() {
		JsonObject super_json = super.encode();
		
		JsonArray json_operations = new JsonArray();
		for (SCIMBulkOperastion operation : operations) {
			json_operations.add(operation.encode());
		}
		super_json.add("Operations", json_operations);
		if(this.failOnErrors != 0){
			super_json.addProperty("failOnErrors", this.failOnErrors);
		}
		
		return super_json;
	}
}
