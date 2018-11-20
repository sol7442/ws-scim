package com.wowsanta.scim.obj;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.wowsanta.scim.attribute.Attribute;
import com.wowsanta.scim.attribute.MultiValuedAttribute;
import com.wowsanta.scim.schema.SCIMAttributeSchema;
import com.wowsanta.scim.schema.SCIMDefinitions;
import com.wowsanta.scim.schema.SCIMResourceTypeSchema;



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
//				if(attr_schema.getType() == SCIMDefinitions.DataType.COMPLEX){
//					System.out.println(attr_schema.getName() + ":" + "0 complex : " + attr_schema.getSubAttributes());
//				}else {
//					System.out.println(attr_schema.getName() + ":" + "x complex : " + attr_schema.getSubAttributes());
//				}
			}else {
//				System.out.println(attr_schema.getName() + ":" + "x multi : " + attr_schema.getSubAttributes());
//				if(attr_schema.getType() == SCIMDefinitions.DataType.COMPLEX){
//					System.out.println(attr_schema.getName() + ":" + "0 complex : " + attr_schema.getSubAttributes());
//				}else {
//					System.out.println(attr_schema.getName() + ":" + "x complex : " + attr_schema.getSubAttributes());
//				}
			}
//			if(attr_schema.getType() == SCIMDefinitions.DataType.COMPLEX){
//				//attr_schema.getSubAttributes();
//				System.out.println(attr_schema.getName() + ":" + "0 complex : " + attr_schema.getSubAttributes());
//			}else {
//				System.out.println(attr_schema.getName() + ":" + "x complex : " + attr_schema.getSubAttributes());
//			}
			//attr_schema.
			
			if(attribute != null) {
				this.attributes.put(attr_schema.getName(),attribute);
			}
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
	
	public JsonObject encode(boolean nullable) {
		JsonObject root = new JsonObject();
		// add schema
		for (Attribute value : this.attributes.values()) {
			if(value !=null) {
				root.add(value.getName(), value.encode(nullable));
			}
		}
		return root;
	}
}
