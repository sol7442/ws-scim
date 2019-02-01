package com.wowsanta.scim.resource;

import java.io.Serializable;
import java.util.Date;

public interface SCIMMeta extends Serializable{
	
	public String getResourceType();
	public String getLocation();
//	public void setResourceType(String type);
	public void setLocation(String location);
}
