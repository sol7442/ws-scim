package com.wowsanta.scim.obj;

import com.wowsanta.scim.SCIMSystemManager;
import com.wowsanta.scim.resource.SCIMMeta;
import com.wowsanta.scim.schema.SCIMDefinitions;

public class DefaultMeta implements SCIMMeta {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5217314000123367910L;
	
	private final String resourceType;
	private String location;
	
	public DefaultMeta(String type) {
		this.resourceType = type;
		
	}
	@Override
	public String getResourceType() {
		return resourceType;
	}

	@Override
	public String getLocation() {
		return this.location;
	}
//
	@Override
	public void setLocation(String location) {
		if(SCIMDefinitions.ResoureType.USER.toString().equals(this.resourceType)) {
			this.location = SCIMSystemManager.getInstance().getServiceProvider().getSystemInfo().getUserEndpoint() + "/" + location;
		}else if(SCIMDefinitions.ResoureType.GROUP.toString().equals(this.resourceType)) {
			this.location = SCIMSystemManager.getInstance().getServiceProvider().getSystemInfo().getGroupEndpoint() + "/" + location;
		}else if(SCIMDefinitions.ResoureType.ResourceType.toString().equals(this.resourceType)) {
			this.location = SCIMSystemManager.getInstance().getServiceProvider().getSystemInfo().getResoureTypeEndpoint() + "/" + location;
		}
	}
}
