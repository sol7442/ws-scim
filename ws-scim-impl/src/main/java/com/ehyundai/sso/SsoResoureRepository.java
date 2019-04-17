package com.ehyundai.sso;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ehyundai.object.Resource;
import com.ehyundai.object.User;
import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.log.SCIMLogger;
import com.wowsanta.scim.obj.SCIMResource2;
import com.wowsanta.scim.obj.SCIMUser;
import com.wowsanta.scim.obj.SCIMUserMeta;
import com.wowsanta.scim.repo.rdb.AbstractRDBRepository;
import com.wowsanta.scim.repo.rdb.DBCP;
import com.wowsanta.scim.repository.RepositoryException;
import com.wowsanta.scim.repository.ResourceColumn;
import com.wowsanta.scim.repository.ResourceTable;
import com.wowsanta.scim.repository.SCIMResourceGetterRepository;
import com.wowsanta.scim.resource.SCIMGroup;
import com.wowsanta.scim.resource.SCIMResourceSetterRepository;
import com.wowsanta.scim.resource.SCIMSystemColumn;
import com.wowsanta.scim.schema.SCIMResourceTypeSchema;

public class SsoResoureRepository extends AbstractRDBRepository implements SCIMResourceGetterRepository, SCIMResourceSetterRepository{

	private transient Logger logger = LoggerFactory.getLogger(SsoResoureRepository.class);
	private transient static final long serialVersionUID = 5170670467974453815L;

	public SsoResoureRepository() {
		super();
		//setClassName(SsoResoureRepository.class.getCanonicalName());
	}
	
	public List<ResourceTable> getTables() throws RepositoryException{
		return null;
	}
	public List<ResourceColumn> getTableColums(String tableName) throws RepositoryException{
		return null;
	}
	
	@Override
	public boolean validate() throws SCIMException {
		
		final String selectSQL = this.dbcp.getValiationQuery();
		
		Connection connection = null;
		PreparedStatement statement = null;
	    ResultSet resultSet = null;        

        try {
        	connection = getConnection();
        	statement  = connection.prepareStatement(selectSQL);
        	
        	resultSet = statement.executeQuery();
        
        	while(resultSet.next()) {
        		SCIMSystemColumn column = new SCIMSystemColumn();
        		
        		column.setColumnName(resultSet.getString("COLUMN_NAME"));
        		column.setDataType(resultSet.getString("DATA_TYPE"));
        		column.setAllowNull(resultSet.getBoolean("NULLABLE"));
        		
        		logger.info("COLUMNS : {} ", column.tojson(false));
        	}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SCIMException(selectSQL, e);
		}finally {
			DBCP.close(connection, statement, resultSet);
		}
        logger.info("REPOSITORY VAILDATE : {} ", selectSQL);	
		return true;
	}
	
	//SELECT UR_CODE, MODIFYDATE FROM BASE_OBJECT_UR WHERE MODIFYDATE > '2019-01-14 23:17:36'

	
	@Override
	public List<SCIMResource2> getUsersByWhere(String where) throws SCIMException {
		final String selectSQL = "SELECT * FROM WA3_USER " + where;
	
		Connection connection = null;
		PreparedStatement statement = null;
	    ResultSet resultSet = null;              

	    List<SCIMResource2> user_list = new ArrayList<SCIMResource2>();
        try {
        	connection = getConnection();
        	statement  = connection.prepareStatement(selectSQL);
        	
        	resultSet = statement.executeQuery();
        	while(resultSet.next()) {
        		SCIMResource2 sso_user = newResourceFromDB(resultSet);
        		
        		user_list.add(sso_user);
        	}
        	
		} catch (SQLException e) {
			throw new SCIMException(selectSQL, e);
		}finally {
			DBCP.close(connection, statement, resultSet);
		}
        
		return user_list;
	}


	

	private SCIMResource2 newResourceFromDB(ResultSet resultSet) throws SQLException {
		Resource resource = new Resource();  
				
		resource.setId(resultSet.getString("ID"));
		resource.setExernalId(resultSet.getString("ID"));
		resource.setEmployeeNumber(resultSet.getString("ID"));
		
		resource.setUserName(resultSet.getString("NAME"));
		resource.setActive(resultSet.getBoolean("DISABLED"));
		resource.setLastAccessDate(new Date(resultSet.getLong("LAST_LOGON_TIME")));
		resource.setCreated(new Date(resultSet.getLong("CREATE_TIME")));
		resource.setLastModified(new Date(resultSet.getLong("MODIFY_TIME")));
		
		return resource;
	}

