package com.wowsanta.scim.protocol;

public enum ResponseState {
	
	Success("sucess",1),
	Fail("fail",0);;
	
	
	private String value;
	private int code;
	
	private ResponseState(String value,int code){
		this.value = value;
		this.code  = code;
	}
	
	public String getValue( ) {return this.value;}
	public int getCode() {return this.code;}
}
