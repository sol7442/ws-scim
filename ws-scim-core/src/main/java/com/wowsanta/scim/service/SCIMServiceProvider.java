package com.wowsanta.scim.service;

import com.wowsanta.scim.resource.SCIMMeta;

public interface SCIMServiceProvider {
	public SCIMMeta getMeta();
	public void setMeta(SCIMMeta meta);
	
	public String getDocumentationUri();
	public void setDocumentationUri(String documentationUri);

	public String[] getSchemas();
	public void setSchemas(String[] schemas);
	
	public SCIMServiceServer getServer();

}
