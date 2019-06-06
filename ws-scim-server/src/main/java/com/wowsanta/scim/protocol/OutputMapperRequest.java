package com.wowsanta.scim.protocol;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wowsanta.scim.repository.RepositoryOutputMapper;

public class OutputMapperRequest implements JsonRequest{
	
	static transient Logger logger = LoggerFactory.getLogger(OutputMapperRequest.class);
	
	String name;
	String type;
	RepositoryOutputMapper mapper;
	 
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public RepositoryOutputMapper getMapper() {
		return mapper;
	}
	public void setMapper(RepositoryOutputMapper mapper) {
		this.mapper = mapper;
	}
	
	public String toString() {
		return toString(false);
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
}
