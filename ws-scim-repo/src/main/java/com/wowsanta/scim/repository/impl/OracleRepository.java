package com.wowsanta.scim.repository.impl;

import java.sql.ResultSet;
import java.util.List;

import com.wowsanta.scim.object.SCIM_Resource;
import com.wowsanta.scim.object.SCIM_User;
import com.wowsanta.scim.repository.RepositoryException;
import com.wowsanta.scim.repository.ResourceColumn;
import com.wowsanta.scim.repository.ResourceTable;
import com.wowsanta.scim.repository.SCIMResourceRepository;

public class OracleRepository extends DefaultRepository implements SCIMResourceRepository{
	public  List<ResourceTable> getTables() throws RepositoryException{
		return null;
	}
	public  List<ResourceColumn> getTableColums(String tableName) throws RepositoryException{
		return null;
	}
	
	public Object getOutObject(ResultSet resultSet, ResourceColumn column) throws RepositoryException {
		return null;
	}
	@Override
	public List<SCIM_Resource> findUsers(String where, String order, int start_index, int page_count)
			throws RepositoryException {
		return null;
	}

		
}
