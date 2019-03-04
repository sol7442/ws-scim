package com.ehyundai.im;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ehyundai.object.User;
import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.obj.SCIMResource2;
import com.wowsanta.scim.obj.SCIMUser;
import com.wowsanta.scim.obj.SCIMUserMeta;
import com.wowsanta.scim.repo.rdb.AbstractRDBRepository;
import com.wowsanta.scim.repo.rdb.DBCP;
import com.wowsanta.scim.resource.SCIMAuditData;
import com.wowsanta.scim.resource.SCIMGroup;
import com.wowsanta.scim.resource.SCIMServerResourceRepository;
import com.wowsanta.scim.schema.SCIMDefinitions;
import com.wowsanta.scim.schema.SCIMErrorCode;
import com.wowsanta.scim.schema.SCIMResourceTypeSchema;

public class IMResourceRepository extends AbstractRDBRepository implements SCIMServerResourceRepository{

	@Override
	public void setUserSchema(SCIMResourceTypeSchema userSchema) {
		// TODO Auto-generated method stub
		
	}
	
	
	@Override
	public List<SCIMUser> getSystemUsersBysystemIdWidthPage(String systemId)throws SCIMException{
		
		final String selectSQL = "SELECT * FROM SCIM_SYSTEM_USER WHERE systemId=? ";
		
		Connection connection = null;
		PreparedStatement statement = null;
	    ResultSet resultSet = null;              

	    List<SCIMUser> user_list = new ArrayList<SCIMUser>();
	    
        try {
        	connection = getConnection();
        	statement  = connection.prepareStatement(selectSQL);
        	
        	statement.setString(1, systemId);
        	
        	resultSet = statement.executeQuery();
        	while(resultSet.next()) {
        		user_list.add(newUser(resultSet));
        	}
        	
		} catch (SQLException e) {
			throw new SCIMException(selectSQL, e);
		}finally {
			DBCP.close(connection, statement, resultSet);
		}
        
        for (SCIMUser scimUser : user_list) {
        	User system_user = (User)scimUser;
        	setSystemUserProfileFromDB(systemId,system_user);
		}
        
		return user_list;
	}

	@Override
	public List<SCIMAuditData> getAccountHistoryByUsrIdWidthPage(String userId)throws SCIMException{
		
final String selectSQL = "SELECT * FROM SCIM_AUDIT WHERE userId=? ";
		
		Connection connection = null;
		PreparedStatement statement = null;
	    ResultSet resultSet = null;              

	    List<SCIMAuditData> audit_data_list = new ArrayList<SCIMAuditData>();
	    
        try {
        	connection = getConnection();
        	statement  = connection.prepareStatement(selectSQL);
        	
        	statement.setString(1, userId);
        	
        	resultSet = statement.executeQuery();
        	while(resultSet.next()) {
        		SCIMAuditData data = new SCIMAuditData();
        		
        		data.setAdminId(resultSet.getString("adminId"));
        		data.setSourceSystemId(resultSet.getString("sourceSystem"));
        		data.setDirectSystemId(resultSet.getString("directSystem"));
        		data.setMethod(resultSet.getString("method"));
        		data.setResult(resultSet.getString("result"));
        		data.setDetail(resultSet.getString("detail"));
        		data.setWorkDate(resultSet.getDate("workDate"));
        		
        		audit_data_list.add(data);
        		
        	}
        	
		} catch (SQLException e) {
			throw new SCIMException(selectSQL, e);
		}finally {
			DBCP.close(connection, statement, resultSet);
		}
        
        
		return audit_data_list;
		
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
	    	throw new SCIMException("CREATE USER FAILED : " + insertSQL1 , e);
		}finally {
	    	DBCP.close(connection, statement1, resultSet);
	    }

	    insertProfiles(im_user);
	    
