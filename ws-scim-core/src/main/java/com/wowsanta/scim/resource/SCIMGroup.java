package com.wowsanta.scim.resource;

import java.util.List;

public interface SCIMGroup {
	public void addSchema(String urls);
	public void setSchemaUrls(List<String> urls);
	public List<String> getSchemas();
	
	public void setId(String id);
	public String getId();
	
	public void setMeta(SCIMMeta meta);
	public SCIMMeta getMeta();
}
