package com.wowsanta.scim.protocol;

public class FrontResponse {
	private ResponseState state;
	private String message;
	private Object data;
	public ResponseState getState() {
		return state;
	}
	public void setState(ResponseState state) {
		this.state = state;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
}
