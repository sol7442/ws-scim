package com.wowsanta.scim.resource;

public class SCIMSchemaExtension {
	private String schema;
	private boolean required;
	
	public String getSchema() {
		return schema;
	}
	public void setSchema(String schema) {
		this.schema = schema;
	}
	public boolean isRequired() {
		return required;
	}
	public void setRequired(boolean required) {
		this.required = required;
	}
}
