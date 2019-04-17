package com.wowsanta.scim.repository;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wowsanta.scim.schema.SCIMAttributeSchema;

public class SchemaMapper extends SCIMAttributeSchema {
	private static final long serialVersionUID = -3577018152089114633L;
	
	private String id;
	private ResourceColumn colunm;
	public SchemaMapper(SCIMAttributeSchema attribute, ResourceColumn colunm) {
		super(  attribute.getUri(),
				attribute.getName(),
				attribute.getType(),
				attribute.getMultiValued(),
				attribute.getDescription(),
				attribute.getRequired(),
				attribute.getCaseExact(),
				attribute.getMutability(),
				attribute.getReturned(),
				attribute.getUniqueness(),
				attribute.getCanonicalValues(),
				attribute.getReferenceTypes(),
				attribute.getSubAttributes());
		this.setColunm(colunm);
		this.id = attribute.getName();
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public ResourceColumn getColunm() {
		return colunm;
	}
	public void setColunm(ResourceColumn colunm) {
		this.colunm = colunm;
	}

	

}
