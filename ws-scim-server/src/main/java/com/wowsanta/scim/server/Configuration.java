package com.wowsanta.scim.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

public class Configuration {
	
	private String serviceProviderPath;
	private String serviceConsumerPath;
	
	public String getServiceProviderPath() {
		return serviceProviderPath;
	}
	public void setServiceProviderPath(String serviceProviderPath) {
		this.serviceProviderPath = serviceProviderPath;
	}
	public String getServiceConsumerPath() {
		return serviceConsumerPath;
	}
	public void setServiceConsumerPath(String serviceConsumerPath) {
		this.serviceConsumerPath = serviceConsumerPath;
	}
	
	public static Configuration load(String file_name) throws FileNotFoundException {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonReader reader = new JsonReader(new FileReader(file_name));
		return gson.fromJson(reader,Configuration.class);
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
