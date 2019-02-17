package com.wowsanta.scim.repository;

import java.util.Date;

import org.junit.Test;

import com.ehyundai.object.User;
import com.wowsanta.scim.SCIMSystemManager;
import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.obj.SCIMUserMeta;
import com.wowsanta.scim.resource.SCIMRepositoryManager;
import com.wowsanta.scim.resource.SCIMResourceRepository;
import com.wowsanta.scim.util.Random;

public class GroupwareRepositoryTest {

	private final String config_file = "../config/home_dev_gw_scim-service-provider.json";
	
	//@Test
	public void groupware_repository_create_20000_test() {
		try {
			
			load_repository(config_file);
			SCIMResourceRepository resource_repository = SCIMRepositoryManager.getInstance().getResourceRepository();
			
			for(int i= 0; i< 20000; i++) {
				User user = new User();
				user.setId(Random.number(10000000, 99999999));
				user.setEmployeeNumber(user.getId());
				user.setUserName(Random.name());
				user.setActive(Random.yn_boolean(90));
				user.setPosition(Random.position());
				user.setJob(Random.job());
				
				user.seteMail(user.getEmployeeNumber() + "@ehyundai.com");
				
				user.setCompanyCode(Random.number(0,5));
				user.setGroupCode(Random.number(0,10));
				
				Date join_date = Random.beforeYears(10);
				user.setJoinDate(join_date);
				
				user.setMeta(new SCIMUserMeta());
				if(user.isActive()) {
					Date create_date = Random.beforeYears(1);;
					Date modify_date = Random.date(create_date, new Date());
					
					user.getMeta().setCreated(create_date);
					user.getMeta().setLastModified(modify_date);
				}else {
					if(Random.yn_boolean(20)) {
						user.setRetireDate(Random.date(join_date, new Date()));
						user.getMeta().setCreated((Random.date(join_date, user.getRetireDate())));
						user.getMeta().setLastModified(user.getRetireDate());
					}else {
						Date create_date = Random.beforeYears(1);
						user.getMeta().setCreated(create_date);
						user.getMeta().setLastModified(Random.date(create_date, new Date()));
					}
				}
				System.out.println(user.toString());
				
				resource_repository.createUser(user);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//@Test
	public void groupware_repository_create_test() {
		try {
			load_repository(config_file);
			SCIMResourceRepository resource_repository = SCIMRepositoryManager.getInstance().getResourceRepository();			
			
			User user = new User();
			user.setId("sys-gw-tester");
			resource_repository.createUser(user);
			
		} catch (SCIMException e) {
			e.printStackTrace();
		}
	}
	
	//@Test
	public void groupware_repository_get_test() {
		try {
			load_repository(config_file);
			SCIMResourceRepository resource_repository = SCIMRepositoryManager.getInstance().getResourceRepository();			
			
			User user = (User) resource_repository.getUser("sys-gw-tester");
			System.out.println(">>>>>>===");
			System.out.println(user.toString(true));
			System.out.println(">>>>>>===");
		} catch (SCIMException e) {
			e.printStackTrace();
		}
	}
	
	public void load_repository(String conf_file_path) {
		try {
			SCIMSystemManager.getInstance().load(conf_file_path);
			SCIMSystemManager.getInstance().loadRepositoryManager();
			SCIMRepositoryManager.getInstance().initailze();
		} catch (SCIMException e) {
			e.printStackTrace();
		}
	}
	
}
