package com.wession.scim.exception;

public class ScimAuthException extends ScimException {

	public ScimAuthException(int code, String message) {
		super(code, message);
		// TODO Auto-generated constructor stub
	}

	public ScimAuthException() {
		super(403, "Failed Authentication");
		// TODO Auto-generated constructor stub
	}

}
