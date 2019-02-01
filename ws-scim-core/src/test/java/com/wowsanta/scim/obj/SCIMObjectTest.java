package com.wowsanta.scim.obj;

import java.util.Date;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wowsanta.scim.SCIMSystemInfo;
import com.wowsanta.scim.resource.SCIMResourceType;
import com.wowsanta.scim.resource.SCIMSchemaExtension;
import com.wowsanta.scim.schema.SCIMConstants;


public class SCIMObjectTest {

	static SCIMSystemInfo sys_info = SCIMSystemInfo.getInstance();
	static {
		sys_info.setUrl("https://wowsanta.com/scim");
		sys_info.setVersion("v2");
	}
	
	//@Test
	public void resource_type_test() {
		SCIMResourceType resType = new SCIMResourceType();
		resType.setSchemas(SCIMConstants.RESOURCE_TYPE_SCHEMA_URI);
		
		resType.setId(SCIMConstants.USER);
		resType.setName(SCIMConstants.USER);
		resType.setEndpoint(SCIMConstants.USER_ENDPOINT);
		resType.setSchema(SCIMConstants.USER_CORE_SCHEMA_URI);
		SCIMSchemaExtension ex_schema = new SCIMSchemaExtension();
		ex_schema.setSchema(SCIMConstants.ENTERPRISEUSER_CORE_SCHEMA_URI);
		ex_schema.setRequired(true);
		resType.addSchemaExtension(ex_schema);
		
		resType.getMeta().setLocation(SCIMConstants.USER);
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		gson.toJson(resType);
		
		System.out.println(gson.toJson(resType));
	}
	
	@Test
	public void default_object_test() {
		DefaultEnterpriseUser employeer = new DefaultEnterpriseUser();
		employeer.setId("12345");
		
		DefaultUserMeta meta = (DefaultUserMeta)employeer.getMeta();
		meta.setCreated(new Date());
		meta.setLocation(employeer.getId());
		meta.setLastModified(new Date());
		
		System.out.println(employeer.toString(true));
	}
}
