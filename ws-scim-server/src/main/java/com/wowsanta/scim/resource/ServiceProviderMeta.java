package com.wowsanta.scim.resource;

import java.util.Date;

public class ServiceProviderMeta implements SCIMMeta {
	/**
	 * 
	 */
	private static final long serialVersionUID = -880138764728219411L;
	private String resourceType;
	private Date created;
	private Date lastModified;
	private String location;
	private String version;
	
	public String getResourceType() {
		return resourceType;
	}
	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public Date getLastModified() {
		return lastModified;
	}
	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
}
