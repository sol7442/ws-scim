package com.wession.scim.controller;

import java.util.regex.Pattern;

import org.slf4j.Logger;

import com.wession.common.JsonUtils;
import com.wession.common.WessionLog;
import com.wession.scim.intf.JsonFilePath;

import net.minidev.json.JSONObject;

public class Password implements JsonFilePath {
	
	private static final String PASSWORD_PATTERN = "password_pattern";
	
	private static WessionLog wlog = new WessionLog();
	private static Logger processLog = wlog.getProcessLog();
	
	private static JSONObject patternJson = JsonUtils.getJSonFile(_pattern_file_path);
	private static String PASSWORD_REGEX = (String) patternJson.get(PASSWORD_PATTERN);
	
	
	/* JSON으로 비밀번호 정책 저장 
	  - 최소길이:8 
	  - 최대길이:20 
	  - 특수문자 포함여부 및 개수:1개필수 
	  - 영문자 포함여부 및 개수:1개필수 
	  - 숫자 포함여부 및 개수:1개필수
	  - 연속문자 제한 여부 (영문자, 숫자, 제한자리수, 역순) : 미적용
	  */
	/**
	 * 비밀번호 패턴 검증
	 * @param id
	 * @param passwd
	 * @return boolean
	 */
	public static boolean checkPassword(String id, String passwd) {
		
		passwd = passwd.replaceAll("\\s", ""); //trim \t \n ...
		boolean isValid = Pattern.matches(PASSWORD_REGEX, passwd);
		
		if(!isValid) {
			processLog.debug("password[{}] is in violation of the policy.", id);
		}
		
		return isValid;
	}
	
	/**
	 * 정책 패턴 파일갱신
	 */
	public static void reloadPattern() {
		patternJson = JsonUtils.getJSonFile(_pattern_file_path);
		PASSWORD_REGEX = (String) patternJson.get(PASSWORD_PATTERN);
	}

}
