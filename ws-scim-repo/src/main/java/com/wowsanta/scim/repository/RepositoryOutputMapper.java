package com.wowsanta.scim.repository;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.wowsanta.scim.filter.BranchNode;
import com.wowsanta.scim.filter.FilterNode;
import com.wowsanta.scim.filter.FilterParser;
import com.wowsanta.scim.filter.TerminalNode;
import com.wowsanta.scim.schema.SCIMDefinitions.Uniqueness;

import oracle.net.aso.u;

public class RepositoryOutputMapper {
	static transient Logger logger = LoggerFactory.getLogger(RepositoryOutputMapper.class);
	
	private String name;
	private ResourceTable table;
	private Map<String,AttributeSchema> attributes = new HashMap<String,AttributeSchema>(); 
	
	public Map<String,AttributeSchema> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String,AttributeSchema> attributes) {
		this.attributes = attributes;
	}
	
	public void putAttribute(String key, AttributeSchema attribute) {
		this.attributes.put(key,attribute);
	}
	
	public void putAttribute(AttributeSchema attribute) {
		this.attributes.put(attribute.getName(),attribute);
	}
	public AttributeSchema getAttribute(String name) {
		return this.attributes.get(name);
	}
	public AttributeSchema getKeyAttribte() {
		Set<String> key_set = this.attributes.keySet();
		for (String key : key_set) {
			AttributeSchema attribute = this.attributes.get(key);
			Uniqueness uniqueness = attribute.getUniqueness();
			if(uniqueness == Uniqueness.SERVER) {
				return attribute;
			}
		}
		return null;
	}
	
	public void save(String file_name) throws RepositoryException{
		try {
			GsonBuilder builder = new GsonBuilder().disableHtmlEscaping();
			builder.setPrettyPrinting();
			Gson gson = builder.create();

			FileWriter writer = new FileWriter(file_name);
			writer.write(gson.toJson(this));
			writer.close();
		}catch (Exception e) {
			throw new RepositoryException("RepositoryMapper SAVE FAILED ", e);
		}
	}

	public String toString() {
		return toString(false);
	}
	
	public String toString(boolean pretty) {
		GsonBuilder builder = new GsonBuilder().disableHtmlEscaping();
		if(pretty) {
			builder.setPrettyPrinting();
		}
		Gson gson = builder.create(); 
		return gson.toJson(this);
	}
	
	public static RepositoryOutputMapper load(String file_name) throws RepositoryException {
		RepositoryOutputMapper schema = null;
		try {
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			JsonReader reader = new JsonReader(new FileReader(file_name));
			schema = gson.fromJson(reader,RepositoryOutputMapper.class);
			reader.close();
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			throw new RepositoryException("RepositoryOutputMapper LOAD FAILED ", e);
		}
		return schema;
	}

	public void setTable(ResourceTable table) {
		this.table = table;
	}
	public ResourceTable getTable() {
		return this.table;
	}

	public String getSelectQuery(List<AttributeValue> attribute_list) {
		StringBuffer query_buffer = new StringBuffer();
		query_buffer.append("SELECT * FROM").append(" ");
		query_buffer.append(this.table.getName()).append(" ");
		
		if(attribute_list.size() > 0) {
			query_buffer.append("WHERE").append(" ");
			for(int i=0; i< attribute_list.size(); i++) {

				AttributeValue value = attribute_list.get(i);
				AttributeSchema schema = getAttribute(value.getName());
				DataMapper dataMapper = schema.getDataMapper();
				ResourceColumn column  = schema.getResourceColumn();
				
				Object set_data = null;
				if(dataMapper != null) {
					set_data = dataMapper.convert(value.getValue());
				}else {
					set_data = value.getValue();
				}
				
				if( set_data != null) {
					query_buffer.append(column.getName()).append("='").append(set_data).append("'");
				}
				
				if(i < attribute_list.size() - 1) {// OPERATOR .. FILTER
					query_buffer.append(" ").append("AND").append(" ");
				}
			}
		}
		
		return query_buffer.toString();
	}
	
	public String getSelectQuery() {
		StringBuffer query_buffer = new StringBuffer();
		query_buffer.append("SELECT * FROM").append(" ");
		query_buffer.append(this.table.getName()).append(" ");
		query_buffer.append("WHERE").append(" ");
		query_buffer.append(this.getKeyAttribte().getResourceColumn().getName()).append("=").append("?");
		return query_buffer.toString();
	}
	public String getSearchQuery(String filter) {
		StringBuffer query_buffer = new StringBuffer();
		
		query_buffer.append("SELECT * FROM").append(" ");
		query_buffer.append(this.table.getName()).append(" ");

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
		query_buffer.append(this.table.getName()).append(" ");
		
		if(filter != null && filter.length() > 0) {
			query_buffer.append("WHERE").append(" ");
			FilterNode node = FilterParser.parse( filter );

			logger.info("filter {} > {}", filter, node.toString());
			String node_str = filterNodeToWhere(node);
			logger.info("node   {} > {}", node.toString(),node_str );
			query_buffer.append(node_str);
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
        	AttributeSchema attribute = getAttribute(tn.getAttribute());//this.getColumnByMapperId(tn.getAttribute());
        	if(attribute != null) {
        		ResourceColumn column = attribute.getResourceColumn();

            	buffer.append("(");
            	buffer.append(column.getName());
            	
            	buffer.append(" ");
            	buffer.append(tn.getOperator().getRdbOperator());
            	buffer.append(" ");
            	
            	if(tn.getValue() != null ) {
    	        	buffer.append("'");
    	        	buffer.append(tn.getValue());
    	        	buffer.append("'");
            	}
            	buffer.append(")");	
        	}
        }
		return buffer.toString();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}



}
