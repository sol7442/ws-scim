package com.wowsanta.scim.repo.rdb;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;
import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.log.SCIMLogger;
import com.wowsanta.scim.repository.AbstractSCIMRepository;
import com.wowsata.util.json.JsonException;
import com.wowsata.util.json.WowsantaJson;

public abstract class AbstractRDBRepository extends AbstractSCIMRepository {
	
	private transient Logger logger = LoggerFactory.getLogger(AbstractRDBRepository.class);
	
	private static final long serialVersionUID = 1L;
	protected DBCP dbcp;
	protected String tableName;
	
	public AbstractRDBRepository() {
		setType("RDB");
	}
	
	public DBCP getDbcp() {
		return this.dbcp;
	}
	
//	@Override
//	public void fromJson(JsonObject jsonObject)throws SCIMException{
//		System.out.println("jsonObject : " + jsonObject);
//		setClassName(jsonObject.get("className").getAsString());
//		setType(jsonObject.get("type").getAsString());
//				
//		this.dbcp = new DBCP();
//		this.dbcp.parse(jsonObject.get("dbcp").getAsJsonObject());
//		
//		this.tableName = jsonObject.get("tableName").getAsString();
//	}
	

	@Override
	public void initialize() throws SCIMException {
		try {
			this.dbcp.setUp();
			logger.info("Repository initialize : {}", this.dbcp.getPoolName());
		} catch (Exception e) {
			throw new SCIMException("DBCP Setup Error ",e);
		}
	}
	
	@Override
	public boolean validate() throws SCIMException {
		final String selectSQL = this.dbcp.getValiationQuery();//
		Connection connection = null;
		PreparedStatement statement = null;
	    ResultSet resultSet = null;        

        try {
        	connection = getConnection();
        	statement  = connection.prepareStatement(selectSQL);
        	resultSet = statement.executeQuery();
        	
		} catch (SQLException e) {
			throw new SCIMException(selectSQL, e);
		}finally {
			DBCP.close(connection, statement, resultSet);
		}
        logger.info("REPOSITORY VAILDATE : {} ", selectSQL);	
		return true;
	}
	
	@Override
	public void close() {
		logger.info("CLOSE DBCP {} ", this.dbcp.getPoolName());
	}
	
	public void initDBCP(DBCP dbcp) {
		this.dbcp = dbcp;
	}
	
	public Connection getConnection() throws SCIMException {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(this.dbcp.getPoolName());
		} catch (SQLException e) {
			logger.info("{}, - RETRY ", e.getMessage() );
			initialize();			
			//throw new SCIMException("DBCP CONNECTION FAILED : " + this.dbcp.getPoolName(), e);
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
	
	public String toStringDay(Date date) {
		SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");
		if(date == null) {
			return "";
		}else {
			return transFormat.format(date);
		}
	}
	public Date dayToJavaDate(String str) {
		SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");
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
		
		System.out.println("active : " + yn);
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
