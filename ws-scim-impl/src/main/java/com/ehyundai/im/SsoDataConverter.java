package com.ehyundai.im;

public class SsoDataConverter {
	public Object defaultOrgId(Object data) {
		return "111111";
	}
	
	public Object OrgPathConverter(Object data) {
		if(data == null) {
			return "11111";
		}
		
		StringBuffer buffer = new StringBuffer();
		buffer.append("/");
		
		String[] paths = ((String)data).split(";");
		for (String path : paths) {
			buffer.append(path).append("/");
		}
		
		return buffer.toString();
	}
}
