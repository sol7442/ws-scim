package com.wowsanta.scim.sec;

import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.obj.SCIMUser;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

public class SCIMJWTToken {

	private String userId;
	private String userName;
	private String userType;
	
	private Date issueTime;
	private Date expireTime;
	
	private int status;
	private String detail;

	private String key 		= "WS-SCIM@12334";
	private String issuer 	= "ws-scim";
	
	public void setUserId(String id) {
		this.userId = id;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserName(String name) {
		this.userName = name;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setExpireTime(long duration) {
		this.expireTime = new Date(System.currentTimeMillis() + duration);
	}

	public Date getExpireTime() {
		return this.expireTime;
	}
	
	public Date getIssueTime() {
		return this.issueTime;
	}

	public int getStatus() {
		return this.status;
	}
	public String getDetail() {
		return this.detail;
	}
	public String getUserType() {
		return this.userType;
	}
	public void setUserType(String type) {
		this.userType = type;
	}
	public String issue() throws SCIMException {
		return issue(this.issuer, this.key);
	}
	
	public String issue(String issuer, String key) throws SCIMException {
		JwtBuilder builder = Jwts.builder();

		this.issueTime = new Date(System.currentTimeMillis());
		
		builder.setHeaderParam("type", Header.JWT_TYPE);
		builder.setHeaderParam("alg", "HS256");

		builder.setIssuer(issuer);
		builder.setIssuedAt(this.issueTime);
		builder.setExpiration(this.expireTime);
		
		builder.claim("userId", this.userId);
		builder.claim("userName", this.userName);
		builder.claim("userType", this.userType);
		
		return builder.signWith(SignatureAlgorithm.HS256, key.getBytes()).compact();
	}

	public SCIMUser  verify(String token) throws SCIMException {
		return verify(token, this.key);
	}
	public SCIMUser verify(String token, String key) throws SCIMException {
		
		SCIMUser user = null;
		try {
			Jws<Claims> claims = Jwts.parser().setSigningKey(key.getBytes()).parseClaimsJws(token);
			this.userId = (String) claims.getBody().get("userId");
			this.userName = (String) claims.getBody().get("userName");
			this.issueTime  = claims.getBody().getIssuedAt();
			this.expireTime = claims.getBody().getExpiration();
			
			this.status = 0;
			this.detail = "OK";
			
			user = new SCIMUser();
			user.setId((String) claims.getBody().get("userId"));
			user.setUserName((String) claims.getBody().get("userName"));

		} catch (SignatureException e) {
			status = 403;
			detail = e.getMessage();
			throw new SCIMException("JWT-Token Vaildation failed : " + e.getMessage(), e);
		} catch (ExpiredJwtException e) {
			status = 404;
			detail = e.getMessage();
			throw new SCIMException("JWT-Token Vaildation failed : " + e.getMessage(), e);
		} catch (UnsupportedJwtException e) {
			status = 405;
			detail = e.getMessage();
			throw new SCIMException("JWT-Token Vaildation failed : " + e.getMessage(), e);
		} catch (MalformedJwtException e) {
			status = 406;
			detail = e.getMessage();
			throw new SCIMException("JWT-Token Vaildation failed : " + e.getMessage(), e);
		} catch (IllegalArgumentException e) {
			status = 407;
			detail = e.getMessage();
			throw new SCIMException("JWT-Token Vaildation failed : " + e.getMessage(), e);
		}  
		
		return user;
	}

	public boolean isAvailable() {
		return this.expireTime.after(new Date(System.currentTimeMillis()));
	}

	public String toString() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(this);
	}
}

