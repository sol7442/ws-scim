package com.wowsanta.scim.server;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wowsanta.scim.exception.SCIMError;
import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.json.SCIMJsonObject;

//import spark.ResponseTransformer;

public class JsonUtil {
	static Logger logger = LoggerFactory.getLogger(JsonUtil.class);
	
	public static String toJson(Object object) {
		if (object instanceof SCIMJsonObject) {
			SCIMJsonObject jons_object = (SCIMJsonObject) object;
			return jons_object.toString();
		}else if (object instanceof SCIMError){
			SCIMError jons_object = (SCIMError) object;
			return jons_object.toJson();
			
		}else {
			Gson gson = new GsonBuilder().disableHtmlEscaping().create();
			return gson.toJson(object);
		}
	}
	
	public static <T> T  json_parse(String json, Class<T> classOfT) throws SCIMException {
		
		try {
			logger.debug("parse json : {} ", json);
			
			Gson gson = new GsonBuilder().disableHtmlEscaping().create();
			return gson.fromJson(json, classOfT); 
			
		}catch (Exception e) {
			throw new SCIMException("Json Data Parse Error", SCIMError.BadRequest, e);
		}
	}

	public static JsonObject json_parse(String json) {
		return new JsonParser().parse(json).getAsJsonObject();
	}
	
}
