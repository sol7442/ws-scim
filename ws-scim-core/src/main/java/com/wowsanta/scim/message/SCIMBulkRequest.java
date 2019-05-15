package com.wowsanta.scim.message;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wowsanta.scim.object.SCIM_Object;
import com.wowsanta.scim.schema.SCIMConstants;

public class SCIMBulkRequest extends SCIM_Object {
	static transient Logger logger = LoggerFactory.getLogger(SCIMBulkRequest.class);
	
	private int failOnErrors;
	private List<SCIMBulkOperation> operations;// = new ArrayList<SCIMBulkOperation>();
	
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
		if(this.operations == null) {
			this.operations = new ArrayList<SCIMBulkOperation>();
		}
			
		this.operations.add(operation);
	}
	
	public static SCIMBulkRequest parse(String json_string) {
		SCIMBulkRequest object = null;
		try {
			GsonBuilder builder = new GsonBuilder().disableHtmlEscaping();
			Gson gson  = builder.create();
			object = gson.fromJson(json_string, SCIMBulkRequest.class);	
		}catch (Exception e) {
			logger.error("JSON PARSE ERROR {},\n{}",e.getMessage(),json_string,e);
		}
		return object;
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
