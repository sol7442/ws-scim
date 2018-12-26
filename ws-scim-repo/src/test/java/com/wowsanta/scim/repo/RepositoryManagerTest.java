package com.wowsanta.scim.repo;

import java.io.IOException;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wowsanta.scim.attribute.SimpleAttribute;
import com.wowsanta.scim.obj.User;
import com.wowsanta.scim.repo.rdb.DBCP;
import com.wowsanta.scim.repo.rdb.RDBQueryManager;
import com.wowsanta.scim.repo.rdb.AbstractRDBRepository;
import com.wowsanta.scim.repo.rdb.UserQueryManager;
import com.wowsanta.scim.repository.SCIMResouceManager;
import com.wowsanta.scim.resource.RepositoryManager;
import com.wowsanta.scim.schema.SCIMDefinitions;
import com.wowsanta.scim.schema.SCIMResourceTypeSchema;
import com.wowsanta.scim.schema.SCIMSchemaDefinitions;

public class RepositoryManagerTest {

	private String conf_file = "../config/repository.json";
	
	//@Test
	public void make() {
		try {
			AbstractRDBRepository repository = new AbstractRDBRepository();
			DBCP dbcp = DBCP.load("../config/sqlite_dbcp.json");
			repository.initDBCP(dbcp, null);
			repository.save(conf_file);
			
			
//			SCIMRepositoryManager repo_mgr = SCIMRepositoryManager.getInstance();
//			repo_mgr.setRepositoryClass(RDBRepository.class.getCanonicalName());
//			repo_mgr.setRepositoryConfig("../config/sqlite_dbcp.json");
//			repo_mgr.save(conf_file);
//			repo_mgr.setRepositoryManger(repository);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void load() {
		try {
			
			String repositoryClass = "com.wowsanta.scim.repo.rdb.RDBRepository";
			SCIMResouceManager repo_mgr = SCIMResouceManager.getInstance();			
			repo_mgr.initialize(repositoryClass,this.conf_file);
			RDBQueryManager quer_mgr = RDBQueryManager.load("../config/query_schmea.json");
			System.out.println(quer_mgr.toJson());
			
			SCIMResourceTypeSchema user_schema = SCIMResourceTypeSchema.load("../config/schema/local_user_schema.json");			
			RepositoryManager repository = repo_mgr.getRepositoryManger();
			repository.setQueryManager(quer_mgr);
			
			
			User user = new User(user_schema);
			
			SimpleAttribute name = new SimpleAttribute(SCIMSchemaDefinitions.SCIMUserSchemaDefinition.NAME);//''.getName(),"tester_name");
			name.setValue("테스터");
			SimpleAttribute id = new SimpleAttribute(SCIMSchemaDefinitions.ID);//.getName(),null);
			id.setValue("00001");
			
			user.addAttribute(name.getName(), name);
			user.addAttribute(id.getName(), id);
			 
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			System.out.println(gson.toJson(user.encode()));
			
			repository.createUser(user);
//			System.out.println(repo_mgr.getRepositoryManger());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
