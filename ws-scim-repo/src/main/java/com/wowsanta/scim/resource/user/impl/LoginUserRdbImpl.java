package com.wowsanta.scim.resource.user.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.wowsanta.scim.resource.user.LoginUser;
import com.wowsanta.scim.resource.user.LoginUserType;
import com.wowsanta.scim.resource.user.UserStatus;

public class LoginUserRdbImpl extends LoginUser {

//	public LoginUserRdbImpl(ResultSet result) throws SQLException {
//		super();
//		this.userId 		= result.getString("USERID");
//		this.userName 		= result.getString("USERNAME");
//		this.password   	= result.getString("PASSWORD");
//		this.type			= LoginUserType.valueOf(result.getString("USERTYPE"));
//		this.status     	= UserStatus.valueOf(result.getInt("ACTIVE"));
//		this.pwChangeDate 	= result.getDate("PWCHANGEDATE");
//	}
	public LoginUserRdbImpl(ResultSet result) throws SQLException {
		super();
		this.userId 		= result.getString("ADMINID");
		this.userName 		= result.getString("ADMINNAME");
		this.password   	= result.getString("PASSWD");
		this.type			= LoginUserType.valueOf(result.getString("ADMINTYPE"));
		this.status     	= UserStatus.valueOf(result.getInt("ACTIVE"));
		this.pwExpireTime 	= result.getDate("PWEXPIRETIME");
	}
}
