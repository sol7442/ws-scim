package com.wowsanta.scim.repository.impl;

public enum DataType {
	varchar("varchar"),
	nvarchar("nvarchar"),
	char_("char");
	
	private String value;
	private DataType(String value) {
		this.value = value;
	}
}
