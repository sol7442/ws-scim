package com.wowsanta.scim.message;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.obj.SCIMResource2;
import com.wowsanta.scim.object.SCIM_Resource;
import com.wowsanta.scim.object.SCIM_User;
import com.wowsanta.scim.resource.SCIMResouceFactory;
import com.wowsanta.scim.schema.SCIMConstants;

public class SCIMListResponse {

	private int totalResults;
	private int itemsPerPage;
	private int startIndex;
	
	//private List<SCIMResource2> Resources = new ArrayList<SCIMResource2>();
	
	private List<SCIM_Resource> Resources = new ArrayList<SCIM_Resource>();
	public SCIMListResponse() {
		//addSchema(SCIMConstants.LISTED_RESOURCE_CORE_SCHEMA_URI);
	}

	public int getTotalResults() {
		return totalResults;
	}

	public void setTotalResults(int totalResults) {
		this.totalResults = totalResults;
	}

	public int getItemsPerPage() {
		return itemsPerPage;
	}

	public void setItemsPerPage(int itemsPerPage) {
		this.itemsPerPage = itemsPerPage;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	public void addReource(SCIM_Resource resource) {
		this.Resources.add(resource);
	}
	
	public List<SCIM_Resource> getResources() {
		return Resources;
	}

	public void setResources(List<SCIM_Resource> resources) {
		Resources = resources;
	}
//	
//	@Override
//	public JsonObject parse(String json_str) {
//		
//		JsonObject json_obj = super.parse(json_str);
//		JsonArray json_resources = json_obj.get("Resources").getAsJsonArray();
//		for (JsonElement json_resource : json_resources) {
//			
//			SCIMResource2 resource = null;
//			try {
//				resource = SCIMResouceFactory.getInstance().parse(json_resource);
//				this.Resources.add(resource);
//				
//			} catch (SCIMException e) {
//				e.printStackTrace();
//			}
//		}
//		
//		this.totalResults = json_obj.get("totalResults").getAsInt();
//		return json_obj;
//	}
//	
//	@Override
//	public JsonObject encode() {
//		JsonObject super_json = super.encode();
//		JsonArray json_resources = new JsonArray();
//		for (SCIMResource2 resource : this.Resources) {
//			json_resources.add(resource.encode());
//		}
//		super_json.add("Resources", json_resources);
//		super_json.addProperty("totalResults",this.totalResults);
//		return super_json;
//	}
	
}
