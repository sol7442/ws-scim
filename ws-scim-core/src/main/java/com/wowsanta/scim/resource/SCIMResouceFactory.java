package com.wowsanta.scim.resource;

import java.util.HashMap;
import java.util.Map;

import com.wowsanta.scim.attribute.Attribute;
import com.wowsanta.scim.attribute.ComplexAttribute;
import com.wowsanta.scim.attribute.MultiValuedAttribute;
import com.wowsanta.scim.attribute.SimpleAttribute;
import com.wowsanta.scim.schema.SCIMAttributeSchema;
import com.wowsanta.scim.schema.SCIMDefinitions;
import com.wowsanta.scim.schema.SCIMResourceTypeSchema;

public class SCIMResouceFactory {
	private static SCIMResouceFactory instance = null;
	
	private SCIMResourceTypeSchema userResourceSchema;
	
	public static SCIMResouceFactory getInstance() {
		if(instance == null) {
			instance = new SCIMResouceFactory();
		}
		return instance;
	}
	
	public void setUserResourceSchema(SCIMResourceTypeSchema schema) {
		this.userResourceSchema = schema;
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
	
	public SCIMUser createUser() {
		return new SCIMUser(this.userResourceSchema);//createAttribute(this.userResourceSchema));
	}
}
