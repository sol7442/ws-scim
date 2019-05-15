package com.wowsanta.scim.repository.resource;

import java.io.FileReader;
import java.io.FileWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.wowsanta.scim.repo.rdb.DBCP;
import com.wowsanta.scim.repository.RepositoryDefinitions.REPOSITORY_TYPE;

public class ResourceRepositoryConfig {
	
	transient Logger logger = LoggerFactory.getLogger(ResourceRepositoryConfig.class);
	
	private REPOSITORY_TYPE type;
	private DBCP dbcp;
	private String userOutputMapper;
	private String userInputMapper;
	private String groupOutputMapper;
	private String groupInputMapper;
	
	private String systemUserInputMapper;
	private String systemUseroutputMapper;
	
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
	
	public String getUserOutputMapper() {
		return userOutputMapper;
	}
	public void setUserOutputMapper(String userOutputMapper) {
		this.userOutputMapper = userOutputMapper;
	}
	public String getUserInputMapper() {
		return userInputMapper;
	}
	public void setUserInputMapper(String userInputMapper) {
		this.userInputMapper = userInputMapper;
	}
	public String getGroupOutputMapper() {
		return groupOutputMapper;
	}
	public void setGroupOutputMapper(String groupOutputMapper) {
		this.groupOutputMapper = groupOutputMapper;
	}
	public String getGroupInputMapper() {
		return groupInputMapper;
	}
	public void setGroupInputMapper(String groupInputMapper) {
		this.groupInputMapper = groupInputMapper;
	}
	
	public String toString(boolean pretty) {
		try {
			GsonBuilder builder = new GsonBuilder().disableHtmlEscaping();
			if(pretty) {
				builder.setPrettyPrinting();
			}
			Gson gson  = builder.create();
			return gson.toJson(this);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
	public void save(String fileName) {
		
		try {
			GsonBuilder builder = new GsonBuilder().disableHtmlEscaping();
			builder.setPrettyPrinting();			
			Gson gson = builder.create();

			FileWriter writer = new FileWriter(fileName);
			writer.write(gson.toJson(this));
			writer.close();
			
		}catch (Exception e) {
		}
	}
	public static ResourceRepositoryConfig load(String json_config_file) {
		try {
			Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
			JsonReader reader = new JsonReader(new FileReader(json_config_file));
			return gson.fromJson(reader, ResourceRepositoryConfig.class);
		} catch (Exception e) {
			
		}
		return null;
	}
	public String getSystemUserInputMapper() {
		return systemUserInputMapper;
	}
	public void setSystemUserInputMapper(String systemUserInputMapper) {
		this.systemUserInputMapper = systemUserInputMapper;
	}
	public String getSystemUseroutputMapper() {
		return systemUseroutputMapper;
	}
	public void setSystemUseroutputMapper(String systemUseroutputMapper) {
		this.systemUseroutputMapper = systemUseroutputMapper;
	}
}
