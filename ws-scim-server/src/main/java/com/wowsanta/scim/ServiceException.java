package com.wowsanta.scim;

public class ServiceException extends Exception {
	private static final long serialVersionUID = 307546497191769731L;

	public ServiceException(String message) {
		super(message);
	}
	
	public ServiceException(String message, Throwable e) {
		super(message,e);
	}
}
