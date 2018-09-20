package com.wession.scim.test;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.wession.common.JsonUtils;
import com.wession.jwt.JWTToken;

import net.minidev.json.JSONObject;

public class JwtTest {

	public static void main(String[] args) {
		
		JSONObject json = JsonUtils.getJSonFile("./sample/jwt.json");
		
		String token = JWTToken.createToken(json);
		
		System.out.println("token : " + token);
		
		JSONObject decodeJson = JWTToken.jwtDecode(token);
		
		System.out.println("header : \t" + decodeJson.get("header"));
		System.out.println("body : \t" + decodeJson.get("body"));
		
		System.out.println("exp : " + (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date((long) ((JSONObject)decodeJson.get("body")).get("exp"))));
		System.out.println("iat : " + (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date((long) ((JSONObject)decodeJson.get("body")).get("iat"))));
		
	}
}
