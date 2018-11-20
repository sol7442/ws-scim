package com.wowsanta.scim.repo;

import java.io.IOException;

import org.junit.Test;

import com.wowsanta.scim.attribute.SimpleAttribute;
import com.wowsanta.scim.obj.User;
import com.wowsanta.scim.repo.rdb.DBCP;
import com.wowsanta.scim.repo.rdb.RDBRepository;
import com.wowsanta.scim.repository.SCIMRepositoryManager;
import com.wowsanta.scim.resource.RepositoryManager;
import com.wowsanta.scim.schema.SCIMDefinitions;
import com.wowsanta.scim.schema.SCIMSchemaDefinitions;

public class RepositoryManagerTest {

	private String conf_file = "../config/repository.json";
	
	//@Test
	public void make() {
		try {
			RDBRepository repository = new RDBRepository();
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
			SCIMRepositoryManager repo_mgr = SCIMRepositoryManager.getInstance();			
			repo_mgr.initialize(repositoryClass,this.conf_file);
			
			
			RepositoryManager repository = repo_mgr.getRepositoryManger();
			User user = new User();
			SimpleAttribute name = new SimpleAttribute(SCIMSchemaDefinitions.SCIMUserSchemaDefinition.NAME.getName(),"tester_name");
			user.addAttribute(name.getName(), name);
			
			SimpleAttribute id = new SimpleAttribute(SCIMSchemaDefinitions.ID.getName(),"tester_id");
			user.addAttribute(id.getName(), id);
			
			System.out.println(user.toString());
			
			repository.createUser(user);
			System.out.println(repo_mgr.getRepositoryManger());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
