package com.wowsanta.scim.resource;

import com.wowsanta.scim.json.AbstractJsonObject;

public class SCIMSystemColumn extends AbstractJsonObject {
	private String 	systemId;
	private String 	columnName;
	private String  displayName;
	
	private String 	dataType;
	private int    	dataSize;
	private boolean allowNull;
	
	private String 	defaultValue;
	private String 	comment;
	
	private String 	mappingColumn;
	
	public SCIMSystemColumn(String systemId, String name,String displayName) {
		this.systemId = systemId;
		this.columnName = name;
		this.displayName = displayName;
	}
	
	public SCIMSystemColumn(String systemId, String name,String displayName, String mappingColumn) {
		this.systemId = systemId;
		this.columnName = name;
		this.displayName = displayName;
		this.mappingColumn = mappingColumn;
	}
	
	public SCIMSystemColumn() {
	}

	public String getSystemId() {
		return systemId;
	}
	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}
	public String getColumnName() {
		return this.columnName;
	}
	public void setColumnName(String name) {
		this.columnName = name;
	}
	
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public int getDataSize() {
		return dataSize;
	}

	public void setDataSize(int dataSize) {
		this.dataSize = dataSize;
	}

	public boolean isAllowNull() {
		return allowNull;
	}

	public void setAllowNull(boolean allowNull) {
		this.allowNull = allowNull;
	}

	public String getMappingColumn() {
		return mappingColumn;
	}

	public void setMappingColumn(String mappingColumn) {
		this.mappingColumn = mappingColumn;
	}
	
}
