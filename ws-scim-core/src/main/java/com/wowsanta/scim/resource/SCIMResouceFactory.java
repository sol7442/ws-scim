package com.wowsanta.scim.resource;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wowsanta.scim.attribute.Attribute;
import com.wowsanta.scim.attribute.ComplexAttribute;
import com.wowsanta.scim.attribute.MultiValuedAttribute;
import com.wowsanta.scim.attribute.SimpleAttribute;
import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.schema.SCIMAttributeSchema;
import com.wowsanta.scim.schema.SCIMDefinitions;
import com.wowsanta.scim.schema.SCIMResourceTypeSchema;

public class SCIMResouceFactory {
	private static SCIMResouceFactory instance = null;
	
	private SCIMResourceTypeSchema userResourceSchema;
	private String userResourceClass ;
	
	public static SCIMResouceFactory getInstance() {
		if(instance == null) {
			instance = new SCIMResouceFactory();
		}
		return instance;
	}
	
	public void setUserResourceSchema(SCIMResourceTypeSchema schema) {
		this.userResourceSchema = schema;
	}
	
	public void setUserResourceClass(String className) {
		this.userResourceClass = className;
	}
	
	public Map<String, Attribute> createAttribute(SCIMResourceTypeSchema schema) {
		Map<String, Attribute> resouce_attributes = new HashMap<String, Attribute>();
		for (SCIMAttributeSchema attr_schema : schema.getAttributes().values()) {
			Attribute attribute = null;
			if(attr_schema.getMultiValued()){
				attribute = MultiValuedAttribute.create(attr_schema);
			}else {
				if(attr_schema.getType() == SCIMDefinitions.DataType.COMPLEX){
					attribute = ComplexAttribute.create(attr_schema);
				}else {
					attribute = SimpleAttribute.create(attr_schema);
				}
			}
			resouce_attributes.put(attr_schema.getName(),attribute);
		};
		return resouce_attributes;
	}
	
	public SCIMUser createUser() throws  SCIMException{
		
		SCIMUser user = null;
		try {
			user = (SCIMUser) Class.forName(this.userResourceClass).newInstance();
			user.setSchemas(this.userResourceSchema.getSchemas());
		} catch (Exception e) {
			throw new SCIMException("Create User Error ",e);
		}
		
		return user;
	}
	
	public String toJson(Object obj) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(obj);
	}
	public String toJsonString(Object obj) {
		Gson gson = new GsonBuilder().create();
		return gson.toJson(obj);
	}
}
