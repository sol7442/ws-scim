package com.wowsanta.scim.repository;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

public class SCIMSchemaMapper {
	static Logger logger = LoggerFactory.getLogger(SCIMSchemaMapper.class);
	
	private List<String> schemas = new ArrayList<String>();
	private ResourceTable resourceTable;
	private ResourceColumn IdColumn;
	private Map<String,SchemaMapper> schemaMapper = new HashMap<String,SchemaMapper>();
	public List<String> getSchemas() {
		return schemas;
	}
	public void setSchemas(List<String> schemas) {
		this.schemas = schemas;
	}
	public void addSchema(String schema) {
		this.schemas.add(schema);
	}
	public Map<String,SchemaMapper> getSchemaMapper() {
		return schemaMapper;
	}
	public void addSchemaMapper(SchemaMapper mapper) {
		this.schemaMapper.put(mapper.getName(), mapper);
	}
	public void setSchemaMapper(Map<String,SchemaMapper> schemaMapper) {
		this.schemaMapper = schemaMapper;
	}
	public static SCIMSchemaMapper load(String file_name) throws RepositoryException {
		try {
			logger.info("load schmea mapper : {} ", file_name);
			
			Gson gson = new GsonBuilder().disableHtmlEscaping().create();
			JsonReader reader = new JsonReader(new FileReader(file_name));
			return gson.fromJson(reader, SCIMSchemaMapper.class);
		} catch (Exception e) {
			throw new RepositoryException(file_name, e);
		} 
	}
	public void setResourceTable(ResourceTable table) {
		this.resourceTable = table;
	}
	public ResourceTable getResourceTable() {
		return this.resourceTable;
	}
	public void setIdColumn(ResourceColumn column) {
		this.IdColumn = column;
	}
	public ResourceColumn getIdColumn() {
		return this.IdColumn;
	}
	
	
	
//	
////	List<SchemaMapper> userSchemaMapper = new ArrayList<SchemaMapper>();
////	List<SchemaMapper> groupSchemaMapper = new ArrayList<SchemaMapper>();
//	public List<SchemaMapper> getUserSchemaMapper() {
//		return userSchemaMapper;
//	}
//	
//	public void addUserSchemaMapper(SchemaMapper mapper) {
//		this.userSchemaMapper.add(mapper);
//	}
//	
//	public void setUserSchemaMapper(List<SchemaMapper> userSchemaMapper) {
//		this.userSchemaMapper = userSchemaMapper;
//	}
//	
//	
//	public List<SchemaMapper> getGroupSchemaMapper() {
//		return groupSchemaMapper;
//	}
//	public void setGroupSchemaMapper(List<SchemaMapper> groupSchemaMapper) {
//		this.groupSchemaMapper = groupSchemaMapper;
//	}
//	
//	
//	
//	private SCIMResourceTypeSchema userResourceSchema;
//	private SCIMResourceTypeSchema groupResourceSchema;
//	
//	
//	public static SCIMSchemaMapper load(String file_name) throws RepositoryException {
//		try {
//			Gson gson = new GsonBuilder().disableHtmlEscaping().create();
//			JsonReader reader = new JsonReader(new FileReader(file_name));
//			return gson.fromJson(reader, SCIMSchemaMapper.class);
//		} catch (Exception e) {
//			throw new RepositoryException(file_name, e);
//		} 
//	}
//
//
//	public SCIMResourceTypeSchema getUserResourceSchema() {
//		return userResourceSchema;
//	}
//
//
//	public void setUserResourceSchema(SCIMResourceTypeSchema userResourceSchema) {
//		this.userResourceSchema = userResourceSchema;
//	}
//
//
//	public SCIMResourceTypeSchema getGroupResourceSchema() {
//		return groupResourceSchema;
//	}
//
//
//	public void setGroupResourceSchema(SCIMResourceTypeSchema groupResourceSchema) {
//		this.groupResourceSchema = groupResourceSchema;
//	}
}
