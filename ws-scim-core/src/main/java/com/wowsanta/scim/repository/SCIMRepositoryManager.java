package com.wowsanta.scim.repository;

public class SCIMRepositoryManager {
	private static SCIMRepositoryManager instance = null;
	public static SCIMRepositoryManager getInstance() {
		if(instance == null) {
			instance = new SCIMRepositoryManager();
		}
		return instance;
	}
	
	public void loadConfig(String conf_file) {
		
	}
}
