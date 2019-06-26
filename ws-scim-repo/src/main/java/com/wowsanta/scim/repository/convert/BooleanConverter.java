package com.wowsanta.scim.repository.convert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BooleanConverter {
	static Logger logger = LoggerFactory.getLogger(BooleanConverter.class);
	
	public Object workToInteger(Object yn) {
		if (yn instanceof String) {
			String yn_string = (String) yn;
			return yn_string.toUpperCase().equals("재직") ? 1:0;
		}else {
			return 0;
		}
	}
	
	public Object ynToInteger(Object yn) {
		int value = 0;
		try {
			if(yn != null) {
				if (yn instanceof String) {
					String yn_string = (String) yn;
					value = yn_string.toUpperCase().equals("Y") ? 1:0;
					logger.debug("{}>{}",yn,value);
				}else {
					value = 0;
				}
			}else {
				logger.debug("value is null");
			}
			
		}catch(Exception e) {
			logger.error("{}",e.getMessage(),e);
		}
		return value;
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
	
	public Object reversNumber(Object number) {
		if (number instanceof Integer) {
			int value  = (Integer) number;
			return value == 0 ? 1 : 0;
		}else {
			return 0;
		}
	}
}
