package com.wowsanta.scim.obj;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class AbstractJsonObject {
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
}
