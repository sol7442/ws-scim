package com.wowsanta.scim.message;

import com.wowsanta.scim.json.SCIMJsonObject;

public class SCIMBulkOperation extends SCIMJsonObject{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6173234519005676647L;
	
	private String method;
	private String bulkId;
	private String version;
	private String path;
	private String data;
	private String location;
	private String response;
	private String status;
}
