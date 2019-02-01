package com.wowsanta.scim.obj;

import java.util.Date;

import com.wowsanta.scim.schema.SCIMDefinitions;

public class DefaultUserMeta extends DefaultMeta{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1600557587781949156L;
	
	private Date created;
	private Date lastModified;
	private String version = "v2";
	
	public DefaultUserMeta() {
		super(SCIMDefinitions.ReferenceType.USER.toString());
		//setResourceType(SCIMDefinitions.ReferenceType.USER.toString());
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
