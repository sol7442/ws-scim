package com.wowsanta.scim.resource;

import java.util.Date;
import java.util.List;

import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.obj.SCIMUser;

public interface SCIMResourceGetterRepository extends SCIMResourceRepository{
	public SCIMUser getUser(String userId) throws SCIMException;
	public List<SCIMUser> getUsersByActive() throws SCIMException;
	public List<SCIMUser> getUsersByDate(Date from, Date to)throws SCIMException;
	public List<SCIMUser> getUsersByWhere(String where)throws SCIMException;	
	public List<SCIMUser> getAllUsers()throws SCIMException;
	
	public SCIMGroup getGroup(String groupId) throws SCIMException;
}
