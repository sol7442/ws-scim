package com.wowsanta.scim.repository;

import java.util.Date;
import java.util.List;

import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.obj.SCIMResource2;
import com.wowsanta.scim.obj.SCIMUser;
import com.wowsanta.scim.resource.SCIMGroup;

public interface SCIMResourceGetterRepository extends SCIMResourceRepository{
	public SCIMUser getUser(String userId) throws SCIMException;
	public List<SCIMUser> getUsersByActive() throws SCIMException;
	public List<SCIMUser> getUsersByDate(Date from, Date to)throws SCIMException;
		
	public List<SCIMResource2> getUsersByWhere(String where)throws SCIMException;
	public List<SCIMUser> getAllUsers()throws SCIMException;
	public SCIMGroup getGroup(String groupId) throws SCIMException;
	
}
