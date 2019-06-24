package com.wowsanta.scim.repository;



import java.util.List;
import java.util.Map;
public interface SCIMRepositoryController {
	public List<ResourceTable> getTables() throws RepositoryException;
	public List<ResourceColumn> getTableColums(String tableName, String keyColumn) throws RepositoryException;
	public List<Map<String,Object>> excuteQuery(String query)throws RepositoryException;
}
