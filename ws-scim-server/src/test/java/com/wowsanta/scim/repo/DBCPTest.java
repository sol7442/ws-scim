package com.wowsanta.scim.repo;

import com.wowsanta.scim.repository.DBCP;

public class DBCPTest {
	
	private final String config_file_name = "../config/dbcp.json";

	public void make() {
		DBCP dbcp = new DBCP();
		
		dbcp.setJdbcUrl("wession.com");
		dbcp.setUserName("");
		dbcp.setPassword("");
		dbcp.setDriverName("");
		dbcp.setPoolName("");
		
	}
}
