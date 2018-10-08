package com.wowsanta.scim.server.auth.login;

import com.wowsanta.scim.spark.AbstractResponse;

public class LoginResponse extends AbstractResponse {
	private int code;
	private String result;
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	private String url;
}
