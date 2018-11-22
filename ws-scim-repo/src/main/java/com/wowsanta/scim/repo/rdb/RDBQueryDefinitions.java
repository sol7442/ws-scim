package com.wowsanta.scim.repo.rdb;

public class RDBQueryDefinitions {

	public static enum QueryType {
		SELECT, UPDATE, INCERT, DELETE
	}

	public static enum ColumType {
		SCIMATTRIBUTE,SCIMMAPPER,STRING,OBJECT
	}
}
