package com.wowsanta.scim.resource;


import java.io.File;
import java.io.FileReader;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.json.AbstractJsonObject;

public class SCIMRepositoryManager extends AbstractJsonObject {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7705904809644253370L;
	private static transient SCIMRepositoryManager instance;
	
	private SCIMRepository resourceRepository;
	private SCIMRepository systemRepository;
	
	public static SCIMRepositoryManager getInstance() {
		if(instance == null) {
			instance = new SCIMRepositoryManager();
		}
		return instance;
	}
	
	public static void load(String json_file_path) throws SCIMException {
		if(instance == null) {
			try {
				instance = new SCIMRepositoryManager();
				
				JsonReader reader = new JsonReader(new FileReader(json_file_path));
				JsonObject config = new JsonParser().parse(reader).getAsJsonObject();
				instance.resourceRepository = (SCIMRepository) load(config.get("resourceRepository").getAsJsonObject());
				instance.systemRepository = (SCIMRepository) load(config.get("systemRepository").getAsJsonObject());
			} catch (Exception e) {
				instance = null;
				throw new SCIMException("Repository Load Failed : " + json_file_path, e);
			}
		}
	}
	
	public void initailze() throws SCIMException {
		resourceRepository.initialize();
		systemRepository.initialize();
	}
	public void setResourceRepository(SCIMResourceRepository repo) {
		this.resourceRepository = (SCIMRepository) repo;
	}
	public void setSystemRepository(SCIMSystemRepository repo) {
		this.systemRepository = (SCIMRepository) repo;
	}
	public SCIMResourceRepository getResourceRepository() {
		return (SCIMResourceRepository) this.resourceRepository;
	}
	public SCIMSystemRepository getSystemRepository() {
		return (SCIMSystemRepository) this.systemRepository;
	}
	
	public void close() throws SCIMException {
		this.resourceRepository.close();
		this.systemRepository.close();
	}


}
