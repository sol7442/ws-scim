package com.wowsanta.scim.repository;

import java.sql.SQLException;

public class RepositoryManager {
	public void initialize() throws ClassNotFoundException, SQLException {
		new DBCP().setUp();
	}
}
