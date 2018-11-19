package com.wowsanta.scim.repo.rdb;


import com.wowsanta.scim.resource.Group;
import com.wowsanta.scim.resource.RepositoryManager;
import com.wowsanta.scim.resource.ResourceMapper;
import com.wowsanta.scim.resource.User;
import com.wowsanta.scim.schema.SCIMResourceTypeSchema;

public class RDBRepository implements RepositoryManager {
	
	private RDBQueryMapper queryMapper ;
	private ResourceMapper resourceMapper;
	private DBCP dbcp;
	
	@Override
	public void initialize() {
		
	}
	public void initDBCP(DBCP dbcp, RDBQueryMapper queryMapper) {
		this.dbcp = dbcp;
		this.queryMapper = queryMapper;
	}
	
	@Override
	public void setMapper(ResourceMapper resoureMapper) {
		this.resourceMapper = resoureMapper;
	}
	
	@Override
	public void setUserSchema(SCIMResourceTypeSchema userSchema) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public User createUser(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User getUser(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User updateUser(User updatedUser) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteUser(String userId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGroupSchema(SCIMResourceTypeSchema groupSchema) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Group createGroup(Group group) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Group getGroup(String groupId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Group updateGroup(Group group) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteGroup(String groupId) {
		// TODO Auto-generated method stub
		
	}


}
