package com.ehyundai.im;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.repo.rdb.AbstractRDBRepository;
import com.wowsanta.scim.repo.rdb.DBCP;
import com.wowsanta.scim.resource.SCIMAdmin;
import com.wowsanta.scim.resource.SCIMGroup;
import com.wowsanta.scim.resource.SCIMResouceFactory;
import com.wowsanta.scim.resource.SCIMResourceRepository;
import com.wowsanta.scim.resource.SCIMSystemRepository;
import com.wowsanta.scim.resource.SCIMUser;
import com.wowsanta.scim.schema.SCIMResourceTypeSchema;

public class IMRepository extends AbstractRDBRepository implements SCIMSystemRepository{


	@Override
	public SCIMAdmin getAdmin(String id) throws SCIMException {
		final String selectSQL = "SELECT A.adminId, B.userName,  A.adminType, A.adminPw FROM SCIM_ADMIN A, SCIM_USER B WHERE A.adminId = B.userId AND A.adminId = ?";
		//final String selectSQL = "SELECT ADMINID FROM SCIM_ADMIN WHERE adminId = ?";
		Connection connection = null;
		PreparedStatement statement = null;
	    ResultSet resultSet = null;        

	    Admin admin = null;
        try {
        	connection = getConnection();
        	statement  = connection.prepareStatement(selectSQL);
        	statement.setString(1, id);
        	resultSet = statement.executeQuery();
        	if(resultSet.next()) {
        		System.out.println("======");
        		admin = new Admin(
        				resultSet.getString("adminId"),
        				resultSet.getString("userName"),
        				resultSet.getString("adminPw"),
        				resultSet.getString("adminType")
        				);
        		
        	}
		} catch (SQLException e) {
			throw new SCIMException(selectSQL, e);
		}finally {
			DBCP.close(connection, statement, resultSet);
		}
		
		
		return admin;
	}
	

}