	@Override
	public SCIMUser createUser(SCIMUser user) throws SCIMException {
		User sso_user = (User)user;
		
		System.out.println("sso user create >>> " + sso_user);
		final String insertSQL = "INSERT INTO WA3_USER ("
				+ "ID,NAME,EMAIL,DIV_ID,ORG_ID,PATH_ID"
				+ ",DISABLED,LOCKED,PWD_MUST_CHANGE, ACCESS_ALLOW, PWD_RETRY_COUNT "
				+ ",PWD_RETRY_TIME, LAST_LOGON_TIME, CREATE_TIME, MODIFY_TIME, VALID_FROM,VALID_TO)"
				+ " VALUES ("
				+ "?,?,?,?,?,?,"
				+ "?,?,?,?,?,"
				+ "?,?,?,?,?,?)";
				
		Connection connection = null;
		PreparedStatement statement = null;
	    ResultSet resultSet = null;        
	    try {
	    	connection = getConnection();
        	statement  = connection.prepareStatement(insertSQL);

        	setCreateUserStatement(sso_user, statement);
        	        	
        	statement.execute();
	    }catch(Exception e) {
	    	e.printStackTrace();
	    	if (e instanceof SQLIntegrityConstraintViolationException) {
				throw new SCIMException(e.getMessage());
			}else {
				throw new SCIMException("CREATE USER FAILED : " + insertSQL , e);
			}
	    }finally {
	    	DBCP.close(connection, statement, resultSet);
	    }
		
		return sso_user;
	}

	private void setCreateUserStatement(User sso_user, PreparedStatement statement) throws SQLException {
		statement.setString(1, sso_user.getId());
		statement.setString(2, sso_user.getUserName());
		statement.setString(3, sso_user.geteMail());
		
		statement.setString(4, sso_user.getOrganization());
		statement.setString(5, sso_user.getDivision());
		statement.setString(6, "NOT DEFINED");//sso_user.getDepartment()

		statement.setBoolean(7,false);
		statement.setBoolean(8,false);
		statement.setBoolean(9,true);
		statement.setBoolean(10,true);
		statement.setInt(11,3);
		        	
		Date nowDate = new Date();
		statement.setLong(12, nowDate.getTime());
		statement.setLong(13,nowDate.getTime());
		statement.setLong(14,nowDate.getTime());
		statement.setLong(15,nowDate.getTime());
		statement.setLong(16,nowDate.getTime());
		statement.setLong(17,new Date(2147483647).getTime());
	}

	@Override
	public SCIMUser getUser(String userId) throws SCIMException {
		final String selectSQL = "SELECT * FROM WA3_USER WHERE ID=?";
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
        		//throw new SCIMException("USER NOT FOUND : " + userId, RESULT_IS_NULL);
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
		return getAllUsers();
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
	public SCIMUser updateUser(SCIMUser user) throws SCIMException {
		
		final String updateSQL = "UPDATE WA3_USER "
				+ "SET NAME=?, EMAIL=?,DIV_ID=?, ORG_ID=?,PATH_ID=?,"
				+ "DISABLED=?, MODIFY_TIME=? WHERE ID=?"; 
				
		Connection connection = null;
		PreparedStatement statement = null;
	    ResultSet resultSet = null;        
	    try {
    		User sso_user = (User)user;

	    	connection = getConnection();
        	statement  = connection.prepareStatement(updateSQL);

        	setUpdateUserStatement(statement, sso_user);
        	
        	statement.executeUpdate();
	    } catch (SQLException e) {
	    	throw new SCIMException("UPDATE USER FAILED : " + updateSQL , e);
		}finally {
	    	DBCP.close(connection, statement, resultSet);
	    }		
				
		return null;
	}

	private void setUpdateUserStatement(PreparedStatement statement, User sso_user) throws SQLException {
		statement.setString(1, sso_user.getUserName());
		statement.setString(2, sso_user.geteMail());
		statement.setString(3, sso_user.getOrganization());
		statement.setString(4, sso_user.getDivision());
		statement.setString(5, "NOT DEFINED-");
		statement.setBoolean(6, sso_user.isActive());
		statement.setLong(7, new Date().getTime());
		
		statement.setString(8, sso_user.getId());
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
