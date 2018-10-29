package com.wowsanta.scim.filter;

public enum SCIMFilterType {
	  AND("and"),
	  OR("or"),
	  EQUALITY("eq"),
	  CONTAINS("co"),
	  STARTS_WITH("sw"),
	  PRESENCE("pr"),
	  GREATER_THAN("gt"),
	  GREATER_OR_EQUAL("ge"),
	  LESS_THAN("lt"),
	  LESS_OR_EQUAL("le"),
	  UNKNOWN("", "unknown operator");
	
	private String stringValue;
	
	private SCIMFilterType(final String stringValue, String value){
		this.stringValue = stringValue;
	}
	private SCIMFilterType(final String stringValue){
		this.stringValue = stringValue;
	}
	public String getStringValue() {
	    return stringValue;
	}
	
	public String toString() {
		return stringValue;
	}
}
