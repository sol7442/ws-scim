package com.wowsanta.scim.obj;

import com.wowsanta.scim.resource.SCIMEnterpriseUser;
import com.wowsanta.scim.schema.SCIMConstants;

public class DefaultEnterpriseUser extends DefaultUser implements SCIMEnterpriseUser {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2560987662892106687L;

	private String schema = SCIMConstants.ENTERPRISEUSER_CORE_SCHEMA_URI;
	
	private String employeeNumber;
	public DefaultEnterpriseUser() {
		super();
		addSchema(SCIMConstants.ENTERPRISEUSER_CORE_SCHEMA_URI);
	}
	@Override
	public void setEmployeeNumber(String empNo) {
		this.employeeNumber = empNo;
	}

	@Override
	public String getEmployeeNumber() {
		return this.employeeNumber;
	}
	
	public String getSchema() {
		return schema;
	}
	public void setSchema(String schema) {
		this.schema = schema;
	}

}
