package com.wowsanta.scim.repository;

public interface SCIMRepository {
	public void initialize() throws RepositoryException ;
	public void close() ;
}
