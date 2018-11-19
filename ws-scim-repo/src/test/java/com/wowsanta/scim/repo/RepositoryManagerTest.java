package com.wowsanta.scim.repo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Test;

import com.wowsanta.scim.repo.rdb.DBCP;
import com.wowsanta.scim.repo.rdb.RDBRepository;
import com.wowsanta.scim.repository.SCIMRepositoryManager;
import com.wowsanta.scim.resource.RepositoryManager;

public class RepositoryManagerTest {

	private String conf_file = "../config/repository.json";
	
	@Test
	public void make() {
		try {
//			RDBRepository repository = new RDBRepository();
//			DBCP dbcp = DBCP.load("../config/sqlite_dbcp.json");
//			repository.initDBCP(dbcp, null);
//			repository.save(conf_file);
			
			
			SCIMRepositoryManager repo_mgr = SCIMRepositoryManager.getInstance();
			repo_mgr.setRepositoryClass(RDBRepository.class.getCanonicalName());
			repo_mgr.setRepositoryConfig("../config/sqlite_dbcp.json");
			repo_mgr.save(conf_file);
//			repo_mgr.setRepositoryManger(repository);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void load() {
		try {
			SCIMRepositoryManager repo_mgr = SCIMRepositoryManager.getInstance();			
			repo_mgr.load(conf_file);
			
			System.out.println(repo_mgr);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		
//		try {
//			//RDBRepository repository = RDBRepository.load(conf_file);
//			Method load_method = Class.forName("com.wowsanta.scim.repo.rdb.RDBRepository").getDeclaredMethod("load",String.class);
//			RepositoryManager repository = (RepositoryManager)load_method.invoke(null,this.conf_file);
//			System.out.println("===repository===");
//			System.out.println(repository);
//			System.out.println("===repository===");
////		} catch (FileNotFoundException e) {
////			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IllegalArgumentException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (InvocationTargetException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (NoSuchMethodException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (SecurityException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (ClassNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
	}
}
