package com.ehyundai.im;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.ehyundai.object.User;
import com.wowsanta.scim.SCIMSystemManager;
import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.obj.SCIMUser;
import com.wowsanta.scim.obj.SCIMUserMeta;
import com.wowsanta.scim.repository.SCIMRepositoryManager;
import com.wowsanta.scim.repository.SCIMRepositoryController;
import com.wowsanta.scim.repository.SCIMServerResourceRepository;
import com.wowsanta.scim.repository.system.SCIMProviderRepository;
import com.wowsanta.scim.repository.system.SCIMSystemRepository;
import com.wowsanta.scim.resource.SCIMSystemColumn;
import com.wowsanta.scim.util.Random;

public class IMResourceRepositoryTest {

	private final String config_file = "../config/home_dev_scim-service-provider.json";
	private final String repository_config_file = "../config/im_oracle_repository.json";
	
	private final int create_user_size = 1;
		
	public void load_manager(String conf_file_path) {
		try {
			SCIMRepositoryManager.loadFromFile(conf_file_path).initailze();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//@Test
	public void groupware_repository_create_20000_test() {
		try {
			
			load_manager(config_file);
			createUsers(create_user_size);
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//@Test
	public void groupware_repository_update() {
		try {
			
			load_manager(config_file);
			
			SCIMServerResourceRepository res_repo = (SCIMServerResourceRepository)SCIMRepositoryManager.getInstance().getResourceRepository();
			
			List<SCIMUser> user_list = res_repo.getUsersByActive();
			System.out.println("user_list["+user_list.size()+"]");
			if(user_list.size() > 0) {
				SCIMUser user_0 = user_list.get(0);
				user_0.setActive(!user_0.isActive());
				
				System.out.println("UPDATE : " + user_0.isActive());
				
				res_repo.updateUser(user_0);
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//@Test
	public void im_repository_get_all() {
		try {
			
			load_manager(config_file);
			
			SCIMServerResourceRepository res_repo = (SCIMServerResourceRepository)SCIMRepositoryManager.getInstance().getResourceRepository();
			
			List<SCIMUser> user_list = res_repo.getUsersByActive();
			System.out.println("user_list["+user_list.size()+"]");
			for (SCIMUser scimUser : user_list) {
				System.out.println(scimUser.toString());
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	//@Test
	public void im_repository_get_by_date() {
		try {
			
			load_manager(config_file);
			
			SCIMServerResourceRepository res_repo = (SCIMServerResourceRepository)SCIMRepositoryManager.getInstance().getResourceRepository();
			
			Calendar cal = Calendar.getInstance();
			Date to = cal.getTime();
			cal.add(Calendar.DATE, -365*10);	// 10 년간의 데이터 모두
			Date from = cal.getTime();
			
			List<SCIMUser> user_list = res_repo.getUsersByDate(from, to);
			System.out.println("user_list["+user_list.size()+"]");
			for (SCIMUser scimUser : user_list) {
				System.out.println(scimUser.toString());
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	//
	public void createUsers(int size) {
		SCIMServerResourceRepository res_repo = (SCIMServerResourceRepository)SCIMRepositoryManager.getInstance().getResourceRepository();
		
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

			Date join_date   = Random.beforeYears(1);
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
}
