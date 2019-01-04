package com.wowsanta.scim.resource;

import java.io.Serializable;
import java.util.Date;

public interface SCIMMeta extends Serializable{
	
	public String getResourceType();
	public void setResourceType(String type);
	public Date getCreated();
	public void setCreated(Date created);
	public Date getLastModified();
	public void setLastModified(Date lastModified);
	public String getLocation();
	public void setLocation(String location);
	public String getVersion();
	public void setVersion(String version);
}
