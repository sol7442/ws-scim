package com.wowsanta.scim.resource;

import java.lang.reflect.Method;

import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.resource.SCIMRepositoryManager;

public class SCIMResourceManager {
	private static SCIMResourceManager instance = null;
	private SCIMRepositoryManager repositoryManger;
	
	private boolean load = false;
	public static SCIMResourceManager getInstance() {
		if(instance == null) {
			instance = new SCIMResourceManager();
		}
		return instance;
	}

	public boolean isLoad() {
		return this.load;
	}

	public SCIMRepositoryManager loadRepositoryManager(String repository_class, String repository_config) throws SCIMException {
		try {
			Method load_method = Class.forName(repository_class).getDeclaredMethod("load",String.class);
			this.repositoryManger = (SCIMRepositoryManager)load_method.invoke(null,repository_config);
		} catch (Exception e) {
			throw new SCIMException("RepositoryManager Load Error ["+repository_class+"]["+repository_config+"]",e);
		} 
		
		this.load = true;
		return this.repositoryManger;
	}
	
	

	public SCIMRepositoryManager getRepositoryManger() {
		return repositoryManger;
	}

	public void setRepositoryManger(SCIMRepositoryManager repositoryManger) {
		this.repositoryManger = repositoryManger;
	}




}
