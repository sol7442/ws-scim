package com.wowsanta.scim.object;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wowsanta.scim.schema.SCIMConstants;

public class SCIM_Resource_Test {

	@Test
	public void create_resource_test() {
		SCIM_Resource resource = new SCIM_Resource();
		
		resource.setId("testid");
		resource.addSchema(SCIMConstants.USER_CORE_SCHEMA_URI);
		resource.addSchema("urn:com:wowsanta:scim:schemas:core:2.0:User");
		resource.addAttribute("employeeNumber","1234567");
		
		
		GsonBuilder builder = new GsonBuilder().disableHtmlEscaping();
		if(true) {
			builder.setPrettyPrinting();
		}
		Gson gson = builder.create();
		
		System.out.println(gson.toJson(resource));
		
		System.out.println(resource.toString());
	}
}
