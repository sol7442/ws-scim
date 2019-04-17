package com.wowsanta.scim.repository;



import java.util.List;

import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.schema.SCIMResourceTypeSchema;

public interface SCIMResourceRepository {
	public List<ResourceTable> getTables() throws RepositoryException;
	public List<ResourceColumn> getTableColums(String tableName) throws RepositoryException;
}
