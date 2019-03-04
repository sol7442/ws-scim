package com.wowsanta.scim.resource.user;

import java.util.Date;

import com.wowsanta.scim.json.AbstractJsonObject;

public class LoginUser extends AbstractJsonObject{
	
	protected String 			userId;
	protected String 			userName;
	protected transient String 	password;
	protected LoginUserType 	type;
	protected UserStatus 		status;
	protected Date				pwExpireTime;
	
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public LoginUserType getType() {
		return type;
	}
	public void setType(LoginUserType type) {
		this.type = type;
	}
	public UserStatus getStatus() {
		return status;
	}
	public void setStatus(UserStatus status) {
		this.status = status;
	}
	public Date getPwExpireTime() {
		return pwExpireTime;
	}
	public void getPwExpireTime(Date pwExpireTime) {
		this.pwExpireTime = pwExpireTime;
	}
}
