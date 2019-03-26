package com.wowsanta.scim.conf;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.wowsanta.scim.obj.DefaultMeta;
import com.wowsanta.scim.obj.SCIMResource;
import com.wowsanta.scim.obj.ServiceProviderConfigMeta;
import com.wowsanta.scim.resource.SCIMResoureDefinition;
import com.wowsanta.scim.schema.SCIMAttributeSchema;
import com.wowsanta.scim.schema.SCIMConstants;
import com.wowsanta.scim.schema.SCIMDefinitions;
import com.wowsanta.scim.schema.SCIMResourceTypeSchema;
import com.wowsanta.scim.schema.SCIMSchemaDefinitions;
import com.wowsanta.scim.schema.SCIMSchemaDefinitions.SCIMServiceProviderConfigSchemaDefinition;
import com.wowsanta.scim.service.SCIMServiceProvider;

public class SCIMServiceProviderTest {

	final String config_file = "../config/im_scim-service-provider.json";

	@Test
	public void service_provider_test() {
		try {
			
			SCIMServiceProvider service_provider = SCIMServiceProvider.loadFromFile(config_file);
			
			System.out.println(service_provider.tojson(true));
//			
//			
//			JsonReader reader = new JsonReader(new InputStreamReader(new FileInputStream(new File(config_file)),"UTF-8"));// FileReader(file_name));
//			JsonObject json_object = new JsonParser().parse(reader).getAsJsonObject();
//			
//			System.out.println(json_object);
//			
//			List<String> schema_list = new ArrayList<String>();
//			JsonArray schema_list_json = json_object.get("schemas").getAsJsonArray();
//			for (JsonElement jsonElement : schema_list_json) {
//				System.out.println("schema_list_json" + jsonElement.getAsString());
//				schema_list.add(jsonElement.getAsString());
//			}
//			
//			JsonElement meta_json = json_object.get("meta");
//			System.out.println("meta" + meta_json);
//			
//			
//			
//			Gson gson = new GsonBuilder().disableHtmlEscaping().create();
//			ServiceProviderConfigMeta meta = gson.fromJson(meta_json, ServiceProviderConfigMeta.class);
//			 
//			
//			SCIMResourceTypeSchema aa = SCIMResourceTypeSchema.load(config_file);
//			System.err.println(aa.toJson());
//			
//			
//			
//			
//			SCIMServiceProvider service_provider = new SCIMServiceProvider();
//			service_provider.setSchemas(schema_list.toArray(new String[1]));
//			service_provider.setMeta(meta);
//			service_provider.setDocumentationUri(json_object.get("documentationUri").getAsString());
//			
//			Map<String,SCIMResoureDefinition> resource = service_provider.getResources();
//			//resource.put("patch", value)
//			System.out.println("patch : " + json_object.get("patch"));
//			resource.put("patch", gson.fromJson(json_object.get("patch"), SCIMResoureDefinition.class));
//			
//			
//			service_provider.save(config_file + "_new");
//			SCIMServiceProvider read_service_provider = SCIMServiceProvider.loadFromFile(config_file + "_new");
//			System.err.println(read_service_provider.tojson(true));

//			SCIMResourceTypeSchema SCIM_SERVICE_PROVIDER_CONFIG_SCHEMA = new SCIMResourceTypeSchema();
//
//			SCIM_SERVICE_PROVIDER_CONFIG_SCHEMA.addSchema(SCIMConstants.SERVICE_PROVIDER_CONFIG_SCHEMA_URI);
//			SCIM_SERVICE_PROVIDER_CONFIG_SCHEMA.putAttribute(SCIMSchemaDefinitions.META);
//			SCIM_SERVICE_PROVIDER_CONFIG_SCHEMA
//					.putAttribute(SCIMServiceProviderConfigSchemaDefinition.DOCUMENTATION_URI);
//			SCIM_SERVICE_PROVIDER_CONFIG_SCHEMA.putAttribute(SCIMServiceProviderConfigSchemaDefinition.PATCH);
//			SCIM_SERVICE_PROVIDER_CONFIG_SCHEMA.putAttribute(SCIMServiceProviderConfigSchemaDefinition.BULK);
//			SCIM_SERVICE_PROVIDER_CONFIG_SCHEMA.putAttribute(SCIMServiceProviderConfigSchemaDefinition.SORT);
//			SCIM_SERVICE_PROVIDER_CONFIG_SCHEMA.putAttribute(SCIMServiceProviderConfigSchemaDefinition.FILTER);
//			SCIM_SERVICE_PROVIDER_CONFIG_SCHEMA.putAttribute(SCIMServiceProviderConfigSchemaDefinition.CHANGE_PASSWORD);
//			SCIM_SERVICE_PROVIDER_CONFIG_SCHEMA.putAttribute(SCIMServiceProviderConfigSchemaDefinition.ETAG);
//			SCIM_SERVICE_PROVIDER_CONFIG_SCHEMA.putAttribute(SCIMServiceProviderConfigSchemaDefinition.AUTHENTICATION_SCHEMES);
//
//			
//			SCIM_SERVICE_PROVIDER_CONFIG_SCHEMA.save(config_file + "_new");
//			
//			System.out.println(SCIM_SERVICE_PROVIDER_CONFIG_SCHEMA.toJson());
//
//			
//			
//			SCIMResourceTypeSchema service_provider_config_schema = new SCIMResourceTypeSchema();
//			service_provider_config_schema.addSchema(SCIMConstants.SERVICE_PROVIDER_CONFIG_SCHEMA_URI);
//			
//			
//	        SCIMAttributeSchema DOCUMENTATION_URI = new SCIMAttributeSchema(
//	                SCIMConstants.ServiceProviderConfigSchemaConstants.DOCUMENTATION_URI_URI,
//	                SCIMConstants.ServiceProviderConfigSchemaConstants.DOCUMENTATION_URI,
//	                SCIMDefinitions.DataType.REFERENCE, false,
//	                SCIMConstants.ServiceProviderConfigSchemaConstants.DOCUMENTATION_URI_DESC, true, false,
//	                SCIMDefinitions.Mutability.READ_ONLY, SCIMDefinitions.Returned.DEFAULT,
//	                SCIMDefinitions.Uniqueness.NONE, null, null, null);
//
//	        service_provider_config_schema.putAttribute(DOCUMENTATION_URI);
//	        
//			System.out.println(service_provider_config_schema.toJson());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
