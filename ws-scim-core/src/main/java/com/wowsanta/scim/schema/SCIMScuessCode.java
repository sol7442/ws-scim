package com.wowsanta.scim.schema;

public final class SCIMScuessCode {
	private String status = "200";

	public static final SCIMScuessCode OK = new SCIMScuessCode();
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
