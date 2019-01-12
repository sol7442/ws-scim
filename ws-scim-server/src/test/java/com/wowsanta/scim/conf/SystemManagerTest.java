package com.wowsanta.scim.conf;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Method;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.wowsanta.scim.SystemManager;
import com.wowsanta.scim.repo.rdb.AbstractRDBRepository;
import com.wowsanta.scim.resource.SCIMResourceRepository;
import com.wowsanta.scim.resource.SCIMResourceManager;

public class SystemManagerTest {

	String file_name = "../config/scim_system_config.json";
	
	//@Test
	public void save() {
		SystemManager mgr = SystemManager.getInstance();
		
		
		System.out.println("===[init  SystemManagerTest start]========================");
		
		String repositoryClass = "com.ehyundai.sso.SSORepositoryManager";
		String scimUserClass   = "com.ehyundai.im.User";
		String scimUserGroupClass   = "com.ehyundai.im.UserGroup";
		String scimGroupClass   = "com.ehyundai.im.Group";
		String resource_config = "../config/resouce_config_raonsecure_sso.json";
		
		try {
			Method load_method = Class.forName(repositoryClass).getDeclaredMethod("load",String.class);
			SCIMResourceRepository res_rep = (SCIMResourceRepository)load_method.invoke(null,resource_config);
			res_rep.setRepositoryClass(repositoryClass);
			res_rep.setRepositoryType("RDB");
			
			mgr.setResourceRepository(res_rep);
			
			FileWriter writer = new FileWriter(new File(file_name));
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			gson.toJson(mgr,writer);
			writer.flush();
			writer.close();
			
		} catch (Exception e) {
			e.printStackTrace();
			//throw new SCIMException("RepositoryManager Load Error ["+repository_class+"]["+repository_config+"]",e);
		} 
//		
////		SCIMResourceTypeSchema user_schema = SCIMResourceTypeSchema.load("../config/schema/scim_user_schema.json");
//
//		
//		SCIMResourceManager resouce_mgr = SCIMResourceManager.getInstance();
//		SCIMRepository  repository_mgr = resouce_mgr.loadRepositoryManager(repositoryClass, resource_config);
//		repository_mgr.initialize();
//		
//		SCIMResouceFactory user_factory = SCIMResouceFactory.getInstance();
//		user_factory.setUserResourceClass(scimUserClass);
//		user_factory.setUserGroupResoureClass(scimUserGroupClass);			
//		user_factory.setUserResourceSchema(user_schema);
	

		
		System.out.println("===[init  SystemManagerTest end]========================");
		
		
	}
	
	@Test
	public void load() {
		try {
			SystemManager mgr = SystemManager.getInstance();
			mgr.load(file_name);
			mgr.initialize();
			
			
//			JsonReader reader = new JsonReader(new FileReader(file_name));
//			JsonObject conf = new JsonParser().parse(reader).getAsJsonObject();
//			JsonObject res_rep_json = conf.get("resourceRepository").getAsJsonObject();
//			
//			Class.forName(res_rep_json.get("repositoryClass").getAsString());
//			
//			SCIMResourceRepository res_rep = (SCIMResourceRepository) new GsonBuilder().create().fromJson(res_rep_json,Class.forName(res_rep_json.get("repositoryClass").getAsString()));
//			res_rep.initialize();
//			
			
			
//			System.out.println(conf);
//			System.out.println();
			
			
			
//			
//			
//			Gson gson = new GsonBuilder().setPrettyPrinting().create();
//			
//			SystemManager mgr = gson.fromJson(reader,SystemManager.class);
			
//			System.out.println(mgr.getResourceRepository());
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
