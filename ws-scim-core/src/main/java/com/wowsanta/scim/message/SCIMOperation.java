package com.wowsanta.scim.message;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.wowsanta.scim.exception.SCIMError;
import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.json.SCIMJsonObject;
import com.wowsanta.scim.obj.JsonUtil;
import com.wowsanta.scim.obj.SCIMResource;
import com.wowsanta.scim.obj.SCIMUser;
import com.wowsanta.scim.resource.SCIMResouceFactory;
import com.wowsanta.scim.schema.SCIMConstants;

public class SCIMOperation extends SCIMJsonObject{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6173234519005676647L;
	
	private String method;
	private String path;
	private SCIMResource data;
	private String location;
	private String status;
	private SCIMError response;
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public SCIMResource getData() {
		return data;
	}
	public void setData(SCIMResource data) {
		if (data instanceof SCIMUser) {
			this.path = SCIMConstants.USER_ENDPOINT;
		}else{
			this.path = SCIMConstants.GROUP_ENDPOINT;
		}
		this.data = data;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public void setStatus(int status) {
		this.status = String.valueOf(status);
	}
	
	public SCIMError getResponse() {
		return response;
	}
	public void setResponse(SCIMError response) {
		this.response = response;
	}
	
	@Override
	public JsonObject parse(String json_str) {
		JsonObject json_obj = super.parse(json_str);

		this.method = JsonUtil.toString(json_obj.get("method"));
		this.path 	= JsonUtil.toString(json_obj.get("path"));
		if(json_obj.get("data") != null) {
			try {
				this.data = SCIMResouceFactory.getInstance().parse(this.path,json_obj.get("data").getAsJsonObject());
			} catch (SCIMException e) {
				e.printStackTrace();
			}
		}
		
		this.location = JsonUtil.toString(json_obj.get("location"));
		this.status = JsonUtil.toString(json_obj.get("status"));
		
		if(json_obj.get("response") != null) {
			Gson gson = new GsonBuilder().create();
			this.response = gson.fromJson(json_obj, SCIMError.class); 
		}
		
		return json_obj;
	}

	@Override
	public JsonObject encode() {
		JsonObject json_obj = super.encode();
		
		json_obj.addProperty("method",	this.method);
		json_obj.addProperty("path",	this.path);
		
		if(this.data != null){
			json_obj.add("data",	this.data.encode());
		}
		json_obj.addProperty("location",	this.location);
		json_obj.addProperty("status",	this.status);
		if(this.response != null) {
			Gson gson = new GsonBuilder().create();
			json_obj.add("response",gson.toJsonTree(this.response));
		}
		return json_obj ;
	}
	
	
}
