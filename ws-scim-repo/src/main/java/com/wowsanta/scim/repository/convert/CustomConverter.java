package com.wowsanta.scim.repository.convert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomConverter {
	static transient Logger logger = LoggerFactory.getLogger(CustomConverter.class);
	public Object OrgPathConverter(Object data) {
		if(data == null) {
			return "/ORGROOT/";
		}
		
		StringBuffer buffer = new StringBuffer();
		buffer.append("/ORGROOT/");
		
		String[] paths = ((String)data).split(";");
		
		if(paths.length == 1) { // ORGROOT
			return buffer.toString();
		}
		
		//
		for (String path : paths) {
			buffer.append(path).append("/");
		}
		
		return buffer.toString();
	}
	
	public Object AcvtiveReversConverter(Object data) {
		int value = 0;
		if (data instanceof java.math.BigDecimal) {
			java.math.BigDecimal int_value = (java.math.BigDecimal) data;
			if(int_value.intValue() == 1) {
				return 0;
			}else {
				return 1;
			} 
		}else {
			if(data != null) {
				logger.info("instance of : {}", data.getClass().getCanonicalName());
			}else {
				logger.info("instance of : {}", "null");
			}
			
		}
		return value;
	}	
}
