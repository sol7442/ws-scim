package com.wowsanta.scim.repository;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.json.AbstractJsonObject;
import com.wowsanta.scim.repo.rdb.AbstractRDBRepository;
import com.wowsanta.scim.repository.impl.DefaultRepository;
import com.wowsanta.scim.resource.SCIMSystemRepository;
import com.wowsata.util.json.JsonException;
import com.wowsata.util.json.WowsantaJson;


public class SCIMRepositoryManager {
	
	static Logger logger = LoggerFactory.getLogger(SCIMRepositoryManager.class);
	
	private static transient SCIMRepositoryManager instance;
	
	private AbstractSCIMRepository resourceRepository;
	private AbstractSCIMRepository systemRepository;
	private SCIMRepository repository;
	
	private transient File configFile;
	
	public static SCIMRepositoryManager getInstance() {
		if(instance == null) {
			instance = new SCIMRepositoryManager();
		}
		return instance;
	}
	
	public static SCIMRepositoryManager loadFromFile(String className, String file_name) throws RepositoryException{
		if(instance == null) {
			instance = new SCIMRepositoryManager();
		}
		
		logger.info("REPOSITORY LOAD : {}", file_name);
		
		try {
			JsonReader reader = new JsonReader(new InputStreamReader(new FileInputStream(new File(file_name)),"UTF-8"));// FileReader(file_name));
			JsonObject json_object = new JsonParser().parse(reader).getAsJsonObject();

			if(json_object.get("systemRepository") != null) {
				JsonObject sys_rep_json_obj = json_object.get("systemRepository").getAsJsonObject();
				instance.setSystemRepository((SCIMSystemRepository) AbstractSCIMRepository.loadFromString(sys_rep_json_obj.toString()));
			}
			
			if(json_object.get("resourceRepository") != null) {
				JsonObject res_rep_json_obj = json_object.get("resourceRepository").getAsJsonObject();
				instance.setResourceRepository((SCIMResourceRepository) AbstractSCIMRepository.loadFromString(res_rep_json_obj.toString()));
			}
			
			if(className != null) {
				String repository_path = json_object.get("repository").getAsString();
				loadFromClass(className, repository_path);
			}
				
			
		} catch (FileNotFoundException e) {
			logger.error("REPOSITORY LOAD FAILED : {}", file_name);
			throw new RepositoryException("",e);
		} catch (JsonException e) {
			logger.error("REPOSITORY LOAD FAILED: {}", file_name);
			throw new RepositoryException("",e);
		} catch (UnsupportedEncodingException e) {
			logger.error("REPOSITORY LOAD FAILED: {}", file_name);
			throw new RepositoryException("",e);
		}
		
		instance.setConfigFile(file_name);
		return instance;
	}
	private void setConfigFile(String file_name) {
		this.configFile = new File(file_name);
	}
	public void save() throws JsonException, IOException {
		try {
			
			OutputStreamWriter writer = new OutputStreamWriter(
					new FileOutputStream(this.configFile),StandardCharsets.UTF_8);
			Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
			gson.toJson(this,writer);
			writer.flush();
			writer.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public SCIMRepositoryManager initailze() throws SCIMException {
		
		try {
			if(resourceRepository != null) {
				logger.info("resourceRepository \n{}",resourceRepository.tojson(true));
				
				resourceRepository.initialize();
				logger.info("resourceRepository validate : {} ",resourceRepository.validate());
			}
			if(systemRepository !=null) {
				logger.info("systemRepository \n{}",systemRepository.tojson(true));
				
				systemRepository.initialize();
				logger.info("systemRepository validate : {} ",systemRepository.validate());
			}
		}catch (SCIMException e) {
			this.close();
			throw e;
		}
		
		return instance;
	}
	public void setResourceRepository(SCIMResourceRepository repo) {
		this.resourceRepository = (AbstractSCIMRepository) repo;
	}
	public void setSystemRepository(SCIMSystemRepository repo) {
		this.systemRepository = (AbstractSCIMRepository) repo;
	}
	public SCIMResourceRepository getResourceRepository() {
		return (SCIMResourceRepository) this.resourceRepository;
	}
	public SCIMSystemRepository getSystemRepository() {
		return (SCIMSystemRepository) this.systemRepository;
	}
	
	public void close() throws SCIMException {
		if(this.resourceRepository != null) {
			this.resourceRepository.close();
		}
		
		if(this.systemRepository != null) {
			this.systemRepository.close();
		}
		
		if(this.repository != null) {
			this.repository.close();
		}
		
	}

	public static void loadFromClass(String className, String configFile) throws RepositoryException {
		if(instance == null) {
			instance = new SCIMRepositoryManager();
		}
		
		try {
			logger.info("Repository load from class : {}",className);
			Method load_method = Class.forName(className).getMethod("load", String.class);
			SCIMRepository repository  = (SCIMRepository) load_method.invoke(null, configFile);
			
			instance.setRepository(repository);
		} catch (Exception e) {
			logger.error("Repository load failed : {}",className,e);
			throw new RepositoryException("Repository load failed", e);
		}
	}

	private void setRepository(SCIMRepository repository) {
		try {
			repository.initialize();
			this.repository = repository;
		} catch (RepositoryException e) {
			logger.error("Repository initialize failed : {}", e);
			e.printStackTrace();
		}
	}
	public SCIMRepository getRepository() {
		return this.repository;
	}
	
}
