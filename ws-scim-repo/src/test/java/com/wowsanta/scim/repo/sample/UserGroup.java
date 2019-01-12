package com.wowsanta.scim.repo.sample;

import com.wowsanta.scim.resource.SCIMUserGroup;

public class UserGroup implements SCIMUserGroup {
	
	private String display;
	private String value;
	private String $ref;
	
	public String getDisplay() {
		return display;
	}
	public void setDisplay(String display) {
		this.display = display;
	}
	
	@Override
	public void setRefValue(String ref, String value) {
		this.$ref = ref + "/Group/" + value;
		this.value = value;
	}
	@Override
	public String getValue() {
		return this.value;
	}
	@Override
	public String getRef() {
		return this.$ref;
	}
}
