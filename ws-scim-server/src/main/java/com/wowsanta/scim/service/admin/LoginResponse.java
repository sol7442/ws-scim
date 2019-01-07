package com.wowsanta.scim.service.admin;

import com.wowsanta.scim.service.SCIMResponse;

public class LoginResponse implements SCIMResponse{
	private int code;
	public void setCode(int code) {
		this.code = code;
	}
	public int getCode() {
		return this.code;
	}
}
