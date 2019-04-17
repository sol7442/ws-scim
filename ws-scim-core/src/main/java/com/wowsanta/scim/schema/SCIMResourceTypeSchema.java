package com.wowsanta.scim.schema;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;


public class SCIMResourceTypeSchema implements Serializable{
	private static final long serialVersionUID = 1292919740668517279L;
	
	private List<String> schemas = new ArrayList<String>();
	private Map<String,SCIMAttributeSchema> attributes = new HashMap<String,SCIMAttributeSchema>(); 


	
	public List<String> getSchemas() {
		return schemas;
	}

	public void setSchemas(List<String> schemas) {
		this.schemas = schemas;
	}
	
	public void addSchema(String schema) {
		this.schemas.add(schema);
	}
	
	public Map<String,SCIMAttributeSchema> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String,SCIMAttributeSchema> attributes) {
		this.attributes = attributes;
	}
	
	public void putAttribute(SCIMAttributeSchema attribute) {
		this.attributes.put(attribute.getName(),attribute);
	}
	public SCIMAttributeSchema getAttribute(String name) {
		return this.attributes.get(name);
	}
	
	
	public void save(String file_name) throws IOException {
		OutputStreamWriter writer = new OutputStreamWriter(
				new FileOutputStream(
						new File(file_name)),StandardCharsets.UTF_8);
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		gson.toJson(this,writer);
		writer.flush();
		writer.close();
	}

	public String toJson() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(this);
	}
	
	public static SCIMResourceTypeSchema load(String file_name) throws FileNotFoundException {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonReader reader = new JsonReader(new FileReader(file_name));
		return gson.fromJson(reader,SCIMResourceTypeSchema.class);
	}

}
