package com.ehyundai.im;

import java.io.FileReader;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.resource.SCIMRepository;
import com.wowsanta.scim.resource.SCIMResourceRepository;
import com.wowsanta.scim.resource.SCIMSystemRepository;

public class TestIMRepository {

	String file_name = "../config/scim_system_config.json";
	
	//@Before
	public SCIMSystemRepository load() {
		SCIMSystemRepository systemRepository = null;
		SCIMResourceRepository resourceRepository = null;
		try {
		
			JsonReader reader = new JsonReader(new FileReader(file_name));
			JsonObject config = new JsonParser().parse(reader).getAsJsonObject();

			JsonObject res_repo_json = config.get("resourceRepository").getAsJsonObject();
			JsonObject sys_repo_json = config.get("systemRepository").getAsJsonObject();

			systemRepository   = (SCIMSystemRepository)loadRepository(sys_repo_json);
			systemRepository.initialize();
			
			
			resourceRepository   = (SCIMResourceRepository)loadRepository(res_repo_json);
			resourceRepository.initialize();
			
			System.out.println(sys_repo_json);
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return systemRepository;
	}
	
	public SCIMResourceRepository load2() {
		SCIMSystemRepository systemRepository = null;
		SCIMResourceRepository resourceRepository = null;
		try {
		
			JsonReader reader = new JsonReader(new FileReader(file_name));
			JsonObject config = new JsonParser().parse(reader).getAsJsonObject();

			JsonObject res_repo_json = config.get("resourceRepository").getAsJsonObject();
			JsonObject sys_repo_json = config.get("systemRepository").getAsJsonObject();

			systemRepository   = (SCIMSystemRepository)loadRepository(sys_repo_json);
			systemRepository.initialize();
			
			
			resourceRepository   = (SCIMResourceRepository)loadRepository(res_repo_json);
			resourceRepository.initialize();
			
			System.out.println(sys_repo_json);
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return resourceRepository;
	}
	
	private SCIMRepository loadRepository(JsonObject repository_json) throws ClassNotFoundException, SCIMException {
		String class_name = repository_json.get("repositoryClass").getAsString();
		Gson gson = new GsonBuilder().create();
		return (SCIMRepository)gson.fromJson(repository_json, Class.forName(class_name)); 
	}
	
	@Test
	public void getUser() {
		SCIMResourceRepository resourceRepository = load2();
		try {
			resourceRepository.getUser("");
		} catch (SCIMException e) {
			e.printStackTrace();
		}
	}
	@Test
	public void getAdmin() {
		SCIMSystemRepository systemRepository = load();
		try {
			Admin admin = (Admin)systemRepository.getAdmin("ADMIN");
			if(admin != null) {
				admin.getId();
				admin.getName();
				admin.getType();
				admin.getPw();
				
				System.out.println("PW : " + admin.getPw());
			}else {
				System.out.println("not found");
			}
		} catch (SCIMException e) {
			e.printStackTrace();
		}
	}
}
