package com.ehyundai.object;

import java.util.Date;

public class SysUser extends Resource{
	private String systemId;
	private Date   provisionDate;
	
	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	public void setProvisionDate(Date date) {
		this.provisionDate = date;
	}
	public Date getProvisionDate() {
		return this.provisionDate;
	}
}
