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


public class CreateRandomUserTest {
	private final String config_file_name = "../config/hr_dbcp.json";

	private DBCP dbcp;
	
	@Before
	public void load() {
		try {
			this.dbcp =  DBCP.load(config_file_name);
			this.dbcp.setUp();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
//	@Test
	public void make() {
		DBCP dbcp = new DBCP();
		
		dbcp.setJdbcUrl("jdbc:mysql://wession.com:3306/ws_scim_hr1?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC");
		dbcp.setUserName("root");
		dbcp.setPassword("wession@12");
		dbcp.setDriverName(com.mysql.cj.jdbc.Driver.class.getCanonicalName());
		dbcp.setPoolName("cp");
	
		try {
			dbcp.save(this.config_file_name);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//@Test
	public void create_user() {
		Connection connection = null;
		PreparedStatement statement = null;
	    ResultSet resultSet = null;
	    int count = 0;
        try {
        	connection =  DriverManager.getConnection("jdbc:apache:commons:dbcp:cp");
        	statement  = connection.prepareStatement("INSERT INTO USER (USER_ID, USER_NAME, USER_ORG, USER_ROLE, USER_POS) VALUES (?,?,?,?,?)");
        	while(count < 100) {
        		statement.setString(1, "USER_" + count);
            	statement.setString(2, "NAME_" + count);
            	statement.setString(3, "ORG");
            	statement.setString(4, "ROLE");
            	statement.setString(5, "POS");
            	
            	statement.executeUpdate();
            	count++;
        	}
        	
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			DBCP.close(connection, statement, resultSet);
		}
        System.out.println("create user ["+count+"]");
	}
}
