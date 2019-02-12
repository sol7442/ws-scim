package com.wowsanta.scim.obj;

import com.wowsanta.scim.schema.SCIMDefinitions;

public class SCIMUserMeta extends SCIMMeta {

	/**
	 * 
	 */
	private static final long serialVersionUID = 979589964864280808L;

	public SCIMUserMeta(){
		setResourceType(SCIMDefinitions.ResoureType.USER.toString());
	}

	
}
