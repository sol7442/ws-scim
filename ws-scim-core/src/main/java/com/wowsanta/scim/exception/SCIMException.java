package com.wowsanta.scim.exception;

import com.wowsanta.scim.log.SCIMLogger;

public class SCIMException extends Exception{
	private static final long serialVersionUID = 6917484448134959239L;
	private int code;
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
	
	public int getCode() {
		return this.code;
	}
}
