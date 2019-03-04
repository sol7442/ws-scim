package com.wowsanta.scim.resource.user;

public enum UserStatus {
	Y("1",1,true),N("0",0,false);
	
	private String  stringValue;
	private int 	numberValue;
	private boolean booleanValue;
	
	private UserStatus(String str, int num, boolean b) {
		this.stringValue  = str;
		this.numberValue     = num;
		this.booleanValue = b;
	}
	
	public String getString() {
		return this.stringValue;
	}
	public int getNumber() {
		return this.numberValue;
	}
	public boolean getBoolean() {
		return this.booleanValue;
	}

	public static UserStatus valueOf(int value) {
		if(value == Y.getNumber()) {
			return Y;
		}else {
			return N;
		}
	}
}
