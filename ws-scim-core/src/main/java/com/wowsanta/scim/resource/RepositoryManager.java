package com.wowsanta.scim.resource;


import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.repository.QueryManager;
import com.wowsanta.scim.schema.SCIMResourceTypeSchema;

public interface RepositoryManager {
	public void initialize() throws SCIMException;
	
	public void setMapper(ResourceMapper mapper);
	public void setUserSchema(SCIMResourceTypeSchema userSchema);
	public SCIMUser createUser(SCIMUser user);
	public SCIMUser getUser(String userId);
	public SCIMUser updateUser(SCIMUser updatedUser);
	public void deleteUser(String userId);
	
	public void setGroupSchema(SCIMResourceTypeSchema groupSchema);
	public Group createGroup(Group group);
	public Group getGroup(String groupId);
	public Group updateGroup(Group group);
	public void deleteGroup(String groupId);

	public void setQueryManager(QueryManager quer_mgr);
}
