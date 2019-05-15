package com.wowsanta.scim.message;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.wowsanta.scim.object.SCIM_Object;
import com.wowsanta.scim.schema.SCIMConstants;
import com.wowsanta.scim.schema.SCIMDefinitions;

public class SCIMFindRequest extends SCIM_Object{
	static transient Logger logger = LoggerFactory.getLogger(SCIMFindRequest.class);

	private List<String> attributes = new ArrayList<String>();
	private String where ;
	private String order;
	private int startIndex;
	private int count;
	
	public SCIMFindRequest() {
		addSchema(SCIMConstants.SEARCH_SCHEMA_URI);
	}

	public void addAttribute(String attribute) {
		this.attributes.add(attribute);
	}
	public List<String> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<String> attributes) {
		this.attributes = attributes;
	}

	public String getWhere() {
		return where;
	}

	public void setWhere(String where) {
		this.where = where;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
	public static SCIMFindRequest parse(String json_string) {
		SCIMFindRequest object = null;
		try {
			GsonBuilder builder = new GsonBuilder().disableHtmlEscaping();
			Gson gson  = builder.create();
			object = gson.fromJson(json_string, SCIMFindRequest.class);	
		}catch (Exception e) {
			logger.error("JSON PARSE ERROR {},\n{}",e.getMessage(),json_string,e);
		}
		return object;
	}
	

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}
}
