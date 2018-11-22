package com.wowsanta.scim.repo.rdb;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.wowsanta.scim.repository.QueryManager;

public class RDBQueryManager implements QueryManager {
	
	private RDBQuery select = new RDBQuery();
	private List<RDBQuery> insert = new ArrayList<RDBQuery>();
	private List<RDBQuery> delete = new ArrayList<RDBQuery>();
	private List<RDBQuery> update = new ArrayList<RDBQuery>();
	
	public RDBQuery getSelect() {
		return select;
	}
	public void setSelect(RDBQuery select) {
		this.select = select;
	}
	public List<RDBQuery> getInsert() {
		return insert;
	}
	public void setInsert(List<RDBQuery> insert) {
		this.insert = insert;
	}
	public void addInsert(RDBQuery insert) {
		this.insert.add(insert);
	}
	public List<RDBQuery> getDelete() {
		return delete;
	}
	public void setDelete(List<RDBQuery> delete) {
		this.delete = delete;
	}
	public List<RDBQuery> getUpdate() {
		return update;
	}
	public void setUpdate(List<RDBQuery> update) {
		this.update = update;
	}
	
	public static RDBQueryManager load(String file_name) throws FileNotFoundException {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonReader reader = new JsonReader(new FileReader(file_name));
		
		return gson.fromJson(reader,RDBQueryManager.class); 
	}
	
	public void save(String file_name) throws IOException {
		OutputStreamWriter writer = new OutputStreamWriter(
				new FileOutputStream(
						new File(file_name)),StandardCharsets.UTF_8);
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		gson.toJson(this,writer);
		writer.flush();
		writer.close();
	}

	public String toJson() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(this);
	}
}
