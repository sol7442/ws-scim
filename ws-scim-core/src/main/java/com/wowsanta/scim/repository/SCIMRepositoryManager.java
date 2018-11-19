package com.wowsanta.scim.repository;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.wowsanta.scim.resource.RepositoryManager;

public class SCIMRepositoryManager {
	private static SCIMRepositoryManager instance = null;
	
	private String repositoryClass;
	private String repositoryConfig;
	
	private RepositoryManager repositoryManger;
	
	public static SCIMRepositoryManager getInstance() {
		if(instance == null) {
			instance = new SCIMRepositoryManager();
		}
		return instance;
	}

	public RepositoryManager getRepositoryManger() {
		return repositoryManger;
	}

	public void setRepositoryManger(RepositoryManager repositoryManger) {
		this.repositoryManger = repositoryManger;
	}

	public String getRepositoryClass() {
		return repositoryClass;
	}

	public void setRepositoryClass(String repositoryClass) {
		this.repositoryClass = repositoryClass;
	}

	public String getRepositoryConfig() {
		return repositoryConfig;
	}

	public void setRepositoryConfig(String repositoryConfig) {
		this.repositoryConfig = repositoryConfig;
	}
	
	public void save(String file_name) throws IOException {
		OutputStreamWriter writer = new OutputStreamWriter(
				new FileOutputStream(
						new File(file_name)),StandardCharsets.UTF_8);
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		gson.toJson(this,writer);
		writer.flush();
		writer.close();
	}
	
	public String toJson() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(this);
	}
	
	public String toString() {
		return toJson();
	}
	
	public static void load(String file_name) throws FileNotFoundException {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonReader reader = new JsonReader(new FileReader(file_name));
		instance =  gson.fromJson(reader,RepositoryManager.class);
	}
}
