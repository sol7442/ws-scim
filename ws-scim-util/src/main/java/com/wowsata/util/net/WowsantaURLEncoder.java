package com.wowsata.util.net;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class WowsantaURLEncoder {

	private static final String[] findList = {"\\#","\\+","\\&","\\%","\\ "};
	private static final String[] replList = {"%23","%2B","%26","%25","%20"};
	
	public static String encode(String value, String enc) {
		try {
			String enc_value = URLEncoder.encode(value,enc);
			for(int i=0; i<findList.length; i++) {
				enc_value = enc_value.replaceAll(findList[i],replList[i]);
			}
			return enc_value;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
		
		
	}
}
