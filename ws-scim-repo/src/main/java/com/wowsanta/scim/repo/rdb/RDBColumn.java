package com.wowsanta.scim.repo.rdb;

import com.wowsanta.scim.schema.SCIMDefinitions;

public class RDBColumn {
	private RDBQueryDefinitions.ColumType columnType;
	private SCIMDefinitions.DataType dataType;
	private String name;
	
	
	public RDBColumn(RDBQueryDefinitions.ColumType ctype, String name ) {
		this.columnType = ctype;
		this.name = name;
	}
	
	public RDBColumn(RDBQueryDefinitions.ColumType ctype, SCIMDefinitions.DataType dtype, String name ) {
		this.columnType = ctype;
		this.dataType   = dtype;
		this.name = name;
	}
	
	public RDBQueryDefinitions.ColumType getColumnType() {
		return this.columnType;
	}
	public void setColumnType(RDBQueryDefinitions.ColumType type) {
		this.columnType = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public SCIMDefinitions.DataType getDataType() {
		return this.dataType;
	}
	public void setDataType(SCIMDefinitions.DataType type) {
		this.dataType = type;
	}
}
