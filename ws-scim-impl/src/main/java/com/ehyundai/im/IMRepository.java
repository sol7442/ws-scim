package com.ehyundai.im;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.ehyundai.object.User;
import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.message.SCIMOperation;
import com.wowsanta.scim.obj.SCIMAdmin;
import com.wowsanta.scim.obj.SCIMUser;
import com.wowsanta.scim.repo.rdb.AbstractRDBRepository;
import com.wowsanta.scim.repo.rdb.DBCP;
import com.wowsanta.scim.resource.SCIMGroup;
import com.wowsanta.scim.resource.SCIMResouceFactory;
import com.wowsanta.scim.resource.SCIMResourceRepository;
import com.wowsanta.scim.resource.SCIMSystemRepository;
import com.wowsanta.scim.schema.SCIMConstants;
import com.wowsanta.scim.schema.SCIMDefinitions;
import com.wowsanta.scim.schema.SCIMResourceTypeSchema;

public class IMRepository extends AbstractRDBRepository implements SCIMSystemRepository, SCIMResourceRepository{


	@Override
	public SCIMAdmin getAdmin(String id) throws SCIMException {
		//final String selectSQL = "SELECT A.adminId, B.userName,  A.adminType, A.adminPw FROM SCIM_ADMIN A, SCIM_USER B WHERE A.adminId = B.userId AND A.adminId = ?";
		final String selectSQL = "SELECT U.userId, U.userName, U.password, SA.systemId FROM SCIM_USER U , SCIM_SYSTEM_ADMIN SA WHERE U.userId = SA.userId AND U.userId = ?";
				
		Connection connection = null;
		PreparedStatement statement = null;
	    ResultSet resultSet = null;        

	    SCIMAdmin admin = null;
        try {
        	connection = getConnection();
        	statement  = connection.prepareStatement(selectSQL);
        	statement.setString(1, id);
        	resultSet = statement.executeQuery();
        	if(resultSet.next()) {
        		admin = new SCIMAdmin();
        		admin.setId(resultSet.getString("userId"));
        		admin.setUserName(resultSet.getString("userName"));
        		admin.setPassword(resultSet.getString("password"));
        		admin.setSystemId(resultSet.getString("systemId"));
        	}else {
        		throw new SCIMException("ADMIN NOT FOUND : " + id, RESULT_IS_NULL);
        	}
		} catch (SQLException e) {
			throw new SCIMException(selectSQL, e);
		}finally {
			DBCP.close(connection, statement, resultSet);
		}
        
		return admin;
	}

	@Override
	public void setUserSchema(SCIMResourceTypeSchema userSchema) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public SCIMUser createUser(SCIMUser user) throws SCIMException {
		User im_user = (User)user;
		
		final String insertSQL = "INSERT INTO SCIM_USER (userId,userName,password,userType,active,createDate,modifyDate)"
				+ " VALUES ("
				+ "?,?,?,?,?,"
				+ "?,?)";
		
		Connection connection = null;
		PreparedStatement statement = null;
	    ResultSet resultSet = null;        
	    try {
	    	connection = getConnection();
        	statement  = connection.prepareStatement(insertSQL);

        	statement.setString(1, im_user.getId());
        	statement.setString(2, im_user.getUserName());
        	statement.setString(3, im_user.getId());
        	statement.setString(4, SCIMDefinitions.UserType.USER.toString());
        	statement.setInt(5, im_user.getActive());
        	statement.setTimestamp(6, toSqlTimestamp(new Date()));
        	statement.setTimestamp(7, toSqlTimestamp(new Date()));
        	
        	statement.execute();
	    } catch (SQLException e) {
	    	if (e instanceof SQLIntegrityConstraintViolationException) {
				throw new SCIMException(e.getMessage(),RESULT_DUPLICATE_ENTRY);
			}else {
				throw new SCIMException("CREATE USER FAILED : " + insertSQL , e);
			}
		}finally {
	    	DBCP.close(connection, statement, resultSet);
	    }
		return im_user;
	}

