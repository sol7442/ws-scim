package com.wowsanta.scim.repo;


import java.io.File;
import java.io.FileWriter;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.repo.sample.*;
import com.wowsanta.scim.resource.SCIMResourceRepository;
import com.wowsanta.scim.resource.SCIMUser;
import com.wowsanta.scim.resource.SCIMResouceFactory;
import com.wowsanta.scim.resource.SCIMRepositoryManager;
import com.wowsanta.scim.schema.SCIMResourceTypeSchema;

public class TestRepositoryManager {

	
	@Before
	public void load() {
		try {
//			if(SCIMResourceManager.getInstance().isLoad() == false) {
//				
//				System.out.println("===[init  SCIMResouceManager start]========================");
//				
//				String repositoryClass = "com.ehyundai.sso.SSORepositoryManager";
//				String scimUserClass   = "com.ehyundai.im.User";
//				String scimUserGroupClass   = "com.ehyundai.im.UserGroup";
//				String scimGroupClass   = "com.ehyundai.im.Group";
//				String resource_config = "../config/resouce_config_raonsecure_sso.json";
//				SCIMResourceTypeSchema user_schema = SCIMResourceTypeSchema.load("../config/schema/scim_user_schema.json");
//
//				
//				SCIMResourceManager resouce_mgr = SCIMResourceManager.getInstance();
//				SCIMRepository  repository_mgr = resouce_mgr.loadRepositoryManager(repositoryClass, resource_config);
//				repository_mgr.initialize();
//				
//				SCIMResouceFactory user_factory = SCIMResouceFactory.getInstance();
//				user_factory.setUserResourceClass(scimUserClass);
//				user_factory.setUserGroupResoureClass(scimUserGroupClass);			
//				user_factory.setUserResourceSchema(user_schema);
//			
//				String file_name = "../config/scim_server_config.json";
//				
//				FileWriter writer = new FileWriter(new File(file_name));
//				Gson gson = new GsonBuilder().setPrettyPrinting().create();
//				gson.toJson(resouce_mgr,writer);
//				writer.flush();
//				writer.close();
//				
//				System.out.println("===[init  SCIMResouceManager end]========================");
//			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
//
//	@Test
//	public void select() {
//		System.out.println("===[select start]========================");
//		
//		String user_id = "testId0";
//		try {
//			User selected_user = (User)SCIMResourceManager.getInstance().getRepositoryManger().getUser(user_id);
//			
//			System.out.println("selected :  ["+ user_id +"]" + selected_user);
//			
//		} catch (SCIMException e) {
//			e.printStackTrace();
//		}
//		
//		System.out.println("===[delete end]========================");
//	}
//	//@Test
//	public void delete() {
//		System.out.println("===[delete start]========================");
//		
//		String data = "{\"schemas\":[\"urn:ietf:params:scim:schemas:core:2.0:User\",\"urn:ietf:params:scim:schemas:extension:enterprise:2.0:User\"],\"id\":\"c##UserId1\",\"userName\":\"홍길동\",\"meta\":{\"resourceType\":\"com.wowsanta.scim.resource.SCIMUser\",\"created\":\"Jan 3, 2019 4:49:53 PM\",\"lastModified\":\"Jan 3, 2019 4:49:53 PM\",\"location\":\"\",\"version\":\"2.0\"},\"groups\":[{\"value\":\"총무부\",\"$ref\":\"https://192.168.11:80/scim/Group/총무부\"}]}";
//		User decoed_user = SCIMResouceFactory.getInstance().decoedUser(data);
//
//		try {
//			SCIMResourceManager.getInstance().getRepositoryManger().deleteUser(decoed_user.getId());
//		} catch (SCIMException e) {
//			e.printStackTrace();
//		}
//		
//		System.out.println("===[delete end]========================");
//	}
//	
//	//@Test
//	public void create100() {
//		System.out.println("===[create start]========================");
//		try {
//			int count = 0;
//			while(count < 100) {
//				User user = (User)SCIMResouceFactory.getInstance().createUser();
//				user.setId("testId" + count);
//				user.setUserName("testName" + count);
//				
//				Meta meta = new Meta();
//				meta.setCreated(new Date());
//				meta.setLastModified(new Date());
//				meta.setLocation("");
//				meta.setResourceType(SCIMUser.class.getName());
//				meta.setVersion("2.0");
//				user.setMeta(meta);
//			
//				UserGroup group = (UserGroup)SCIMResouceFactory.getInstance().createUserGroup("총무부");
//				user.addGroup(group);
//				
//				User created_user = (User)SCIMResourceManager.getInstance().getRepositoryManger().createUser(user);
//				System.out.println("created ["+ count +"]" + created_user);
//				count++;			
//			}
//		}catch (SCIMException e) {
//			e.printStackTrace();
//		}
//		System.out.println("===[create end]========================");
//	}
//	//@Test
//	public void create() {
//		System.out.println("===[create start]========================");
//		try {
//			User user = (User)SCIMResouceFactory.getInstance().createUser();
//			user.setId("c##UserId1");
//			user.setUserName("홍길동");
//			
//			Meta meta = new Meta();
//			meta.setCreated(new Date());
//			meta.setLastModified(new Date());
//			meta.setLocation("");
//			meta.setResourceType(SCIMUser.class.getName());
//			meta.setVersion("2.0");
//			user.setMeta(meta);
//		
//			UserGroup group = (UserGroup)SCIMResouceFactory.getInstance().createUserGroup("총무부");
//			user.addGroup(group);
//			
//			
//			
//			User created_user = (User)SCIMResourceManager.getInstance().getRepositoryManger().createUser(user);
//			
//			System.out.println(created_user);
//			
//		}catch (SCIMException e) {
//			e.printStackTrace();
//		}
//		System.out.println("===[create end]========================");
//	}
//	
//	//@Test
//	public void parse() {
//		System.out.println("===========================");
//		String data = "{\"schemas\":[\"urn:ietf:params:scim:schemas:core:2.0:User\",\"urn:ietf:params:scim:schemas:extension:enterprise:2.0:User\"],\"id\":\"c##UserId1\",\"userName\":\"홍길동\",\"meta\":{\"resourceType\":\"com.wowsanta.scim.resource.SCIMUser\",\"created\":\"Jan 3, 2019 4:49:53 PM\",\"lastModified\":\"Jan 3, 2019 4:49:53 PM\",\"location\":\"\",\"version\":\"2.0\"},\"groups\":[{\"value\":\"총무부\",\"$ref\":\"https://192.168.11:80/scim/Group/총무부\"}]}";
////		Gson gson = new GsonBuilder().setPrettyPrinting().create();
////		User user = gson.fromJson(data,User.class);
//		User decoed_user = SCIMResouceFactory.getInstance().decoedUser(data);
//		System.out.println(decoed_user);
//		
//		System.out.println("===========================");
////		JsonReader reader = new JsonReader(data);
////		return gson.fromJson(reader,Configuration.class);
//		
//	}
}