		return im_user;
	}

	private void removeProfiles(SCIMUser user) throws SCIMException {
		
	}
	private void updateProfiles(SCIMUser user) throws SCIMException {
		
	}
	
	private void insertSystemProfiles(String systemId, User user) throws SCIMException {
		User im_user   = (User)user;
		Map<String,String> user_profile = getProfile(user);
		
		final String insertSQL2 = "INSERT INTO SCIM_SYSTEM_USER_PROFILE (systemId, userId, pkey, pvalue)"
				+ " VALUES (?,?,?,?)";
		
		Connection connection = null;
		PreparedStatement statement = null;
	    
		ResultSet resultSet = null;        
	    try {
	    	connection = getConnection();
        	statement  = connection.prepareStatement(insertSQL2);
        	
        	for(Map.Entry<String, String> entry : user_profile.entrySet()) {
        		
        		statement.setString(1, systemId);
        	    statement.setString(2, im_user.getId());
        	    statement.setString(3, entry.getKey());
        	    statement.setString(4, entry.getValue());
        	    
        	    statement.execute();
        	}
        	
	    } catch (SQLException e) {
	    	throw new SCIMException("CREATE USER FAILED : " + insertSQL2 , e);
//	    	if (e instanceof SQLIntegrityConstraintViolationException) {
//				throw new SCIMException(e.getMessage(),RESULT_DUPLICATE_ENTRY);
//			}else {
//				throw new SCIMException("CREATE USER FAILED : " + insertSQL2 , e);
//			}
		}finally {
	    	DBCP.close(connection, statement, resultSet);
	    }
		
	}
	
	private void insertProfiles(SCIMUser user) throws SCIMException {
		User im_user   = (User)user;
		Map<String,String> user_profile = getProfile(user);
	System.out.println("insert user profiel : " + user.getId());
		final String insertSQL2 = "INSERT INTO SCIM_USER_PROFILE (userId, pkey, pvalue)"
				+ " VALUES (?,?,?)";
		
		Connection connection = null;
		PreparedStatement statement = null;
	    
		ResultSet resultSet = null;        
	    try {
	    	connection = getConnection();
        	statement  = connection.prepareStatement(insertSQL2);
        	
        	for(Map.Entry<String, String> entry : user_profile.entrySet()) {
        		System.out.println("insert user profiel : " + user.getId() + " : " + entry.getKey() + " = " + entry.getValue());
        	    statement.setString(1, im_user.getId());
        	    statement.setString(2, entry.getKey());
        	    statement.setString(3, entry.getValue());
        	    
        	    statement.execute();
        	}
        	
	    } catch (SQLException e) {
	    	throw new SCIMException("CREATE USER FAILED : " + insertSQL2 , e);
//	    	if (e instanceof SQLIntegrityConstraintViolationException) {
//				throw new SCIMException(e.getMessage(),RESULT_DUPLICATE_ENTRY);
//			}else {
//				throw new SCIMException("CREATE USER FAILED : " + insertSQL2 , e);
//			}
		}finally {
	    	DBCP.close(connection, statement, resultSet);
	    }
	}
		
	private SCIMUser setProfile(SCIMUser user, Map<String, String> profile) {
		// TODO Auto-generated method stub
		return null;
	}
	private Map<String, String> getProfile(SCIMUser user) {
		User im_user = (User)user;
	
		Map<String, String> profile = new HashMap<String, String>();
		profile.put("companyCode", im_user.getCompanyCode());
		profile.put("groupCode", im_user.getGroupCode());
		profile.put("groupName", im_user.getGroupName());
		profile.put("positionCode", im_user.getPositionCode());
		profile.put("position", im_user.getPosition());
		profile.put("jobCode", im_user.getJobCode());
		profile.put("job", im_user.getJob());
		profile.put("rankCode", im_user.getRankCode());
		profile.put("rank", im_user.getRank());
		profile.put("joinDate", toString(im_user.getJoinDate()));
		profile.put("retireDate", toString(im_user.getRetireDate()));
		profile.put("eMail", im_user.geteMail());		
		profile.put("employeeNumber", im_user.getEmployeeNumber());
		profile.put("organization", im_user.getOrganization());
		profile.put("division", im_user.getDivision());
		profile.put("department", im_user.getDepartment());
		
		return profile;
	}

