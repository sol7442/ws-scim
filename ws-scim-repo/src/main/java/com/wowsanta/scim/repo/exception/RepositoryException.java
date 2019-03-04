package com.wowsanta.scim.repo.exception;

import com.wowsanta.scim.exception.SCIMError;
import com.wowsanta.scim.exception.SCIMException;

public class RepositoryException extends SCIMException {
	private static final long serialVersionUID = -3916698352414808182L;

	public RepositoryException(SCIMError err) {
		super(err);
	}

}
