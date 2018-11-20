package com.wowsanta.scim.schema;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Set;

import org.junit.Test;
public class USER_SCHEMA_Test {

	private final String config_file_name = "../config/schema/scim_user_schema.json";
	
	//@Test
	public void make() {
		SCIMResourceTypeSchema user_schema = new SCIMResourceTypeSchema();		
		user_schema.addSchema(SCIMConstants.USER_CORE_SCHEMA_URI);		
		user_schema.putAttribute(SCIMSchemaDefinitions.ID);
		user_schema.putAttribute(SCIMSchemaDefinitions.EXTERNAL_ID);
		user_schema.putAttribute(SCIMSchemaDefinitions.META);
		user_schema.putAttribute(SCIMSchemaDefinitions.SCIMUserSchemaDefinition.USERNAME);
		user_schema.putAttribute(SCIMSchemaDefinitions.SCIMUserSchemaDefinition.NAME);
		user_schema.putAttribute(SCIMSchemaDefinitions.SCIMUserSchemaDefinition.USERNAME);
		user_schema.putAttribute(SCIMSchemaDefinitions.SCIMUserSchemaDefinition.DISPLAY_NAME);
		user_schema.putAttribute(SCIMSchemaDefinitions.SCIMUserSchemaDefinition.NICK_NAME);
		user_schema.putAttribute(SCIMSchemaDefinitions.SCIMUserSchemaDefinition.PROFILE_URL);
		user_schema.putAttribute(SCIMSchemaDefinitions.SCIMUserSchemaDefinition.TITLE);
		user_schema.putAttribute(SCIMSchemaDefinitions.SCIMUserSchemaDefinition.PREFERRED_LANGUAGE);
		user_schema.putAttribute(SCIMSchemaDefinitions.SCIMUserSchemaDefinition.LOCALE);
		user_schema.putAttribute(SCIMSchemaDefinitions.SCIMUserSchemaDefinition.TIME_ZONE);
		user_schema.putAttribute(SCIMSchemaDefinitions.SCIMUserSchemaDefinition.ACTIVE);
		user_schema.putAttribute(SCIMSchemaDefinitions.SCIMUserSchemaDefinition.PASSWORD);
		user_schema.putAttribute(SCIMSchemaDefinitions.SCIMUserSchemaDefinition.EMAILS);
		user_schema.putAttribute(SCIMSchemaDefinitions.SCIMUserSchemaDefinition.PHONE_NUMBERS);
		user_schema.putAttribute(SCIMSchemaDefinitions.SCIMUserSchemaDefinition.ADDRESSES);
		user_schema.putAttribute(SCIMSchemaDefinitions.SCIMUserSchemaDefinition.GROUPS);
		user_schema.putAttribute(SCIMSchemaDefinitions.SCIMUserSchemaDefinition.ENTITLEMENTS);
		user_schema.putAttribute(SCIMSchemaDefinitions.SCIMUserSchemaDefinition.ROLES);
		user_schema.putAttribute(SCIMSchemaDefinitions.SCIMUserSchemaDefinition.X509CERTIFICATES);
        
		System.out.println(user_schema.toJson());
		try {
			user_schema.save(this.config_file_name);
		} catch (IOException e) {
			System.out.println(System.getProperty("user.dir"));
			e.printStackTrace();
		}
	}
	@Test
	public void load() {
		try {
			SCIMResourceTypeSchema user_schema = SCIMResourceTypeSchema.load(this.config_file_name);
			//System.out.println(user_schema.toJson());
			Set<String> key_set =  user_schema.getAttributes().keySet();
			for (String key : key_set) {
				System.out.println("key : " + key);
			}
			
//			SCIMAttributeSchema attribute_schema = user_schema.getAttribute("addresses");
//			System.out.println(attribute_schema.getUri() + " : " + attribute_schema.toJson());
			
			System.out.println(user_schema.toJson());
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
