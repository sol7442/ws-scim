package com.wowsanta.scim;

import java.io.FileReader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.log.SCIMLogger;
import com.wowsanta.scim.repository.SystemRepository;
import com.wowsanta.scim.resource.SCIMRepository;
import com.wowsanta.scim.resource.SCIMResourceRepository;
import com.wowsanta.scim.resource.SCIMSystemRepository;

public class SystemManager {

	private transient static SystemManager instance;
	
	private transient JsonObject jsonConfig;
	
	private SCIMSystemRepository systemRepository;
	private SCIMResourceRepository resourceRepository;
	
	public static SystemManager getInstance() {
		if(instance == null) {
			instance = new SystemManager();
		}
		return instance;
	}

	public SCIMSystemRepository getSystemRepository() {
		return systemRepository;
	}

	public void setSystemRepository(SCIMSystemRepository systemRepository) {
		this.systemRepository = systemRepository;
	}

	public SCIMResourceRepository getResourceRepository() {
		return resourceRepository;
	}

	public void setResourceRepository(SCIMResourceRepository resourceRepository) {
		this.resourceRepository = resourceRepository;
	}
	
	public void load(String conf_file) throws SCIMException{
		try {
			JsonReader reader = new JsonReader(new FileReader(conf_file));
			this.jsonConfig = new JsonParser().parse(reader).getAsJsonObject();

			JsonObject res_repo_json = this.jsonConfig.get("resourceRepository").getAsJsonObject();
			JsonObject sys_repo_json = this.jsonConfig.get("systemRepository").getAsJsonObject();
			
			this.resourceRepository = (SCIMResourceRepository)loadRepository(res_repo_json);
			this.systemRepository   = (SCIMSystemRepository)loadRepository(sys_repo_json);
			
		}catch(Exception e) {
			throw new SCIMException("System Manager Load Failed : " + conf_file ,e);
		}finally {
			SCIMLogger.sys("Load System Config : {} ", conf_file);
		}
	}

	private SCIMRepository loadRepository(JsonObject repository_json) throws ClassNotFoundException, SCIMException {
		String class_name = repository_json.get("repositoryClass").getAsString();
		Gson gson = new GsonBuilder().create();
		return (SCIMRepository)gson.fromJson(repository_json, Class.forName(class_name)); 
	}

	public void initialize() throws SCIMException {
		this.systemRepository.initialize();
		this.resourceRepository.initialize();
	}
}
