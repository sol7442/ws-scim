package com.wowsanta.scim.obj;

import com.google.gson.JsonObject;
import com.wowsanta.scim.schema.SCIMConstants;

public class SCIMEnterpriseUser extends SCIMUser {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1328106884536935485L;

	private String employeeNumber;
	private String organization;
	private String division;
	private String department;
	
	public SCIMEnterpriseUser(){
		super();
		addSchema(SCIMConstants.ENTERPRISEUSER_CORE_SCHEMA_URI);
	}

	@Override
	public JsonObject parse(String json_str) {
		JsonObject json_user = super.parse(json_str);
		JsonObject json_enterprise = json_user.get(SCIMConstants.ENTERPRISEUSER_CORE_SCHEMA_URI).getAsJsonObject();
		if(!json_enterprise.isJsonNull()){
			this.employeeNumber = JsonUtil.toString(json_enterprise.get("employeeNumber"));
			this.organization	= JsonUtil.toString(json_enterprise.get("organization"));
			this.division		= JsonUtil.toString(json_enterprise.get("division"));
			this.department		= JsonUtil.toString(json_enterprise.get("department"));
		}
		return json_user;
	}

	@Override
	public JsonObject encode() {
		JsonObject json_enterprise = new JsonObject();
		json_enterprise.addProperty("employeeNumber", this.employeeNumber);
		json_enterprise.addProperty("organization"	, this.organization);
		json_enterprise.addProperty("division"		, this.division);
		json_enterprise.addProperty("department"	, this.department);
		
		JsonObject user_json = super.encode();
		user_json.add(SCIMConstants.ENTERPRISEUSER_CORE_SCHEMA_URI, json_enterprise);
		
		return user_json;
	}
	
	public String getEmployeeNumber() {
		return employeeNumber;
	}

	public void setEmployeeNumber(String employeeNumber) {
		this.employeeNumber = employeeNumber;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getDivision() {
		return division;
	}

	public void setDivision(String division) {
		this.division = division;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}
}
