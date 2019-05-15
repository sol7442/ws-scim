package com.wowsanta.scim.message;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.wowsanta.scim.object.SCIM_Object;
import com.wowsanta.scim.schema.SCIMConstants;

public class SCIMBulkResponse extends SCIM_Object {
	
	static transient Logger logger = LoggerFactory.getLogger(SCIMBulkRequest.class);
	
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
	
	public static SCIMBulkResponse parse(String json_string) {
		SCIMBulkResponse object = null;
		try {
			GsonBuilder builder = new GsonBuilder().disableHtmlEscaping();
			Gson gson  = builder.create();
			object = gson.fromJson(json_string, SCIMBulkResponse.class);	
		}catch (Exception e) {
			logger.error("JSON PARSE ERROR {},\n{}",e.getMessage(),json_string,e);
		}
		return object;
	}
//
//	@Override
//	public JsonObject parse(String json_str) {
//		
//		System.out.println(json_str);
//		
//		JsonObject json_obj = super.parse(json_str);
//		
//		JsonArray json_operations = json_obj.get("Operations").getAsJsonArray();
//		for (JsonElement json_opperation : json_operations) {
//			SCIMBulkOperation operation = new SCIMBulkOperation();
//			operation.parse(json_opperation.toString());
//			this.operations.add(operation);
//		}
//		
//		return json_obj;
//	}
//
//	@Override
//	public JsonObject encode() {
//		JsonObject super_json = super.encode();
//		
//		JsonArray json_operations = new JsonArray();
//		for (SCIMBulkOperation operation : operations) {
//			json_operations.add(operation.encode());
//		}
//		super_json.add("Operations", json_operations);
//		
//		return super_json;
//	}


}
