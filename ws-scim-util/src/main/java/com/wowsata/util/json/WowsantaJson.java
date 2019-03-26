package com.wowsata.util.json;

import java.io.FileReader;
import java.io.FileWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;


public class WowsantaJson {

	static Logger logger = LoggerFactory.getLogger(WowsantaJson.class);
	
	protected String jsonClass;
	protected transient String jsonFileName;
	
	public String tojson(boolean pretty) {
		GsonBuilder builder = new GsonBuilder().disableHtmlEscaping();
		if(pretty) {
			builder.setPrettyPrinting();
		}
		Gson gson = builder.create();
		return gson.toJson(this);
	}
	
	public void save(String file_name) throws JsonException{
		try {
			this.jsonFileName = file_name;
			
			Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
			FileWriter writer = new FileWriter(file_name);
			writer.write(gson.toJson(this));
			writer.close();
		} catch (Exception e) {
			throw new JsonException(file_name, e);
		}
	}
	public static WowsantaJson loadFromString(String json_str) throws JsonException{
		try {
			Gson gson = new GsonBuilder().disableHtmlEscaping().create();
			WowsantaJson json_object = gson.fromJson(json_str, WowsantaJson.class);
			return (WowsantaJson) gson.fromJson(json_str, Class.forName(json_object.jsonClass));
		} catch (Exception e) {
			throw new JsonException(json_str, e);
		} 	
	}
	protected static WowsantaJson loadFromFile(String file_name) throws JsonException{ 
		try {
			Gson gson = new GsonBuilder().disableHtmlEscaping().create();
			JsonReader reader = new JsonReader(new FileReader(file_name));
			WowsantaJson json_object = gson.fromJson(reader, WowsantaJson.class);
			JsonReader reader2 = new JsonReader(new FileReader(file_name));
			Class clazz = Class.forName(json_object.jsonClass);
			return (WowsantaJson) gson.fromJson(reader2, clazz );
		} catch (Exception e) {
			throw new JsonException(file_name, e);
		} 
	}
}
