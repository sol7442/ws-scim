package com.ehyundai.im;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.ehyundai.object.User;
import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.message.SCIMOperation;
import com.wowsanta.scim.obj.DefaultUserMeta;
import com.wowsanta.scim.obj.SCIMAdmin;
import com.wowsanta.scim.obj.SCIMSystem;
import com.wowsanta.scim.obj.SCIMUser;
import com.wowsanta.scim.obj.SCIMUserMeta;
import com.wowsanta.scim.repo.rdb.AbstractRDBRepository;
import com.wowsanta.scim.repo.rdb.DBCP;
import com.wowsanta.scim.resource.SCIMGroup;
import com.wowsanta.scim.resource.SCIMResouceFactory;
import com.wowsanta.scim.resource.SCIMResourceRepository;
import com.wowsanta.scim.resource.SCIMSystemRepository;
import com.wowsanta.scim.scheduler.SCIMScheduler;
import com.wowsanta.scim.schema.SCIMConstants;
import com.wowsanta.scim.schema.SCIMDefinitions;
import com.wowsanta.scim.schema.SCIMResourceTypeSchema;
import com.wowsanta.scim.util.Random;

public class IMResourceRepository extends AbstractRDBRepository implements SCIMResourceRepository{

	@Override
	public void setUserSchema(SCIMResourceTypeSchema userSchema) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public SCIMUser createUser(SCIMUser user) throws SCIMException {
		User im_user = (User)user;
		
		final String insertSQL1 = "INSERT INTO SCIM_USER (userId,userName,password,userType,active,createDate,modifyDate)"
				+ " VALUES ("
				+ "?,?,?,?,?,"
				+ "?,?)";
		
		
		Connection connection = null;
		PreparedStatement statement1 = null;
	    
		ResultSet resultSet = null;        
	    try {
	    	connection = getConnection();
        	statement1  = connection.prepareStatement(insertSQL1);
        	
        	statement1.setString(1, im_user.getId());
        	statement1.setString(2, im_user.getUserName());
        	statement1.setString(3, im_user.getId());
        	statement1.setString(4, SCIMDefinitions.UserType.USER.toString());
        	statement1.setInt(5, im_user.getActive());
        	statement1.setTimestamp(6, toSqlTimestamp(new Date()));
        	statement1.setTimestamp(7, toSqlTimestamp(new Date()));
        	statement1.execute();
        	
	    } catch (SQLException e) {
	    	if (e instanceof SQLIntegrityConstraintViolationException) {
				throw new SCIMException(e.getMessage(),RESULT_DUPLICATE_ENTRY);
			}else {
				throw new SCIMException("CREATE USER FAILED : " + insertSQL1 , e);
			}
		}finally {
	    	DBCP.close(connection, statement1, resultSet);
	    }

	    //insertProfiles(user);
	    
		return im_user;
	}

	private void insertProfiles(SCIMUser user) throws SCIMException {
		
		User im_user   = (User)user;
		String version = Random.number(0,1000000);
		
		insertProfile("CompanyCode"	,im_user.getCompanyCode(),im_user.getId(),version);
		insertProfile("Job"			,im_user.getJob(),im_user.getId(),version);
		insertProfile("Position"	,im_user.getPosition(),im_user.getId(),version);
		insertProfile("RetireDate"	,toString(im_user.getRetireDate()),im_user.getId(),version);
		insertProfile("JoinDate"	,toString(im_user.getJoinDate()),im_user.getId(),version);
	}
		
	private void insertProfile(String key, String value, String userId,String version) throws SCIMException {
		final String insertSQL2 = "INSERT INTO SCIM_USER_PROFILE (userId, pkey, pvalue, pver)"
				+ " VALUES ("
				+ "?,?,?,?)";
		
		Connection connection = null;
		PreparedStatement statement1 = null;
	    
		ResultSet resultSet = null;        
	    try {
	    	connection = getConnection();
        	statement1  = connection.prepareStatement(insertSQL2);
        	
        	statement1.setString(1, userId);
        	statement1.setString(2, key);
        	statement1.setString(3, value);
        	statement1.setString(4, version);
        	statement1.execute();
        	
	    } catch (SQLException e) {
	    	if (e instanceof SQLIntegrityConstraintViolationException) {
				throw new SCIMException(e.getMessage(),RESULT_DUPLICATE_ENTRY);
			}else {
				throw new SCIMException("CREATE USER FAILED : " + insertSQL2 , e);
			}
		}finally {
	    	DBCP.close(connection, statement1, resultSet);
	    }
	}
	

