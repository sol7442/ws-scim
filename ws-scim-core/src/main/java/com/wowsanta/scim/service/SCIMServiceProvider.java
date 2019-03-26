package com.wowsanta.scim.service;

import java.util.HashMap;
import java.util.Map;

import com.wowsanta.scim.SCIMSystemInfo;
import com.wowsanta.scim.json.AbstractJsonObject;
import com.wowsanta.scim.obj.DefaultMeta;
import com.wowsanta.scim.obj.ServiceProviderConfigMeta;
import com.wowsanta.scim.resource.SCIMMeta;
import com.wowsanta.scim.resource.SCIMResoureDefinition;
import com.wowsata.util.json.JsonException;
import com.wowsata.util.json.WowsantaJson;

public class SCIMServiceProvider extends WowsantaJson {

	private static final long serialVersionUID = 1L;

	private SCIMSystemInfo systemInfo;
	
	private String[] schemas;
	private String documentationUri;
	
	
	private Map<String, SCIMResoureDefinition> resources = new HashMap<String,SCIMResoureDefinition>();

	private ServiceProviderConfigMeta meta;
	
	
	
	public SCIMServiceProvider() {
		this.jsonClass = SCIMServiceProvider.class.getCanonicalName();
	}
	
	public static SCIMServiceProvider loadFromFile(String file_name) throws JsonException{ 
		return (SCIMServiceProvider)WowsantaJson.loadFromFile(file_name);
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
	public Map<String,SCIMResoureDefinition> getResources() {
		return resources;
	}
	public void setResources(Map<String, SCIMResoureDefinition> resources) {
		this.resources = resources;
	}

	public DefaultMeta getMeta() {
		return meta;
	}

	public void setMeta(ServiceProviderConfigMeta meta) {
		this.meta = meta;
	}
}
