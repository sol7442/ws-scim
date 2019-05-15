package com.wowsanta.scim.util;

public enum Boolean {
	YES(1,true),
	NO(0,false),
	Y(1,true),
	N(0,false)
	;
	
	private int intValue;
	private boolean booleanValue;
	Boolean(int int_value, boolean boolean_value){
		this.intValue     = int_value;
		this.booleanValue = boolean_value; 
	}
	
	public int toInteger() {
		return this.intValue;
	}
	public boolean toBoolean() {
		return this.booleanValue;
	}
}
