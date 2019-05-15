package com.wowsanta.scim.repository.convert;

public class BooleanConverter {
	public Object ynToInteger(Object yn) {
		if (yn instanceof String) {
			String yn_string = (String) yn;
			return yn_string.toUpperCase().equals("Y") ? 1:0;
		}else {
			return 0;
		}
	}
	
	public Object integerToYnDefaultY(Object yn) {
		if(yn == null) {
			return "Y";
		}
		
		if (yn instanceof String) {
			String str_integer = (String) yn;
			Integer value = Integer.valueOf(str_integer);

			return value == 1 ? "Y":"N"; 
		}else {
			return "Y";
		}
	}
	
	public Object ynToIntegerDefaultZero(Object yn) {
		if(yn == null) return 0; 
		
		if (yn instanceof String) {
			String yn_string = (String) yn;
			return yn_string.toUpperCase().equals("Y") ? 1:0;
		}else {
			return 0;
		}
	}
}
