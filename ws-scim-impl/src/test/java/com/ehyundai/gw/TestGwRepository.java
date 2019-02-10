package com.ehyundai.gw;

import java.io.FileReader;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.ehyundai.im.Meta;
import com.ehyundai.im.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.wowsanta.scim.SCIMSystemInfo;
import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.obj.DefaultUserMeta;
import com.wowsanta.scim.resource.SCIMRepositoryManager;
import com.wowsanta.scim.resource.SCIMUser;
import com.wowsanta.scim.scheduler.SCIMSchedulerManager;
import com.wowsanta.scim.util.Random;

public class TestGwRepository {
	static String file_name = "../config/scim_gw_config.json";

	static SCIMSystemInfo sys_info = SCIMSystemInfo.getInstance();
	static {
		sys_info.setUrl("https://wowsanta.com/scim");
		sys_info.setVersion("v2");
	}
	
	
	
	private final String config_file = "../config/scim_repository.json";
	
	//@Test
	public void create_repository_manager_config_test() {
		SCIMRepositoryManager repository_mgr = SCIMRepositoryManager.getInstance();
		
		//GWRepository resource_repo = new GWRepository();
		JsonObject system_conf = load_system_conf();
		GWRepository gw_repo = load_repository(system_conf, "resourceRepository");
		
		repository_mgr.setSystemRepository(gw_repo);
		repository_mgr.setResourceRepository(gw_repo);
		
		try {
			System.out.println(repository_mgr.toString(true));
			repository_mgr.save(config_file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void run_repository_manager_config_test() {
		try {
			SCIMRepositoryManager repository_mgr = SCIMRepositoryManager.getInstance();
			repository_mgr.load(config_file);
			
			GWRepository gw_repo = (GWRepository) repository_mgr.getResourceRepository();
			
			System.out.println(gw_repo);
			
			Calendar cal = Calendar.getInstance();
			Date to = cal.getTime();
			cal.add(Calendar.DATE, -7);
			Date from = cal.getTime();
			
			System.out.println("Date ["+from+"]["+to+"]");
			
			try {
				List<SCIMUser> user_list = gw_repo.getUsers(from,to);
				System.out.println("===["+user_list.size()+"]========================");
				for (SCIMUser scimUser : user_list) {
					//User gw_user = (User)scimUser;
					System.out.println(scimUser.toString());
				}
				System.out.println("===["+user_list.size()+"]========================");
				
				
				repository_mgr.close();
				
				Thread.sleep(1000 * 30 );
				
			} catch (SCIMException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	
	// @Before
	public JsonObject load_system_conf() {
		JsonObject config = null;
		try {

			JsonReader reader = new JsonReader(new FileReader(file_name));
			config = new JsonParser().parse(reader).getAsJsonObject();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return config;
	}
	
	private GWRepository load_repository(JsonObject system_conf, String repository_name) {
		GWRepository repository = null;
		try {
			JsonObject repo_json = system_conf.get(repository_name).getAsJsonObject();
			String class_name = repo_json.get("repositoryClass").getAsString();
			Gson gson = new GsonBuilder().create();
			
			repository = (GWRepository)gson.fromJson(repo_json, Class.forName(class_name));
			repository.initialize();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return repository;
	}
	
	//@Test
	public void getUser() {
		JsonObject system_conf = load_system_conf();
		GWRepository gw_repo = load_repository(system_conf, "resourceRepository");
		
		try {
			User gw_user = (User)gw_repo.getUser("65836156");
			System.out.println(gw_user.toString(true));
		} catch (SCIMException e) {
			e.printStackTrace();
		}
	}
	
	
	//@Test
	public void getUsers() {
		JsonObject system_conf = load_system_conf();
		GWRepository gw_repo = load_repository(system_conf, "resourceRepository");
		
		Calendar cal = Calendar.getInstance();
		Date to = cal.getTime();
		cal.add(Calendar.DATE, -7);
		Date from = cal.getTime();
		
		System.out.println("Date ["+from+"]["+to+"]");
		
		try {
			List<SCIMUser> user_list = gw_repo.getUsers(from,to);
			System.out.println("===["+user_list.size()+"]========================");
			for (SCIMUser scimUser : user_list) {
				//User gw_user = (User)scimUser;
				System.out.println(scimUser.toString());
			}
			System.out.println("===["+user_list.size()+"]========================");
		} catch (SCIMException e) {
			e.printStackTrace();
		}
		
	}
	
	//@Test
	public void createUsers() {
		
		JsonObject system_conf = load_system_conf();
		GWRepository gw_repo = load_repository(system_conf, "resourceRepository");
		
		for(int i= 0; i< 20000; i++) {
			User user = new User();
			user.setId(Random.number(10000000, 99999999));
			user.setEmployeeNumber(user.getId());
			user.setUserName(Random.name());
			user.setActive(Random.yn(90));
			user.setPosition(Random.position());
			user.setJob(Random.job());
			
			user.seteMail(user.getEmployeeNumber() + "@ehyundai.com");
			
			user.setCompanyCode(Random.number(0,5));
			user.setGroupCode(Random.number(0,10));
			
			Date join_date = Random.beforeYears(10);
			user.setJoinDate(join_date);
			
			DefaultUserMeta meta = (DefaultUserMeta)user.getMeta();
			if(user.getActive().equals("N")) {
				if(Random.yn(60).equals("Y")) {
					user.setRetireDate(Random.date(join_date, new Date()));
					meta.setCreated(Random.date(join_date, user.getRetireDate()));
					meta.setLastModified(user.getRetireDate());
				}else {
					Date create_date = Random.beforeYears(1);;//Random.date(new Date(2019,1,1), new Date());
					
					meta.setCreated(create_date);
					meta.setLastModified(Random.date(create_date, new Date()));
				}
			}else {
				Date create_date = Random.beforeYears(1);;
				meta.setCreated(create_date);
				meta.setLastModified(Random.date(create_date, new Date()));
			}
			meta.setLocation(user.getId());			
			System.out.println(user.toString(true));
			
			try {
				gw_repo.createUser(user);
			} catch (SCIMException e) {
				e.printStackTrace();
			}
		}
	}
	
	//@Test
	public void get() {
		JsonObject system_conf = load_system_conf();
		GWRepository gw_repo = load_repository(system_conf, "resourceRepository");
		
		try {
			SCIMUser user = gw_repo.getUser("UR_Code_0000");
			System.out.println(user);
			
		} catch (SCIMException e) {
			e.printStackTrace();
		}
	}
}
