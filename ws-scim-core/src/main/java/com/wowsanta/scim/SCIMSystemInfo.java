package com.wowsanta.scim;

import com.wowsanta.scim.schema.SCIMConstants;

public class SCIMSystemInfo {
	
	private static transient SCIMSystemInfo instance;
	
	private String url;
	private String ver;
	
	public static SCIMSystemInfo getInstance() {
		if(instance == null) {
			instance = new SCIMSystemInfo();
		}
		return instance;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	public void setVersion(String ver) {
		this.ver = ver;
	}
	public String getUserEndpoint() {
		return this.url + "/" + ver + SCIMConstants.USER_ENDPOINT;
	}
	
	public String getGroupEndpoint() {
		return this.url + "/" + ver + SCIMConstants.GROUP_ENDPOINT;
	}
//	public String getShemaEndpoint() {
//		return this.url + "/" + ver + SCIMConstants.schema;
//	}
	public String getResoureTypeEndpoint() {
		return this.url + "/" + ver + SCIMConstants.RESOURCE_TYPE_ENDPOINT;
	}
	public String getServiceProviderEndpoint() {
		return this.url + "/" + ver + SCIMConstants.SERVICE_PROVIDER_CONFIG_ENDPOINT;
	}
}
