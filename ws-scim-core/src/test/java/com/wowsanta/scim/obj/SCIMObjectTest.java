package com.wowsanta.scim.obj;

import java.util.Date;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wowsanta.scim.SCIMSystemInfo;
import com.wowsanta.scim.json.SCIMJsonObject;
import com.wowsanta.scim.resource.SCIMResourceType;
import com.wowsanta.scim.resource.SCIMSchemaExtension;
import com.wowsanta.scim.schema.SCIMConstants;


public class SCIMObjectTest {

//	static SCIMSystemInfo sys_info = SCIMSystemInfo.getInstance();
//	static {
//		sys_info.setUrl("https://wowsanta.com/scim");
//		sys_info.setVersion("v2");
//	}
	
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
	
//	//@Test
//	public void default_object_test() {
//		DefaultEnterpriseUser employeer = new DefaultEnterpriseUser();
//		employeer.setId("12345");
//		
//		DefaultUserMeta meta = (DefaultUserMeta)employeer.getMeta();
//		meta.setCreated(new Date());
//		meta.setLocation(employeer.getId());
//		meta.setLastModified(new Date());
//		
//		System.out.println(employeer.toString(true));
//	}
	
	@Test
	public void test_user_object() {
		EnterpriseUser user = new EnterpriseUser();
		
		user.id = "1234";
		user.externalId = "1234-ex";
		user.userName = "1234_tester";
		
		user.employeeNumber = "1234-12345";
		user.organization = "dev1";
		
		System.out.println(user);
		System.out.println(user.toString());
		System.out.println(user.toString(true));
		
		EnterpriseUser p_user = new EnterpriseUser();
		p_user.parse(user.toString());
		
		System.out.println("=====================");
		
		System.out.println(p_user.toString(true));
		
	}
	
	public class USER extends SCIMUser{
		/**
		 * 
		 */
		private static final long serialVersionUID = -1971752394408810221L;

		public String id;
		public String externalId;
		public String userName;
		
		public USER() {
			addSchema(SCIMConstants.USER_CORE_SCHEMA_URI);
		}

		@Override
		public JsonObject parse(String json_str) {
			JsonObject json_obj = super.parse(json_str);
			
			this.id 		= JsonUtil.toString(json_obj.get("id"));
			this.externalId = JsonUtil.toString(json_obj.get("externalId"));
			this.userName 	= JsonUtil.toString(json_obj.get("userName"));
			
			return json_obj;
		}

		@Override
		public JsonObject encode() {
			JsonObject json_object = super.encode();
			
			json_object.addProperty("id",this.id);
			json_object.addProperty("externalId",this.externalId);
			json_object.addProperty("userName",this.userName);
			
			return json_object ;//gson.toJson(json_object);
		}
	}
	
	public class EnterpriseUser extends USER{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public String employeeNumber;
		public String organization;
		
		public EnterpriseUser() {
			super();
			addSchema(SCIMConstants.ENTERPRISEUSER_CORE_SCHEMA_URI);
		}
		
		@Override
		public JsonObject parse(String json_str) {
			JsonObject json_user = super.parse(json_str);
			JsonObject json_enterprise = json_user.get(SCIMConstants.ENTERPRISEUSER_CORE_SCHEMA_URI).getAsJsonObject();
			if(!json_enterprise.isJsonNull()){
				this.employeeNumber = JsonUtil.toString(json_enterprise.get("employeeNumber"));
				this.organization	= JsonUtil.toString(json_enterprise.get("organization"));
			}
			return json_user;
		}
		
		public JsonObject encode() {
			JsonObject json_object = new JsonObject();
			
			json_object.addProperty("employeeNumber", this.employeeNumber);
			json_object.addProperty("organization", this.organization);
			
			JsonObject suepr_json = super.encode();
			suepr_json.add(SCIMConstants.ENTERPRISEUSER_CORE_SCHEMA_URI, json_object);
			
			return suepr_json;
		}
	}
}
