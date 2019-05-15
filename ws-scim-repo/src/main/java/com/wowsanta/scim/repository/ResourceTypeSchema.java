package com.wowsanta.scim.repository;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;


public class ResourceTypeSchema implements Serializable{
	private static final long serialVersionUID = 1292919740668517279L;
	
	private List<String> schemas = new ArrayList<String>();
	private Map<String,AttributeSchema> attributes = new HashMap<String,AttributeSchema>(); 


	
	public List<String> getSchemas() {
		return schemas;
	}

	public void setSchemas(List<String> schemas) {
		this.schemas = schemas;
	}
	
	public void addSchema(String schema) {
		this.schemas.add(schema);
	}
	
	public Map<String,AttributeSchema> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String,AttributeSchema> attributes) {
		this.attributes = attributes;
	}
	
	public void putAttribute(String key, AttributeSchema attribute) {
		this.attributes.put(key,attribute);
	}
	
	public void putAttribute(AttributeSchema attribute) {
		this.attributes.put(attribute.getName(),attribute);
	}
	public AttributeSchema getAttribute(String name) {
		return this.attributes.get(name);
	}
	
	public String toString() {
		return toString(false);
	}
	public String toString(boolean pretty) {
		GsonBuilder builder = new GsonBuilder().disableHtmlEscaping();
		if(pretty) {
			builder.setPrettyPrinting();
		}
		Gson gson = builder.create(); 
		return gson.toJson(this);
	}
	public static ResourceTypeSchema load(String file_name) throws FileNotFoundException {
		ResourceTypeSchema schema = null;
		try {
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			JsonReader reader = new JsonReader(new FileReader(file_name));
			schema = gson.fromJson(reader,ResourceTypeSchema.class);
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return schema;
	}
	public void save(String file_name) {
		try {
			GsonBuilder builder = new GsonBuilder().disableHtmlEscaping();
			builder.setPrettyPrinting();
			Gson gson = builder.create();

			FileWriter writer = new FileWriter(file_name);
			writer.write(gson.toJson(this));
			writer.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
