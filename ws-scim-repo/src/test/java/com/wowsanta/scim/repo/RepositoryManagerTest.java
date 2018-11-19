package com.wowsanta.scim.repo;

import com.wowsanta.scim.repository.SCIMRepositoryManager;

public class RepositoryManagerTest {

	private String conf_file = "";
	private SCIMRepositoryManager repo_mgr = SCIMRepositoryManager.getInstance();
	public void load() {
		repo_mgr.loadConfig(conf_file);
	}
}
