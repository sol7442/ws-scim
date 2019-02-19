package com.ehyundai.gw;

import java.util.Date;

import org.junit.Test;

import com.ehyundai.object.User;
import com.wowsanta.scim.SCIMSystemManager;
import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.obj.SCIMUserMeta;
import com.wowsanta.scim.resource.SCIMRepositoryManager;
import com.wowsanta.scim.resource.SCIMResourceRepository;
import com.wowsanta.scim.util.Random;

public class TestGwRepository {
	private final String config_file_path = "../config/home_dev_gw_scim-service-provider.json";

	@Test
	public void run_repository_manager_config_test() {
		load_manager(this.config_file_path);
		createUsers(10);
	}

	private void load_manager(String config_file) {
		try {
			SCIMSystemManager.getInstance().load(config_file);
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

			Date join_date = Random.beforeYears(10);
			user.setJoinDate(join_date);

			user.setMeta(new SCIMUserMeta());
			if (user.isActive()) {
				if (Random.yn(60).equals("Y")) {
					user.setRetireDate(Random.date(join_date, new Date()));
					user.getMeta().setCreated(Random.date(join_date, user.getRetireDate()));
					user.getMeta().setLastModified(user.getRetireDate());
				} else {
					Date create_date = Random.beforeYears(1);
					user.getMeta().setCreated(create_date);
					user.getMeta().setLastModified(Random.date(create_date, new Date()));
				}
			} else {
				Date create_date = Random.beforeYears(1);
				user.getMeta().setCreated(create_date);
				user.getMeta().setLastModified(Random.date(create_date, new Date()));
			}
			//meta.setLocation(user.getId());
			System.out.println(user.toString(true));

			try {
				res_repo.createUser(user);
			} catch (SCIMException e) {
				e.printStackTrace();
			}
		}
	}

//	
//	//@Test
//	public void getUser() {
//		JsonObject system_conf = load_system_conf();
//		GWResoureRepository gw_repo = load_repository(system_conf, "resourceRepository");
//		
//		try {
//			User gw_user = (User)gw_repo.getUser("65836156");
//			System.out.println(gw_user.toString(true));
//		} catch (SCIMException e) {
//			e.printStackTrace();
//		}
//	}
//	
//	
//	//@Test
//	public void getUsers() {
//		JsonObject system_conf = load_system_conf();
//		GWResoureRepository gw_repo = load_repository(system_conf, "resourceRepository");
//		
//		Calendar cal = Calendar.getInstance();
//		Date to = cal.getTime();
//		cal.add(Calendar.DATE, -7);
//		Date from = cal.getTime();
//		
//		System.out.println("Date ["+from+"]["+to+"]");
//		
//		try {
//			List<SCIMUser> user_list = gw_repo.getUsers(from,to);
//			System.out.println("===["+user_list.size()+"]========================");
//			for (SCIMUser scimUser : user_list) {
//				//User gw_user = (User)scimUser;
//				System.out.println(scimUser.toString());
//			}
//			System.out.println("===["+user_list.size()+"]========================");
//		} catch (SCIMException e) {
//			e.printStackTrace();
//		}
//		
//	}
//	
//	//@Test
//	
//	//@Test
//	public void get() {
//		JsonObject system_conf = load_system_conf();
//		GWResoureRepository gw_repo = load_repository(system_conf, "resourceRepository");
//		
//		try {
//			SCIMUser user = gw_repo.getUser("UR_Code_0000");
//			System.out.println(user);
//			
//		} catch (SCIMException e) {
//			e.printStackTrace();
//		}
//	}
}
