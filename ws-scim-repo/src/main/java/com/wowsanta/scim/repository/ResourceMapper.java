package com.wowsanta.scim.repository;

import java.util.ArrayList;
import java.util.List;

public class ResourceMapper {
	private ResourceTable table;
	private ResourceColumn IdColumn;
	private List<ResourceColumn> columns = new ArrayList<ResourceColumn>();
	
	public ResourceTable getTable() {
		return table;
	}
	public void setTable(ResourceTable table) {
		this.table = table;
	}
	public List<ResourceColumn> getColumns() {
		return columns;
	}
	public void setColumns(List<ResourceColumn> colums) {
		this.columns = colums;
	}
	public void addColumn(ResourceColumn column) {
		this.columns.add(column);
	}
	public ResourceColumn getColumn(String columnName) {
		for (ResourceColumn column : columns) {
			if(column.getId().equals(columnName)) {
				return column;
			}
		}
		return null;
	}
	public void setIdColumn(ResourceColumn column) {
		this.IdColumn = column;
	}
	public ResourceColumn getIdColumn() {
		return this.IdColumn;
	}

}
