package com.wowsanta.scim.repository;

import com.wowsanta.scim.schema.SCIMAttributeSchema;

public class ResourceAttribute extends SCIMAttributeSchema {
	private DataMapper dataMapper;
	public ResourceAttribute(SCIMAttributeSchema attributeSchema) {
		super(attributeSchema);
	}
	public DataMapper getDataMapper() {
		return dataMapper;
	}
	public void setDataMapper(DataMapper dataMapper) {
		this.dataMapper = dataMapper;
	}
}
