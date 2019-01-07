package com.wowsanta.scim.service.admin;

import com.wowsanta.scim.service.SCIMRequest;

public class LoginRequest implements SCIMRequest{
	private String id;
	private String pw;
	
	public void setId(String id) {
		this.id = id;
	}
	public String getId() {
		return this.id;
	}
	public void setPw(String pw) {
		this.pw = pw;
	}
	public String getPw() {
		return this.pw;
	}
}
