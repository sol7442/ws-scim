package com.wowsanta.scim.resource;


import java.util.Collection;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wowsanta.scim.attribute.Attribute;
import com.wowsanta.scim.obj.SCIMObject2;
import com.wowsanta.scim.schema.SCIMResourceTypeSchema;

public class SCIMUser2 extends SCIMObject2{
	private static final long serialVersionUID = 1629646333520103312L;
	
	public SCIMUser2(SCIMResourceTypeSchema schema) {
		createAttribute(schema);
	}
	
	public String toJson() {
//		Set<Entry<String,Attribute>> entry_set =  this.attributes.entrySet();
//		for (Entry<String,Attribute> entry : entry_set) {
//			System.out.println(entry.getKey() + " : " + entry.getValue());
//			System.out.println(entry.getValue());
//		}
//		
//		Gson gson = new GsonBuilder().setPrettyPrinting().create();
//		return gson.toJson(this);
		return encode().toString();
	}
	
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		Collection<Attribute> atts = this.attributes.values();
		for (Attribute attribute : atts) {
			buffer.append(attribute.toString());
		}
		return buffer.toString();
	}
	
	public static SCIMUser2 parse(String strJson)  {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.fromJson(strJson,SCIMUser2.class);
	}
}
