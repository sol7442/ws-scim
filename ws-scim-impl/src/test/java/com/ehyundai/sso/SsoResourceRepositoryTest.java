package com.ehyundai.sso;

import java.io.File;
import java.util.List;

import org.junit.Test;

import com.ehyundai.object.RepositoryUtil;
import com.ehyundai.object.User;
import com.wowsanta.scim.SCIMSystemManager;
import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.repository.SCIMRepositoryManager;
import com.wowsanta.scim.repository.SCIMRepositoryController;

public class SsoResourceRepositoryTest {
	
	private final String repository_config_file = "../config/poc_raonsecure_sso_mysql_repository.json";
	
	@Test
	public void sso_create_user_10_test() {
		try {
			load_manager(repository_config_file);
			SsoResoureRepository res_repo = (SsoResoureRepository)SCIMRepositoryManager.getInstance().getResourceRepository();
			
			List<User> user_list = RepositoryUtil.createUsers(10);
			for(User user : user_list) {
				System.out.println(user);
				res_repo.createUser(user);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void load_manager(String conf_file_path) {
		try {
			SCIMRepositoryManager.loadFromFile(conf_file_path).initailze();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
