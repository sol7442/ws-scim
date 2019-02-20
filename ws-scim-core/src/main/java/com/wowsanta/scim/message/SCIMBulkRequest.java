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
	
	private String requestId;
	private String sourceSystemId;
	private String targetSystemId;
	private String schedulerId;
	
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
			this.requestId      = JsonUtil.toString(wow_json_obj.get("requestId"));
			this.sourceSystemId = JsonUtil.toString(wow_json_obj.get("sourceSystemId"));
			this.targetSystemId = JsonUtil.toString(wow_json_obj.get("targetSystemId"));	
			this.schedulerId	= JsonUtil.toString(wow_json_obj.get("schedulerId"));
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
		wow_json_obj.addProperty("requestId",this.requestId);
		wow_json_obj.addProperty("sourceSystemId",this.sourceSystemId);
		wow_json_obj.addProperty("targetSystemId",this.targetSystemId);
		wow_json_obj.addProperty("schedulerId",this.schedulerId);
		
		json_obj.add(SCIMConstants.WowsantaConstansts.WOWSANAT_BULK_REQUEST_URI, wow_json_obj);
		
		return json_obj;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getTargetSystemId() {
		return targetSystemId;
	}

	public void setTargetSystemId(String targetSystemId) {
		this.targetSystemId = targetSystemId;
	}

	public String getSchedulerId() {
		return schedulerId;
	}

	public void setSchedulerId(String schedulerId) {
		this.schedulerId = schedulerId;
	}

	public String getSourceSystemId() {
		return sourceSystemId;
	}

	public void setSourceSystemId(String sourceSystemId) {
		this.sourceSystemId = sourceSystemId;
	}
}