	@Override
	public SCIMUser getUser(String userId) throws SCIMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SCIMUser updateUser(SCIMUser user) throws SCIMException {
		final String updateSQL = "UPDATE SCIM_USER SET userName=?, active=?, modifyDate=? WHERE userId=?";

		User im_user = (User)user;
		Connection connection = null;
		PreparedStatement statement = null;
	    ResultSet resultSet = null;        
	    try {
	    	connection = getConnection();
        	statement  = connection.prepareStatement(updateSQL);
        	
        	statement.setString(1, im_user.getUserName());
        	statement.setInt(2, im_user.getActive());
        	
        	ZonedDateTime nowSeoul = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
        	statement.setTimestamp(3, toSqlTimestamp(Date.from(nowSeoul.toInstant())));
        	
        	statement.setString(4, im_user.getId());
        	
        	statement.executeUpdate();
	    } catch (SQLException e) {
	    	throw new SCIMException("UPDATE USER FAILED : " + updateSQL , e);
		}finally {
	    	DBCP.close(connection, statement, resultSet);
	    }
				
		return im_user;
	}
	
	@Override
	public void lockUser(String userId) throws SCIMException{
		final String updateSQL = "UPDATE SCIM_USER SET active=?, modifyDate=? WHERE userId=?";

		Connection connection = null;
		PreparedStatement statement = null;
	    ResultSet resultSet = null;        
	    try {
	    	connection = getConnection();
        	statement  = connection.prepareStatement(updateSQL);
        	
        	statement.setInt(1, 0);
        	
        	ZonedDateTime nowSeoul = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
        	statement.setTimestamp(2, toSqlTimestamp(Date.from(nowSeoul.toInstant())));
        	
        	statement.setString(3, userId);
        	
        	statement.executeUpdate();
	    } catch (SQLException e) {
	    	throw new SCIMException("UPDATE USER FAILED : " + updateSQL , e);
		}finally {
	    	DBCP.close(connection, statement, resultSet);
	    }
	}

	@Override
	public List<SCIMUser> getUsers(Date from, Date to) throws SCIMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SCIMUser> getUsers(String where) throws SCIMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteUser(String userId) throws SCIMException {
		
		final String delateSQL = "DELETE FROM SCIM_USER WHERE userId=?";

		Connection connection = null;
		PreparedStatement statement = null;
	    ResultSet resultSet = null;        
	    try {
	    	connection = getConnection();
        	statement  = connection.prepareStatement(delateSQL);
        	
        	statement.setString(1, userId);
        	
        	statement.executeUpdate();
	    } catch (SQLException e) {
	    	throw new SCIMException("DELETE USER FAILED : " + delateSQL , e);
		}finally {
	    	DBCP.close(connection, statement, resultSet);
	    }
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

	@Override
	public void addOperationResult(SCIMUser user, String source,String direct, SCIMOperation operation, SCIMOperation result) throws SCIMException {
		final String insertSQL = "INSERT INTO SCIM_AUDIT (adminId,userId,sourceSystem,directSystem,method,result,detail,workDate)"
				+ " VALUES ("
				+ "?,?,?,?,?,"
				+ "?,?,?)";
		
		Connection connection = null;
		PreparedStatement statement = null;
	    ResultSet resultSet = null;        
	    try {
	    	connection = getConnection();
        	statement  = connection.prepareStatement(insertSQL);

        	statement.setString(1, user.getId());
        	statement.setString(2, operation.getData().getId());
        	statement.setString(3, source);
        	statement.setString(4, direct);
        	statement.setString(5, operation.getMethod());
        	statement.setString(6, result.getStatus());
        	if(result.getResponse() != null) {
        		statement.setString(7, result.getResponse().getDetail());
        	}else {
        		statement.setString(7, null);
        	}
        	statement.setTimestamp(8, toSqlTimestamp(new Date()));
        	
        	statement.execute();
	    } catch (SQLException e) {
	    	if (e instanceof SQLIntegrityConstraintViolationException) {
				throw new SCIMException(e.getMessage(),RESULT_DUPLICATE_ENTRY);
			}else {
				throw new SCIMException("ADD AUDIT DATA FAILED : " + insertSQL , e);
			}
		}finally {
	    	DBCP.close(connection, statement, resultSet);
	    }
	}
}
