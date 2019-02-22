package com.ehyundai.sso;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ehyundai.object.User;
import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.obj.SCIMUser;
import com.wowsanta.scim.obj.SCIMUserMeta;
import com.wowsanta.scim.repo.rdb.AbstractRDBRepository;
import com.wowsanta.scim.repo.rdb.DBCP;
import com.wowsanta.scim.resource.SCIMGroup;
import com.wowsanta.scim.resource.SCIMResourceRepository;
import com.wowsanta.scim.schema.SCIMResourceTypeSchema;

public class SsoResoureRepository extends AbstractRDBRepository implements SCIMResourceRepository{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5170670467974453815L;

	public SsoResoureRepository() {
		super();
		setClassName(SsoResoureRepository.class.getCanonicalName());
	}
	
	//SELECT UR_CODE, MODIFYDATE FROM BASE_OBJECT_UR WHERE MODIFYDATE > '2019-01-14 23:17:36'

	@Override
	public void setUserSchema(SCIMResourceTypeSchema userSchema) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public SCIMUser createUser(SCIMUser user) throws SCIMException {
		User sso_user = (User)user;
		
		System.out.println("sso user create >>> " + sso_user);
		final String insertSQL = "INSERT INTO WA3_USER (ID,NAME,EMAIL,DIV_ID,ORG_ID,PATH_ID"
				+ ",DISABLED,LOCKED"
				+ ",LAST_LOGON_TIME,CREATE_TIME,MODIFY_TIME )"
				+ " VALUES ("
				+ "?,?,?,?,?,?,"
				+ "?,?,?,?,?)";
				
	    System.out.println(">>>>>>" + sso_user.toString(true));    	

		
		Connection connection = null;
		PreparedStatement statement = null;
	    ResultSet resultSet = null;        
	    try {
	    	connection = getConnection();
        	statement  = connection.prepareStatement(insertSQL);

        	statement.setString(1, sso_user.getId());
        	statement.setString(2, sso_user.getUserName());
        	statement.setString(3, sso_user.geteMail());
        	
        	statement.setString(4, sso_user.getOrganization());
        	statement.setString(5, sso_user.getDivision());
        	statement.setString(6, sso_user.getDepartment());

        	statement.setBoolean(7,sso_user.isActive());
        	statement.setBoolean(8,sso_user.isActive());
        	
        	if(sso_user.getLastAccessDate() != null) {
        		statement.setLong(9,sso_user.getLastAccessDate().getTime());	
        	}else {
        		statement.setLong(9,new Date().getTime());
        	}
        	
        	if(sso_user.getMeta() != null) {
        		statement.setLong(10,sso_user.getMeta().getCreated().getTime());
        		statement.setLong(11,sso_user.getMeta().getLastModified().getTime());
        	}else {
        		statement.setLong(10,new Date().getTime());
        		statement.setLong(11,new Date().getTime());
        	}
        	
        	statement.execute();
	    }catch(Exception e) {
System.out.println("sso user create error >>> " + e.getMessage());
	    	
	    	if (e instanceof SQLIntegrityConstraintViolationException) {
				throw new SCIMException(e.getMessage());
			}else {
				throw new SCIMException("CREATE USER FAILED : " + insertSQL , e);
			}
	    }finally {
	    	DBCP.close(connection, statement, resultSet);
	    }
System.out.println("sso user create >>> " + sso_user);
		
		return sso_user;
	}

	@Override
	public SCIMUser getUser(String userId) throws SCIMException {
		final String selectSQL = "SELECT ID,NAME,EMAIL,DIV_ID,ORG_ID,PATH_ID"
				+ ",DISABLED,LOCKED"
				+ ",CREATE_TIME,MODIFY_TIME "
				+ "FROM WA3_USER ID = ?";
		Connection connection = null;
		PreparedStatement statement = null;
	    ResultSet resultSet = null;              

	    User sso_user = null;
	    
        try {
        	connection = getConnection();
        	statement  = connection.prepareStatement(selectSQL);
        	statement.setString(1, userId);
        	resultSet = statement.executeQuery();
        	if(resultSet.next()) {
        		sso_user = new User();
        		
        		sso_user.setId(resultSet.getString("ID"));
        		sso_user.setUserName(resultSet.getString("NAME"));
        		sso_user.seteMail(resultSet.getString("EMAIL"));
        		
        		sso_user.setOrganization(resultSet.getString("DIV_ID"));
        		sso_user.setActive(resultSet.getBoolean("DISABLED"));
        		
        		sso_user.setMeta(new SCIMUserMeta());
        		sso_user.getMeta().setCreated(new Date(resultSet.getLong("CREATE_TIME")));
        		sso_user.getMeta().setLastModified(new Date(resultSet.getLong("MODIFY_TIME")));
        	}else {
        		throw new SCIMException("USER NOT FOUND : " + userId, RESULT_IS_NULL);
        	}
        	
		} catch (SQLException e) {
			throw new SCIMException(selectSQL, e);
		}finally {
			DBCP.close(connection, statement, resultSet);
		}
        
		return sso_user;
	}

	@Override
	public List<SCIMUser> getUsersByActive() throws SCIMException{
		
		return new ArrayList<SCIMUser>();
	}
	

