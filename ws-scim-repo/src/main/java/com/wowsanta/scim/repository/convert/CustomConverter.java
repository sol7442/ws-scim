package com.wowsanta.scim.repository.convert;

public class CustomConverter {
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
		if (data instanceof Integer) {
			int int_value = (int) data;
			if(int_value == 1) {
				return 0;
			}else {
				return 1;
			} 
		}
		return value;
	}	
}
