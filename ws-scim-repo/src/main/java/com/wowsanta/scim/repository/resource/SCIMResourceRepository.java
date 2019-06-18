package com.wowsanta.scim.repository.resource;

import java.util.List;

import com.wowsanta.scim.object.Resource_Object;
import com.wowsanta.scim.repository.AttributeValue;
import com.wowsanta.scim.repository.RepositoryException;
import com.wowsanta.scim.repository.RepositoryInputMapper;
import com.wowsanta.scim.repository.RepositoryOutputMapper;

public interface SCIMResourceRepository{ 
	
	public void createUser(Resource_Object group_object) throws RepositoryException;
	public void updateUser(Resource_Object group_object)throws RepositoryException;
	public int getUserCount(String filter)throws RepositoryException;
	public Resource_Object getUser(String id)throws RepositoryException ;
	public List<Resource_Object> searchUser(String filter,int startIndex, int pageCont, int totalCount)throws RepositoryException;
	
	
	public void createGroup(Resource_Object group_object) throws RepositoryException;
	public void updateGroup(Resource_Object group_object)throws RepositoryException;
	public int getGroupCount(String filter) throws RepositoryException;
	public Resource_Object getGroup(String groupId)throws RepositoryException;
	public List<Resource_Object> searchGroup(String filter, int startIndex, int pageCont, int totalCount)throws RepositoryException;

	public int getSystemUserCount(RepositoryOutputMapper outMapper, String filter) throws RepositoryException;
	public List<Resource_Object> searchSystemUser(RepositoryOutputMapper outMapper, String filter, int startIndex, int pageCont, int totalCount)throws RepositoryException;
	public List<Resource_Object> searchSystemUser(RepositoryOutputMapper outMapper,	List<AttributeValue> attribute_list, int startIndex, int pageCont, int totalCount)  throws RepositoryException;
	
	public void createSystemUser(RepositoryInputMapper inMapper,Resource_Object resource)  throws RepositoryException;
	public void updateSystemUser(RepositoryInputMapper inMapper,Resource_Object resource)  throws RepositoryException;
	public Resource_Object getSystemUser(RepositoryOutputMapper outMapper,	List<AttributeValue> attribute_list)  throws RepositoryException;
	
	
}
