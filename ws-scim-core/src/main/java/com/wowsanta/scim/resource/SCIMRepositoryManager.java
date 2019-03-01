package com.wowsanta.scim.resource;


import java.io.File;
import java.io.FileReader;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.json.AbstractJsonObject;

public class SCIMRepositoryManager {
	
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
	
	public void load(File config_file) throws SCIMException {
		try {
			
			JsonReader reader = new JsonReader(new FileReader(config_file));
			JsonObject json_object = new JsonParser().parse(reader).getAsJsonObject();
			
			if(json_object.get("systemRepository") != null) {
				JsonObject sys_rep_json_obj = json_object.get("systemRepository").getAsJsonObject();
				this.systemRepository  = (SCIMRepository) SCIMRepository.load(sys_rep_json_obj);;
			}
			
			if(json_object.get("resourceRepository") != null) {
				JsonObject res_rep_json_obj = json_object.get("resourceRepository").getAsJsonObject();
				this.resourceRepository = (SCIMRepository) SCIMRepository.load(res_rep_json_obj);
			}
			
			
		} catch (Exception e) {
			instance = null;
			throw new SCIMException("Repository Load Failed : " + config_file, e);
		}
	}
	
	public void initailze() throws SCIMException {
		if(resourceRepository != null) {
			resourceRepository.initialize();
		}
		if(systemRepository !=null) {
			systemRepository.initialize();
		}
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
