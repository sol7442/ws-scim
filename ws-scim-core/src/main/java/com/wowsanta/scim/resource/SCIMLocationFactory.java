package com.wowsanta.scim.resource;

import com.wowsanta.scim.SCIMSystemManager;
import com.wowsanta.scim.obj.SCIMResource;
import com.wowsanta.scim.obj.SCIMUser;
import com.wowsanta.scim.schema.SCIMConstants;

public class SCIMLocationFactory {
	private static SCIMLocationFactory instance;
	public static SCIMLocationFactory getInstance() {
		if(instance == null) {
			instance = new SCIMLocationFactory();
		}
		return instance;
	}
	
	public String get(SCIMResource resource) {
		if(resource.getSchemas().contains(SCIMConstants.USER_CORE_SCHEMA_URI)) {
			if (resource instanceof SCIMUser) {
				SCIMUser user = (SCIMUser) resource;
				return SCIMSystemManager.getInstance().getServiceProvider().getSystemInfo().getUserEndpoint() + "/" + user.getId();
			}
		}
		return  "";
	}
}
