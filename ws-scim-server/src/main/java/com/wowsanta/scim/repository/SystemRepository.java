package com.wowsanta.scim.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.obj.SCIMAdmin;
import com.wowsanta.scim.repo.rdb.AbstractRDBRepository;
import com.wowsanta.scim.repo.rdb.DBCP;
import com.wowsanta.scim.resource.SCIMSystemRepository;
import com.wowsanta.scim.resource.ServiceAdmin;

public class SystemRepository extends AbstractRDBRepository implements SCIMSystemRepository{

	@Override
	public SCIMAdmin getAdmin(String id) throws SCIMException {
		final String selectSQL = "SELECT A.ID, A.NAME, A.TYPE, B.PW FROM SCIM_ADMIN A, SCIM_ADMIN_ACCOUNT B WHERE A.ID == B.ID AND A.ID= ?";
		
		ServiceAdmin admin = new ServiceAdmin();
		
		Connection connection = null;
		PreparedStatement statement = null;
	    ResultSet resultSet = null;        

        try {
        	connection = getConnection();
        	statement  = connection.prepareStatement(selectSQL);
        	statement.setString(1, id);
        	resultSet = statement.executeQuery();
        	if(resultSet.next()) {
        		admin.setId(resultSet.getString("ID"));
        		admin.setName(resultSet.getString("NAME"));
        		admin.setType(resultSet.getString("TYPE"));
        		admin.setId(resultSet.getString("PW"));
        	}
        	        	
		} catch (SQLException e) {
			throw new SCIMException(selectSQL, e);
		}finally {
			DBCP.close(connection, statement, resultSet);
		}
		
		return admin;
	}
}
