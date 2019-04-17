package com.wowsanta.scim.object;

import java.util.Date;

public class SCIM_Meta {
	private String type;
	private String location;
	private String version;
	
	private Date   created;
	private Date   modified;
	private Date   vaild;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public Date getModified() {
		return modified;
	}
	public void setModified(Date modified) {
		this.modified = modified;
	}
	public Date getVaild() {
		return vaild;
	}
	public void setVaild(Date vaild) {
		this.vaild = vaild;
	}
}
