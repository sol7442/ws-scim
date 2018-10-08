package com.wowsanta.scim.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.wowsanta.scim.SystemManager;
import com.wowsanta.scim.resource.Meta;
import com.wowsanta.scim.spark.SparkService;

public class ServiceProvider {

	private String[] schemas;
	private String documentationUri;
	
	private Meta meta;
	private SparkService wessionIM;;
	
	public SparkService getWessionIM() {
		return this.wessionIM;
	}
	public void setWessionIM(SparkService service) {
		this.wessionIM = service;
	}
	public Meta getMeta() {
		return this.meta;
	}
	public void setMeta(Meta meta) {
		this.meta = meta;
	}
	
	public String getDocumentationUri() {
		return documentationUri;
	}

	public void setDocumentationUri(String documentationUri) {
		this.documentationUri = documentationUri;
	}

	public String[] getSchemas() {
		return schemas;
	}

	public void setSchemas(String[] schemas) {
		this.schemas = schemas;
	}
	
	public String getSchema(int index) {
		if(this.schemas != null && this.schemas.length > 0) {
			return this.schemas[index];
		}else {
			return "";
		}
	}
	public static ServiceProvider load(String file_name) throws FileNotFoundException {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonReader reader = new JsonReader(new FileReader(file_name));
		
		ServiceProvider sp = gson.fromJson(reader,ServiceProvider.class);
		
		SystemManager.getInstance().setServiceProvider(sp);
		
		return sp;
	}
	
	public void save(String file_name) throws IOException {
		FileWriter writer = new FileWriter(new File(file_name));
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		gson.toJson(this,writer);
		writer.flush();
		writer.close();
	}

	public String toString() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(this);
	}

}
