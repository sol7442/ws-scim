package com.wowsanta.scim.resource;

import com.wowsanta.scim.exception.SCIMException;

public interface SCIMRepository {
	public void initialize() throws SCIMException;
	public void setRepositoryClass(String className);
	public void setRepositoryType(String string);
}