	@Override
	public List<SCIMUser> getAllUsers() throws SCIMException {
		final String selectSQL = "SELECT ID,NAME,EMAIL"
				+ ",DISABLED,LOCKED"
				+ ",CREATE_TIME,LAST_LOGON_TIME,MODIFY_TIME "
				+ "FROM WA3_USER";
	
		Connection connection = null;
		PreparedStatement statement = null;
	    ResultSet resultSet = null;              

	    List<SCIMUser> user_list = new ArrayList<SCIMUser>();
        try {
        	connection = getConnection();
        	statement  = connection.prepareStatement(selectSQL);
        	
        	resultSet = statement.executeQuery();
        	while(resultSet.next()) {
        		User sso_user = new User();
        		
        		sso_user.setId(resultSet.getString("ID"));
        		sso_user.setExernalId(resultSet.getString("ID"));
        		
        		sso_user.setUserName(resultSet.getString("NAME"));
        		sso_user.seteMail(resultSet.getString("EMAIL"));
        		
        		sso_user.setEmployeeNumber(resultSet.getString("ID"));
        		
        		sso_user.setActive(resultSet.getBoolean("DISABLED"));
        		
        		sso_user.setLastAccessDate(new Date(resultSet.getLong("LAST_LOGON_TIME")));
        		
        		sso_user.setMeta(new SCIMUserMeta());
        		sso_user.getMeta().setCreated(new Date(resultSet.getLong("CREATE_TIME")));
        		sso_user.getMeta().setLastModified(new Date(resultSet.getLong("MODIFY_TIME")));
        		
        		user_list.add(sso_user);
        	}
        	
		} catch (SQLException e) {
			throw new SCIMException(selectSQL, e);
		}finally {
			DBCP.close(connection, statement, resultSet);
		}
        
		return user_list;
	}
	
	@Override
	public List<SCIMUser> getUsersByDate(Date from, Date to)throws SCIMException{
		final String selectSQL = "SELECT ID,NAME,EMAIL,DIV_ID,ORG_ID,PATH_ID"
				+ ",DISABLED,LOCKED"
				+ ",CREATE_TIME,MODIFY_TIME "
				+ "FROM WA3_USER WHERE MODIFY_TIME BETWEEN ? AND ?";
	
		Connection connection = null;
		PreparedStatement statement = null;
	    ResultSet resultSet = null;              

	    List<SCIMUser> user_list = new ArrayList<SCIMUser>();
        try {
        	connection = getConnection();
        	statement  = connection.prepareStatement(selectSQL);
        	
        	System.out.println(new Date().getTime() + ":" + from.getTime() + " : " + to.getTime());
        	statement.setLong(1, from.getTime());
        	statement.setLong(2, to.getTime());
        	
        	resultSet = statement.executeQuery();
        	while(resultSet.next()) {
        		User sso_user = new User();
        		
        		sso_user.setId(resultSet.getString("ID"));
        		sso_user.setUserName(resultSet.getString("NAME"));
        		sso_user.seteMail(resultSet.getString("EMAIL"));
        		
        		sso_user.setEmployeeNumber(resultSet.getString("ID"));
        		sso_user.setGroupCode(resultSet.getString("DIV_ID"));
        		sso_user.setActive(resultSet.getBoolean("DISABLED"));
        		
        		sso_user.setMeta(new SCIMUserMeta());
        		sso_user.getMeta().setCreated(new Date(resultSet.getLong("CREATE_TIME")));
        		sso_user.getMeta().setLastModified(new Date(resultSet.getLong("MODIFY_TIME")));
        		
        		user_list.add(sso_user);
        	}
        	
		} catch (SQLException e) {
			throw new SCIMException(selectSQL, e);
		}finally {
			DBCP.close(connection, statement, resultSet);
		}
        
		return user_list;
	}
	@Override
	public List<SCIMUser> getUsers(String where) {
		
		return null;
	}
	
	@Override
	public SCIMUser updateUser(SCIMUser user) throws SCIMException {
		
		final String updateSQL = "UPDATE WA3_USER "
				+ "SET NAME=?, EMAIL=?,DIV_ID=?, ORG_ID=?,PATH_ID=?,"
				+ "DISABLED=?,LOCKED=?, LAST_LOGON_TIME=?, MODIFY_TIME=? WHERE ID=?"; 
				
		Connection connection = null;
		PreparedStatement statement = null;
	    ResultSet resultSet = null;        
	    try {
    		User sso_user = (User)user;

	    	connection = getConnection();
        	statement  = connection.prepareStatement(updateSQL);

        	statement.setString(1, sso_user.getUserName());
        	statement.setString(2, sso_user.geteMail());
        	
          	statement.setString(3, "div_id_up");
        	statement.setString(4, "org_id_up");
        	statement.setString(5, "path_id_up");
        	
        	statement.setBoolean(6, sso_user.isActive());
        	statement.setBoolean(7, sso_user.isActive());
        	
        	statement.setLong(8, toLong(sso_user.getLastAccessDate()));
        	statement.setLong(9, new Date().getTime());
        	
        	statement.setString(10, sso_user.getEmployeeNumber());
        	
        	statement.executeUpdate();
	    } catch (SQLException e) {
	    	throw new SCIMException("UPDATE USER FAILED : " + updateSQL , e);
		}finally {
	    	DBCP.close(connection, statement, resultSet);
	    }		
				
		return null;
	}

	private long toLong(Date lastAccessDate) {
		if(lastAccessDate == null) {
			return 0;
		}else {
			return lastAccessDate.getTime();
		}
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
	public void lockUser(String userId) throws SCIMException {
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
