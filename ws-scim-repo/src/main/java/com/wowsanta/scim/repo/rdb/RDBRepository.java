package com.wowsanta.scim.repo.rdb;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.wowsanta.scim.obj.User;
import com.wowsanta.scim.resource.Group;
import com.wowsanta.scim.resource.RepositoryManager;
import com.wowsanta.scim.resource.ResourceMapper;
import com.wowsanta.scim.schema.SCIMResourceTypeSchema;

public class RDBRepository implements RepositoryManager {
	
	private RDBQueryMapper queryMapper ;
	private ResourceMapper resourceMapper;
	private DBCP dbcp;
	
	@Override
	public void initialize() {
		
	}
	public void initDBCP(DBCP dbcp, RDBQueryMapper queryMapper) {
		this.dbcp = dbcp;
		this.queryMapper = queryMapper;
	}
	
	@Override
	public void setMapper(ResourceMapper resoureMapper) {
		this.resourceMapper = resoureMapper;
	}
	
	@Override
	public void setUserSchema(SCIMResourceTypeSchema userSchema) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public User createUser(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User getUser(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User updateUser(User updatedUser) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteUser(String userId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGroupSchema(SCIMResourceTypeSchema groupSchema) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Group createGroup(Group group) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Group getGroup(String groupId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Group updateGroup(Group group) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteGroup(String groupId) {
		// TODO Auto-generated method stub
		
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
	
	public String toString() {
		return toJson();
	}
	
	public static RDBRepository load(String file_name) throws FileNotFoundException {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonReader reader = new JsonReader(new FileReader(file_name));
		return gson.fromJson(reader,RDBRepository.class);
	}
}
