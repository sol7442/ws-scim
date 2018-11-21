package com.wowsanta.scim.obj;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.wowsanta.scim.attribute.Attribute;
import com.wowsanta.scim.attribute.ComplexAttribute;
import com.wowsanta.scim.attribute.MultiValuedAttribute;
import com.wowsanta.scim.attribute.SimpleAttribute;
import com.wowsanta.scim.schema.SCIMAttributeSchema;
import com.wowsanta.scim.schema.SCIMConstants;
import com.wowsanta.scim.schema.SCIMDefinitions;
import com.wowsanta.scim.schema.SCIMResourceTypeSchema;
import com.wowsanta.scim.schema.SCIMSchemaDefinitions;




public class SCIMObject implements Serializable{
	private static final long serialVersionUID = -7757000188475144311L;
	
	protected List<String> schmeas;
	protected  Map<String, Attribute> attributes = new HashMap<String, Attribute>();

	public void createAttribute(SCIMResourceTypeSchema schema) {
		this.schmeas = schema.getSchemas();
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
			this.attributes.put(attr_schema.getName(),attribute);
		};
		
	}
	public List<String> getSchemas(){
		return this.schmeas;
	}
	
	public Map<String, Attribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, Attribute> attributes) {
		this.attributes = attributes;
	}
	
	public void addAttribute(String key, Attribute attribute) {
		this.attributes.put(key, attribute);
	}
	public Attribute getAttribute(String key) {
		return this.attributes.get(key);
	}
	
	public Attribute removeAttribute(String key) {
		return this.attributes.remove(key);
	}
	
	public JsonObject encode() {
		JsonObject root = new JsonObject();
		encodeSchemas(root);
		
		for (Attribute attribute : this.attributes.values()) {	
			if (attribute instanceof SimpleAttribute) {
				encodeSimpleAttribute(root, (SimpleAttribute)attribute);
			}else if( attribute instanceof ComplexAttribute) {
				encodeComplexAttribute(root, (ComplexAttribute)attribute);
			}else if( attribute instanceof MultiValuedAttribute) {
				encodeMultiValuedAttribute(root, (MultiValuedAttribute)attribute);
			}
		}
		
		return root;
	}
	private void encodeSchemas(JsonObject root) {
		JsonArray schema_array = new JsonArray();
		for(String schema_str : getSchemas()) {
			schema_array.add(schema_str);
		}
		root.add(SCIMConstants.CommonSchemaConstants.SCHEMAS, schema_array);
	}
	private void encodeMultiValuedAttribute(JsonObject root, MultiValuedAttribute attribute) {
		JsonArray array = new JsonArray();
		for (Attribute subAttribute : attribute.getAttributeValues()) {
			if(attribute.getType() == SCIMDefinitions.DataType.COMPLEX) {
				encodeComplexAttribute(array, (ComplexAttribute)subAttribute);
			}else {
				encodeSimpleAttribute(array, (SimpleAttribute)subAttribute);
			}
		}
		
		attribute.getAttributePrimitiveValues();
		root.add(attribute.getName(), array);
	}
	

	private void encodeComplexAttribute(JsonObject root, ComplexAttribute attribute) {
		JsonObject object = new JsonObject();
		for (Attribute subAttribute : attribute.getSubAttributesList().values()) {
			if (subAttribute instanceof SimpleAttribute) {				
				encodeSimpleAttribute(object, (SimpleAttribute)subAttribute);
			}else if( subAttribute instanceof ComplexAttribute) {
				encodeComplexAttribute(object, (ComplexAttribute)subAttribute);
			}else if( subAttribute instanceof MultiValuedAttribute) {
				encodeMultiValuedAttribute(object, (MultiValuedAttribute)subAttribute);
			}
		}
		root.add(attribute.getName(),object);
	}
	private void encodeComplexAttribute(JsonArray array, ComplexAttribute attribute) {
		JsonObject object = new JsonObject();
		for (Attribute subAttribute : attribute.getSubAttributesList().values()) {
			if (subAttribute instanceof SimpleAttribute) {				
				encodeSimpleAttribute(object, (SimpleAttribute)subAttribute);
			}else if( subAttribute instanceof ComplexAttribute) {
				encodeComplexAttribute(object, (ComplexAttribute)subAttribute);
			}else if( subAttribute instanceof MultiValuedAttribute) {
				encodeMultiValuedAttribute(object, (MultiValuedAttribute)subAttribute);
			}
		}
		array.add(object);
	}
	
	private void encodeSimpleAttribute(JsonObject object, SimpleAttribute attribute) {
		if(attribute.isNull()) {
			object.addProperty(attribute.getName(),"");
		}else {
			object.addProperty(attribute.getName(), attribute.getValue().toString());
		}
	}
	private void encodeSimpleAttribute(JsonArray array, SimpleAttribute attribute) {
		JsonObject object = new JsonObject();
		if(attribute.isNull()) {
			object.addProperty(attribute.getName(),"");
		}else {
			object.addProperty(attribute.getName(),attribute.getValue().toString());
		}
		array.add(object);
	}
}
