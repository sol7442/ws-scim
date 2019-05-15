package com.ehyundai.gw;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.ehyundai.object.RepositoryUtil;
import com.ehyundai.object.User;
import com.wowsanta.scim.SCIMSystemManager;
import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.obj.SCIMUser;
import com.wowsanta.scim.obj.SCIMUserMeta;
import com.wowsanta.scim.repository.SCIMRepositoryManager;
import com.wowsanta.scim.repository.SCIMResourceGetterRepository;
import com.wowsanta.scim.repository.SCIMRepositoryController;
import com.wowsanta.scim.util.Random;

public class TestGwRepository {
	private final String config_file_path = "../config/ehyundai_gw_mssql_repository.json";

	public void load_manager(String conf_file_path) {
		try {
			File config_file = new File(conf_file_path);
			SCIMRepositoryManager.loadFromFile(config_file_path).initailze();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//@Test
	public void create_user_10_test() {
		try {
			load_manager(config_file_path);
			GWResourceRepository res_repo = (GWResourceRepository) SCIMRepositoryManager.getInstance().getResourceRepository();
			
			List<User> user_list = RepositoryUtil.createUsers(20);
			for(User user : user_list) {
				System.out.println(user.toString(true));
				res_repo.createUser(user);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void getAllActiveUser() {
	
		load_manager(config_file_path);
		SCIMResourceGetterRepository res_repo = (SCIMResourceGetterRepository) SCIMRepositoryManager.getInstance().getResourceRepository();
		
		try {
			List<SCIMUser> user_list = res_repo.getUsersByActive();
			for (SCIMUser scimUser : user_list) {
				System.out.println(scimUser);
			}
			System.out.println("all active user : " + user_list.size());
		} catch (SCIMException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void getUsersByDate() {
	
		load_manager(config_file_path);
		SCIMResourceGetterRepository res_repo = (SCIMResourceGetterRepository) SCIMRepositoryManager.getInstance().getResourceRepository();
		
		try {
			
			Calendar cal = Calendar.getInstance();
			Date to = cal.getTime();
			cal.add(Calendar.DATE, -365*1);	// 1 년간의 데이터 모두
			Date from = cal.getTime();
			
			System.out.println("from : " + from);
			List<SCIMUser> user_list = res_repo.getUsersByDate(from, to);
			for (SCIMUser scimUser : user_list) {
				System.out.println(scimUser);
			}
			System.out.println("from : " + from + " : " + user_list.size());
		} catch (SCIMException e) {
			e.printStackTrace();
		}
	}

}
