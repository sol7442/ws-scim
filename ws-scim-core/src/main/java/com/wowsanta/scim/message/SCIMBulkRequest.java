package com.wowsanta.scim.message;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.wowsanta.scim.obj.JsonUtil;
import com.wowsanta.scim.schema.SCIMConstants;

public class SCIMBulkRequest extends SCIMMessage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1432540498690136463L;
	
	private int failOnErrors;
	private List<SCIMBulkOperation> operations = new ArrayList<SCIMBulkOperation>();
	
	private String sourecSystemId;
	private String directSystemId;
	
	public SCIMBulkRequest(){
		addSchema(SCIMConstants.BULK_REQUEST_URI);
		addSchema(SCIMConstants.WowsantaConstansts.WOWSANAT_BULK_REQUEST_URI);
	}

	public int getFailOnErrors() {
		return failOnErrors;
	}

	public void setFailOnErrors(int failOnErrors) {
		this.failOnErrors = failOnErrors;
	}

	public List<SCIMBulkOperation> getOperations() {
		return operations;
	}

	public void setOperations(List<SCIMBulkOperation> operations) {
		this.operations = operations;
	}
	
	public void addOperation(SCIMBulkOperation operation){
		this.operations.add(operation);
	}

	public String getSourecSystemId() {
		return sourecSystemId;
	}

	public void setSourecSystemId(String sourecSystemId) {
		this.sourecSystemId = sourecSystemId;
	}

	public String getDirectSystemId() {
		return directSystemId;
	}

	public void setDirectSystemId(String directSystemId) {
		this.directSystemId = directSystemId;
	}
	
	@Override
	public JsonObject parse(String json_str) {
		JsonObject json_obj = super.parse(json_str);
		if(json_obj.get("failOnErrors") != null) {
			this.failOnErrors = json_obj.get("failOnErrors").getAsInt();
		}
		
		JsonArray json_operations = json_obj.get("Operations").getAsJsonArray();
		for (JsonElement json_opperation : json_operations) {
			SCIMBulkOperation operation = new SCIMBulkOperation();
			operation.parse(json_opperation.toString());
			this.operations.add(operation);
		}
		
		
		if(json_obj.get(SCIMConstants.WowsantaConstansts.WOWSANAT_BULK_REQUEST_URI) != null) {
			JsonObject wow_json_obj = json_obj.get(SCIMConstants.WowsantaConstansts.WOWSANAT_BULK_REQUEST_URI).getAsJsonObject();
			this.sourecSystemId = JsonUtil.toString(wow_json_obj.get("sourecSystemId"));
			this.directSystemId = JsonUtil.toString(wow_json_obj.get("directSystemId"));	
		}
		
		return json_obj;
	}

	@Override
	public JsonObject encode() {
		JsonObject json_obj = super.encode();
		
		JsonArray json_operations = new JsonArray();
		for (SCIMBulkOperation operation : operations) {
			json_operations.add(operation.encode());
		}
		json_obj.add("Operations", json_operations);
		if(this.failOnErrors != 0){
			json_obj.addProperty("failOnErrors", this.failOnErrors);
		}
		
		JsonObject wow_json_obj = new JsonObject();
		wow_json_obj.addProperty("sourecSystemId",this.sourecSystemId);
		wow_json_obj.addProperty("directSystemId",this.directSystemId);
		
		json_obj.add(SCIMConstants.WowsantaConstansts.WOWSANAT_BULK_REQUEST_URI, wow_json_obj);
		
		return json_obj;
	}
}
