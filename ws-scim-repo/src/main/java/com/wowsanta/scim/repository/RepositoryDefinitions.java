package com.wowsanta.scim.repository;

public class RepositoryDefinitions {

	public enum REPOSITORY_TYPE{
		ORACLE("11g","com.wowsanta.scim.repository.impl.OracleRepository"),
		MSSQL("2014","com.wowsanta.scim.repository.impl.MsSqlRepository"),
		MYSQL("7",null),
		SQLITE("5.3",null)
		;
		
		private String version;
		private String className;
		REPOSITORY_TYPE(String version, String className){
			this.version = version;
			this.className = className;
		}
		
		public String getVersion() {return this.version;}
		public String getClassName() {return this.className;}
	}
}
