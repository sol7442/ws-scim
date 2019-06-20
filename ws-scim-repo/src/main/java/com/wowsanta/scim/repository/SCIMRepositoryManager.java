package com.wowsanta.scim.repository;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.object.Resource_Object;
import com.wowsanta.scim.repository.RepositoryDefinitions.REPOSITORY_TYPE;
import com.wowsanta.scim.repository.impl.DefaultRepository;
import com.wowsanta.scim.repository.impl.MsSqlRepository;
import com.wowsanta.scim.repository.impl.OracleRepository;
import com.wowsanta.scim.repository.resource.ResourceRepositoryConfig;
import com.wowsanta.scim.repository.resource.SCIMResourceRepository;
import com.wowsanta.scim.repository.system.SCIMSystemRepository;
import com.wowsanta.scim.repository.system.SystemRepositoryConfig;
import com.wowsanta.scim.repository.system.impl.IMSystemRepository;

public class SCIMRepositoryManager {
	static transient Logger logger = LoggerFactory.getLogger(SCIMRepositoryManager.class);
	private transient static SCIMRepositoryManager instance;

	private ResourceRepositoryConfig resourceRepositoryConfig;
	private SystemRepositoryConfig   systemRepositoryConfig;
	
	private transient SCIMResourceRepository resourceRepository;
	private transient SCIMSystemRepository systemRepository;
	

	public static SCIMRepositoryManager getInstance() {
		if (instance == null) {
			instance = new SCIMRepositoryManager();
		}
		return instance;
	}

	public static SCIMRepositoryManager load(String json_config_file) throws RepositoryException {
		try {
			Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
			JsonReader reader = new JsonReader(new FileReader(json_config_file));
			instance = gson.fromJson(reader, SCIMRepositoryManager.class);
		} catch (Exception e) {
			logger.error(e.getMessage() + ": {}", json_config_file, e);
		}
		logger.info("- SCIMRepositoryManager : {}", instance.toString(false));		
		return instance;
	}
	public void save(String fileName) {
		
		try {
			GsonBuilder builder = new GsonBuilder().disableHtmlEscaping();
			builder.setPrettyPrinting();			
			Gson gson = builder.create();

			FileWriter writer = new FileWriter(fileName);
			writer.write(gson.toJson(this));
			writer.close();
			
		}catch (Exception e) {
			logger.error(e.getMessage() + " : {} ", fileName, e);
		}
	}
	public String toString(boolean pretty) {
		try {
			GsonBuilder builder = new GsonBuilder().disableHtmlEscaping();
			if (pretty) {
				builder.setPrettyPrinting();
			}
			Gson gson = builder.create();
			return gson.toJson(this);
		} catch (Exception e) {
			logger.error(e.getMessage() + " : ",  e);
		}
		return null;
	}

	
	public SCIMRepositoryManager initailze() throws RepositoryException {
		try {
			logger.info("------------ [initailze Repository ] --- {} ", resourceRepositoryConfig.toString(false));
			if(resourceRepository == null && resourceRepositoryConfig != null) {
				resourceRepositoryInitialize();
			}
			
			if(systemRepository == null && systemRepositoryConfig != null) {
				systemRepositoryInitialize();
			}	
			
			logger.info("------------ [initailze Repository ] --- {} ", new Date());
		}catch (Exception e) {
			throw new RepositoryException(e.getMessage(), e);
		}
		
		return this;
	}

	private void systemRepositoryInitialize() throws SCIMException {
		IMSystemRepository repository = new IMSystemRepository();
		repository.setDbcp(systemRepositoryConfig.getDbcp());
		repository.initialize();
		
		this.systemRepository = repository;
	}

