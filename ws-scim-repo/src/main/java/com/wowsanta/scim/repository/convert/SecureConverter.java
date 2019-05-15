package com.wowsanta.scim.repository.convert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wowsanta.scim.sec.encryptSHA256;

public class SecureConverter {
	static transient Logger logger = LoggerFactory.getLogger(SecureConverter.class);
	
	public Object StringToSHA256(Object object) {
		if(object == null) {
			return null;
		}
		
		String hash_string = null;
		try {
			hash_string = encryptSHA256.encrypt((String) object);
		}catch (Exception e) {
			logger.error(e.getMessage());
		}
		
		return hash_string;
	}
}
