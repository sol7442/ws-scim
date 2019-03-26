package com.wowsanta.scim.repository;


import com.wowsanta.scim.SCIMSystemManager;
import com.wowsanta.scim.conf.SystemManagerTest;
import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.repository.SCIMRepositoryManager;

public class RepositoryManagerTest extends SystemManagerTest{

	private final String config_file = "../config/home_dev_scim-service-provider.json";
	
//	public void repository_manager_load_and_init_test() {
//		
//		load_repository_manager(config_file);
//		
//		try {
//			SCIMRepositoryManager.getInstance().initailze();
//		} catch (SCIMException e) {
//			e.printStackTrace();
//		}
//		
//		SCIMRepository resourec_repository = (SCIMRepository) SCIMRepositoryManager.getInstance().getResourceRepository();
//		SCIMRepository system_repository   = (SCIMRepository) SCIMRepositoryManager.getInstance().getSystemRepository();
//		
//		System.out.println("resourec repository check : " + resourec_repository.toString(true));;
//		System.out.println("resourec repository check : " + system_repository.toString(true));;
//	}
	
	public void load_repository_manager(String config_file_path) {
		load_system_manager(config_file_path);

//		try {
//			SCIMSystemManager.getInstance().loadRepositoryManager();
//		} catch (SCIMException e) {
//			e.printStackTrace();
//		}
	}
}
