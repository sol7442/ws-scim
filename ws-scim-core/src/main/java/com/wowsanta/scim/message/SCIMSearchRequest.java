package com.wowsanta.scim.message;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.wowsanta.scim.schema.SCIMConstants;
import com.wowsanta.scim.schema.SCIMDefinitions;

public class SCIMSearchRequest extends SCIMMessage{
	
	private List<String> attributes = new ArrayList<String>();
	private String filter ;
	private int startIndex;
	private int count;
	
	public SCIMSearchRequest() {
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

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
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
	
	@Override
	public String toString() {
		Gson gson = new GsonBuilder().create();
		return gson.toJson(this);
	}
	
//	@Override
//	public JsonObject parse(String json_str) {
//		JsonObject json_obj = super.parse(json_str);
//	}
//	
//	@Override
//	public JsonObject encode() {
//		JsonObject json_obj = super.encode();
//		
//		return json_obj;
//	}
}
