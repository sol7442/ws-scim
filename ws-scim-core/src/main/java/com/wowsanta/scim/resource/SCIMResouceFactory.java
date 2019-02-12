package com.wowsanta.scim.resource;

import java.util.List;

import com.google.gson.JsonObject;
import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.obj.SCIMEnterpriseUser;
import com.wowsanta.scim.obj.SCIMResource;
import com.wowsanta.scim.obj.SCIMUser;
import com.wowsanta.scim.schema.SCIMConstants;

public class SCIMResouceFactory {
	private static SCIMResouceFactory instance = null;
	
	private SCIMResoureDefinition user;
	private SCIMResoureDefinition group;
	
	public static SCIMResouceFactory getInstance() {
		if(instance == null) {
			instance = new SCIMResouceFactory();
		}
		return instance;
	}
	public void setUserClass(String schema, String className){
		this.user = new SCIMResoureDefinition();
		this.user.setSchema(schema);
		this.user.setClassName(className);
	}
	public void setGroupClass(String schema, String className){
		this.group = new SCIMResoureDefinition();
		this.group.setSchema(schema);
		this.group.setClassName(className);
	}
	public SCIMResource newUserResource(String schema) throws SCIMException{
		SCIMResource resource = null;
		
		if(SCIMConstants.USER_CORE_SCHEMA_URI.equals(schema)){
			resource = new SCIMUser();
		}else if(SCIMConstants.ENTERPRISEUSER_CORE_SCHEMA_URI.equals(schema)){
			resource = new SCIMEnterpriseUser();
		}else if(this.user.getSchema().equals(schema)){
			try {
				resource = (SCIMResource) Class.forName(this.user.getClassName()).newInstance();
			} catch (Exception e) {
				throw new SCIMException("NEW RESOURCE INSTANCE CREATE FAILED : " + schema, e);
			}
		}
		return resource;
	}
	
	public SCIMResource newUserResource(List<String> schemas) throws SCIMException{
		SCIMResource resource = null;
		if(schemas.contains(this.user.getSchema())){
			try {
				resource = (SCIMResource) Class.forName(this.user.getClassName()).newInstance();
			} catch (Exception e) {
				throw new SCIMException("NEW RESOURCE INSTANCE CREATE FAILED : " + this.user, e);
			}
		}else {
			if(schemas.contains(SCIMConstants.ENTERPRISEUSER_CORE_SCHEMA_URI)){
				resource = new SCIMEnterpriseUser();
			}else{
				resource = new SCIMUser();
			}
		}
		return resource;
	}
	public SCIMResource newUserResource(JsonObject json_obj) throws SCIMException {
		SCIMResource resource = newUserResource(new SCIMResource(json_obj).getSchemas());
		resource.parse(json_obj.toString());
		return resource;
	}
	public SCIMResource parse(String path, JsonObject json_obj) throws SCIMException {
		SCIMResource resource = null;
		if(path.equals(SCIMConstants.USER_ENDPOINT)){
			resource = newUserResource(json_obj);//
		}else {
			
		}
		return resource;
	}
}
