package com.wowsanta.scim.server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import spark.ResponseTransformer;

public class JsonUtil {
	public static String toJson(Object object) {
		return new Gson().toJson(object);
	}
	
	public static ResponseTransformer json() {
		return JsonUtil::toJson;
	}

	public static JsonObject json_parse(String json) {
		return new JsonParser().parse(json).getAsJsonObject();
	}
	
//	
//	Gson gson = new GsonBuilder().create();
//	LoginRequest loing_request = gson.fromJson(request.body(), LoginRequest.class);
}
