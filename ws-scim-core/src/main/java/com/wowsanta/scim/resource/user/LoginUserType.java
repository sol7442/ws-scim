package com.wowsanta.scim.resource.user;

public enum LoginUserType {
	ADMIN("관리자"),
	OPERATOR("작업자"),
	USER("사용자");
	
	private String text;
	
	private LoginUserType(String text) {
		this.text = text;
	}
	public String getText() {
		return this.text;
	}
}
