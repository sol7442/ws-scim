package com.wession.jwt;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Iterator;

import com.wession.scim.schema.Error;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import net.minidev.json.JSONObject;

public class JWTToken {
	
	//			iss: 토큰을 발급한 발급자(Issuer)
	//			sub: Claim의 주제(Subject)로 토큰이 갖는 문맥을 의미한다.
	//			aud: 이 토큰을 사용할 수신자(Audience)
	//			exp: 만료시간(Expiration Time)은 만료시간이 지난 토큰은 거절해야 한다.
	//			nbf: Not Before의 의미로 이 시간 이전에는 토큰을 처리하지 않아야 함을 의미한다.
	//			iat: 토큰이 발급된 시간(Issued At)
	//			jti: JWT ID로 토큰에 대한 식별자이다.
    private static final String SUBJECT = "SCIM/USER";	//Claim의 주제(Subject)
    private static final String ISSUER = "SCIM";			//토큰을 발급한 발급자(Issuer)
    private static final String SIGNING_KEY = "wession@%&^tokenTest34olsjasdf##8";

	public static String createToken(JSONObject json) {
		String jwtStr = null;
		
		try {
			JwtBuilder jwtBuilder = Jwts.builder();
			jwtBuilder.setSubject(SUBJECT);
			jwtBuilder.setIssuer(ISSUER);
			jwtBuilder.setHeaderParam("typ", "JWT");
			jwtBuilder.setHeaderParam("issueDate", System.currentTimeMillis());
			jwtBuilder.setIssuedAt(new Date(System.currentTimeMillis()));
			jwtBuilder.setExpiration(new Date(System.currentTimeMillis() + 1000L * 60L * 60L * 24L * 365L));
			
			Iterator<String> keys = json.keySet().iterator();
			while (keys.hasNext()) {
				String key = keys.next();
				jwtBuilder.claim(key, json.get(key));
			}
			jwtStr = jwtBuilder.signWith(SignatureAlgorithm.HS256, SIGNING_KEY.getBytes("UTF-8")).compact();
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return jwtStr;
	}

	
	public static JSONObject jwtDecode(String jwtToken) {

		JSONObject json = new JSONObject();
		
		int status = 0;
		String detail = "success";

		try {
			Jws<Claims> jwt = Jwts.parser().setSigningKey(SIGNING_KEY.getBytes("UTF-8")).parseClaimsJws(jwtToken);
			
//			System.out.println((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(jwt.getBody().getExpiration()));
			
			JSONObject header = makeJson(jwt.getHeader());
			json.put("header", header);
			
			JSONObject body = makeJson(jwt.getBody());
			json.put("body", body);
			
			json.put("status", status+"");
			json.put("detail", detail);
			
		} catch (SignatureException e) {
			status = 403;
			detail = e.getMessage();
			return new Error(status, detail);
			
		} catch (ExpiredJwtException e) {
			status = 403;
			detail = e.getMessage();
			return new Error(status, detail);
		
		} catch (UnsupportedJwtException e) {
			status = 403;
			detail = e.getMessage();
			return new Error(status, detail);
		
		} catch (MalformedJwtException e) {
			status = 403;
			detail = e.getMessage();
			return new Error(status, detail);
		
		} catch (IllegalArgumentException e) {
			status = 403;
			detail = e.getMessage();
			return new Error(status, detail);
		
		} catch (UnsupportedEncodingException e) {
			status = 403;
			detail = e.getMessage();
			return new Error(status, detail);
		}
		
		return json;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static JSONObject makeJson(JwsHeader jwsHeader) {
		
		JSONObject json = new JSONObject();
		
		Iterator<String> iters = jwsHeader.keySet().iterator();
		while (iters.hasNext()) {
			String key = iters.next();
			json.put(key, jwsHeader.get(key));
		}
		return json;
	}
	
	public static JSONObject makeJson(Claims claims) {
		
		JSONObject json = new JSONObject();
		
//		System.out.println("claims.getExpiration:" + (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(claims.getExpiration()));
//		System.out.println("claims.getIssuedAt:" + (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(claims.getIssuedAt()));
//		System.out.println(claims.getExpiration().getClass());
//		System.out.println(claims.getIssuedAt().getClass());
		Iterator<String> iters = claims.keySet().iterator();
		while (iters.hasNext()) {
			String key = iters.next();
//			System.out.println(key + " : " + claims.get(key).getClass());
			json.put(key, claims.get(key));
		}
		json.put("iat", claims.getExpiration().getTime());
		json.put("exp", claims.getIssuedAt().getTime());
		return json;
	}
 
}
