package com.wowsanta.scim.resource;

import java.lang.reflect.Method;

import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.resource.SCIMResourceRepository;

public interface SCIMResourceManager {
	public SCIMResourceRepository getSystemRepository();
	public SCIMResourceRepository getResourceRepository();
	
////	private static SCIMResourceManager instance = null;
//	
//	private SCIMRepositoryManager resourceRository;
//	private SCIMRepositoryManager systemRepository;
//	
////	private boolean load = false;
////	public static SCIMResourceManager getInstance() {
////		if(instance == null) {
////			instance = new SCIMResourceManager();
////		}
////		return instance;
////	}
//
//	public SCIMResourceManager() {}
//	
////	public boolean isLoad() {
////		return this.load;
////	}
//
//	public SCIMRepositoryManager loadRepositoryManager(String repository_class, String repository_config) throws SCIMException {
//		try {
//			Method load_method = Class.forName(repository_class).getDeclaredMethod("load",String.class);
//			this.resourceRository = (SCIMRepositoryManager)load_method.invoke(null,repository_config);
//			this.resourceRository.setRepositoryClass(repository_class);
//			
//		} catch (Exception e) {
//			throw new SCIMException("RepositoryManager Load Error ["+repository_class+"]["+repository_config+"]",e);
//		} 
//		
////		this.load = true;
//		return this.resourceRository;
//	}
//	
//	
//
//	public SCIMRepositoryManager getRepositoryManger() {
//		return resourceRository;
//	}
//
//	public void setRepositoryManger(SCIMRepositoryManager repositoryManger) {
//		this.resourceRository = repositoryManger;
//	}
//



}
