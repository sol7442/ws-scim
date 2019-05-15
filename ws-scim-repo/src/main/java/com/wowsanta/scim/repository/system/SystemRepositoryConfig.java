package com.wowsanta.scim.repository.system;

import com.wowsanta.scim.repo.rdb.DBCP;
import com.wowsanta.scim.repository.RepositoryDefinitions.REPOSITORY_TYPE;

public class SystemRepositoryConfig {
	private REPOSITORY_TYPE type;
	private DBCP dbcp;
	
	public REPOSITORY_TYPE getType() {
		return type;
	}
	public void setType(REPOSITORY_TYPE type) {
		this.type = type;
	}
	public DBCP getDbcp() {
		return dbcp;
	}
	public void setDbcp(DBCP dbcp) {
		this.dbcp = dbcp;
	}
}