	private void resourceRepositoryInitialize() {
		try {
			DefaultRepository repository = null;
			switch (resourceRepositoryConfig.getType()) {
			case ORACLE:
				repository = new OracleRepository();
				break;
			case MSSQL:
				repository = new MsSqlRepository();
				break;
			case MYSQL:
				break;
			case SQLITE:
				break;
			default:
				break;
			}
			
			logger.info("Resource Repository DBCP : {} ", resourceRepositoryConfig.getDbcp());
			repository.setDbcp(resourceRepositoryConfig.getDbcp());
			repository.initialize();
			this.resourceRepository = repository;
			
			if(resourceRepositoryConfig.getUserOutputMapper() != null) {
				RepositoryOutputMapper user_resource_output_mapper = RepositoryOutputMapper.load(resourceRepositoryConfig.getUserOutputMapper());
				repository.setUserOutputMapper(  user_resource_output_mapper);

				logger.info("setUserOutputMapper : {}", resourceRepositoryConfig.getUserOutputMapper());
			}
			
			if(resourceRepositoryConfig.getUserInputMapper() != null) {
				RepositoryInputMapper  user_resource_input_mapper = RepositoryInputMapper.load(resourceRepositoryConfig.getUserInputMapper());
				repository.setUserInputMapper(   user_resource_input_mapper);
				
				logger.info("setUserInputMapper : {}", resourceRepositoryConfig.getUserInputMapper());
			}
			
			if(resourceRepositoryConfig.getGroupOutputMapper() != null) {
				RepositoryOutputMapper group_resource_output_mapper = RepositoryOutputMapper.load(resourceRepositoryConfig.getGroupOutputMapper());
				repository.setGrouptOutputMapper(group_resource_output_mapper);
				
				logger.info("setGrouptOutputMapper : {}", resourceRepositoryConfig.getGroupOutputMapper());
			}

			if(resourceRepositoryConfig.getGroupInputMapper() != null) {
				RepositoryInputMapper  group_resource_input_mapper = RepositoryInputMapper.load(resourceRepositoryConfig.getGroupInputMapper());
				repository.setGroupInputMapper (group_resource_input_mapper);
				
				logger.info("setGroupInputMapper : {}", resourceRepositoryConfig.getGroupInputMapper());
			}
			
			
			if(resourceRepositoryConfig.getGroupOutputMapper() != null) {
				logger.info("Resource Repository VALIDATE GROUP : {} ", resourceRepositoryConfig.getGroupOutputMapper());
				
				int total_count = repository.getGroupCount(null);
				List<Resource_Object> group_list = repository.searchGroup("",0,1,total_count);
				if(group_list.size() > 0) {
					logger.info("Resource Repository VALIDATE Result : {}", group_list.get(0));
				}
			}
			
			if(resourceRepositoryConfig.getUserOutputMapper() != null) {
				logger.info("Resource Repository VALIDATE : {} ", resourceRepositoryConfig.getUserOutputMapper() );
				int totalCount = repository.getUserCount(null);
				List<Resource_Object> user_list = repository.searchUser(null,0,1,totalCount);
				if(user_list.size() > 0) {
					logger.info("Resource Repository VALIDATE Result : {}", user_list.get(0));	
				}
			}
			
		}catch (Exception e) {
			logger.error("Repository Validate Faeild {}", e.getMessage(), e);
		}
	}
	
	
	public ResourceRepositoryConfig getResourceRepositoryConfig() {
		return resourceRepositoryConfig;
	}

	public void setResourceRepositoryConfig(ResourceRepositoryConfig resourceRepositoryConfig) {
		this.resourceRepositoryConfig = resourceRepositoryConfig;
	}
	
	public SystemRepositoryConfig getSystemRepositoryConfig() {
		return systemRepositoryConfig;
	}

	public void setSystemRepositoryConfig(SystemRepositoryConfig systemRepositoryConfig) {
		this.systemRepositoryConfig = systemRepositoryConfig;
	}

	public SCIMResourceRepository getResourceRepository() {
		return resourceRepository;
	}

	public void setResourceRepository(SCIMResourceRepository resourceRepository) {
		this.resourceRepository = resourceRepository;
	}

	public SCIMSystemRepository getSystemRepository() {
		return systemRepository;
	}

