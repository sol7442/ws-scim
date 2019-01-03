package com.wowsanta.scim.resource;

public class SCIMRefValue {
	private String display;
	private String value;
	private String $ref;
	
	public String getDisplay() {
		return display;
	}
	public void setDisplay(String display) {
		this.display = display;
	}
	public String getValue() {
		return value;
	}
	public String get$ref() {
		return $ref;
	}
	public void setRefValue(String ref, String value) {
		this.$ref = ref + value;
		this.value = value;
	}
}
