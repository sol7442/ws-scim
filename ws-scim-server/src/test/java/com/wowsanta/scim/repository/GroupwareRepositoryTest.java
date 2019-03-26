package com.wowsanta.scim.repository;

import java.util.Date;

import org.junit.Test;

import com.ehyundai.object.User;
import com.wowsanta.scim.SCIMSystemManager;
import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.obj.SCIMUserMeta;
import com.wowsanta.scim.repository.SCIMRepositoryManager;
import com.wowsanta.scim.repository.SCIMResourceRepository;
import com.wowsanta.scim.util.Random;

public class GroupwareRepositoryTest {

	private final String config_file = "../config/home_dev_gw_scim-service-provider.json";
	private final int create_user_size = 100;
	
	@Test
	public void groupware_repository_create_20000_test() {
		try {
			
			load_manager(config_file);
			createUsers(create_user_size);
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void load_manager(String conf_file_path) {
		try {
			SCIMSystemManager.getInstance().load(conf_file_path);
			SCIMSystemManager.getInstance().loadRepositoryManager();
			SCIMRepositoryManager.getInstance().initailze();
		} catch (SCIMException e) {
			e.printStackTrace();
		}
	}
	
	public void createUsers(int size) {
		SCIMResourceRepository res_repo = SCIMRepositoryManager.getInstance().getResourceRepository();
		
		for (int i = 0; i < size; i++) {
			User user = new User();
			user.setId(Random.number(10000000, 99999999));
			user.setEmployeeNumber(user.getId());
			user.setUserName(Random.name());
			user.setActive(Random.yn_boolean(90));
			user.setPosition(Random.position());
			user.setJob(Random.job());

			user.seteMail(user.getEmployeeNumber() + "@ehyundai.com");

			user.setCompanyCode(Random.number(0, 5));
			user.setGroupCode(Random.number(0, 10));

			Date join_date   = Random.beforeYears(10);
			Date retire_date = null;
			Date create_date = join_date;//Random.beforeYears(1);
			Date modify_date = null;
			
			user.setMeta(new SCIMUserMeta());
			if (user.isActive()) {
				if(Random.yn_boolean(10)) {	// 신입사원 비율
					modify_date = create_date;
				}else {
					modify_date = Random.date(create_date, new Date());
				}
				
			}else {
				if(Random.yn_boolean(40)) { // 비 활성화된 사용자 중 40 % 1년 안에 퇴사한 사람  
					retire_date   = Random.beforeYears(1);
					modify_date   = retire_date;
				}else {
					retire_date   = Random.date(create_date, new Date());
					modify_date   = retire_date;
				}
			}
			
			user.setJoinDate(join_date);
			user.setRetireDate(retire_date);
			user.getMeta().setCreated(create_date);
			user.getMeta().setLastModified(modify_date);
			
			System.out.println(user.toString());

			try {
				res_repo.createUser(user);
			} catch (SCIMException e) {
				e.printStackTrace();
			}
		}
	}
	
	
//	//@Test
//	public void groupware_repository_create_test() {
//		try {
//			load_manager(config_file);
//			SCIMResourceRepository resource_repository = SCIMRepositoryManager.getInstance().getResourceRepository();			
//			
//			User user = new User();
//			user.setId("sys-gw-tester");
//			resource_repository.createUser(user);
//			
//		} catch (SCIMException e) {
//			e.printStackTrace();
//		}
//	}
//	
//	//@Test
//	public void groupware_repository_get_test() {
//		try {
//			load_manager(config_file);
//			SCIMResourceRepository resource_repository = SCIMRepositoryManager.getInstance().getResourceRepository();			
//			
//			User user = (User) resource_repository.getUser("sys-gw-tester");
//			System.out.println(">>>>>>===");
//			System.out.println(user.toString(true));
//			System.out.println(">>>>>>===");
//		} catch (SCIMException e) {
//			e.printStackTrace();
//		}
//	}
	
}
