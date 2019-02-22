package com.wowsanta.scim.exception;

import com.wowsanta.scim.log.SCIMLogger;
import com.wowsanta.scim.message.SCIMError;
import com.wowsanta.scim.schema.SCIMErrorCode.SCIMType;

public class SCIMException extends Exception{
	private static final long serialVersionUID = 6917484448134959239L;
	private int code;
	private SCIMType scimType;
	private SCIMError error;
	
	public SCIMException(String msg) {
		super(msg);
	}
	
	public SCIMException(String msg, Throwable e) {
		super(msg,e);
		SCIMLogger.error(msg, e);
	}
	
	public SCIMException(String msg, int code) {
		super(msg);
		this.code = code;
	}
	
	public SCIMException(String msg, String str_code) {
		super(msg);
		this.code = Integer.valueOf(str_code);
	}
	
	public SCIMException(String msg, SCIMType type) {
		super(msg);
		this.scimType = type;
		SCIMLogger.error(msg,this);
	}

	public SCIMException(String msg, SCIMError error, SCIMType type) {
		super(msg);
		this.error = error;
		this.scimType = type;
		SCIMLogger.error(msg,this);
	}
	public SCIMError getError() {
		return this.error;
	}
	public SCIMType getType() {
		return this.scimType;
	}
	public int getCode() {
		return this.code;
	}
}
