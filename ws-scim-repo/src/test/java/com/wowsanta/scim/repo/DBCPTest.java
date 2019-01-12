

package com.wowsanta.scim.repo;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

import com.wowsanta.scim.repo.exception.RepositoryException;
import com.wowsanta.scim.repo.rdb.DBCP;


public class DBCPTest {
	
	private final String config_file_name = "../config/dbcp.json";

	private DBCP dbcp;
	
	@Before
	public void load() throws RepositoryException {
		try {
			this.dbcp =  DBCP.load(config_file_name);
			this.dbcp.setUp();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	//@Test
	public void make() {
		DBCP dbcp = new DBCP();
		
		dbcp.setJdbcUrl("jdbc:mysql://wession.com:3306/ws-scim-im");
		dbcp.setUserName("root");
		dbcp.setPassword("wession@12");
		dbcp.setDriverName("com.mysql.cj.jdbc.Driver");
		dbcp.setPoolName("cp");
	
		try {
			dbcp.save(this.config_file_name);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Test
	public void query() {
		Connection connection = null;
		PreparedStatement statement = null;
	    ResultSet resultSet = null;        


        try {
        	connection =  DriverManager.getConnection("jdbc:apache:commons:dbcp:cp");
        	statement  = connection.prepareStatement("select * from account");
        	resultSet  = statement.executeQuery();
			
        	while(resultSet.next()) {
        		System.out.println("namge : " + resultSet.getString("accountname"));
        	}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			DBCP.close(connection, statement, resultSet);
		}
	}
}
