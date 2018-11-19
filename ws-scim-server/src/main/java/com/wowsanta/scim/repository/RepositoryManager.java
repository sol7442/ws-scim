package com.wowsanta.scim.repository;

import java.sql.SQLException;

import com.wowsanta.scim.repository.DBCP;

public class RepositoryManager {
	public void initialize() throws ClassNotFoundException, SQLException {
		new DBCP().setUp();
	}
}
