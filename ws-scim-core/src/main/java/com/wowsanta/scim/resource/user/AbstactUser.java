package com.wowsanta.scim.resource.user;

import java.util.ArrayList;
import java.util.List;

import com.wowsanta.scim.resource.Meta;
import com.wowsanta.scim.resource.group.Group;

public class AbstactUser {
	private String id;
	private Meta meta;
	private String userName;
	private List<Group> groups = new ArrayList<Group>();
	private boolean active;
	private String profileUrl;
	private transient String ref;
		
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Meta getMeta() {
		return meta;
	}
	public void setMeta(Meta meta) {
		this.meta = meta;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public List<Group> getGroups() {
		return groups;
	}
	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public String getProfileUrl() {
		return this.ref + "/" +this.id;
	}
	public String getRef() {
		return ref;
	}
	public void setRef(String ref) {
		this.ref = ref;
	}
	
}
