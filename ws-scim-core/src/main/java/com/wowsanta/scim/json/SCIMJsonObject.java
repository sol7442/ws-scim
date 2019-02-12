package com.wowsanta.scim.json;

import java.io.Serializable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public abstract class SCIMJsonObject implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3608570793684619034L;

	public JsonObject parse(String json_str){
		JsonParser parser = new JsonParser();
		return parser.parse(json_str).getAsJsonObject();
	}
	public JsonObject encode(){
		return new JsonObject();
	}
	
	public String toString() {
		return encode().toString();
	}
	public String toString(boolean pretty) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(encode());
	}
}
