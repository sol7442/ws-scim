package com.wowsanta.scim.resource;

import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.obj.SCIMUser;

public interface SCIMResourceSetterRepository {
	public SCIMUser createUser(SCIMUser user) throws SCIMException;
	public SCIMUser updateUser(SCIMUser updatedUser) throws SCIMException;
	public void deleteUser(String userId) throws SCIMException;
	public void lockUser(String userId) throws SCIMException;
	public SCIMGroup createGroup(SCIMGroup group) throws SCIMException;
	public SCIMGroup updateGroup(SCIMGroup group) throws SCIMException;
	public void deleteGroup(String groupId) throws SCIMException;
}
