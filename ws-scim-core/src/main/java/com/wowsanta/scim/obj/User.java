package com.wowsanta.scim.obj;


import java.util.Collection;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wowsanta.scim.attribute.Attribute;

public class User extends SCIMObject{
	private static final long serialVersionUID = 1629646333520103312L;
	
	public String toJson() {
		Set<Entry<String,Attribute>> entry_set =  this.attributes.entrySet();
		for (Entry<String,Attribute> entry : entry_set) {
			System.out.println(entry.getKey() + " : " + entry.getValue());
			
			System.out.println(entry.getValue());
		}
		
		
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(this);
	}
	
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		Collection<Attribute> atts = this.attributes.values();
		for (Attribute attribute : atts) {
			buffer.append(attribute.toString());
		}
		return buffer.toString();
	}
	
	public static User parse(String strJson)  {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.fromJson(strJson,User.class);
	}
}
