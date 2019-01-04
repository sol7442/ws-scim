package com.wowsanta.scim.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.log.SCIMLogger;
import com.wowsanta.scim.resource.ServiceProviderMeta;
import com.wowsanta.scim.resource.SCIMMeta;
import com.wowsanta.scim.server.spark.SparkServer;

public class ServiceProvider implements SCIMServiceProvider {

	private String[] schemas;
	private String documentationUri;
	
	private ServiceProviderMeta meta;
	private SparkServer server;;
	
	@Override
	public SCIMServiceServer getServer() {
		return this.server;
	}
	
	public void setServer(SCIMServiceServer server) {
		this.server = (SparkServer) server;
	}
	
	@Override
	public SCIMMeta getMeta() {
		return this.meta;
	}
	
	@Override
	public void setMeta(SCIMMeta meta) {
		this.meta = (ServiceProviderMeta)meta;
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
	public static ServiceProvider load(String file_name) throws SCIMException  {
		SCIMLogger.sys("LOAD SERVICE PROVIDER : {} ", file_name);
		ServiceProvider service_provider = null;
		try {
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			JsonReader reader = new JsonReader(new FileReader(file_name));
			service_provider = gson.fromJson(reader,ServiceProvider.class);
		} catch (FileNotFoundException e) {
			throw new SCIMException("ServiceProvider Loade : ",e);
		}
		SCIMLogger.sys("SERVICE PROVIDER INFO : {} ", service_provider);
		return service_provider;
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
