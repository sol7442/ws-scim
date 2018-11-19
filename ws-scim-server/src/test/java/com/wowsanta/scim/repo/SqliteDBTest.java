package com.wowsanta.scim.repo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

import com.wowsanta.scim.repository.DBCP;

public class SqliteDBTest {
	private final String config_file_name = "../config/sqlite_dbcp.json";

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
	
	@Test
	public void make() {
		DBCP dbcp = new DBCP();
		
		File path = new File(System.getProperty("user.dir"));
		String db_url = path.getParentFile().getAbsolutePath() + "\\ws-scim-repo\\data\\scim.db";
		System.out.println(db_url);
		
		dbcp.setJdbcUrl("jdbc:sqlite:" + db_url);
//		dbcp.setUserName("root");
//		dbcp.setPassword("wession@12");
		dbcp.setDriverName("org.sqlite.JDBC");
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
        	statement  = connection.prepareStatement("select * from user");
        	resultSet  = statement.executeQuery();
			
        	while(resultSet.next()) {
        		System.out.println("namge : " + resultSet.getString("id"));
        		System.out.println("namge : " + resultSet.getString("name"));
        	}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			DBCP.close(connection, statement, resultSet);
		}
	}
}
