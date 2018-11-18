package com.wowsanta.scim.resource;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

import com.wowsanta.scim.schema.SCIMAttributeSchema;
import com.wowsanta.scim.schema.SCIMConstants;
import com.wowsanta.scim.schema.SCIMDefinitions;
import com.wowsanta.scim.schema.SCIMResourceTypeSchema;

public class ResourceManagerTest {

	
	@Test
	public void createUserSchema() {
		SCIMResourceTypeSchema user_schema = new SCIMResourceTypeSchema();
		
		//user_schema.addAttribute(attribute);
		SCIMAttributeSchema ID = new SCIMAttributeSchema(
				SCIMConstants.CommonSchemaConstants.ID_URI,
				SCIMConstants.CommonSchemaConstants.ID,
				SCIMDefinitions.DataType.STRING,
				false,
				SCIMConstants.CommonSchemaConstants.ID_DESC,
				true,
				true,
                SCIMDefinitions.Mutability.READ_ONLY,
                SCIMDefinitions.Returned.ALWAYS,
                SCIMDefinitions.Uniqueness.SERVER,
                null,
                null,
                null
				);		
		SCIMAttributeSchema EXTERNAL_ID = new SCIMAttributeSchema(SCIMConstants.CommonSchemaConstants.EXTERNAL_ID_URI,
                SCIMConstants.CommonSchemaConstants.EXTERNAL_ID,
                SCIMDefinitions.DataType.STRING, false, SCIMConstants.CommonSchemaConstants.EXTERNAL_ID_DESC,
                false, true,
                SCIMDefinitions.Mutability.READ_WRITE, SCIMDefinitions.Returned.DEFAULT,
                SCIMDefinitions.Uniqueness.NONE, null, null, null);

		
		SCIMAttributeSchema RESOURCE_TYPE = new SCIMAttributeSchema(SCIMConstants.CommonSchemaConstants.RESOURCE_TYPE_URI,
                SCIMConstants.CommonSchemaConstants.RESOURCE_TYPE,
                SCIMDefinitions.DataType.STRING, false, SCIMConstants.CommonSchemaConstants.RESOURCE_TYPE_DESC,
                false, true,
                SCIMDefinitions.Mutability.READ_ONLY, SCIMDefinitions.Returned.DEFAULT,
                SCIMDefinitions.Uniqueness.NONE, null, null, null);
        

		SCIMAttributeSchema CREATED = new SCIMAttributeSchema(SCIMConstants.CommonSchemaConstants.CREATED_URI,
                SCIMConstants.CommonSchemaConstants.CREATED,
                SCIMDefinitions.DataType.DATE_TIME, false, SCIMConstants.CommonSchemaConstants.CREATED_DESC,
                false, false,
                SCIMDefinitions.Mutability.READ_ONLY, SCIMDefinitions.Returned.DEFAULT,
                SCIMDefinitions.Uniqueness.NONE, null, null, null);
        
        SCIMAttributeSchema LAST_MODIFIED = new SCIMAttributeSchema(SCIMConstants.CommonSchemaConstants.LAST_MODIFIED_URI,
                SCIMConstants.CommonSchemaConstants.LAST_MODIFIED,
                SCIMDefinitions.DataType.DATE_TIME, false, SCIMConstants.CommonSchemaConstants
                        .LAST_MODIFIED_DESC, false, false,
                SCIMDefinitions.Mutability.READ_ONLY, SCIMDefinitions.Returned.DEFAULT,
                SCIMDefinitions.Uniqueness.NONE, null, null, null);
        
        SCIMAttributeSchema LOCATION = new SCIMAttributeSchema(SCIMConstants.CommonSchemaConstants.LOCATION_URI,
                SCIMConstants.CommonSchemaConstants.LOCATION,
                SCIMDefinitions.DataType.STRING, false, SCIMConstants.CommonSchemaConstants.LOCATION_DESC, false,
                false,
                SCIMDefinitions.Mutability.READ_ONLY, SCIMDefinitions.Returned.DEFAULT,
                SCIMDefinitions.Uniqueness.NONE, null, null, null);
        
        SCIMAttributeSchema VERSION = new SCIMAttributeSchema(SCIMConstants.CommonSchemaConstants.VERSION_URI,
                SCIMConstants.CommonSchemaConstants.VERSION,
                SCIMDefinitions.DataType.STRING, false, SCIMConstants.CommonSchemaConstants.VERSION_DESC, false,
                true,
                SCIMDefinitions.Mutability.READ_ONLY, SCIMDefinitions.Returned.DEFAULT,
                SCIMDefinitions.Uniqueness.NONE, null, null, null);
        
		SCIMAttributeSchema META =  new SCIMAttributeSchema(SCIMConstants.CommonSchemaConstants.META_URI,
                SCIMConstants.CommonSchemaConstants.META,
                SCIMDefinitions.DataType.COMPLEX, false, SCIMConstants.CommonSchemaConstants.META_DESC, false,
                false,
                SCIMDefinitions.Mutability.READ_ONLY, SCIMDefinitions.Returned.DEFAULT,
                SCIMDefinitions.Uniqueness.NONE, null, null,
                new ArrayList<SCIMAttributeSchema>(Arrays.asList(RESOURCE_TYPE, CREATED, LAST_MODIFIED, LOCATION, VERSION)));
		
		
		
//		user_schema.setSchemasList(new ArrayList<String>(Arrays.asList(SCIMConstants.USER_CORE_SCHEMA_URI)));
//		user_schema.addAttribute(ID);
		
		user_schema.addSchema(SCIMConstants.USER_CORE_SCHEMA_URI);		
		user_schema.putAttribute(ID);
		user_schema.putAttribute(EXTERNAL_ID);
		user_schema.putAttribute(META);
		
		System.out.println(user_schema.toJson());
		
		
		SCIMResourceTypeSchema service_provider_config_schema = new SCIMResourceTypeSchema();
		service_provider_config_schema.addSchema(SCIMConstants.SERVICE_PROVIDER_CONFIG_SCHEMA_URI);
		
		
        SCIMAttributeSchema DOCUMENTATION_URI = new SCIMAttributeSchema(
                SCIMConstants.ServiceProviderConfigSchemaConstants.DOCUMENTATION_URI_URI,
                SCIMConstants.ServiceProviderConfigSchemaConstants.DOCUMENTATION_URI,
                SCIMDefinitions.DataType.REFERENCE, false,
                SCIMConstants.ServiceProviderConfigSchemaConstants.DOCUMENTATION_URI_DESC, true, false,
                SCIMDefinitions.Mutability.READ_ONLY, SCIMDefinitions.Returned.DEFAULT,
                SCIMDefinitions.Uniqueness.NONE, null, null, null);

        service_provider_config_schema.putAttribute(DOCUMENTATION_URI);
        
		System.out.println(service_provider_config_schema.toJson());
		
//		new ArrayList<String>(Arrays.asList(SCIMConstants.SERVICE_PROVIDER_CONFIG_SCHEMA_URI)),
//        META,
//        SCIMServiceProviderConfigSchemaDefinition.DOCUMENTATION_URI,
//        SCIMServiceProviderConfigSchemaDefinition.PATCH,
//        SCIMServiceProviderConfigSchemaDefinition.BULK,
//        SCIMServiceProviderConfigSchemaDefinition.SORT,
//        SCIMServiceProviderConfigSchemaDefinition.FILTER,
//        SCIMServiceProviderConfigSchemaDefinition.CHANGE_PASSWORD,
//        SCIMServiceProviderConfigSchemaDefinition.ETAG,
//        SCIMServiceProviderConfigSchemaDefinition.AUTHENTICATION_SCHEMES);
        
        
//		private SCIMAttributeSchema(String uri, String name, SCIMDefinitions.DataType type, Boolean multiValued,
//				String description, Boolean required, Boolean caseExact, SCIMDefinitions.Mutability mutability,
//				SCIMDefinitions.Returned returned, SCIMDefinitions.Uniqueness uniqueness, ArrayList<String> canonicalValues,
//				ArrayList<SCIMDefinitions.ReferenceType> referenceTypes, ArrayList<SCIMAttributeSchema> subAttributes) {\
//			
//		
//		SCIMAttributeSchema.createSCIMAttributeSchema(SCIMConstants.CommonSchemaConstants.ID_URI,
//                SCIMConstants.CommonSchemaConstants.ID,
//                SCIMDefinitions.DataType.STRING, false, SCIMConstants.CommonSchemaConstants.ID_DESC, true, true,
//                SCIMDefinitions.Mutability.READ_ONLY, SCIMDefinitions.Returned.ALWAYS,
//                SCIMDefinitions.Uniqueness.SERVER, null, null, null);
//		
		
//        new ArrayList<String>(Arrays.asList(SCIMConstants.USER_CORE_SCHEMA_URI, schemaExtension.getURI())),
//        SCIMSchemaDefinitions.ID, SCIMSchemaDefinitions.EXTERNAL_ID, SCIMSchemaDefinitions.META,
//        SCIMSchemaDefinitions.SCIMUserSchemaDefinition.USERNAME,
//        SCIMSchemaDefinitions.SCIMUserSchemaDefinition.NAME,
//        SCIMSchemaDefinitions.SCIMUserSchemaDefinition.DISPLAY_NAME,
//        SCIMSchemaDefinitions.SCIMUserSchemaDefinition.NICK_NAME,
//        SCIMSchemaDefinitions.SCIMUserSchemaDefinition.PROFILE_URL,
//        SCIMSchemaDefinitions.SCIMUserSchemaDefinition.TITLE,
//        SCIMSchemaDefinitions.SCIMUserSchemaDefinition.USER_TYPE,
//        SCIMSchemaDefinitions.SCIMUserSchemaDefinition.PREFERRED_LANGUAGE,
//        SCIMSchemaDefinitions.SCIMUserSchemaDefinition.LOCALE,
//        SCIMSchemaDefinitions.SCIMUserSchemaDefinition.TIME_ZONE,
//        SCIMSchemaDefinitions.SCIMUserSchemaDefinition.ACTIVE,
//        SCIMSchemaDefinitions.SCIMUserSchemaDefinition.PASSWORD,
//        SCIMSchemaDefinitions.SCIMUserSchemaDefinition.EMAILS,
//        SCIMSchemaDefinitions.SCIMUserSchemaDefinition.PHONE_NUMBERS,
//        SCIMSchemaDefinitions.SCIMUserSchemaDefinition.IMS,
//        SCIMSchemaDefinitions.SCIMUserSchemaDefinition.PHOTOS,
//        SCIMSchemaDefinitions.SCIMUserSchemaDefinition.ADDRESSES,
//        SCIMSchemaDefinitions.SCIMUserSchemaDefinition.GROUPS,
//        SCIMSchemaDefinitions.SCIMUserSchemaDefinition.ENTITLEMENTS,
//        SCIMSchemaDefinitions.SCIMUserSchemaDefinition.ROLES,
//        SCIMSchemaDefinitions.SCIMUserSchemaDefinition.X509CERTIFICATES,
//        schemaExtension);
	}
}
