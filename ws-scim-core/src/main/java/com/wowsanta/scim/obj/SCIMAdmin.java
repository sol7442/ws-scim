package com.wowsanta.scim.obj;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wowsanta.scim.resource.user.LoginUserType;

public class SCIMAdmin {

	static transient Logger logger = LoggerFactory.getLogger(SCIMAdmin.class);
	
	private String adminId;
	private String adminName;
	private String adminType;
	private int state;
	private String password;
	private Date   loginTime;
	private Date   pwExpireTime;
	
	public SCIMAdmin() {}
	public SCIMAdmin(SCIMUser user) {
		this.adminId   = user.getId();
		this.adminName = user.getUserName();
		this.adminType = LoginUserType.ADMIN.toString();
	}
	
	public String getAdminId() {
		return adminId;
	}
	public void setAdminId(String adminId) {
		this.adminId = adminId;
	} 
	public String getAdminName() {
		return adminName;
	}
	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}
	public String getAdminType() {
		return adminType;
	}
	public void setAdminType(String adminType) {
		this.adminType = adminType;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Date getLoginTime() {
		return loginTime;
	}
	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}
	public Date getPwExpireTime() {
		return pwExpireTime;
	}
	public void setPwExpireTime(Date pwExpireTime) {
		this.pwExpireTime = pwExpireTime;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	
	public static SCIMAdmin parse(String result_string) {
		SCIMAdmin admin = null;
		try {
			Gson gson = new GsonBuilder().disableHtmlEscaping().create();
			admin = gson.fromJson(result_string, SCIMAdmin.class);
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return admin;
	}
	
	public String toString() {
		return toString(false);
	}
	public String toString(boolean pretty) {
		try {
			GsonBuilder builder = new GsonBuilder().disableHtmlEscaping();
			if (pretty) {
				builder.setPrettyPrinting();
			}
			Gson gson = builder.create();
			return gson.toJson(this);
		} catch (Exception e) {
			logger.error(e.getMessage() + " : ",  e);
		}
		return null;
	}

}
