package com.wowsanta.scim.repository;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.wowsanta.scim.object.SCIM_Object;

public class SCIMResposeMapper extends SCIM_Object {
	List<ResourceMapper> userResourceMapper = new ArrayList<ResourceMapper>();
	List<ResourceMapper> groupResourceMapper = new ArrayList<ResourceMapper>();
	
	ResourceMapper userMetaMapper;   // = ResourceMapper();
	ResourceMapper groupMeataMapper; // = ResourceMapper();
	
	//List<ResourceMapper> groupMeataMapper = new ArrayList<ResourceMapper>();
	
	public void addUserResourceMapper(ResourceMapper mapper) {
		this.userResourceMapper.add(mapper);
	}
	
	public void addGroupResourceMapper(ResourceMapper mapper) {
		this.groupResourceMapper.add(mapper);
	}
	
	public ResourceMapper getUserResourceMapper(String tableId) {
		for (ResourceMapper tableMapper : userResourceMapper) {
			if(tableMapper.getTable().getId().equals(tableId)) {
				return tableMapper;
			}
		}
		return null;
	}
	
	public void setUserMetaMapper(ResourceMapper meta) {
		this.userMetaMapper = meta;
	}
	public ResourceMapper getUserMataMapper() {
		return this.userMetaMapper;
	}
	
	
	
	public ResourceMapper getGroupResourceMapper(String tableId) {
		for (ResourceMapper tableMapper : groupResourceMapper) {
			if(tableMapper.getTable().getId().equals(tableId)) {
				return tableMapper;
			}
		}
		return null;
	}

	public static SCIMResposeMapper load(String file_name) throws RepositoryException {
		try {
			Gson gson = new GsonBuilder().disableHtmlEscaping().create();
			JsonReader reader = new JsonReader(new FileReader(file_name));
			return gson.fromJson(reader, SCIMResposeMapper.class);
		} catch (Exception e) {
			throw new RepositoryException(file_name, e);
		} 
	}
}
