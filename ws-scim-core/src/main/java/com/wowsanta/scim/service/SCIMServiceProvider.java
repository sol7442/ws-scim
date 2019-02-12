package com.wowsanta.scim.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wowsanta.scim.SCIMSystemInfo;
import com.wowsanta.scim.json.AbstractJsonObject;
import com.wowsanta.scim.resource.SCIMMeta;
import com.wowsanta.scim.resource.SCIMRepository;
import com.wowsanta.scim.resource.SCIMRepositoryManager;
import com.wowsanta.scim.resource.SCIMResoureDefinition;

public abstract class SCIMServiceProvider extends AbstractJsonObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private SCIMSystemInfo systemInfo;
	
	private String[] schemas;
	private String documentationUri;
	
	private String repositoryConfig;
	private Map<String, SCIMResoureDefinition> resources = new HashMap<String,SCIMResoureDefinition>();
	
	public abstract SCIMMeta getMeta();
	public abstract void setMeta(SCIMMeta meta);
	public abstract SCIMServiceServer getServer();
	
	public String getDocumentationUri() {
		return documentationUri;
	}

	public void setDocumentationUri(String documentationUri) {
		this.documentationUri = documentationUri;
	}

	public String[] getSchemas() {
		return schemas;
	}

	public void setSchemas(String[] schemas) {
		this.schemas = schemas;
	}
	
	public SCIMSystemInfo getSystemInfo() {
		return systemInfo;
	}
	public void setSystemInfo(SCIMSystemInfo systemInfo) {
		this.systemInfo = systemInfo;
	}
	public String getRepositoryConfig() {
		return repositoryConfig;
	}
	public void setRepositoryConfig(String repositoryConfig) {
		this.repositoryConfig = repositoryConfig;
	}
	public Map<String,SCIMResoureDefinition> getResources() {
		return resources;
	}
	public void setResources(Map<String, SCIMResoureDefinition> resources) {
		this.resources = resources;
	}
}
