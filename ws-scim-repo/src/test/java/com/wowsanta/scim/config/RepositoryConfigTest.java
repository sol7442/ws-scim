package com.wowsanta.scim.config;

import org.junit.Test;

import com.wowsanta.scim.repository.RepositoryDefinitions.REPOSITORY_TYPE;
import com.wowsanta.scim.repository.RepositoryOutputMapper;
import com.wowsanta.scim.repository.SCIMRepositoryManager;
import com.wowsanta.scim.repository.impl.DefaultRepository;
import com.wowsanta.scim.repository.impl.OracleRepository;
import com.wowsanta.scim.repository.resource.ResourceRepositoryConfig;

public class RepositoryConfigTest {

	String resource_repository_config_file = "../config/backup_conf_20190419/im_oracle_repository_config.json";
	
	
	//public static final String im_repository_config_file = "../config/default_oracle_im_repository.json";

	public static final String im_user_resource_output_mapper_file = "../config/backup_conf_20190419/default_oracle_im_user_resource_output_mapper.json";
	public static final String im_user_resource_output_schema_file = "../config/backup_conf_20190419/default_oracle_im_user_resource_output_schema.json";
	public static final String im_user_resource_input_mapper_file = "../config/backup_conf_20190419/default_oracle_im_user_resource_input_mapper.json";
	public static final String im_user_resource_input_schema_file = "../config/backup_conf_20190419/default_oracle_im_user_resource_input_schema.json";

	public static final String im_group_resource_output_mapper_file = "../config/backup_conf_20190419/default_oracle_im_group_resource_output_mapper.json";
	public static final String im_group_resource_output_schema_file = "../config/backup_conf_20190419/default_oracle_im_group_resource_output_schema.json";
	public static final String im_group_resource_input_mapper_file = "../config/backup_conf_20190419/default_oracle_im_group_resource_input_mapper.json";
	public static final String im_group_resource_input_schema_file = "../config/backup_conf_20190419/default_oracle_im_group_resource_input_schema.json";
	
	
	static final String   repository_manager_config_file = "../config/repository_config.json";
	
	@Test
	public void gen_new_repository_manager_config_test() {
		try {
			SCIMRepositoryManager manager = SCIMRepositoryManager.load("../config/im_oracle_repository.json");
			
			ResourceRepositoryConfig config = ResourceRepositoryConfig.load(resource_repository_config_file);
			//System.out.println(config.toString(true));

			
			manager.setResourceRepositoryConfig(config);
			System.out.println(manager.toString(true));
			
			manager.save(repository_manager_config_file);
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//@Test
	public void load_repository_config_test() {
		ResourceRepositoryConfig config = ResourceRepositoryConfig.load(resource_repository_config_file);
		System.out.println(config.toString(true));
		
		System.out.println(config.getType().getClassName());
	}
	//@Test
	public void save_repository_config_test() {
		
		try {
			DefaultRepository repository = OracleRepository.load("../config/default_oracle_im_repository.json");
//			repository.setUserOutputMapper(user_resource_output_mapper);
//			repository.initialize();
			
			ResourceRepositoryConfig config = new ResourceRepositoryConfig();
			config.setType(REPOSITORY_TYPE.ORACLE);
			config.setDbcp(repository.getDbcp());
		
			config.setUserInputMapper(im_user_resource_input_mapper_file);
			config.setUserOutputMapper(im_user_resource_output_mapper_file);
			config.setGroupInputMapper(im_group_resource_input_mapper_file);
			config.setGroupOutputMapper(im_group_resource_output_mapper_file);
			
			config.save(resource_repository_config_file);
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		
		 
	}
}
