package com.wowsanta.scim.repository;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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

	public void initialize(String repositoryClass, String repositoryConfig) {
		try {
			Method load_method = Class.forName(repositoryClass).getDeclaredMethod("load",String.class);
			this.repositoryManger = (RepositoryManager)load_method.invoke(null,repositoryConfig);
			this.repositoryManger.initialize();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	public RepositoryManager getRepositoryManger() {
		return repositoryManger;
	}

	public void setRepositoryManger(RepositoryManager repositoryManger) {
		this.repositoryManger = repositoryManger;
	}


}
