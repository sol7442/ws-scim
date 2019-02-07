package com.wowsanta.scim.json;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.wowsanta.scim.exception.SCIMException;

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
	public static <T> T  load(JsonObject jsonObject, Class<T> classOfT) {
		Gson gson = new GsonBuilder().create();
		return gson.fromJson(jsonObject, classOfT); 
	}
	public static <T> T load(String file_name, Class<T> classOfT) throws SCIMException {
		try {
		
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			JsonReader reader = new JsonReader(new FileReader(file_name));
			return gson.fromJson(reader,classOfT); 
			
		}catch(Exception e) {
			throw new SCIMException("Json File load Exception ["+file_name+"]",e);
		}
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