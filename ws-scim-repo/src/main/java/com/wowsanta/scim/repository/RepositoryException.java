package com.wowsanta.scim.repository;


public class RepositoryException extends Exception {
	private static final long serialVersionUID = 1287564515808761201L;
	public RepositoryException(String message, Throwable e) {
		super(message,e);
	}
}
