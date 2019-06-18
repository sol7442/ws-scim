package com.wowsanta.scim.protocol;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wowsanta.scim.object.SCIM_Object;
import com.wowsanta.scim.repository.AttributeValue;
import com.wowsanta.scim.schema.SCIMConstants;

public class FrontSearchRequest extends SCIM_Object{
	static transient Logger logger = LoggerFactory.getLogger(FrontSearchRequest.class);

	private List<AttributeValue> attributes = new ArrayList<AttributeValue>();
	private String where ;	
	private int startIndex;
	private int count;
	private String order;
	
	public FrontSearchRequest() {
		addSchema(SCIMConstants.SEARCH_SCHEMA_URI);
	}

	public void addAttribute(AttributeValue attribute) {
		this.attributes.add(attribute);
	}
	public List<AttributeValue> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<AttributeValue> attributes) {
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
	
	public static FrontSearchRequest parse(String json_string) {
		FrontSearchRequest object = null;
		try {
			GsonBuilder builder = new GsonBuilder().disableHtmlEscaping();
			Gson gson  = builder.create();
			object = gson.fromJson(json_string, FrontSearchRequest.class);	
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