	public void setSystemRepository(SCIMSystemRepository systemRepository) {
		this.systemRepository = systemRepository;
	}
	

//	
//	public static SCIMRepositoryManager loadFromFile(String className, String file_name) throws RepositoryException{
//		if(instance == null) {
//			instance = new SCIMRepositoryManager();
//		}
//		
//		logger.info("REPOSITORY LOAD : {}", file_name);
//		
//		try {
//			JsonReader reader = new JsonReader(new InputStreamReader(new FileInputStream(new File(file_name)),"UTF-8"));// FileReader(file_name));
//			JsonObject json_object = new JsonParser().parse(reader).getAsJsonObject();
//
//			if(json_object.get("systemRepository") != null) {
//				JsonObject sys_rep_json_obj = json_object.get("systemRepository").getAsJsonObject();
//				instance.setSystemRepository((SCIMSystemRepository) AbstractSCIMRepository.loadFromString(sys_rep_json_obj.toString()));
//			}
//			
//			if(json_object.get("resourceRepository") != null) {
//				JsonObject res_rep_json_obj = json_object.get("resourceRepository").getAsJsonObject();
//				instance.setResourceRepository((SCIMRepositoryController) AbstractSCIMRepository.loadFromString(res_rep_json_obj.toString()));
//			}
//			
//			if(className != null) {
//				String repository_path = json_object.get("repository").getAsString();
//				loadFromClass(className, repository_path);
//			}
//				
//			
//		} catch (FileNotFoundException e) {
//			logger.error("REPOSITORY LOAD FAILED : {}", file_name);
//			throw new RepositoryException("",e);
//		} catch (JsonException e) {
//			logger.error("REPOSITORY LOAD FAILED: {}", file_name);
//			throw new RepositoryException("",e);
//		} catch (UnsupportedEncodingException e) {
//			logger.error("REPOSITORY LOAD FAILED: {}", file_name);
//			throw new RepositoryException("",e);
//		}
//		
//		instance.setConfigFile(file_name);
//		return instance;
//	}
//	private void setConfigFile(String file_name) {
//		this.configFile = new File(file_name);
//	}
//	public void save() throws JsonException, IOException {
//		try {
//			
//			OutputStreamWriter writer = new OutputStreamWriter(
//					new FileOutputStream(this.configFile),StandardCharsets.UTF_8);
//			Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
//			gson.toJson(this,writer);
//			writer.flush();
//			writer.close();
//			
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//	public SCIMRepositoryManager initailze() throws SCIMException {
//		
//		try {
//			if(resourceRepository != null) {
//				logger.info("resourceRepository \n{}",resourceRepository.tojson(true));
//				
//				resourceRepository.initialize();
//				logger.info("resourceRepository validate : {} ",resourceRepository.validate());
//			}
//			if(systemRepository !=null) {
//				logger.info("systemRepository \n{}",systemRepository.tojson(true));
//				
//				systemRepository.initialize();
//				logger.info("systemRepository validate : {} ",systemRepository.validate());
//			}
//		}catch (SCIMException e) {
//			this.close();
//			throw e;
//		}
//		
//		return instance;
//	}
//	public void setResourceRepository(SCIMRepositoryController repo) {
//		this.resourceRepository = (AbstractSCIMRepository) repo;
//	}
//	public void setSystemRepository(SCIMSystemRepository repo) {
//		this.systemRepository = (AbstractSCIMRepository) repo;
//	}
//	public SCIMRepositoryController getResourceRepository() {
//		return (SCIMRepositoryController) this.resourceRepository;
//	}
//	public SCIMSystemRepository getSystemRepository() {
//		return (SCIMSystemRepository) this.systemRepository;
//	}
//	
//	public void close() throws SCIMException {
//		if(this.resourceRepository != null) {
//			this.resourceRepository.close();
//		}
//		
//		if(this.systemRepository != null) {
//			this.systemRepository.close();
//		}
//		
//		if(this.repository != null) {
//			this.repository.close();
//		}
//		
//	}
//
//	public String toString(boolean pretty) {
//		try {
//			GsonBuilder builder = new GsonBuilder().disableHtmlEscaping();
//			if(pretty) {
//				builder.setPrettyPrinting();
//			}
//			Gson gson  = builder.create();
//			return gson.toJson(this);
//		} catch (Exception e) {
//			
//		}
//		return null;
//	}
//	public static SCIMRepositoryManager load(String json_config_file) throws RepositoryException{
//		try {
//			Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
//			JsonReader reader = new JsonReader(new FileReader(json_config_file));
//			instance =  gson.fromJson(reader, SCIMRepositoryManager.class);
//		} catch (Exception e) {
//			logger.error(e.getMessage() + ": {}",json_config_file, e);
//		}
//		return instance;
//	}
//	
//	
//	public static void loadFromClass(String className, String configFile) throws RepositoryException {
//		if(instance == null) {
//			instance = new SCIMRepositoryManager();
//		}
//		
//		try {
//			logger.info("Repository load from class : {}",className);
//			Method load_method = Class.forName(className).getMethod("load", String.class);
//			SCIMRepository repository  = (SCIMRepository) load_method.invoke(null, configFile);
//			
//			instance.setRepository(repository);
//		} catch (Exception e) {
//			logger.error("Repository load failed : {}",className,e);
//			throw new RepositoryException("Repository load failed", e);
//		}
//	}
//
//	private void setRepository(SCIMRepository repository) {
//		try {
//			repository.initialize();
//			this.repository = repository;
//		} catch (RepositoryException e) {
//			logger.error("Repository initialize failed : {}", e);
//			e.printStackTrace();
//		}
//	}
//	public SCIMRepository getRepository() {
//		return this.repository;
//	}
//
//
//
//	public ResourceRepositoryConfig getResourceRepositoryConfig() {
//		return resourceRepositoryConfig;
//	}
//
//	public void setResourceRepositoryConfig(ResourceRepositoryConfig resourceRepositoryConfig) {
//		this.resourceRepositoryConfig = resourceRepositoryConfig;
//	}

}
