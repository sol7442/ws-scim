package com.wowsanta.scim.spark;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class AbstractResponse {

	public String toString() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(this);
	}

}
