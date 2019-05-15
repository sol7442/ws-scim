package com.wowsanta.scim.repository;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

public class RepositoryInputMapper {
	static transient Logger logger = LoggerFactory.getLogger(RepositoryInputMapper.class);
	
	private String name;
	private List<ResourceTable> tables = new ArrayList<ResourceTable>();
	public List<ResourceTable> getTables() {
		Collections.sort(this.tables, new TableComparator());
		return tables;
	}

	public void setTables(List<ResourceTable> tables) {
		this.tables = tables;
	}

	public void addTable(ResourceTable table) {
		this.tables.add(table);
		logger.debug("{}-{}",table.getName());
	}
	public ResourceTable getTable(String name) {
		for (ResourceTable table : tables) {
			if(table.getName().equals(name)) {
				return table;
			}
		}
		return null;
	}
	public static RepositoryInputMapper load(String json_config_file) throws RepositoryException {
		logger.info("RepositoryInputMapper LOAD : {} ", json_config_file);
		RepositoryInputMapper mapper = null;
		Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();

		try {
			JsonReader reader = new JsonReader(new FileReader(json_config_file));
			mapper =  gson.fromJson(reader, RepositoryInputMapper.class);
			return mapper;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			throw new RepositoryException("RepositoryInputMapper LOAD FAILED ", e);
		}finally {
			logger.debug("RepositoryInputMapper {}", mapper);
		}
	}
	public void setName(String name) {
		this.name = name;
	}
	
	class TableComparator implements Comparator<ResourceTable> {
		@Override
		public int compare(ResourceTable o1, ResourceTable o2) {
			return Integer.compare(o1.getIndex(),o2.getIndex());
		} 
		
	}
	
	public String toString() {
		return toString(false);
	}
	public String toString(boolean pretty) {
		try {
			GsonBuilder builder = new GsonBuilder().disableHtmlEscaping();
			if (pretty) {
				builder.setPrettyPrinting();
			}
			Gson gson = builder.create();
			return gson.toJson(this);
		} catch (Exception e) {
			logger.error(e.getMessage() + " : ",  e);
		}
		return null;
	}
	public void save(String fileName) {
		try {
			GsonBuilder builder = new GsonBuilder().disableHtmlEscaping();
			builder.setPrettyPrinting();
			Gson gson = builder.create();

			FileWriter writer = new FileWriter(fileName);
			writer.write(gson.toJson(this));
			writer.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}


}
