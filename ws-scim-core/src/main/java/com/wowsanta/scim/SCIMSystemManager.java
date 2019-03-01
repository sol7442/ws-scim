package com.wowsanta.scim;

import java.io.File;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.log.SCIMLogger;
import com.wowsanta.scim.resource.SCIMRepository;
import com.wowsanta.scim.resource.SCIMRepositoryManager;
import com.wowsanta.scim.resource.SCIMResouceFactory;
import com.wowsanta.scim.resource.SCIMSystemRepository;
import com.wowsanta.scim.scheduler.SCIMScheduler;
import com.wowsanta.scim.scheduler.SCIMSchedulerManager;
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
		
		System.out.println("server config loading........");
		
		File config_file = new File(conf_file_path);
		try {
			System.out.println("load config.......");
			this.serviceProvider = (SCIMServiceProvider) SCIMServiceProvider.load(config_file);
			SCIMResouceFactory.getInstance().setUserClass(
					this.serviceProvider.getResources().get("user").getSchema(),
					this.serviceProvider.getResources().get("user").getClassName());
		} catch (Exception e) {
			e.printStackTrace();
			throw new SCIMException("LOAD CONFIG FILE ERROR : " + conf_file_path, e);
		} finally {
			SCIMLogger.sys("==[SERVICE INFO]=========  \n {}-{} \n{}", 
					config_file.getName(), 
					new Date(config_file.lastModified()),
					this.serviceProvider.toString(true));
			SCIMLogger.sys("==[SERVICE INFO]=========  "); 
		}
	}

	public SCIMServiceProvider getServiceProvider() {
		return serviceProvider;
	}

	public void setServiceProvider(SCIMServiceProvider servicveProvider) {
		this.serviceProvider = servicveProvider;
	}
	
	public void loadRepositoryManager(File config_file) throws SCIMException {
		SCIMRepositoryManager.getInstance().load(config_file);
	}
	public void loadRepositoryManager() throws SCIMException {
		String repository_config_file_path = this.serviceProvider.getRepositoryConfig();
		File repository_config_file = new File(repository_config_file_path);
		loadRepositoryManager(repository_config_file);
	}
	
	public void loadSchdulerManager() {
		SCIMSystemRepository system_repository = SCIMRepositoryManager.getInstance().getSystemRepository();
		try {
			if(system_repository != null) {
				List<SCIMScheduler> scheduler_list = system_repository.getSchdulerAll();
				for (SCIMScheduler scimScheduler : scheduler_list) {
					SCIMSchedulerManager.getInstance().addScheduler(scimScheduler);
				}
			}
			
		} catch (SCIMException e) {
			e.printStackTrace();
		}
	}
}
