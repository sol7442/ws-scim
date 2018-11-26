package com.wowsanta.scim.repo.rdb;

import java.util.ArrayList;
import java.util.List;

public class RDBQuery {
	
	private String table;
	private String queryString;
	private List<RDBColumn> columns = new ArrayList<RDBColumn>();
	public String getTable() {
		return table;
	}
	public void setTable(String table) {
		this.table = table;
	}
	public List<RDBColumn> getColums() {
		return columns;
	}
	public void setColums(List<RDBColumn> columns) {
		this.columns = columns;
	}
	public void addColum(RDBColumn column) {
		this.columns.add(column);
	}
	
	public String getQueryString() {
		return queryString;
	}
}
