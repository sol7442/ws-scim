package com.wowsanta.scim.resource;


import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
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
	
	private transient File configFile;
	public static SCIMRepositoryManager getInstance() {
		if(instance == null) {
			instance = new SCIMRepositoryManager();
		}
		return instance;
	}
	
	public void save() throws SCIMException {
		try {
			
			OutputStreamWriter writer = new OutputStreamWriter(
					new FileOutputStream(this.configFile),StandardCharsets.UTF_8);
			Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
			gson.toJson(this,writer);
			writer.flush();
			writer.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void load(File config_file) throws SCIMException {
		try {
			this.configFile = config_file;
			
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
			resourceRepository.validate();
		}
		if(systemRepository !=null) {
			systemRepository.initialize();
			systemRepository.validate();
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
