package com.wowsanta.scim.server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wowsanta.scim.json.SCIMJsonObject;

//import spark.ResponseTransformer;

public class JsonUtil {
	public static String toJson(Object object) {
		if (object instanceof SCIMJsonObject) {
			SCIMJsonObject jons_object = (SCIMJsonObject) object;
			return jons_object.toString();
		}else {
			return new Gson().toJson(object);
		}
	}
	
//	public static ResponseTransformer json() {
//		return JsonUtil::toJson;
//	}

	public static JsonObject json_parse(String json) {
		return new JsonParser().parse(json).getAsJsonObject();
	}
	
}
