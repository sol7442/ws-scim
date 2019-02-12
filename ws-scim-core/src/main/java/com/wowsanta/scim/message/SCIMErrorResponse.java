package com.wowsanta.scim.message;

import java.util.ArrayList;
import java.util.List;

import com.wowsanta.scim.json.SCIMJsonObject;
import com.wowsanta.scim.schema.SCIMConstants;

public class SCIMErrorResponse extends SCIMJsonObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5691726644403816355L;
	private List<String> schemas = new ArrayList<String>();
	
	private String scimType;
	private String detail;
	private String status;
	
	public SCIMErrorResponse(){
		addSchema(SCIMConstants.ERROR_SCHEMA_URI);
	}
	public void addSchema(String schema) {
		this.schemas.add(schema);
	}
	public List<String> getSchemas() {
		return schemas;
	}
	public void setSchemas(List<String> schemas) {
		this.schemas = schemas;
	}
	public String getScimType() {
		return scimType;
	}
	public void setScimType(String scimType) {
		this.scimType = scimType;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
