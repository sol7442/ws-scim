package com.wowsanta.scim.message;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.json.SCIMJsonObject;
import com.wowsanta.scim.obj.JsonUtil;
import com.wowsanta.scim.obj.SCIMResource;
import com.wowsanta.scim.obj.SCIMUser;
import com.wowsanta.scim.resource.SCIMResouceFactory;
import com.wowsanta.scim.schema.SCIMConstants;

public class SCIMBulkOperastion extends SCIMJsonObject{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2290817678960481644L;
	
	private String method;
	private String path;
	private String bulkId;
	private SCIMResource data;
	
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
	public String getBulkId() {
		return bulkId;
	}
	public void setBulkId(String bulkId) {
		this.bulkId = bulkId;
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
	
	@Override
	public JsonObject parse(String json_str) {
		JsonObject json_obj = super.parse(json_str);
		
		this.method = JsonUtil.toString(json_obj.get("method"));
		this.path 	= JsonUtil.toString(json_obj.get("path"));
		this.bulkId = JsonUtil.toString(json_obj.get("bulkId"));
		
		try {
			this.data = SCIMResouceFactory.getInstance().parse(this.path,json_obj.get("data").getAsJsonObject());
		} catch (SCIMException e) {
			e.printStackTrace();
		}
		
		return json_obj;
	}

	@Override
	public JsonObject encode() {
		JsonObject json_obj = super.encode();
		
		json_obj.addProperty("method",	this.method);
		json_obj.addProperty("path",	this.path);
		json_obj.addProperty("bulkId",	this.bulkId);
		
		if(this.data != null){
			json_obj.add("data",	this.data.encode());
		}
		
		return json_obj ;
	}
	
}
