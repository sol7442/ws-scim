package com.wowsanta.scim.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import spark.ResponseTransformer;

public class JsonUtil {
	public static String toJson(Object object) {
		return new Gson().toJson(object);
	}
	
	public static ResponseTransformer json() {
		return JsonUtil::toJson;
	}

	public static <T> T json(String json, Class<T> clz) {
		Gson gson = new GsonBuilder().create();
		return gson.fromJson(json, clz);
	}
	
//	
//	Gson gson = new GsonBuilder().create();
//	LoginRequest loing_request = gson.fromJson(request.body(), LoginRequest.class);
}
