package com.wowsanta.scim.repository;

import java.io.FileNotFoundException;

public class RepositoryException extends Exception {
	private static final long serialVersionUID = 1287564515808761201L;
	public RepositoryException(String message, Throwable e) {
		super(message,e);
	}
}
