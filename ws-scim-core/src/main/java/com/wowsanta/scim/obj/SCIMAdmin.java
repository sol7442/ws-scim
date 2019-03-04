package com.wowsanta.scim.obj;

import java.util.Date;

import com.wowsanta.scim.json.AbstractJsonObject;
import com.wowsanta.scim.resource.user.LoginUserType;

public class SCIMAdmin extends AbstractJsonObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3655609649725263735L;
	
	private String adminId;
	private String adminName;
	private String adminType;
	private transient String password;
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

}
