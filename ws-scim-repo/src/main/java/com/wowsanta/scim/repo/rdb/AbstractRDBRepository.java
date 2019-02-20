package com.wowsanta.scim.repo.rdb;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.log.SCIMLogger;
import com.wowsanta.scim.resource.SCIMRepository;

public abstract class AbstractRDBRepository extends SCIMRepository {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DBCP dbcp;
	
	public AbstractRDBRepository() {
		setType("RDB");
	}
	@Override
	public void initialize() throws SCIMException {
		try {
			this.dbcp.setUp();
			SCIMLogger.sys("Repository initialize : {}", this.dbcp.getPoolName());
		} catch (Exception e) {
			throw new SCIMException("DBCP Setup Error ",e);
		}
	}
	
	@Override
	public void close() {
		SCIMLogger.sys("CLOSE DBCP {} ", this.dbcp.getPoolName());
	}
	
	public void initDBCP(DBCP dbcp) {
		this.dbcp = dbcp;
	}
	
	public Connection getConnection() throws SCIMException {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(this.dbcp.getPoolName());
		} catch (SQLException e) {
			throw new SCIMException("DBCP CONNECTION FAILED : " + this.dbcp.getPoolName(), e);
		}
		return connection;
	}

	public java.sql.Date toSqlDate(Date date) {
		if(date != null) {
			return new java.sql.Date(date.getTime());
		}else {
			return null;
		}	
	}
	public java.sql.Timestamp toSqlTimestamp(Date date) {
		if(date != null) {
			return new java.sql.Timestamp(date.getTime());
		}else {
			return null;
		}	
	}
	
	public String toString(Date date) {
		SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(date == null) {
			return "";
		}else {
			return transFormat.format(date);
		}
	}
	
	public Date toDate(String str) {
		SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(str == null) {
			return null;
		}else {
			try {
				return transFormat.parse(str);
			} catch (ParseException e) {
				return null;
			}
		}
	}
	public String toYN(boolean yn ) {
		if(yn) {
			return "Y";
		}else {
			return "N";
		}
	}
	public boolean toBoolean(String yn) {
		if(yn.equals("Y")) {
			return true;
		}else {
			return false;
		}
	}
	public boolean toBoolean(int yn) {
		if(yn == 1) {
			return true;
		}else {
			return false;
		}
	}
	public int toInteger(boolean yn) {
		if(yn) {
			return 1;
		}else {
			return 0;
		}
	}
	public Date toJavaDate(java.sql.Timestamp date) {
		if(date != null) {
			return new Date(date.getTime());
		}else {
			return null;
		}	
	}
	
	public Date toJavaDate(java.sql.Date date) {
		if(date != null) {
			return new Date(date.getTime());
		}else {
			return null;
		}	
	}
}
