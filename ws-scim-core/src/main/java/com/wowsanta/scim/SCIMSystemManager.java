package com.wowsanta.scim;

import java.io.File;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.log.SCIMLogger;
import com.wowsanta.scim.resource.SCIMRepository;
import com.wowsanta.scim.resource.SCIMRepositoryManager;
import com.wowsanta.scim.resource.SCIMResouceFactory;
import com.wowsanta.scim.service.SCIMServiceProvider;

public class SCIMSystemManager {

	private transient static SCIMSystemManager instance;

	private SCIMServiceProvider serviceProvider;
	public static SCIMSystemManager getInstance() {
		if (instance == null) {
			instance = new SCIMSystemManager();
		}
		return instance;
	}

	public void load(String conf_file_path) throws SCIMException {
		File config_file = new File(conf_file_path);
		try {
			this.serviceProvider = (SCIMServiceProvider) SCIMServiceProvider.load(config_file);
			
			SCIMResouceFactory.getInstance().setUserClass(
					this.serviceProvider.getResources().get("user").getSchema(),
					this.serviceProvider.getResources().get("user").getClassName());
			
			load_repository(this.serviceProvider.getRepositoryConfig());
			
			
		} catch (Exception e) {
			throw new SCIMException("LOAD CONFIG FILE ERROR : " + conf_file_path, e);
		} finally {
			SCIMLogger.sys("SERVICE INFO ===={}-{} \n{}", config_file.getName(), new Date(config_file.lastModified()),
					this.serviceProvider.toString(true));
		}
	}

	public SCIMServiceProvider getServiceProvider() {
		return serviceProvider;
	}

	public void setServiceProvider(SCIMServiceProvider servicveProvider) {
		this.serviceProvider = servicveProvider;
	}
	
	private void load_repository(String repository_config) {
		
		try {
			SCIMRepositoryManager.load(repository_config);
		} catch (SCIMException e) {
			e.printStackTrace();
		}
		
//		SCIMRepository repository = null;
//		try {
//			JsonObject repo_json = system_conf.get(repository_name).getAsJsonObject();
//			String class_name = repo_json.get("repositoryClass").getAsString();
//			Gson gson = new GsonBuilder().create();
//			
//			repository = (SCIMRepository)gson.fromJson(repo_json, Class.forName(class_name));
//			repository.initialize();
//		} catch (Exception e) {
//			e.printStackTrace();
//		} 
		
	}
}
