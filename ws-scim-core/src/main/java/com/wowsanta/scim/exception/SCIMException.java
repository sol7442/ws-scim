package com.wowsanta.scim.exception;

public class SCIMException extends Exception{
	private static final long serialVersionUID = 6917484448134959239L;

	public SCIMException(String msg) {
		super(msg);
	}

	public SCIMException(String msg, Throwable e) {
		super(msg,e);
	}
}
