package com.wession.scim.schema;

import org.slf4j.Logger;

import com.wession.common.WessionLog;
import com.wession.scim.Const;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

public class Error extends JSONObject {
	/*
	   status
      	  The HTTP status code (see Section 6 of [RFC7231]) expressed as a JSON string.  REQUIRED.

	   scimType
	      A SCIM detail error keyword.  See Table 9.  OPTIONAL.
	
	   detail
	      A detailed human-readable message.  OPTIONAL.
	 */
	WessionLog wsLog = new WessionLog();
	Logger processLog = wsLog.getProcessLog();
	
	public Error() {
		addSchema();
	}
	public Error(int code) {
		addSchema();
		this.put("status", Integer.toString(code));
		processLog.error("Error code " + code);
	}
	public Error(int code, String detail) {
		addSchema();
		this.put("status", Integer.toString(code));
		this.put("detail", detail);
		processLog.error("Error code " + code + " : " + detail);
	}
	public Error(int code, String scimType, String detail) {
		addSchema();
		this.put("status", Integer.toString(code));
		this.put("detail", detail);
		this.put("scimType", scimType);
		processLog.error("[" + scimType + "] Error code " + code + " : " + detail);
	}
	
	private void addSchema() {
		JSONArray schemas = new JSONArray();
		schemas.add(Const.schemas_v20_error);
		this.put("schemas", schemas);
	}
}
