package com.wowsanta.scim.repository;



import java.util.List;
public interface SCIMRepositoryController {
	public List<ResourceTable> getTables() throws RepositoryException;
	public List<ResourceColumn> getTableColums(String tableName, String keyColumn) throws RepositoryException;
}
