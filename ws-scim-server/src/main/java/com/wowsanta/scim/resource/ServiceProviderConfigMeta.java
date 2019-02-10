package com.wowsanta.scim.resource;

import java.util.Date;

import com.wowsanta.scim.obj.DefaultMeta;

public class ServiceProviderConfigMeta extends DefaultMeta {

	/**
	 * 
	 */
	private static final long serialVersionUID = -880138764728219411L;
	private Date created;
	private Date lastModified;
	private String version;
	
	public ServiceProviderConfigMeta(String type) {
		super("ServiceProviderConfig");
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
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
}
