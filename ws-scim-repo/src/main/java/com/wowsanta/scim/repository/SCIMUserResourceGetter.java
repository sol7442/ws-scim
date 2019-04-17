package com.wowsanta.scim.repository;

import java.sql.ResultSet;
import java.util.Date;
import java.util.List;

import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.obj.SCIMResource2;
import com.wowsanta.scim.obj.SCIMUser;
import com.wowsanta.scim.object.SCIM_Resource;
import com.wowsanta.scim.object.SCIM_User;
import com.wowsanta.scim.resource.SCIMGroup;

public interface SCIMUserResourceGetter{ 
	public void setUserSchemaMapper(SCIMSchemaMapper user_schema_mapper) throws RepositoryException;
	public Object getOutObject(ResultSet resultSet, ResourceColumn column) throws RepositoryException;
	
	public int getTotoalUserCount(String where)throws RepositoryException;
	public List<SCIM_Resource> findUsers(String where, String order, int start_index, int page_count)throws RepositoryException;


}
