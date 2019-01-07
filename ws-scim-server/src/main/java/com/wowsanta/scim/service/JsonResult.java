package com.wowsanta.scim.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public abstract class JsonResult {
	private int code;
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	
	public String toString() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(this);
	}
	
}
