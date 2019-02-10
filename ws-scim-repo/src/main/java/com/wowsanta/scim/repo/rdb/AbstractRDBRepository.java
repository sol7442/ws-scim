package com.wowsanta.scim.repo.rdb;


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
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
	
	public Connection getConnection() {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(this.dbcp.getPoolName());
		} catch (SQLException e) {
			e.printStackTrace();
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
	public Date toJavaDate(java.sql.Date date) {
		if(date != null) {
			return new Date(date.getTime());
		}else {
			return null;
		}	
	}
	
	public static AbstractRDBRepository load(String file_name) throws FileNotFoundException {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonReader reader = new JsonReader(new FileReader(file_name));
		return gson.fromJson(reader,AbstractRDBRepository.class);
	}

}
