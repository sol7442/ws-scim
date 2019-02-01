package com.wowsanta.scim.resource;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wowsanta.scim.obj.DefaultMeta;
import com.wowsanta.scim.schema.SCIMDefinitions;

public class SCIMResourceType {
	private List<String> schemas = new ArrayList<String>();
	private String id;
	private String name;
	private String endpoint;
	private String description;
	private String schema;
	private List<SCIMSchemaExtension> schemaExtensions = new ArrayList<SCIMSchemaExtension>();
	
	private SCIMMeta meta = new DefaultMeta(SCIMDefinitions.ResoureType.ResourceType.toString());

	public List<String> getSchemas() {
		return schemas;
	}

	public void setSchemas(String... schemas) {
		for(int i = 0 ; i<schemas.length; i++) {
			this.schemas.add(schemas[i]);
		}
	}
	
	public void setSchemas(List<String> schemas) {
		this.schemas = schemas;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public void addSchemaExtension(SCIMSchemaExtension extention) {
		this.schemaExtensions.add(extention);
	}
	public List<SCIMSchemaExtension> getSchemaExtensions() {
		return schemaExtensions;
	}

	public void setSchemaExtensions(List<SCIMSchemaExtension> schemaExtensions) {
		this.schemaExtensions = schemaExtensions;
	}

	public SCIMMeta getMeta() {
		return meta;
	}

	public void setMeta(SCIMMeta meta) {
		this.meta = meta;
	}
	
	public String toString() {
		Gson gson = new GsonBuilder().create();
		return gson.toJson(this);
	}
}
