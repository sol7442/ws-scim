package com.wowsanta.scim.resource;


import java.util.Date;
import java.util.List;

import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.schema.SCIMResourceTypeSchema;

public interface SCIMResourceRepository {
	
	public void setUserSchema(SCIMResourceTypeSchema userSchema);
	public SCIMUser createUser(SCIMUser user) throws SCIMException;
	public SCIMUser getUser(String userId) throws SCIMException;
	public SCIMUser updateUser(SCIMUser updatedUser) throws SCIMException;
	public List<SCIMUser> getUsers(Date from, Date to)throws SCIMException;
	public List<SCIMUser> getUsers(String where)throws SCIMException;
	
	public void deleteUser(String userId) throws SCIMException;
	
	public void setGroupSchema(SCIMResourceTypeSchema groupSchema) throws SCIMException;
	public SCIMGroup createGroup(SCIMGroup group) throws SCIMException;
	public SCIMGroup getGroup(String groupId) throws SCIMException;
	public SCIMGroup updateGroup(SCIMGroup group) throws SCIMException;
	public void deleteGroup(String groupId) throws SCIMException;

	

}
