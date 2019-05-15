package com.wowsanta.scim.repository.convert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CnvInteger {

	private static Logger logger = LoggerFactory.getLogger(CnvInteger.class);
	public Object J2D(Object obj) {
		
		int value = 0;
		try {
			if (obj instanceof Integer) {
				value = (int) obj;
			}else if (obj instanceof Double) {
				Double obj_value = (Double) obj;
				value = obj_value.intValue();
			}else {
				logger.error("instance type {} : {}",obj, obj.getClass().getName());
			}
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}

		logger.debug("{}>{}",obj,value);
		
		return value;
	}
}
