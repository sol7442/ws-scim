package com.wowsanta.scim.resource;

public class ServiceResult implements SCIMResult{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8146312744317013007L;
	
	private String code;
	private String message;
	private Object data;
	
	@Override
	public String getCode() {
		return this.code;
	}
	@Override
	public String getMessage() {
		return this.message;
	}
	@Override
	public Object getData() {
		return this.data;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public void setMessage(String msg) {
		this.message = msg;
	}
	public void setData(Object obj) {
		this.data = obj;
	}
}
