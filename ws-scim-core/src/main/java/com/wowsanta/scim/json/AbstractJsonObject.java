package com.wowsanta.scim.json;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.service.SCIMServiceProvider;

public class AbstractJsonObject implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4771336880785104423L;
	private String className;
	
	public String toString(boolean pretty) {
		if(pretty) {
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			return gson.toJson(this);
		}else {
			return toString();
		}
	}
	public String toString() {
		Gson gson = new GsonBuilder().create();
		return gson.toJson(this);
	}
	
	public static AbstractJsonObject load(JsonObject jsonObject) throws SCIMException {
		String class_name = jsonObject.get("className").getAsString();
		
		AbstractJsonObject object = null;
		try {
			Gson gson = new GsonBuilder().create();
			object = (AbstractJsonObject) gson.fromJson(jsonObject, Class.forName(class_name));
		} catch (Exception e) {
			throw new SCIMException("Json File load Exception ["+jsonObject+"]",e);
		} 
		
		return object;
	}
	
	public static AbstractJsonObject load(String json_str) throws SCIMException {
		return load(new JsonParser().parse(json_str).getAsJsonObject());
	}
	
	public static AbstractJsonObject load(File json_file) throws SCIMException {
		try {
			JsonReader reader = new JsonReader(new FileReader(json_file));
			return load(new JsonParser().parse(reader).getAsJsonObject());
		} catch (FileNotFoundException e) {
			throw new SCIMException("Json File load Exception ["+json_file.getName()+"]",e);
		}
	}
	public static <T> T  load(JsonObject jsonObject, Class<T> classOfT) {
		Gson gson = new GsonBuilder().create();
		return gson.fromJson(jsonObject, classOfT); 
	}

	public void save(String file_name) throws SCIMException {
		try {
			OutputStreamWriter writer = new OutputStreamWriter(
					new FileOutputStream(
							new File(file_name)),StandardCharsets.UTF_8);
			
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			gson.toJson(this,writer);
			writer.flush();
			writer.close();
			
		}catch (Exception e) {
			throw new SCIMException("Json File save Exception ["+file_name+"]",e);
		}
	}
	
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
}
