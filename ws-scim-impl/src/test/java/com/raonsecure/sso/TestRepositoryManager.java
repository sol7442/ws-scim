package com.raonsecure.sso;


import org.junit.Before;
import org.junit.Test;

import com.wowsanta.scim.attribute.SimpleAttribute;
import com.wowsanta.scim.repo.exception.RepositoryException;
import com.wowsanta.scim.repo.rdb.DBCP;
import com.wowsanta.scim.repository.SCIMResouceManager;
import com.wowsanta.scim.resource.RepositoryManager;
import com.wowsanta.scim.resource.SCIMResouceFactory;
import com.wowsanta.scim.resource.SCIMUser;
import com.wowsanta.scim.schema.SCIMResourceTypeSchema;
import com.wowsanta.scim.schema.SCIMSchemaDefinitions;

public class TestRepositoryManager {

	
	@Before
	public void load() {
		try {
			String repositoryClass = "com.raonsecure.sso.SSORepositoryManager";
			String resource_config = "../config/resouce_config_raonsecure_sso.json";
			SCIMResouceManager resouce_mgr = SCIMResouceManager.getInstance();
			RepositoryManager  repository_mgr = resouce_mgr.loadRepositoryManager(repositoryClass, resource_config);
			repository_mgr.initialize();
			
			SCIMResourceTypeSchema user_schema = SCIMResourceTypeSchema.load("../config/schema/scim_user_schema.json");
			SCIMResouceFactory user_factory = SCIMResouceFactory.getInstance();
			user_factory.setUserResourceSchema(user_schema);
			
			SCIMUser user = user_factory.createUser();
			SimpleAttribute name = new SimpleAttribute(SCIMSchemaDefinitions.SCIMUserSchemaDefinition.NAME);//''.getName(),"tester_name");
			name.setValue("테스터");
			SimpleAttribute id = new SimpleAttribute(SCIMSchemaDefinitions.ID);//.getName(),null);
			id.setValue("00001");
			
			user.addAttribute(id);
			user.addAttribute(name);
			
			System.out.println(user.toJson());
			
			
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
}
