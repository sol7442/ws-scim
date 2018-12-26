package com.raonsecure.sso;


import org.junit.Before;
import org.junit.Test;

import com.wowsanta.scim.repo.exception.RepositoryException;
import com.wowsanta.scim.repo.rdb.DBCP;
import com.wowsanta.scim.repository.SCIMResouceManager;
import com.wowsanta.scim.resource.RepositoryManager;

public class TestRepositoryManager {

	
	@Before
	public void load() {
		try {
			SCIMResouceManager resource_manger = SCIMResouceManager.getInstance();
			
			resource_manger.getRepositoryManger().initialize();

			//repository_raonsecure_sso.json
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void create() {
			System.out.println("----");
	}
}
