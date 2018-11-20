package com.wowsanta.scim.resource;


import com.wowsanta.scim.obj.User;
import com.wowsanta.scim.schema.SCIMResourceTypeSchema;

public interface RepositoryManager {
	public void initialize();
	
	public void setMapper(ResourceMapper mapper);
	public void setUserSchema(SCIMResourceTypeSchema userSchema);
	public User createUser(User user);
	public User getUser(String userId);
	public User updateUser(User updatedUser);
	public void deleteUser(String userId);
	
	public void setGroupSchema(SCIMResourceTypeSchema groupSchema);
	public Group createGroup(Group group);
	public Group getGroup(String groupId);
	public Group updateGroup(Group group);
	public void deleteGroup(String groupId);
}
