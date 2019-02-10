package com.wowsanta.scim;

import com.wowsanta.scim.json.AbstractJsonObject;
import com.wowsanta.scim.schema.SCIMConstants;

public class SCIMSystemInfo extends AbstractJsonObject {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 9066725581672501344L;
	private String url;
	private String ver;
	
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
	public String getResoureTypeEndpoint() {
		return this.url + "/" + ver + SCIMConstants.RESOURCE_TYPE_ENDPOINT;
	}
	public String getServiceProviderEndpoint() {
		return this.url + "/" + ver + SCIMConstants.SERVICE_PROVIDER_CONFIG_ENDPOINT;
	}
}
