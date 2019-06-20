package com.wowsanta.scim.repository;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wowsanta.scim.filter.BranchNode;
import com.wowsanta.scim.filter.FilterNode;
import com.wowsanta.scim.filter.FilterParser;
import com.wowsanta.scim.filter.Operator;
import com.wowsanta.scim.filter.TerminalNode;
import com.wowsanta.scim.object.Resource_Object;
import com.wowsanta.scim.object.SCIM_Object;

public class ResourceTable extends SCIM_Object {
	
	transient Logger logger = LoggerFactory.getLogger(ResourceTable.class);
	
	private String name;
	private int index;
	private ResourceType type = ResourceType.StructuredTable;
	private Map<String,Object> attributes = new HashMap<String, Object>();
	private List<ResourceColumn> columns = new ArrayList<ResourceColumn>();
	private String keyColumn;
	
	public Map<String, Object> getAttributes() {
		return attributes;
	}
	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}
	
	public Object getAttribute(String key) {
		return this.attributes.get(key);
	}
	public void addAttribute(String key,Object value) {
		this.attributes.put(key,value);
	}
	public String getName() {
		return name;
	}
	public void setName(String tableName) {
		this.name = tableName;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public List<ResourceColumn> getColumns() {
		return columns;
	}
	public void setColumns(List<ResourceColumn> columns) {
		this.columns = columns;
	}
	public void addColumn(ResourceColumn column) {
		this.columns.add(column);
	}
	
	public ResourceColumn getColumn(String name) {
		for (ResourceColumn column : columns) {
			if(column.getName().toUpperCase().equals(name.toUpperCase())) {
				return column;
			}
		}
		return null;
	}
	public ResourceColumn getColumnByMapperId(String id) {
		for (ResourceColumn column : columns) {
			DataMapper mapper = column.getDataMapper();
			if(mapper != null) {
				if(mapper.getId().toUpperCase().equals(id.toUpperCase())) {
					return column;
				}
			}
		}
		return null;
	}
	
	public ResourceColumn getPrimaryColumn() {
		for (ResourceColumn column : columns) {
			if(column.isPrimary()) {
				return column;
			}
		}
		return null;
	}
	
	public List<ResourceColumn> getPrimaryColumns() {
		List<ResourceColumn> column_list = new ArrayList<ResourceColumn>();
		for (ResourceColumn column : columns) {
			if(column.isPrimary()) {
				column_list.add(column);
			}
		}
		return column_list;
	}
	
	public ResourceType getType() {
		return type;
	}
	public void setType(ResourceType type) {
		this.type = type;
	}
	
	
	public String getInsertQuery(Resource_Object resource) {
		
		StringBuffer query_buffer = new StringBuffer();
		query_buffer.append("INSERT INTO").append(" ");
		query_buffer.append(this.name).append(" ");
		query_buffer.append("(");

		int append_count = 0;
		for(int i=0; i<this.columns.size(); i++) {
			ResourceColumn column = this.columns.get(i);
			if(column.convertMappingData(resource) != null) {
				if(append_count > 0) {
					query_buffer.append(",");
				}
				query_buffer.append(column.getName());
				append_count++;
			}
		}
		
		query_buffer.append(")").append(" ");
		
		logger.debug("create insert query - add columns : {}", query_buffer.toString());
		
		query_buffer.append("VALUES").append("(");
		
		append_count = 0;
		for(int i=0; i<this.columns.size(); i++) {
			ResourceColumn column = this.columns.get(i);
			if(column.convertMappingData(resource) != null) {
				if(append_count > 0) {
					query_buffer.append(",");
				}
				query_buffer.append("?");
				append_count++;
			}
		}
		query_buffer.append(")");
		return query_buffer.toString();
	}
	public String getUpdateQuery(Resource_Object resource) {
		StringBuffer query_buffer = new StringBuffer();
		query_buffer.append("UPDATE").append(" ");
		query_buffer.append(this.name).append(" ");
		query_buffer.append("SET").append(" ");
		
		int append_count = 0;
		for(int i=0; i<this.columns.size(); i++) {
			ResourceColumn column = this.columns.get(i);
			if(column.convertMappingData(resource) != null) {
				if(append_count > 0) {
					query_buffer.append(",");
				}
				query_buffer.append(column.getName()).append("=").append("?");
				append_count++;
			}
		}
		query_buffer.append(" ");
		
		List<ResourceColumn> column_list = getPrimaryColumns();
		if(column_list.size() > 0) {
			query_buffer.append("WHERE").append(" ");
			
			for(int i=0; i< column_list.size(); i++) {
				ResourceColumn column = column_list.get(i);
				query_buffer.append(column.getName()).append("=").append("?");

				if(i < column_list.size() - 1) {// OPERATOR .. FILTER
					query_buffer.append(" ").append("AND").append(" ");
				}
			}
		}
		
		//query_buffer.append(this.getPrimaryColumn().getId()).append("=").append("?");
		return query_buffer.toString();
	}
	public String getSelectQuery() {
		StringBuffer query_buffer = new StringBuffer();
		query_buffer.append("SELECT * FROM").append(" ");
		query_buffer.append(this.name).append(" ");
		query_buffer.append("WHERE").append(" ");
		query_buffer.append(this.getPrimaryColumn().getId()).append("=").append("?");
		return query_buffer.toString();
	}
	
	public String getSearchQuery(String filter) {
		StringBuffer query_buffer = new StringBuffer();
		
		query_buffer.append("SELECT * FROM").append(" ");
		query_buffer.append(this.name).append(" ");

		if(filter != null && filter.length() > 0) {
			query_buffer.append("WHERE").append(" ");

			FilterNode node = FilterParser.parse( filter );
			
			logger.debug("search filter : {} ", filter);
			String where = filterNodeToWhere(node);
			logger.debug("search where : {} ",where );
			
			query_buffer.append(where);
		}
		
		return query_buffer.toString();
	}
	
	public String getCountQuery(String filter) {
		StringBuffer query_buffer = new StringBuffer();
		
		query_buffer.append("SELECT COUNT(*) AS COUNT FROM").append(" ");
		query_buffer.append(this.name).append(" ");
		
		if(filter != null && filter.length() > 0) {
			query_buffer.append("WHERE").append(" ");
			FilterNode node = FilterParser.parse( filter );
			query_buffer.append(filterNodeToWhere(node));
		}
		
		return query_buffer.toString();
	}
	
	private String filterNodeToWhere(FilterNode node) {
		StringBuffer buffer = new StringBuffer();
        if(node instanceof BranchNode) {
        	BranchNode bn =  ( BranchNode ) node;
 		   	
 		   	buffer.append( filterNodeToWhere(bn.getLeftNode()).toString() );
 		   	buffer.append(" ").append( bn.getOperator() ).append(" ");
 		   	buffer.append( filterNodeToWhere(bn.getRightNode()).toString() );
        	
        }else {
        	TerminalNode tn = ( TerminalNode ) node;
        	ResourceColumn column = this.getColumnByMapperId(tn.getAttribute());
        	
        	buffer.append("(");
        	buffer.append(column.getId());
        	
        	buffer.append(" ");
        	buffer.append(tn.getOperator().getRdbOperator());
        	buffer.append(" ");
        	
        	//buffer.append("="); // op
        	if(tn.getValue().length() > 1) {
	        	buffer.append("'");
	        	buffer.append(tn.getValue());
	        	buffer.append("'");
        	}
        	buffer.append(")");
        }
		return buffer.toString();
	}
	public String getKeyColumn() {
		return keyColumn;
	}
	public void setKeyColumn(String keyColumn) {
		this.keyColumn = keyColumn;
	}

}
