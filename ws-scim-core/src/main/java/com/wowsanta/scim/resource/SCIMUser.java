package com.wowsanta.scim.resource;

import java.io.Serializable;
import java.util.List;


public interface SCIMUser extends Serializable {
	public void setSchemas(List<String> urls);
	public List<String> getSchemas();
	
	public void setId(String id);
	public String getId();
	
	public void setMeta(SCIMMeta meta);
	public SCIMMeta getMeta();
	
	public void addGroup(SCIMUserGroup group);
	public List<SCIMUserGroup> getGroups();
	public void setGroups(List<SCIMUserGroup> groups);
}
