package com.ehyundai.gw;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ehyundai.im.Admin;
import com.ehyundai.im.User;
import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.repo.rdb.AbstractRDBRepository;
import com.wowsanta.scim.repo.rdb.DBCP;
import com.wowsanta.scim.resource.SCIMAdmin;
import com.wowsanta.scim.resource.SCIMGroup;
import com.wowsanta.scim.resource.SCIMResourceRepository;
import com.wowsanta.scim.resource.SCIMSystemRepository;
import com.wowsanta.scim.resource.SCIMUser;
import com.wowsanta.scim.schema.SCIMResourceTypeSchema;

public class GWRepository extends AbstractRDBRepository implements SCIMSystemRepository, SCIMResourceRepository{

	@Override
	public SCIMAdmin getAdmin(String id) throws SCIMException {
		// TODO Auto-generated method stub
		return null;
	}
	//SELECT UR_CODE, MODIFYDATE FROM BASE_OBJECT_UR WHERE MODIFYDATE > '2019-01-14 23:17:36'

	@Override
	public void setUserSchema(SCIMResourceTypeSchema userSchema) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public SCIMUser createUser(SCIMUser user) throws SCIMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SCIMUser getUser(String userId) throws SCIMException {
		System.out.println("=====getUser ===");
		final String selectSQL = "SELECT UR_CODE, EmpNo, DisplayName FROM BASE_OBJECT_UR WHERE EmpNo = ?";
		Connection connection = null;
		PreparedStatement statement = null;
	    ResultSet resultSet = null;        

	    User user = null;
	    
        try {
        	connection = getConnection();
        	statement  = connection.prepareStatement(selectSQL);
        	statement.setString(1, userId);
        	resultSet = statement.executeQuery();
        	if(resultSet.next()) {
        		user = new User();
        		user.setId(resultSet.getString("EmpNo"));
        		user.setUserName(resultSet.getString("DisplayName"));
        		System.out.println("ddd : " + resultSet.getString("DisplayName"));
        	}
        	
        	System.out.println(user.getId());
        	System.out.println(user.getUserName());
        	
		} catch (SQLException e) {
			throw new SCIMException(selectSQL, e);
		}finally {
			DBCP.close(connection, statement, resultSet);
		}
        
		return user;
	}

	@Override
	public SCIMUser updateUser(SCIMUser updatedUser) throws SCIMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteUser(String userId) throws SCIMException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGroupSchema(SCIMResourceTypeSchema groupSchema) throws SCIMException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public SCIMGroup createGroup(SCIMGroup group) throws SCIMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SCIMGroup getGroup(String groupId) throws SCIMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SCIMGroup updateGroup(SCIMGroup group) throws SCIMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteGroup(String groupId) throws SCIMException {
		// TODO Auto-generated method stub
		
	}
}
