package com.wowsanta.scim;


import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.resource.SCIMResouceFactory;
import com.wowsanta.scim.service.SCIMServiceProvider;

public class SCIMSystemManager {

	Logger logger = LoggerFactory.getLogger(SCIMSystemManager.class);
	
	private transient static SCIMSystemManager instance;

	private SCIMServiceProvider serviceProvider;
	public static SCIMSystemManager getInstance() {
		if (instance == null) {
			instance = new SCIMSystemManager();
		}
		return instance;
	}

	public void load(String conf_file_path) throws SCIMException {
		
		try {
			this.serviceProvider = SCIMServiceProvider.loadFromFile(conf_file_path);
			SCIMResouceFactory.getInstance().setUserClass(
					this.serviceProvider.getResources().get("user").getSchema(),
					this.serviceProvider.getResources().get("user").getClassName());
			
			logger.info("SERVICE PROVIDER \n {}", this.serviceProvider.tojson(true));
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new SCIMException("LOAD CONFIG FILE ERROR : " + conf_file_path, e);
		} finally {
		}
	}

	public SCIMServiceProvider getServiceProvider() {
		return serviceProvider;
	}

	public void setServiceProvider(SCIMServiceProvider servicveProvider) {
		this.serviceProvider = servicveProvider;
	}
	
//	public void loadRepositoryManager(File config_file) throws SCIMException {
//		SCIMRepositoryManager.getInstance().load(config_file);
//	}
//	public void loadRepositoryManager() throws SCIMException {
//		String repository_config_file_path = this.serviceProvider.getRepositoryConfig();
//		File repository_config_file = new File(repository_config_file_path);
//		loadRepositoryManager(repository_config_file);
//	}
//	
//	public void loadSchdulerManager() {
//		SCIMSystemRepository system_repository = SCIMRepositoryManager.getInstance().getSystemRepository();
//		try {
//			if(system_repository != null) {
//				List<SCIMScheduler> scheduler_list = system_repository.getSchdulerAll();
//				for (SCIMScheduler scimScheduler : scheduler_list) {
//					SCIMSchedulerManager.getInstance().addScheduler(scimScheduler);
//				}
//			}
//			
//		} catch (SCIMException e) {
//			e.printStackTrace();
//		}
//	}
}
