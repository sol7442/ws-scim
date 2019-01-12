package com.wowsanta.scim.repo.sample;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wowsanta.scim.resource.SCIMMeta;
import com.wowsanta.scim.resource.SCIMUser;
import com.wowsanta.scim.resource.SCIMUserGroup;

public class User implements SCIMUser {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6709617785615904142L;
	
	private List<String> schemas = new ArrayList<String>();
	private String id;
	private String userName;
	private Meta meta;
	private List<UserGroup> groups = new ArrayList<UserGroup>();
	
	@Override
	public void setSchemas(List<String> urls) {
		this.schemas = urls;
	}
	
	@Override
	public List<String> getSchemas(){
		return this.schemas;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public void setMeta(SCIMMeta meta) {
		this.meta = (Meta)meta;
	}

	@Override
	public SCIMMeta getMeta() {
		return this.meta;
	}
	
	@Override
	public void addGroup(SCIMUserGroup group) {
		this.groups.add((UserGroup) group);
	}
	
	@Override
	public List<SCIMUserGroup> getGroups() {
		List<SCIMUserGroup> groups = new ArrayList<SCIMUserGroup>();
		groups.addAll(this.groups);
		return groups;
	}
	
	@Override
	public void setGroups(List<SCIMUserGroup> groups) {
		this.groups.clear();
		for(SCIMUserGroup group : groups) {
			this.groups.add((UserGroup) group);
		}
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String toString() {
		Gson gson = new GsonBuilder().create();
		return gson.toJson(this);
	}
}
