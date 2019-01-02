package com.raonsecure.sso;


import java.io.FileReader;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import com.ehyundai.im.Meta;
import com.ehyundai.im.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.wowsanta.scim.Configuration;
import com.wowsanta.scim.attribute.SimpleAttribute;
import com.wowsanta.scim.repo.exception.RepositoryException;
import com.wowsanta.scim.repo.rdb.DBCP;
import com.wowsanta.scim.repository.SCIMResouceManager;
import com.wowsanta.scim.resource.SCIMRepositoryManager;
import com.wowsanta.scim.resource.SCIMResouceFactory;
import com.wowsanta.scim.resource.SCIMUser;
import com.wowsanta.scim.resource.SCIMUser2;
import com.wowsanta.scim.schema.SCIMResourceTypeSchema;
import com.wowsanta.scim.schema.SCIMSchemaDefinitions;

public class TestRepositoryManager {

	
	@Before
	public void load() {
		try {
			String repositoryClass = "com.ehyundai.sso.SSORepositoryManager";
			String scimUserClass   = "com.ehyundai.im.User";
			String scimGroupClass   = "com.ehyundai.im.Group";
			
			String resource_config = "../config/resouce_config_raonsecure_sso.json";
			SCIMResouceManager resouce_mgr = SCIMResouceManager.getInstance();
			SCIMRepositoryManager  repository_mgr = resouce_mgr.loadRepositoryManager(repositoryClass, resource_config);
			repository_mgr.initialize();
			
			SCIMResourceTypeSchema user_schema = SCIMResourceTypeSchema.load("../config/schema/scim_user_schema.json");
			SCIMResouceFactory user_factory = SCIMResouceFactory.getInstance();
			user_factory.setUserResourceClass(scimUserClass);
			
			user_factory.setUserResourceSchema(user_schema);
			
			User user = (User)user_factory.createUser();
			user.setId("c##UserId1");
			user.setUserName("홍길동");
			
			Meta meta = new Meta();
			meta.setCreated(new Date());
			meta.setLastModified(new Date());
			meta.setLocation("");
			meta.setResourceType(SCIMUser.class.getName());
			meta.setVersion("2.0");
			user.setMeta(meta);
		
			
			System.out.println(user_factory.toJson(user));
			System.out.println(user_factory.toJsonString(user));
			
//			SimpleAttribute name = new SimpleAttribute(SCIMSchemaDefinitions.SCIMUserSchemaDefinition.NAME);//''.getName(),"tester_name");
//			name.setValue("테스터");
//			SimpleAttribute id = new SimpleAttribute(SCIMSchemaDefinitions.ID);//.getName(),null);
//			id.setValue("00001");
//			
//			user.addAttribute(id);
//			user.addAttribute(name);
//			
//			System.out.println(user.toJson());
			
			
			repository_mgr.createUser(user);
//			
//			
//			resouce_mgr.initialize(resource_config);
//			resouce_mgr.getRepositoryManger().initialize();
//			
			//repository_raonsecure_sso.json
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void create() {
			System.out.println("----");
	}
	
	@Test
	public void parse() {
		System.out.println("===========================");
		String data = "{\"schemas\":[],\"id\":\"c##UserId1\",\"meta\":{\"resourceType\":\"com.wowsanta.scim.resource.SCIMUser\",\"created\":\"Jan 2, 2019 9:34:36 PM\",\"lastModified\":\"Jan 2, 2019 9:34:36 PM\",\"location\":\"\",\"version\":\"2.0\"}}";		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		User user = gson.fromJson(data,User.class);
		System.out.println(SCIMResouceFactory.getInstance().toJson(user));
		System.out.println("===========================");
//		JsonReader reader = new JsonReader(data);
//		return gson.fromJson(reader,Configuration.class);
		
	}
}
