package com.wowsanta.scim.message;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wowsanta.scim.object.Resource_Object;
import com.wowsanta.scim.object.SCIM_Object;
import com.wowsanta.scim.schema.SCIMConstants;

public class SCIMListResponse extends SCIM_Object {

	static transient Logger logger = LoggerFactory.getLogger(SCIMBulkRequest.class);

	
	private int totalResults;
	private int itemsPerPage;
	private int startIndex;
	
	private List<Resource_Object> Resources = new ArrayList<Resource_Object>();
	public SCIMListResponse() {
		addSchema(SCIMConstants.LISTED_RESOURCE_CORE_SCHEMA_URI);
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

	public void addReource(Resource_Object resource) {
		this.Resources.add(resource);
	}
	
	public List<Resource_Object> getResources() {
		return Resources;
	}

	public void setResources(List<Resource_Object> resources) {
		Resources = resources;
	}
	
	public static SCIMListResponse parse(String json_string) {
		SCIMListResponse object = null;
		try {
			GsonBuilder builder = new GsonBuilder().disableHtmlEscaping();
			Gson gson  = builder.create();
			object = gson.fromJson(json_string, SCIMListResponse.class);	
		}catch (Exception e) {
			logger.error("JSON PARSE ERROR {},\n{}",e.getMessage(),json_string,e);
		}
		return object;
	}
	
}
