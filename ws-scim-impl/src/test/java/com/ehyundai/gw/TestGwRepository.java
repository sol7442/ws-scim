package com.ehyundai.gw;

import java.io.FileReader;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.resource.SCIMUser;

public class TestGwRepository {
	String file_name = "../config/scim_gw_config.json";

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
	
	@Test
	public void get() {
		JsonObject system_conf = load_system_conf();
		GWRepository gw_repo = load_repository(system_conf, "resourceRepository");
		
		try {
			SCIMUser user = gw_repo.getUser("aaaa");
			
			System.out.println(user);
			
		} catch (SCIMException e) {
			e.printStackTrace();
		}
	}
}