//	private void insertProfile(String key, String value, String userId,String version) throws SCIMException {
//		final String insertSQL2 = "INSERT INTO SCIM_USER_PROFILE (userId, pkey, pvalue, pver)"
//				+ " VALUES ("
//				+ "?,?,?,?)";
//		
//		Connection connection = null;
//		PreparedStatement statement1 = null;
//	    
//		ResultSet resultSet = null;        
//	    try {
//	    	connection = getConnection();
//        	statement1  = connection.prepareStatement(insertSQL2);
//        	
//        	statement1.setString(1, userId);
//        	statement1.setString(2, key);
//        	statement1.setString(3, value);
//        	statement1.setString(4, version);
//        	statement1.execute();
//        	
//	    } catch (SQLException e) {
//	    	if (e instanceof SQLIntegrityConstraintViolationException) {
//				throw new SCIMException(e.getMessage(),RESULT_DUPLICATE_ENTRY);
//			}else {
//				throw new SCIMException("CREATE USER FAILED : " + insertSQL2 , e);
//			}
//		}finally {
//	    	DBCP.close(connection, statement1, resultSet);
//	    }
//	}
	

	@Override
	public SCIMUser getUser(String userId) throws SCIMException {
		final String selectSQL = "SELECT * FROM SCIM_USER WHERE USERID=?";
	
		Connection connection = null;
		PreparedStatement statement = null;
	    ResultSet resultSet = null;              

	    SCIMUser user = null;
        try {
        	connection = getConnection();
        	statement  = connection.prepareStatement(selectSQL);
        	statement.setString(1, userId);
        	
        	resultSet = statement.executeQuery();
        	if(resultSet.next()) {
        		user = newUser(resultSet);
        	}
        	
		} catch (SQLException e) {
			throw new SCIMException(selectSQL, e);
		}finally {
			DBCP.close(connection, statement, resultSet);
		}
        
		return user;
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
	
	public List<SCIMUser> getSystemUserByDate(String system_id, Date from, Date to)throws SCIMException{
		final String selectSQL = "SELECT * FROM SCIM_SYSTEM_USER WHERE systemId=? AND (ModifyDate BETWEEN ? AND ?)";
		
		Connection connection = null;
		PreparedStatement statement = null;
	    ResultSet resultSet = null;              

	    List<SCIMUser> user_list = new ArrayList<SCIMUser>();
        try {
        	connection = getConnection();
        	statement  = connection.prepareStatement(selectSQL);
        	
        	statement.setString(1, system_id);
        	statement.setTimestamp(2, toSqlTimestamp(from));
        	statement.setTimestamp(3, toSqlTimestamp(to));
        	
        	resultSet = statement.executeQuery();
        	while(resultSet.next()) {
        		user_list.add(newUser(resultSet));
        	}
        	
		} catch (SQLException e) {
			throw new SCIMException(selectSQL, e);
		}finally {
			DBCP.close(connection, statement, resultSet);
		}
        
        for (SCIMUser scimUser : user_list) {
        	User system_user = (User)scimUser;
        	
        	setSystemUserProfileFromDB(system_id,system_user);
		}
        
		return user_list;
	}
	private Map<String, String> setSystemUserProfileFromDB(String system_id, SCIMUser user) throws SCIMException{
		User system_user = (User) user;
		final String selectSQL = "SELECT * FROM SCIM_SYSTEM_USER_PROFILE WHERE systemId=? AND userId=?";
		Connection connection = null;
		PreparedStatement statement = null;
	    ResultSet resultSet = null;              

	    Map<String, String> user_profile_mape = new HashMap<String, String>();
	    
        try {
        	connection = getConnection();
        	statement  = connection.prepareStatement(selectSQL);
        	
        	statement.setString(1, system_id);
        	statement.setString(2, system_user.getId());
        	resultSet = statement.executeQuery();
        	while(resultSet.next()) {
        		String key = resultSet.getString("pkey");
        		
        		if(key.equals("companyCode")) {
        			system_user.setCompanyCode(resultSet.getString("pvalue"));
        		}else if(key.equals("rankCode")) {
        			system_user.setRankCode(resultSet.getString("pvalue"));
        		}else if(key.equals("positionCode")) {
        			system_user.setPositionCode(resultSet.getString("pvalue"));
        		}else if(key.equals("jobCode")) {
        			system_user.setJobCode(resultSet.getString("pvalue"));
        		}else if(key.equals("retireDate")) {
        			system_user.setRetireDate(toDate(resultSet.getString("pvalue")));
        		}else if(key.equals("eMail")) {
        			system_user.seteMail(resultSet.getString("pvalue"));
        		}else if(key.equals("employeeNumber")) {
        			system_user.setEmployeeNumber(resultSet.getString("pvalue"));
        		}else if(key.equals("division")) {
        			system_user.setDivision(resultSet.getString("pvalue"));
        		}else if(key.equals("groupName")) {
        			system_user.setGroupName(resultSet.getString("pvalue"));
        		}else if(key.equals("joinDate")) {
        			system_user.setJoinDate(toDate(resultSet.getString("pvalue")));
        		}else if(key.equals("organization")) {
        			system_user.setOrganization(resultSet.getString("pvalue"));
        		}else if(key.equals("rank")) {
        			system_user.setRank(resultSet.getString("pvalue"));
        		}else if(key.equals("position")) {
        			system_user.setPosition(resultSet.getString("pvalue"));
        		}else if(key.equals("job")) {
        			system_user.setJob(resultSet.getString("pvalue"));
        		}else if(key.equals("department")) {
        			system_user.setDepartment(resultSet.getString("pvalue"));
        		}else if(key.equals("groupCode")) {
        			system_user.setGroupCode(resultSet.getString("pvalue"));
        		}else {
        			System.out.println("key ??" + key + ">> " + resultSet.getString("pvalue"));
        		}
        	}
        	
		} catch (SQLException e) {
			throw new SCIMException(selectSQL, e);
		}finally {
			DBCP.close(connection, statement, resultSet);
		}

        return user_profile_mape;
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
        
        
        for (SCIMUser scimUser : user_list) {
        	User system_user = (User)scimUser;
        	setUserProfileFromDB(system_user);
		}
        
        
		return user_list;
	}

	private Map<String, String> setUserProfileFromDB(SCIMUser user) throws SCIMException{
		User im_user = (User) user;
		final String selectSQL = "SELECT * FROM SCIM_USER_PROFILE WHERE userId=?";
		Connection connection = null;
		PreparedStatement statement = null;
	    ResultSet resultSet = null;              

	    Map<String, String> user_profile_mape = new HashMap<String, String>();
	    
        try {
        	connection = getConnection();
        	statement  = connection.prepareStatement(selectSQL);
        	
        	statement.setString(1, im_user.getId());
        	resultSet = statement.executeQuery();
        	
        	while(resultSet.next()) {
        		String key = resultSet.getString("pkey");
        		
        		if(key.equals("companyCode")) {
        			im_user.setCompanyCode(resultSet.getString("pvalue"));
        		}else if(key.equals("rankCode")) {
        			im_user.setRankCode(resultSet.getString("pvalue"));
        		}else if(key.equals("positionCode")) {
        			im_user.setPositionCode(resultSet.getString("pvalue"));
        		}else if(key.equals("jobCode")) {
        			im_user.setJobCode(resultSet.getString("pvalue"));
        		}else if(key.equals("retireDate")) {
        			im_user.setRetireDate(toDate(resultSet.getString("pvalue")));
        		}else if(key.equals("eMail")) {
        			im_user.seteMail(resultSet.getString("pvalue"));
        		}else if(key.equals("employeeNumber")) {
        			im_user.setEmployeeNumber(resultSet.getString("pvalue"));
        		}else if(key.equals("division")) {
        			im_user.setDivision(resultSet.getString("pvalue"));
        		}else if(key.equals("groupName")) {
        			im_user.setGroupName(resultSet.getString("pvalue"));
        		}else if(key.equals("joinDate")) {
        			im_user.setJoinDate(toDate(resultSet.getString("pvalue")));
        		}else if(key.equals("organization")) {
        			im_user.setOrganization(resultSet.getString("pvalue"));
        		}else if(key.equals("rank")) {
        			im_user.setRank(resultSet.getString("pvalue"));
        		}else if(key.equals("position")) {
        			im_user.setPosition(resultSet.getString("pvalue"));
        		}else if(key.equals("job")) {
        			im_user.setJob(resultSet.getString("pvalue"));
        		}else if(key.equals("department")) {
        			im_user.setDepartment(resultSet.getString("pvalue"));
        		}else if(key.equals("groupCode")) {
        			im_user.setGroupCode(resultSet.getString("pvalue"));
        		}else {
        			System.out.println("key ??" + key + ">> " + resultSet.getString("pvalue"));
        		}
        	}
        	
		} catch (SQLException e) {
			throw new SCIMException(selectSQL, e);
		}finally {
			DBCP.close(connection, statement, resultSet);
		}

        return user_profile_mape;
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
	public List<SCIMResource2> getUsersByWhere(String where) throws SCIMException {
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
	public void clearSystemUserProfile(String systemId) throws SCIMException {
		final String delateSQL = "DELETE FROM SCIM_SYSTEM_USER_PROFILE WHERE systemId=?";
		Connection connection = null;
		PreparedStatement statement = null;
	    ResultSet resultSet = null;        
	    try {
	    	connection = getConnection();
        	statement  = connection.prepareStatement(delateSQL);
        	
        	statement.setString(1, systemId);
        	
        	statement.executeUpdate();
	    } catch (SQLException e) {
	    	throw new SCIMException("DELETE SYSTEM USER PROFILE FAILED : " + delateSQL , e);
		}finally {
	    	DBCP.close(connection, statement, resultSet);
	    }
	}

	@Override
	public void clearUser()throws SCIMException{
		final String delateSQL = "DELETE FROM SCIM_USER WHERE userType='USER'";
				Connection connection = null;
				PreparedStatement statement = null;
			    ResultSet resultSet = null;        
			    try {
			    	connection = getConnection();
		        	statement  = connection.prepareStatement(delateSQL);
		        	
		        	statement.executeUpdate();
			    } catch (SQLException e) {
			    	throw new SCIMException("DELETE FAILED : " + delateSQL , e);
				}finally {
			    	DBCP.close(connection, statement, resultSet);
			    }
	}
	@Override
	public void clearUserProfile()throws SCIMException{
		final String delateSQL = "DELETE FROM SCIM_USER_PROFILE ";
		Connection connection = null;
		PreparedStatement statement = null;
	    ResultSet resultSet = null;        
	    try {
	    	connection = getConnection();
        	statement  = connection.prepareStatement(delateSQL);
        	
        	statement.executeUpdate();
	    } catch (SQLException e) {
	    	throw new SCIMException("DELETE FAILED : " + delateSQL , e);
		}finally {
	    	DBCP.close(connection, statement, resultSet);
	    }
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

	
	@Override
	public void createSystemDummyUser(String systemId, SCIMUser user)throws SCIMException{
		User system_user = (User)user;
		
		User existed_user = (User) getSystemUser(systemId, user.getId());
//		if(existed_user != null) {
//			System.out.println("exist ..>>" + existed_user);
//			throw new SCIMException("", SCIMErrorCode.e409, SCIMErrorCode.SCIMType.uniqueness) ;
//		}
		
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
        	statement1.setString(6, SCIMDefinitions.UserType.DummyUser.toString());
        	statement1.setBoolean(7,system_user.isActive());
        	
        	statement1.setTimestamp(8,  toSqlTimestamp(new Date()));
        	statement1.setTimestamp(9,  toSqlTimestamp(new Date()));
        	statement1.setTimestamp(10, toSqlTimestamp(system_user.getLastAccessDate()));
        	statement1.execute();
        	
	    } catch (SQLException e) {
	    	throw new SCIMException("CREATE USER FAILED : " + insertSQL1 , e);
//	    	if (e instanceof SQLIntegrityConstraintViolationException) {
//				throw new SCIMException(e.getMessage(),RESULT_DUPLICATE_ENTRY);
//			}else {
//				throw new SCIMException("CREATE USER FAILED : " + insertSQL1 , e);
//			}
		}finally {
	    	DBCP.close(connection, statement1, resultSet);
	    }
	}

	@Override
	public SCIMUser createSystemUser(String systemId, SCIMUser user) throws SCIMException {
		User system_user = (User)user;
	
		User existed_user = (User) getSystemUser(systemId, user.getId());
//		if(existed_user != null) {
//			System.out.println("exist ..>>" + existed_user);
//			throw new SCIMException("", SCIMErrorCode.e409, SCIMErrorCode.SCIMType.uniqueness) ;
//			//return existed_user;
//		}
		
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
        	
        	statement1.setTimestamp(8,  toSqlTimestamp(new Date()));
        	statement1.setTimestamp(9,  toSqlTimestamp(new Date()));
        	statement1.setTimestamp(10, toSqlTimestamp(system_user.getLastAccessDate()));
        	statement1.execute();
        	
	    } catch (SQLException e) {
	    	throw new SCIMException("CREATE USER FAILED : " + insertSQL1 , e);
//	    	if (e instanceof SQLIntegrityConstraintViolationException) {
//				throw new SCIMException(e.getMessage(),RESULT_DUPLICATE_ENTRY);
//			}else {
//				throw new SCIMException("CREATE USER FAILED : " + insertSQL1 , e);
//			}
		}finally {
	    	DBCP.close(connection, statement1, resultSet);
	    }

	    insertSystemProfiles(systemId,system_user);
	    
		return system_user;
	}
	
	@Override
	public SCIMUser getSystemUser(String systemId, String userId) throws SCIMException{
		final String selectSQL = "SELECT * FROM SCIM_SYSTEM_USER WHERE systemId=? AND userId=?";
	
		Connection connection = null;
		PreparedStatement statement = null;
	    ResultSet resultSet = null;              

	    SCIMUser system_user = null;
        try {
        	connection = getConnection();
        	statement  = connection.prepareStatement(selectSQL);
        	
        	statement.setString(1, systemId);
        	statement.setString(2, userId);
        	
        	resultSet = statement.executeQuery();
        	if(resultSet.next()) {
        		system_user = getSystemUser(resultSet);
        	}
        	
		} catch (SQLException e) {
			throw new SCIMException(selectSQL, e);
		}finally {
			DBCP.close(connection, statement, resultSet);
		}
        
		return system_user;
	}

	private SCIMUser getSystemUser(ResultSet resultSet) {
		User system_user = new User();
		try {
			system_user.setId(resultSet.getString("userId"));
			system_user.setExernalId(resultSet.getString("externalId"));
			system_user.setUserName(resultSet.getString("userName"));
			system_user.setActive(resultSet.getBoolean("active"));
			
			system_user.setMeta(new SCIMUserMeta());
			system_user.getMeta().setCreated(toJavaDate(resultSet.getTimestamp("createDate")));
    		system_user.getMeta().setLastModified(toJavaDate(resultSet.getTimestamp("modifyDate")));
    		
    		system_user.setLastAccessDate(toJavaDate(resultSet.getTimestamp("lastAccessDate")));
    		
    		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return system_user;
	}

	@Override
	public SCIMUser updateSystemUser(String systemId, SCIMUser user) throws SCIMException {
		final String updateSQL = "UPDATE SCIM_SYSTEM_USER SET userName=?, active=?, modifyDate=?, lastAccessDate=? WHERE systemId=? AND userId=?";

		User system_user = (User)user;
		Connection connection = null;
		PreparedStatement statement = null;
	    ResultSet resultSet = null;        
	    try {
	    	connection = getConnection();
        	statement  = connection.prepareStatement(updateSQL);
        	
        	statement.setString(1, system_user.getUserName());
        	statement.setInt(2, system_user.getActive());
        	
        	statement.setTimestamp(3,  toSqlTimestamp(new Date()));
        	statement.setTimestamp(4, toSqlTimestamp(system_user.getLastAccessDate()));

        	statement.setString(5, systemId);
        	statement.setString(6, system_user.getId());
        	
        	statement.executeUpdate();
        	
	    } catch (SQLException e) {
	    	throw new SCIMException("UPDATE USER FAILED : " + updateSQL , e);
		}finally {
	    	DBCP.close(connection, statement, resultSet);
	    }
				
		return system_user;
	}

	@Override
	public void deleteSystemUser(String systemId, String userId) throws SCIMException {
		// TODO Auto-generated method stub
		
	}
}
