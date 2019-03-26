package com.wowsanta.scim.server;

import java.io.FileReader;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.wowsanta.scim.repository.SCIMRepositoryManager;
import com.wowsanta.scim.schema.SCIMResourceTypeSchema;

public class SCIMServerTest {

	@Test
	public void load_resource_manager() {
		System.out.println("------------------------------");
		String file_name = "../config/scim_server_config.json";
		
		try {

			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			JsonReader reader = new JsonReader(new FileReader(file_name));

			SCIMRepositoryManager resouce_mgr = gson.fromJson(reader,SCIMRepositoryManager.class);

			System.out.println(resouce_mgr);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		System.out.println("------------------------------");
	}
}
