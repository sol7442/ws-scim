package com.wowsanta.scim.sec;

import org.junit.Test;

import com.wowsanta.scim.exception.SCIMException;

public class JWTTokenTest {

	private final String issuer_name = "SCIM";
	private final String issuer_key = "SCIM_KEY_@1234";
	
	private final String expired_str_token = "eyJ0eXBlIjoiSldUIiwiYWxnIjoiSFMyNTYifQ.eyJpc3MiOiJTQ0lNIiwiaWF0IjoxNTQ2OTUxNDE0LCJleHAiOjE1NDY5NTE0NzQsInVzZXJJZCI6InVzZXJJZCIsInVzZXJOYW1lIjoidXNlck5hbWUifQ._MKwES_MfbIpAeIYRJZGoreZvmAsdvd139QxJN4EK5I";
	
	
	@Test
	public void issueNverifyTest() throws SCIMException {
		SCIMJWTToken issue_token = new SCIMJWTToken();
		issue_token.setUserId("userId");
		issue_token.setUserName("userName");
		issue_token.setExpireTime(1000*60); // 1분
		
		String str_token = issue_token.issue(issuer_name,issuer_key);
		SCIMJWTToken verify_token = new SCIMJWTToken();

		try {
			verify_token.verify(str_token, issuer_key);
		} catch (SCIMException e) {
			e.printStackTrace();
		}
		
		System.out.println("==[token_str]=============================");
		System.out.println(str_token);
		System.out.println("==[issue_token]=============================");
		System.out.println(issue_token);
		System.out.println("==[verify_token]=============================");
		System.out.println(verify_token);
		
		System.out.println("isAvailable : " + issue_token.isAvailable());
	}
	
	@Test
	public void expiredTokenTest() {
		SCIMJWTToken verify_token = new SCIMJWTToken();
		try {
			verify_token.verify(expired_str_token, issuer_key);
		} catch (SCIMException e) {
			System.out.println(e.getMessage());
		}
		System.out.println(verify_token);
	}
	
	@Test
	public void keyTokenTest() {
		SCIMJWTToken verify_token = new SCIMJWTToken();
		try {
			verify_token.verify(expired_str_token, issuer_key + "error");
		} catch (SCIMException e) {
			System.out.println(e.getMessage());
		}
		System.out.println(verify_token);
		
	}
}
