package com.wession.scim.policy;

import java.util.regex.Pattern;

import org.slf4j.Logger;

import com.wession.common.WessionLog;

public class Restrict {

	private static int maxLength = 10;
	private static int minLength = 5;
	private static String Pattern_String_externalId = "^[a-zA-Z0-9|_|\\-|\\.]*$";
	
	private static WessionLog wlog = new WessionLog();
	private static Logger processLog = wlog.getProcessLog();
			
	public static void main(String[] args) {
		Restrict rtc = new Restrict();
		String id = "WS002011";
		System.out.println(rtc.checkExternalId(id));
	}

	public static boolean checkExternalId(String id) {
		// 최소길이
		// 최대길이
		// 적용패턴 (영문자로만 구성, 영문자+숫자로만 구성, 숫자로만 구성, 첫n개 문자는 영대문자, dot, dash, underbar 사용)
		if (id == null) return false;
		int length = id.length();
		
		if (length < minLength || length > maxLength) {
			processLog.debug("externalId[{0}] is in violation of the policy(length).", id);
			return false;
		}
		boolean flag = Pattern.matches(Pattern_String_externalId, id); 
		if (!flag) processLog.debug("externalId[{0}] is in violation of the policy(pattern).", id);
		return flag;
	}
}
