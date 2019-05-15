package com.wowsanta.scim.message;

import com.wowsanta.scim.exception.SCIMError;
import com.wowsanta.scim.object.Resource_Object;
import com.wowsanta.scim.object.SCIM_Object;

public class SCIMBulkOperation extends SCIM_Object {
	
	private String bulkId;
	
	private String method;
	private String path;
	private Resource_Object data;
	private String location;
	private String status;
	private SCIMError response;
	
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
	
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public Resource_Object getData() {
		return data;
	}
	public void setData(Resource_Object data) {
		this.data = data;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public SCIMError getResponse() {
		return response;
	}
	public void setResponse(SCIMError response) {
		this.response = response;
	}
	
	
}
