package com.wowsanta.scim.schema;

public class SCIMSchemaManager {
	private static SCIMSchemaManager instance;
	
	
	private SCIMResourceTypeSchema userSchema;
	private SCIMResourceTypeSchema groupSchema;
	private SCIMResourceTypeSchema serviceProviderConfigSchema;
	
	public static SCIMSchemaManager getInstance() {
		if(instance == null) {
			instance = new SCIMSchemaManager();
		}
		return instance;
	}
	
}
