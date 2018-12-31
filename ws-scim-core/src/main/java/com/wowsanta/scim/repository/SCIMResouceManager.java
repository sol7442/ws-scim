package com.wowsanta.scim.repository;

import java.lang.reflect.Method;

import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.resource.RepositoryManager;

public class SCIMResouceManager {
	private static SCIMResouceManager instance = null;
	private RepositoryManager repositoryManger;
	
	public static SCIMResouceManager getInstance() {
		if(instance == null) {
			instance = new SCIMResouceManager();
		}
		return instance;
	}

	public RepositoryManager loadRepositoryManager(String repository_class, String repository_config) throws SCIMException {
		try {
			Method load_method = Class.forName(repository_class).getDeclaredMethod("load",String.class);
			this.repositoryManger = (RepositoryManager)load_method.invoke(null,repository_config);
		} catch (Exception e) {
			throw new SCIMException("RepositoryManager Load Error ["+repository_class+"]["+repository_config+"]",e);
		} 
		return this.repositoryManger;
	}
	
	

	public RepositoryManager getRepositoryManger() {
		return repositoryManger;
	}

	public void setRepositoryManger(RepositoryManager repositoryManger) {
		this.repositoryManger = repositoryManger;
	}




}
