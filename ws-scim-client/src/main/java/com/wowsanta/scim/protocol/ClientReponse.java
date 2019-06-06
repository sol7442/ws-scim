package com.wowsanta.scim.protocol;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wowsata.util.json.WowsantaJson;

public class ClientReponse {
	private ResponseState state;
	private String message;
	private Object data;
	public ResponseState getState() {
		return state;
	}
	public void setState(ResponseState state) {
		this.state = state;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public static ClientReponse parse(String result_string) {
		ClientReponse response = null;
		try {
			Gson gson = new GsonBuilder().disableHtmlEscaping().create();
			response = gson.fromJson(result_string, ClientReponse.class);
			
		}catch (Exception e) {
			response = new ClientReponse();
			response.setState(ResponseState.Fail);
			response.setMessage("RESULT PARSE ERROR : " + e.getMessage());
		}
		
		return response;
		
	}
	
	public String toString() {
		return toString(false);
	}
	public String toString(boolean pretty) {
		try {
			GsonBuilder builder = new GsonBuilder().disableHtmlEscaping();
			if (pretty) {
				builder.setPrettyPrinting();
			}
			Gson gson = builder.create();
			return gson.toJson(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
