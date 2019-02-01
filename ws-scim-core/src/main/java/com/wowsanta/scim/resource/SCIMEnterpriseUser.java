package com.wowsanta.scim.resource;

public interface SCIMEnterpriseUser extends SCIMUser{
	public void setEmployeeNumber(String empNo);
	public String getEmployeeNumber();
}