	@Override
	public SCIMUser getUser(String userId) throws SCIMException {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<SCIMUser> getUsersByActive() throws SCIMException {
		final String selectSQL = "SELECT userId,userName,active,createDate,modifyDate"
				+ " FROM SCIM_USER WHERE active=?";
	
		Connection connection = null;
		PreparedStatement statement = null;
	    ResultSet resultSet = null;              

	    List<SCIMUser> user_list = new ArrayList<SCIMUser>();
        try {
        	connection = getConnection();
        	statement  = connection.prepareStatement(selectSQL);
        	statement.setBoolean(1, true);
        	
        	resultSet = statement.executeQuery();
        	while(resultSet.next()) {
        		user_list.add(newUser(resultSet));
        	}
        	
		} catch (SQLException e) {
			throw new SCIMException(selectSQL, e);
		}finally {
			DBCP.close(connection, statement, resultSet);
		}
        
		return user_list;
	}
	
	
	@Override
	public List<SCIMUser> getUsersByDate(Date from, Date to) throws SCIMException {
		final String selectSQL = "SELECT userId,userName,active,createDate, modifyDate"
				+ " FROM SCIM_USER WHERE ModifyDate BETWEEN ? AND ?";
	
		Connection connection = null;
		PreparedStatement statement = null;
	    ResultSet resultSet = null;              

	    List<SCIMUser> user_list = new ArrayList<SCIMUser>();
        try {
        	connection = getConnection();
        	statement  = connection.prepareStatement(selectSQL);
        	
        	statement.setTimestamp(1, toSqlTimestamp(from));
        	statement.setTimestamp(2, toSqlTimestamp(to));
        	
        	resultSet = statement.executeQuery();
        	while(resultSet.next()) {
        		user_list.add(newUser(resultSet));
        	}
        	
		} catch (SQLException e) {
			throw new SCIMException(selectSQL, e);
		}finally {
			DBCP.close(connection, statement, resultSet);
		}
        
		return user_list;
	}

	private User newUser(ResultSet resultSet) {
		User im_user = new User();
		try {
    		im_user.setId(resultSet.getString("userId"));
    		
    		im_user.setEmployeeNumber(resultSet.getString("userId"));
    		im_user.setUserName(resultSet.getString("userName"));
    		im_user.setActive(resultSet.getBoolean("active"));
    		
    		im_user.setMeta(new SCIMUserMeta());
    		im_user.getMeta().setCreated(toJavaDate(resultSet.getTimestamp("createDate")));
    		im_user.getMeta().setLastModified(toJavaDate(resultSet.getTimestamp("modifyDate")));
    		
    		
    		//add user profile data//
    		im_user.setCompanyCode("companycode");
    		im_user.setOrganization("organization");
    		im_user.setDepartment("department");
    		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return im_user;
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
	public List<SCIMUser> getUsers(String where) throws SCIMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void clearSystemUser(String systemId) throws SCIMException {
		final String delateSQL = "DELETE FROM SCIM_SYSTEM_USER WHERE systemId=?";
System.out.println(">>>> clearSystemUser : " + systemId);
		Connection connection = null;
		PreparedStatement statement = null;
	    ResultSet resultSet = null;        
	    try {
	    	connection = getConnection();
        	statement  = connection.prepareStatement(delateSQL);
        	
        	statement.setString(1, systemId);
        	
        	statement.executeUpdate();
	    } catch (SQLException e) {
	    	throw new SCIMException("DELETE SYSTEM USER FAILED : " + delateSQL , e);
		}finally {
	    	DBCP.close(connection, statement, resultSet);
	    }
	}

	@Override
	public SCIMUser createSystemUser(String systemId, SCIMUser user) throws SCIMException {
		User system_user = (User)user;
	System.out.println(">>>> system_user ("+systemId+"): " + user);
	
		final String insertSQL1 = "INSERT INTO SCIM_SYSTEM_USER ("
				+ "systemId, userId, externalId,userName,"
				+ "password,userType,active,"
				+ "createDate,modifyDate,lastAccessDate)"
				+ " VALUES ("
				+ "?,?,?,?,"
				+ "?,?,?,"
				+ "?,?,?)";
		
		
		Connection connection = null;
		PreparedStatement statement1 = null;
	    
		ResultSet resultSet = null;        
	    try {
	    	connection = getConnection();
        	statement1  = connection.prepareStatement(insertSQL1);
        	
        	statement1.setString(1, systemId);
        	statement1.setString(2, system_user.getId());
        	statement1.setString(3, system_user.getExernalId());
        	statement1.setString(4, system_user.getUserName());
        	
        	statement1.setString(5, system_user.getPassword());
        	statement1.setString(6, SCIMDefinitions.UserType.USER.toString());
        	statement1.setBoolean(7,system_user.isActive());
        	
        	statement1.setTimestamp(8,  toSqlTimestamp(system_user.getMeta().getCreated()));
        	statement1.setTimestamp(9,  toSqlTimestamp(system_user.getMeta().getLastModified()));
        	statement1.setTimestamp(10, toSqlTimestamp(system_user.getLastAccessDate()));
        	statement1.execute();
        	
	    } catch (SQLException e) {
	    	if (e instanceof SQLIntegrityConstraintViolationException) {
				throw new SCIMException(e.getMessage(),RESULT_DUPLICATE_ENTRY);
			}else {
				throw new SCIMException("CREATE USER FAILED : " + insertSQL1 , e);
			}
		}finally {
	    	DBCP.close(connection, statement1, resultSet);
	    }

		return system_user;
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
	public List<SCIMUser> getAllUsers() throws SCIMException {
		// TODO Auto-generated method stub
		return null;
	}
}
