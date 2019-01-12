package com.wowsanta.scim.resource;


import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.schema.SCIMResourceTypeSchema;

public interface SCIMResourceRepository extends SCIMRepository {
	
	public void setUserSchema(SCIMResourceTypeSchema userSchema);
	public SCIMUser createUser(SCIMUser user) throws SCIMException;
	public SCIMUser getUser(String userId) throws SCIMException;
	public SCIMUser updateUser(SCIMUser updatedUser) throws SCIMException;
	public void deleteUser(String userId) throws SCIMException;
	
	public void setGroupSchema(SCIMResourceTypeSchema groupSchema) throws SCIMException;
	public SCIMGroup createGroup(SCIMGroup group) throws SCIMException;
	public SCIMGroup getGroup(String groupId) throws SCIMException;
	public SCIMGroup updateGroup(SCIMGroup group) throws SCIMException;
	public void deleteGroup(String groupId) throws SCIMException;

	

}
