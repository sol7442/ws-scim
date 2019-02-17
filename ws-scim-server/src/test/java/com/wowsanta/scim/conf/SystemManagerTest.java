package com.wowsanta.scim.conf;


import org.junit.Test;

import com.wowsanta.scim.SCIMSystemManager;
import com.wowsanta.scim.exception.SCIMException;

public class SystemManagerTest {

	private final String config_file = "../config/home_dev_scim-service-provider.json";

	//@Test
	public void system_manager_load_test() {
		load_system_manager(config_file);
		System.out.println(SCIMSystemManager.getInstance().getServiceProvider().toString(true));
	}
	
	
	public void load_system_manager(String config_file_path) {
		try {
			SCIMSystemManager.getInstance().load(config_file_path);
		} catch (SCIMException e) {
			e.printStackTrace();
		}
	}
	
	
//	
//	//@Test
//	public void save() {
//		SCIMSystemManager mgr = SCIMSystemManager.getInstance();
//		
//		
//		System.out.println("===[init  SystemManagerTest start]========================");
//		
//		String repositoryClass = "com.ehyundai.sso.SSORepositoryManager";
//		String scimUserClass   = "com.ehyundai.im.User";
//		String scimUserGroupClass   = "com.ehyundai.im.UserGroup";
//		String scimGroupClass   = "com.ehyundai.im.Group";
//		String resource_config = "../config/resouce_config_raonsecure_sso.json";
//		
//		try {
//			Method load_method = Class.forName(repositoryClass).getDeclaredMethod("load",String.class);
//			SCIMResourceRepository res_rep = (SCIMResourceRepository)load_method.invoke(null,resource_config);
//			res_rep.setRepositoryClass(repositoryClass);
//			res_rep.setRepositoryType("RDB");
//			
//			mgr.setResourceRepository(res_rep);
//			
//			FileWriter writer = new FileWriter(new File(file_name));
//			Gson gson = new GsonBuilder().setPrettyPrinting().create();
//			gson.toJson(mgr,writer);
//			writer.flush();
//			writer.close();
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//			//throw new SCIMException("RepositoryManager Load Error ["+repository_class+"]["+repository_config+"]",e);
//		} 
////		
//////		SCIMResourceTypeSchema user_schema = SCIMResourceTypeSchema.load("../config/schema/scim_user_schema.json");
////
////		
////		SCIMResourceManager resouce_mgr = SCIMResourceManager.getInstance();
////		SCIMRepository  repository_mgr = resouce_mgr.loadRepositoryManager(repositoryClass, resource_config);
////		repository_mgr.initialize();
////		
////		SCIMResouceFactory user_factory = SCIMResouceFactory.getInstance();
////		user_factory.setUserResourceClass(scimUserClass);
////		user_factory.setUserGroupResoureClass(scimUserGroupClass);			
////		user_factory.setUserResourceSchema(user_schema);
//	
//
//		
//		System.out.println("===[init  SystemManagerTest end]========================");
//		
//		
//	}
//	
//	@Test
//	public void load() {
//		try {
//			SCIMSystemManager mgr = SCIMSystemManager.getInstance();
//			mgr.load(file_name);
//			mgr.initialize();
//			
//			
////			JsonReader reader = new JsonReader(new FileReader(file_name));
////			JsonObject conf = new JsonParser().parse(reader).getAsJsonObject();
////			JsonObject res_rep_json = conf.get("resourceRepository").getAsJsonObject();
////			
////			Class.forName(res_rep_json.get("repositoryClass").getAsString());
////			
////			SCIMResourceRepository res_rep = (SCIMResourceRepository) new GsonBuilder().create().fromJson(res_rep_json,Class.forName(res_rep_json.get("repositoryClass").getAsString()));
////			res_rep.initialize();
////			
//			
//			
////			System.out.println(conf);
////			System.out.println();
//			
//			
//			
////			
////			
////			Gson gson = new GsonBuilder().setPrettyPrinting().create();
////			
////			SystemManager mgr = gson.fromJson(reader,SystemManager.class);
//			
////			System.out.println(mgr.getResourceRepository());
//			
//		}catch(Exception e) {
//			e.printStackTrace();
//		}
//	}
	
}
