package com.wowsanta.scim.message;

public class SCIMOperation {
	private String op;
	private String path;
	
	public String getOp() {
		return op;
	}
	public void setOp(String op) {
		this.op = op;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
}
