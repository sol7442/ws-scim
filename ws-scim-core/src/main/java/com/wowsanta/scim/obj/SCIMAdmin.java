package com.wowsanta.scim.obj;


public class SCIMAdmin extends SCIMUser {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3655609649725263735L;
	
	private String systemId;

	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

}
