package com.wowsanta.scim.obj;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.JsonObject;
import com.wowsanta.scim.json.SCIMJsonObject;
import com.wowsanta.scim.schema.SCIMConstants;

public class SCIMMeta extends SCIMJsonObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7574412408521630782L;

	private String resourceType;
	private Date   created;
	private Date   lastModified;
	private String location;
	private String version;
	
	public SCIMMeta(){
	}

	@Override
	public JsonObject parse(String json_str) {
		JsonObject json_obj = super.parse(json_str);
		
		this.version 	  = JsonUtil.toString(json_obj.get("version"));
		this.resourceType = JsonUtil.toString(json_obj.get("resourceType"));
		this.created      = JsonUtil.toDate(json_obj.get("created"));
		this.lastModified = JsonUtil.toDate(json_obj.get("lastModified"));
		
		return json_obj;
	}

	@Override
	public JsonObject encode() {
		JsonObject json_obj = super.encode();
		
		json_obj.addProperty("version", this.version);
		json_obj.addProperty("resourceType", this.resourceType);
		json_obj.addProperty("created", JsonUtil.toString(this.created));
		json_obj.addProperty("lastModified", JsonUtil.toString(this.lastModified));
		
		return json_obj ;
	}
	
	
	
	
	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
	
}
