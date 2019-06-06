package com.wowsanta.scim.repository.system.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.log.SCIMLogger;
import com.wowsanta.scim.obj.SCIMAdmin;
import com.wowsanta.scim.obj.SCIMAudit;
import com.wowsanta.scim.obj.SCIMResource2;
import com.wowsanta.scim.obj.SCIMSchedulerHistory;
import com.wowsanta.scim.obj.SCIMSystem;
import com.wowsanta.scim.obj.SCIMUser;
import com.wowsanta.scim.obj.SCIMUserMeta;
import com.wowsanta.scim.object.Resource_Object;
import com.wowsanta.scim.repo.rdb.AbstractRDBRepository;
import com.wowsanta.scim.repo.rdb.DBCP;
import com.wowsanta.scim.repository.AttributeSchema;
import com.wowsanta.scim.repository.AttributeValue;
import com.wowsanta.scim.repository.DataMapper;
import com.wowsanta.scim.repository.RepositoryException;
import com.wowsanta.scim.repository.system.SCIMProviderRepository;
import com.wowsanta.scim.repository.system.SCIMSystemRepository;
import com.wowsanta.scim.resource.SCIMAuditData;
import com.wowsanta.scim.resource.SCIMSystemColumn;
import com.wowsanta.scim.resource.user.LoginUser;
import com.wowsanta.scim.resource.user.impl.LoginUserRdbImpl;
import com.wowsanta.scim.scheduler.SCIMScheduler;
import com.wowsanta.scim.schema.SCIMDefinitions.ResoureType;
import com.wowsata.util.json.JsonException;
import com.wowsata.util.json.WowsantaJson;

public class IMSystemRepository extends AbstractRDBRepository implements SCIMSystemRepository{
	
	private transient Logger logger = LoggerFactory.getLogger(IMSystemRepository.class);
	private transient static final long serialVersionUID = 1L;
	
	public static IMSystemRepository loadFromFile(String file_name) throws JsonException{ 
		return (IMSystemRepository)WowsantaJson.loadFromFile(file_name);
	}
	
	public int getAccountCount()throws SCIMException{
		int user_count = 0;
		
		final String query_string = "SELECT COUNT(*) AS USER_COUNT FROM SCIM_USER ";
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		
		try {
			connection = getConnection();
			statement  = connection.prepareStatement(query_string);
        	resultSet = statement.executeQuery();
        	if(resultSet.next()) {
        		user_count = resultSet.getInt("USER_COUNT");
        	}
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			logger.info("query  : {}", query_string);
			logger.info("user_count  : {}", user_count);
			
			DBCP.close(connection, statement, resultSet);
		}	
		
		return user_count;
	}
	public List<Resource_Object> getAccountList()throws SCIMException{
		List<Resource_Object> resource_list = new ArrayList<Resource_Object>();
		
		final String query_string = "SELECT A.USERID, A.USERNAME, B.ACTIVE, A.USERSTATE, B.CREATEDATE, B.MODIFYDATE FROM SCIM_USER A, SCIM_USER_META B WHERE A.USERID=B.USERID";

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = getConnection();
			statement  = connection.prepareStatement(query_string);
		    
		    resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Resource_Object resource = new Resource_Object();
				resource.put("id", resultSet.getString("USERID"));
				resource.put("employeeNumber", resultSet.getString("USERID"));
				resource.put("name", resultSet.getString("USERNAME"));
				resource.put("state", resultSet.getString("USERSTATE"));
				resource.put("active", resultSet.getInt("ACTIVE"));
				resource.put("createDate", resultSet.getTimestamp("CREATEDATE"));
				resource.put("modifyDate", resultSet.getTimestamp("MODIFYDATE"));
				resource_list.add(resource);
			}

        	statement.execute();
        	statement.close();
        	
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			DBCP.close(connection, statement, resultSet);

			logger.info("query  : {}", query_string);
			logger.info("result : {}", resource_list.size());
		}
		
		return resource_list;
	};
	
	public int getSystemActiveAccountCount(String systemId)throws SCIMException{
		int user_count = 0;
		
		final String query_string = "SELECT COUNT(*) AS USER_COUNT FROM SCIM_SYSTEM_USER WHERE SYSTEMID=? AND ACTIVE=1";
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		
		try {
			connection = getConnection();
			statement  = connection.prepareStatement(query_string);
			statement.setString(1, systemId);
			
        	resultSet = statement.executeQuery();
        	if(resultSet.next()) {
        		user_count = resultSet.getInt("USER_COUNT");
        	}
			
        	
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			logger.info("query  : {}", query_string);
			logger.info("user_count  : {}", user_count);
			
			DBCP.close(connection, statement, resultSet);
		}	
		
		return user_count;
	}
	public int getSystemGhostAccountCount(String systemId)throws SCIMException{
		int user_count = 0;
		
		final String query_string = "SELECT COUNT(*) AS USER_COUNT FROM SCIM_SYSTEM_USER WHERE SYSTEMID=? AND USERSTATE='Ghost'";
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		
		try {
			connection = getConnection();
			statement  = connection.prepareStatement(query_string);
			statement.setString(1, systemId);
			
        	resultSet = statement.executeQuery();
        	if(resultSet.next()) {
        		user_count = resultSet.getInt("USER_COUNT");
        	}
			
        	
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			logger.info("query  : {}", query_string);
			logger.info("user_count  : {}", user_count);
			
			DBCP.close(connection, statement, resultSet);
		}	
		
		return user_count;
	}
	public int getSystemAccountCount(String systemId)throws SCIMException{
		int user_count = 0;
		
		final String query_string = "SELECT COUNT(*) AS USER_COUNT FROM SCIM_SYSTEM_USER WHERE SYSTEMID=? ";
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		
		try {
			connection = getConnection();
			statement  = connection.prepareStatement(query_string);
			statement.setString(1, systemId);
			
        	resultSet = statement.executeQuery();
        	if(resultSet.next()) {
        		user_count = resultSet.getInt("USER_COUNT");
        	}
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			logger.info("query  : {}", query_string);
			logger.info("user_count  : {}", user_count);
			
			DBCP.close(connection, statement, resultSet);
		}	
		
		return user_count;
	}
	public List<Resource_Object> getSystemAccountList(String systemId)throws SCIMException{
		List<Resource_Object> resource_list = new ArrayList<Resource_Object>();
		
		final String query_string = "SELECT USERID, EXTERNALID, USERNAME, ACTIVE, USERSTATE, CREATEDATE, MODIFYDATE, LASTACCESSDATE,PROVISIONDATE FROM SCIM_SYSTEM_USER WHERE SYSTEMID=?";

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = getConnection();
			statement  = connection.prepareStatement(query_string);
			statement.setString(1, systemId);
			
		    resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Resource_Object resource = new Resource_Object();
				resource.put("id", resultSet.getString("EXTERNALID"));
				resource.put("employeeNumber", resultSet.getString("USERID"));
				resource.put("name", resultSet.getString("USERNAME"));
				resource.put("state", resultSet.getString("USERSTATE"));
				resource.put("active", resultSet.getInt("ACTIVE"));
				resource.put("createDate", resultSet.getTimestamp("CREATEDATE"));
				resource.put("modifyDate", resultSet.getTimestamp("MODIFYDATE"));
				resource.put("lastAceessDate", resultSet.getTimestamp("LASTACCESSDATE"));
				resource.put("provisionDate", resultSet.getTimestamp("PROVISIONDATE"));
				
				resource_list.add(resource);
			}

        	statement.execute();
        	statement.close();
        	
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			DBCP.close(connection, statement, resultSet);

			logger.info("query  : {}", query_string);
			logger.info("result : {}", resource_list.size());
		}
		
		return resource_list;
	}
	public List<Resource_Object> getSystemAccountListByUserId(String userId) throws SCIMException{
		List<Resource_Object> resource_list = new ArrayList<Resource_Object>();
		
		final String query_string = "SELECT SYSTEMID, USERID, EXTERNALID, USERNAME, ACTIVE, USERSTATE, CREATEDATE, MODIFYDATE, LASTACCESSDATE,PROVISIONDATE FROM SCIM_SYSTEM_USER WHERE USERID=?";

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = getConnection();
			statement  = connection.prepareStatement(query_string);
			statement.setString(1, userId);
			
		    resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Resource_Object resource = new Resource_Object();
				resource.put("systemId", resultSet.getString("SYSTEMID"));
				resource.put("id", resultSet.getString("EXTERNALID"));
				resource.put("employeeNumber", resultSet.getString("USERID"));
				resource.put("name", resultSet.getString("USERNAME"));
				resource.put("state", resultSet.getString("USERSTATE"));
				resource.put("active", resultSet.getInt("ACTIVE"));
				resource.put("createDate", resultSet.getTimestamp("CREATEDATE"));
				resource.put("modifyDate", resultSet.getTimestamp("MODIFYDATE"));
				resource.put("lastAceessDate", resultSet.getTimestamp("LASTACCESSDATE"));
				resource.put("provisionDate", resultSet.getTimestamp("PROVISIONDATE"));
				
				resource_list.add(resource);
			}

        	statement.execute();
        	statement.close();
        	
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			DBCP.close(connection, statement, resultSet);

			logger.info("query  : {}", query_string);
			logger.info("result : {}", resource_list.size());
		}
		
		return resource_list;
	}
	
//	public Resource_Object getSystemUser(String systemId, String id) throws SCIMException{
//		final String selectSQL = "SELECT * FROM SCIM_SYSTEM_USER WHERE systemId=? AND userId=?";
//		
//		Connection connection = null;
//		PreparedStatement statement = null;
//	    ResultSet resultSet = null;        
//
//	    Resource_Object resource = null;
//	    
//	    try {
//        	connection = getConnection();
//        	statement  = connection.prepareStatement(selectSQL);
//        	statement.setString(1, systemId);
//        	statement.setString(2, id);
//        	
//        	resultSet = statement.executeQuery();
//        	if(resultSet.next()) {
//        		resource = new Resource_Object();
//        		resource.setId(id);
//        		resource.put("systemId"			, systemId);
//        		resource.put("externalId"		, resultSet.getString("EXTERNALID"));
//        		resource.put("name"				, resultSet.getString("USERNAME"));
//        		resource.put("type"				, resultSet.getString("USERTYPE"));
//        		resource.put("active"			, resultSet.getInt("ACTIVE"));
//        		resource.put("createDate"		, resultSet.getTimestamp("CREATEDATE"));
//        		resource.put("modifyDate"		, resultSet.getTimestamp("MODIFYDATE"));
//        		resource.put("lastAccessDate"	, resultSet.getTimestamp("LASTACCESSDATE"));
//        		resource.put("providsionDate"	, resultSet.getTimestamp("PROVISIONDATE"));
//        	}
//		} catch (SQLException e) {
//			throw new SCIMException(selectSQL, e);
//		}finally {
//			DBCP.close(connection, statement, resultSet);
//		}
//	    
//		return resource;
//	}
//	public void createSystemUser(String systemId, Resource_Object resource)throws SCIMException{
//		String insertSQL = "INSERT INTO SCIM_SYSTEM_USER ";
//		
//		StringBuffer params = new StringBuffer();
//		StringBuffer values = new StringBuffer();
//		
//		
////		
////				+ "(SYSTEMID, USERID,EXTERNALID,USERNAME,USERTYPE,ACTIVE,CREATEDATE,MODIFYDATE,LASTACCESSDATE,PROVISIONDATE)"
////				+ " VALUES (?,?,?,?,?,?,?,?,?,?)";
//		
//		Connection connection = null;
//		PreparedStatement statement = null;
//	    ResultSet resultSet = null;        
//	    try {
//	    	connection = getConnection();
//        	statement  = connection.prepareStatement(insertSQL);
//
//        	statement.setString(1, systemId);
//        	statement.setString(2, resource.getId());
//        	statement.setString(3, resource.getString("externalId"));
//        	statement.setString(4, resource.getString("name"));
//        	statement.setString(5, resource.getString("type"));
//        	statement.setInt(6, resource.getInteger("active"));
//        	statement.setTimestamp(7, resource.getTimestamp("createDate"));
//        	statement.setTimestamp(8, resource.getTimestamp("modifyDate"));
//        	statement.setTimestamp(9, resource.getTimestamp("lastAccessDate"));
//        	statement.setTimestamp(10, resource.getTimestamp("provisionDate"));
//        	
//        	statement.execute();
//	    } catch (SQLException e) {
//	    	throw new SCIMException("ADD FAILED : " + insertSQL , e);
//		}finally {
//	    	DBCP.close(connection, statement, resultSet);
//	    }
//	}
//	public void updateSystemUser(String systemId, Resource_Object resource)throws SCIMException{
////		final String updateSQL = "UPDATE SCIM_SYSTEM_USER"
////				+ " SET EXTERNALID=?, USERNAME=?, USERTYPE=?, ACTIVE=?,"
////				+ " CREATEDATE=?, MODIFYDATE=? , LASTACCESSDATE=? , PROVISIONDATE=?"
////				+ " WHERE SYSTEMID=? AND USERID=?";
////				
////		Connection connection = null;
////		PreparedStatement statement = null;
////	    ResultSet resultSet = null;        
////	    try {
////	    	connection = getConnection();
////        	statement  = connection.prepareStatement(updateSQL);
////        	
////        	statement.setString(1, admin.getAdminName());
////        	statement.setString(2, admin.getAdminType());
////        	statement.setString(3, admin.getPassword());
////        	
////        	statement.setDate(4,  toSqlDate(admin.getLoginTime()));
////        	statement.setDate(5,  toSqlDate(admin.getPwExpireTime()));
////        	
////        	statement.setString(6, admin.getAdminId());
////        	
////        	statement.executeUpdate();
////        	
////	    } catch (SQLException e) {
////	    	throw new SCIMException("UPDATE FAILED : " + updateSQL , e);
////		}finally {
////	    	DBCP.close(connection, statement, resultSet);
////	    }
//	}
	
//	
//	public int getInactiveAccountCount()throws SCIMException{
//		StringBuffer sql_buffer = new StringBuffer();
//		sql_buffer.append("SELECT count(*) as USER_COUNT FROM SCIM_USER WHERE ACTIVE=1 " );
//		
//		
//		int user_count = 0;		
//		Connection connection = null;
//		PreparedStatement statement = null;
//	    ResultSet resultSet = null;              
//
//        try {
//        	connection = getConnection();
//        	statement  = connection.prepareStatement(sql_buffer.toString());
//
//        	resultSet = statement.executeQuery();
//        	if(resultSet.next()) {
//        		user_count = resultSet.getInt("USER_COUNT");
//        	}
//        	
//		} catch (SQLException e) {
//			throw new SCIMException(sql_buffer.toString(), e);
//		}finally {
//			DBCP.close(connection, statement, resultSet);
//		}
//        
//		return user_count;
//	}
//	
//	public int getInactiveSystemAccountCount(String systemId)throws SCIMException{
//		StringBuffer sql_buffer = new StringBuffer();
//		sql_buffer.append("SELECT count(*) as USER_COUNT FROM SCIM_SYSTEM_USER WHERE ACTIVE=1 AND SYSTEMID=? " );
//		
//		
//		int user_count = 0;		
//		Connection connection = null;
//		PreparedStatement statement = null;
//	    ResultSet resultSet = null;              
//
//        try {
//        	connection = getConnection();
//        	statement  = connection.prepareStatement(sql_buffer.toString());
//        	statement.setString(1,systemId);
//        	resultSet = statement.executeQuery();
//        	if(resultSet.next()) {
//        		user_count = resultSet.getInt("USER_COUNT");
//        	}
//        	
//		} catch (SQLException e) {
//			throw new SCIMException(sql_buffer.toString(), e);
//		}finally {
//			DBCP.close(connection, statement, resultSet);
//		}
//        
//		return user_count;
//	}
//
	public int getIsolatatedAccountCount()throws SCIMException{
		StringBuffer sql_buffer = new StringBuffer();
		sql_buffer.append("SELECT count(*) as USER_COUNT FROM SCIM_USER A LEFT JOIN SCIM_SYSTEM_USER B ON A.USERID = B.USERID WHERE B.USERID IS NULL");
		
		int user_count = 0;		
		Connection connection = null;
		PreparedStatement statement = null;
	    ResultSet resultSet = null;              

        try {
        	connection = getConnection();
        	statement  = connection.prepareStatement(sql_buffer.toString());

        	resultSet = statement.executeQuery();
        	if(resultSet.next()) {
        		user_count = resultSet.getInt("USER_COUNT");
        	}
        	
		} catch (SQLException e) {
			throw new SCIMException(sql_buffer.toString(), e);
		}finally {
			DBCP.close(connection, statement, resultSet);
		}
        
		return user_count;
	}
//	
//	public int getGhostSysAccountCount(String systemId)throws SCIMException{
//		StringBuffer sql_buffer = new StringBuffer();
//		sql_buffer.append("SELECT count(*) as USER_COUNT FROM SCIM_USER A RIGHT JOIN SCIM_SYSTEM_USER B ON A.USERID = B.USERID WHERE A.USERID IS NULL AND B.SYSTEMID=?");
//		//
//		int user_count = 0;		
//		Connection connection = null;
//		PreparedStatement statement = null;
//	    ResultSet resultSet = null;              
//
//        try {
//        	connection = getConnection();
//        	statement  = connection.prepareStatement(sql_buffer.toString());
//        	statement.setString(1,systemId);
//        	
//        	resultSet = statement.executeQuery();
//        	if(resultSet.next()) {
//        		user_count = resultSet.getInt("USER_COUNT");
//        	}
//        	
//		} catch (SQLException e) {
//			throw new SCIMException(sql_buffer.toString(), e);
//		}finally {
//			DBCP.close(connection, statement, resultSet);
//		}
//        
//		return user_count;
//	}
//
//	
//	public int getTotoalSystemAccountCount(String systemId) throws SCIMException{
//		StringBuffer sql_buffer = new StringBuffer();
//		sql_buffer.append("SELECT count(*) as USER_COUNT FROM SCIM_SYSTEM_USER WHERE SYSTEMID=?");
//		
//		int user_count = 0;		
//		Connection connection = null;
//		PreparedStatement statement = null;
//	    ResultSet resultSet = null;              
//
//        try {
//        	connection = getConnection();
//        	statement  = connection.prepareStatement(sql_buffer.toString());
//        	statement.setString(1,systemId);
//        	
//        	resultSet = statement.executeQuery();
//        	if(resultSet.next()) {
//        		user_count = resultSet.getInt("USER_COUNT");
//        	}
//        	
//		} catch (SQLException e) {
//			throw new SCIMException(sql_buffer.toString(), e);
//		}finally {
//			DBCP.close(connection, statement, resultSet);
//		}
//        
//		return user_count;
//	}
//	public int getTotoalAccountCount()throws SCIMException{
//		StringBuffer sql_buffer = new StringBuffer();
//		sql_buffer.append("SELECT count(*) as USER_COUNT FROM SCIM_USER");
//		
//		int user_count = 0;		
//		Connection connection = null;
//		PreparedStatement statement = null;
//	    ResultSet resultSet = null;              
//
//        try {
//        	connection = getConnection();
//        	statement  = connection.prepareStatement(sql_buffer.toString());
//
//        	resultSet = statement.executeQuery();
//        	if(resultSet.next()) {
//        		user_count = resultSet.getInt("USER_COUNT");
//        	}
//        	
//		} catch (SQLException e) {
//			throw new SCIMException(sql_buffer.toString(), e);
//		}finally {
//			DBCP.close(connection, statement, resultSet);
//		}
//        
//		return user_count;
//	}
	
	//public int getInactiveSystemAccountCount()throws SCIMException;
//	public List<SCIMResource2> getUserSystemList(String userId) throws SCIMException{
//		final String selectSQL = "SELECT * FROM SCIM_SYSTEM_USER WHERE USERID=?";
//		
//		Connection connection = null;
//		PreparedStatement statement = null;
//	    ResultSet resultSet = null;              
//
//	    List<SCIMResource2> user_list = new ArrayList<SCIMResource2>();
//	    
//        try {
//        	connection = getConnection();
//        	statement  = connection.prepareStatement(selectSQL);
//        	
//        	statement.setString(1, userId);
//
//        	resultSet = statement.executeQuery();
//        	while(resultSet.next()) {
//        		
//        		SysUser user = (SysUser) newSysUserFromDB(resultSet);
//        		user_list.add(user);
//        	}
//        	
//		} catch (SQLException e) {
//			throw new SCIMException(selectSQL, e);
//		}finally {
//			DBCP.close(connection, statement, resultSet);
//		}
//        
//        
//        return user_list;
//	}
//
//	private SysUser newSysUserFromDB(ResultSet resultSet) {
//
//		SysUser sys_user = new SysUser();
//		try {
//			sys_user.setSystemId(resultSet.getString("SYSTEMID"));
//			sys_user.setId(resultSet.getString("userId"));
//    		
//			sys_user.setEmployeeNumber(resultSet.getString("userId"));
//			sys_user.setUserName(resultSet.getString("userName"));
//			sys_user.setActive(resultSet.getBoolean("active"));
//    		
//			sys_user.setCreated(toJavaDate(resultSet.getTimestamp("createDate")));
//			sys_user.setLastModified(toJavaDate(resultSet.getTimestamp("modifyDate")));
//			sys_user.setProvisionDate(toJavaDate(resultSet.getTimestamp("provisionDate")));
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		
//		return sys_user;
//	}

//	public List<SCIMResource2> getSystemUsersByWhere(String systemId, String where, String order, int start_index, int end_index) throws SCIMException{
//String table_name = "SCIM_SYSTEM_USER";
//		
//		StringBuffer query_buffer = new StringBuffer();
//		query_buffer.append("SELECT * FROM ( SELECT ROWNUM AS RNUM, Z.* FROM ( SELECT * FROM ");
//		query_buffer.append(table_name);
//		
//		query_buffer.append(" WHERE ").append("SYSTEMID=?");
//		
//		if(where != null && where.length() > 1) {
//			query_buffer.append(" AND ").append(where);
//		}
//		
//		if(order != null && order.length() > 1) {
//			query_buffer.append(" ORDER BY ").append(order);
//		}
//		query_buffer.append(" ) Z WHERE ROWNUM <= ? ) WHERE RNUM >= ?");
//		
//		
//		Connection connection = null;
//		PreparedStatement statement = null;
//	    ResultSet resultSet = null;              
//
//	    List<SCIMResource2> user_list = new ArrayList<SCIMResource2>();
//	    
//        try {
//        	connection = getConnection();
//        	statement  = connection.prepareStatement(query_buffer.toString());
//        	
//        	statement.setString(1, systemId);
//        	statement.setInt(2, end_index);
//        	statement.setInt(3, start_index);
//
//        	resultSet = statement.executeQuery();
//        	while(resultSet.next()) {
//        		user_list.add(newResourceFromDB(resultSet));
//        	}
//        	
//		} catch (SQLException e) {
//			throw new SCIMException(query_buffer.toString(), e);
//		}finally {
//			DBCP.close(connection, statement, resultSet);
//		}
//        
//        for (SCIMResource2 scimUser : user_list) {
//        	setUserProfileFromDB(scimUser);
//		}
//        
//        return user_list;
//	}
//	public List<SCIMResource2> getUsersByWhere(String where, String order, int start_index, int end_index)throws SCIMException{
//
//		String table_name = "SCIM_USER";
//		
//		StringBuffer query_buffer = new StringBuffer();
//		query_buffer.append("SELECT * FROM ( SELECT ROWNUM AS RNUM, Z.* FROM ( SELECT * FROM ");
//		query_buffer.append(table_name);
//		if(where != null && where.length() > 1) {
//			query_buffer.append(" WHERE ").append(where);
//		}
//		if(order != null && order.length() > 1) {
//			query_buffer.append(" ORDER BY ").append(order);
//		}
//		query_buffer.append(" ) Z WHERE ROWNUM <= ? ) WHERE RNUM >= ?");
//		
//		
//		Connection connection = null;
//		PreparedStatement statement = null;
//	    ResultSet resultSet = null;              
//
//	    List<SCIMResource2> user_list = new ArrayList<SCIMResource2>();
//	    
//        try {
//        	connection = getConnection();
//        	statement  = connection.prepareStatement(query_buffer.toString());
//        	
//        	statement.setInt(1, end_index);
//        	statement.setInt(2, start_index);
//
//        	resultSet = statement.executeQuery();
//        	while(resultSet.next()) {
//        		user_list.add(newResourceFromDB(resultSet));
//        	}
//        	
//		} catch (SQLException e) {
//			throw new SCIMException(query_buffer.toString(), e);
//		}finally {
//			DBCP.close(connection, statement, resultSet);
//		}
//        
//        for (SCIMResource2 scimUser : user_list) {
//        	setUserProfileFromDB(scimUser);
//		}
//        
//        return user_list;
//	}
//	
//	private Map<String, String> setUserProfileFromDB(SCIMResource2 user) throws SCIMException{
//		Resource im_user = (Resource) user;
//		final String selectSQL = "SELECT * FROM SCIM_USER_PROFILE WHERE userId=?";
//		Connection connection = null;
//		PreparedStatement statement = null;
//	    ResultSet resultSet = null;              
//
//	    Map<String, String> user_profile_mape = new HashMap<String, String>();
//	    
//        try {
//        	connection = getConnection();
//        	statement  = connection.prepareStatement(selectSQL);
//        	
//        	statement.setString(1, im_user.getId());
//        	resultSet = statement.executeQuery();
//        	
//        	while(resultSet.next()) {
//        		String key = resultSet.getString("pkey");
//        		if(key.equals("companyCode")) {
//        			im_user.setCompanyCode(resultSet.getString("pvalue"));
//        		}else if(key.equals("rankCode")) {
//        			im_user.setRankCode(resultSet.getString("pvalue"));
//        		}else if(key.equals("positionCode")) {
//        			im_user.setPositionCode(resultSet.getString("pvalue"));
//        		}else if(key.equals("jobCode")) {
//        			im_user.setJobCode(resultSet.getString("pvalue"));
//        		}else if(key.equals("retireDate")) {
//        			im_user.setRetireDate(toDate(resultSet.getString("pvalue")));
//        		}else if(key.equals("employeeNumber")) {
//        			im_user.setEmployeeNumber(resultSet.getString("pvalue"));
//        		}else if(key.equals("division")) {
//        			im_user.setDivision(resultSet.getString("pvalue"));
//        		}else if(key.equals("groupName")) {
//        			im_user.setGroupName(resultSet.getString("pvalue"));
//        		}else if(key.equals("joinDate")) {
//        			im_user.setJoinDate(toDate(resultSet.getString("pvalue")));
//        		}else if(key.equals("organization")) {
//        			im_user.setOrganization(resultSet.getString("pvalue"));
//        		}else if(key.equals("rank")) {
//        			im_user.setRank(resultSet.getString("pvalue"));
//        		}else if(key.equals("position")) {
//        			im_user.setPosition(resultSet.getString("pvalue"));
//        		}else if(key.equals("job")) {
//        			im_user.setJob(resultSet.getString("pvalue"));
//        		}else if(key.equals("department")) {
//        			im_user.setDepartment(resultSet.getString("pvalue"));
//        		}else if(key.equals("groupCode")) {
//        			im_user.setGroupCode(resultSet.getString("pvalue"));
//        		}else {
//        			System.out.println("key ??" + key + ">> " + resultSet.getString("pvalue"));
//        		}
//        	}
//        	
//		} catch (SQLException e) {
//			throw new SCIMException(selectSQL, e);
//		}finally {
//			DBCP.close(connection, statement, resultSet);
//		}
//
//        return user_profile_mape;
//	}
//	private SCIMResource2 newResourceFromDB(ResultSet resultSet) {
//		Resource im_user = new Resource();
//		try {
//			
//    		im_user.setId(resultSet.getString("userId"));
//    		
//    		im_user.setEmployeeNumber(resultSet.getString("userId"));
//    		im_user.setUserName(resultSet.getString("userName"));
//    		im_user.setActive(resultSet.getBoolean("active"));
//    		
//    		im_user.setCreated(toJavaDate(resultSet.getTimestamp("createDate")));
//    		im_user.setLastModified(toJavaDate(resultSet.getTimestamp("modifyDate")));
//    		
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		
//		return im_user;
//	}
	
//	public boolean validate() throws SCIMException {
//		final String selectSQL = this.dbcp.getValiationQuery();//
//		Connection connection = null;
//		PreparedStatement statement = null;
//	    ResultSet resultSet = null;        
//
//        try {
//        	connection = getConnection();
//        	statement  = connection.prepareStatement(selectSQL);
//        	resultSet = statement.executeQuery();
//        	
//        	while(resultSet.next()) {
//        		logger.info("validate result : {} " ,  resultSet.getInt("count(*)"));
//        	}
//		} catch (SQLException e) {
//			throw new SCIMException(selectSQL, e);
//		}finally {
//			DBCP.close(connection, statement, resultSet);
//		}
//        logger.info("REPOSITORY VAILDATE : {} ", selectSQL);	
//		return true;
//	}
	
	@Override
	public SCIMAdmin getAdmin(String adminId)throws SCIMException{
		final String selectSQL = "SELECT * FROM SCIM_ADMIN WHERE ADMINID=?";
		
		Connection connection = null;
		PreparedStatement statement = null;
	    ResultSet resultSet = null;        

	    SCIMAdmin admin = null;
        try {
        	connection = getConnection();
        	statement  = connection.prepareStatement(selectSQL);
        	statement.setString(1, adminId);
        	
        	resultSet = statement.executeQuery();
        	if(resultSet.next()) {
        		admin = newAdminFromDB(resultSet);
        	}
		} catch (SQLException e) {
			throw new SCIMException(selectSQL, e);
		}finally {
			DBCP.close(connection, statement, resultSet);
		}
		
		return admin;
	}
	private SCIMAdmin newAdminFromDB(ResultSet resultSet) throws SQLException {
		SCIMAdmin admin = new SCIMAdmin();
		
		admin.setAdminId(resultSet.getString("ADMINID"));
		admin.setAdminName(resultSet.getString("ADMINNAME"));
		admin.setAdminType(resultSet.getString("ADMINTYPE"));
		admin.setState(resultSet.getInt("ACTIVE"));
		admin.setPassword(resultSet.getString("PASSWD"));
		admin.setLoginTime(toJavaDate(resultSet.getDate("LOGINTIME")));
		admin.setPwExpireTime(toJavaDate(resultSet.getDate("PWEXPIRETIME")));
		
		return admin;
	}
	@Override
	public SCIMAdmin createAdmin(SCIMAdmin admin) throws SCIMException{
		final String insertSQL = "INSERT INTO SCIM_ADMIN "
				+ "(ADMINID, ADMINNAME,ADMINTYPE,PASSWD,PWEXPIRETIME)"
				+ " VALUES (?,?,?,?,?)";
		
		Connection connection = null;
		PreparedStatement statement = null;
	    ResultSet resultSet = null;        
	    try {
	    	connection = getConnection();
        	statement  = connection.prepareStatement(insertSQL);

        	statement.setString(1, admin.getAdminId());
        	statement.setString(2, admin.getAdminName());
        	statement.setString(3, admin.getAdminType());
        	statement.setString(4, admin.getPassword());
        	statement.setDate(5, toSqlDate(admin.getPwExpireTime()));
        	
        	statement.execute();
	    } catch (SQLException e) {
	    	throw new SCIMException("ADD DATA FAILED : " + insertSQL , e);
		}finally {
	    	DBCP.close(connection, statement, resultSet);
	    }
		
		return admin;
	}
	@Override
	public SCIMAdmin updateAdmin(SCIMAdmin admin)throws SCIMException{
		final String updateSQL = "UPDATE SCIM_ADMIN"
				+ " SET ADMINNAME=?, ADMINTYPE=?, PASSWD=?, LOGINTIME=?, PWEXPIRETIME=? "
				+ " WHERE ADMINID=?";

		
		Connection connection = null;
		PreparedStatement statement = null;
	    ResultSet resultSet = null;        
	    try {
	    	connection = getConnection();
        	statement  = connection.prepareStatement(updateSQL);
        	
        	statement.setString(1, admin.getAdminName());
        	statement.setString(2, admin.getAdminType());
        	statement.setString(3, admin.getPassword());
        	
        	statement.setDate(4,  toSqlDate(admin.getLoginTime()));
        	statement.setDate(5,  toSqlDate(admin.getPwExpireTime()));
        	
        	statement.setString(6, admin.getAdminId());
        	
        	statement.executeUpdate();
        	
	    } catch (SQLException e) {
	    	throw new SCIMException("UPDATE FAILED : " + updateSQL , e);
		}finally {
	    	DBCP.close(connection, statement, resultSet);
	    }
		
		return null;
	}
	@Override
	public void deleteAdmin(String adminId)throws SCIMException{
		final String delateSQL = "DELETE FROM SCIM_ADMIN WHERE ADMINID=?";
		Connection connection = null;
		PreparedStatement statement = null;
	    ResultSet resultSet = null;        
	    try {
	    	connection = getConnection();
        	statement  = connection.prepareStatement(delateSQL);
        	
        	statement.setString(1, adminId);
        	
        	statement.executeUpdate();
	    } catch (SQLException e) {
	    	throw new SCIMException("DELETE FAILED : " + delateSQL , e);
		}finally {
	    	DBCP.close(connection, statement, resultSet);
	    }
	}
	@Override
	public List<SCIMAdmin> getAdminList()throws SCIMException{
		
		final String selectSQL = "SELECT * FROM SCIM_ADMIN";
		
		Connection connection = null;
		PreparedStatement statement = null;
	    ResultSet resultSet = null;        

	    List<SCIMAdmin> admin_list = new ArrayList<SCIMAdmin>();
        try {
        	connection = getConnection();
        	statement  = connection.prepareStatement(selectSQL);
        	resultSet = statement.executeQuery();
        	while(resultSet.next()) {
        		SCIMAdmin admin = newAdminFromDB(resultSet);
        		admin_list.add(admin);
        	}
		} catch (SQLException e) {
			throw new SCIMException(selectSQL, e);
		}finally {
			DBCP.close(connection, statement, resultSet);
		}
        
		return admin_list;
	}
	
	@Override
	public List<SCIMSystemColumn> getSystemColumnsBySystemId(String systemId) throws SCIMException {
		final String selectSQL = "SELECT * FROM SCIM_SYSTEM_COLUMN WHERE systemId=?";
		
		Connection connection = null;
		PreparedStatement statement = null;
	    ResultSet resultSet = null;        

	    List<SCIMSystemColumn> column_list = new ArrayList<SCIMSystemColumn>();
        try {
        	connection = getConnection();
        	statement  = connection.prepareStatement(selectSQL);
        	statement.setString(1, systemId);
        	
        	resultSet = statement.executeQuery();
        	while(resultSet.next()) {
        		SCIMSystemColumn column = new SCIMSystemColumn();
        		column.setSystemId(systemId);
        	
        		column.setColumnName(	resultSet.getString("columnName"));
        		column.setAllowNull(	toBoolean(resultSet.getInt("allowNull")));
        		column.setComment(		resultSet.getString("COLUMNCOMMENT"));
        		column.setDataSize(		resultSet.getInt("dataSize"));
        		column.setDataType(		resultSet.getString("dataType"));
        		column.setDefaultValue(	resultSet.getString("defaultValue"));
        		column.setDisplayName(	resultSet.getString("displayName"));
        		column.setMappingColumn(resultSet.getString("mappingColumn"));
        		
        		column_list.add(column);
        	}
		} catch (SQLException e) {
			throw new SCIMException(selectSQL, e);
		}finally {
			DBCP.close(connection, statement, resultSet);
		}
		return column_list;
	}
	
	@Override
	public void addSystemColumn(SCIMSystemColumn scimSystemColumn)throws SCIMException{
		final String insertSQL = "INSERT INTO SCIM_SYSTEM_COLUMN "
				+ "(systemId, columnName,displayName,"
				+ "dataType,dataSize,allowNull,"
				+ "defaultValue,COLUMNCOMMENT,mappingColumn)"
				+ " VALUES ("
				+ "?,?,?,"
				+ "?,?,?,"
				+ "?,?,?)";
		
		Connection connection = null;
		PreparedStatement statement = null;
	    ResultSet resultSet = null;        
	    try {
	    	connection = getConnection();
        	statement  = connection.prepareStatement(insertSQL);

        	statement.setString(1, scimSystemColumn.getSystemId());
        	statement.setString(2, scimSystemColumn.getColumnName());
        	statement.setString(3, scimSystemColumn.getDisplayName());
        	statement.setString(4, scimSystemColumn.getDataType());
        	statement.setInt(5, scimSystemColumn.getDataSize());
        	statement.setInt(6, toInteger(scimSystemColumn.isAllowNull()));
        	statement.setString(7, scimSystemColumn.getDefaultValue());
        	statement.setString(8, scimSystemColumn.getComment());
        	statement.setString(9, scimSystemColumn.getMappingColumn());
        	
        	statement.execute();
	    } catch (SQLException e) {
	    	throw new SCIMException("ADD DATA FAILED : " + insertSQL , e);
		}finally {
	    	DBCP.close(connection, statement, resultSet);
	    }
	}
	
	@Override
	public SCIMScheduler getSchdulerById(String schedulerId) throws SCIMException{
		final String selectSQL = "SELECT * FROM SCIM_SCHEDULER WHERE schedulerId=?";
		
		Connection connection = null;
		PreparedStatement statement = null;
	    ResultSet resultSet = null;        

	    SCIMScheduler scheduler =  null;
        try {
        	connection = getConnection();
        	statement  = connection.prepareStatement(selectSQL);
        	statement.setString(1, schedulerId);
        	
        	resultSet = statement.executeQuery();
        	if(resultSet.next()) {
        		scheduler = newScheduler(resultSet);
        	}
		} catch (SQLException e) {
			throw new SCIMException(selectSQL, e);
		}finally {
			DBCP.close(connection, statement, resultSet);
		}
		return scheduler;
	};
	
	private SCIMScheduler newScheduler(ResultSet resultSet) {
		SCIMScheduler scheduler = new SCIMScheduler();
		try {
		
			scheduler.setSchedulerId(	resultSet.getString("schedulerId"));
			scheduler.setSchedulerName(	resultSet.getString("schedulerName"));
			scheduler.setSchedulerDesc(	resultSet.getString("schedulerDesc"));
			scheduler.setSchedulerType(	resultSet.getString("schedulerType"));
			scheduler.setJobClass(		resultSet.getString("jobClass"));
			scheduler.setTriggerType(	resultSet.getString("triggerType"));
			scheduler.setDayOfMonth(	resultSet.getInt("dayOfMonth"));
			scheduler.setDayOfWeek(		resultSet.getInt("dayOfWeek"));
			scheduler.setHourOfDay(		resultSet.getInt("hourOfDay"));
			scheduler.setMinuteOfHour(	resultSet.getInt("minuteOfHour"));
			scheduler.setExecuteSystemId(resultSet.getString("executeSystemId"));
			scheduler.setSourceSystemId(resultSet.getString("sourceSystemId"));
			scheduler.setTargetSystemId(resultSet.getString("targetSystemId"));
			scheduler.setEncode(resultSet.getString("encode"));
			scheduler.setLastExecuteDate(toJavaDate (resultSet.getTimestamp("lastExecuteDate")));
			
		}catch(Exception e) {
			e.printStackTrace();
			new SCIMException(e.getMessage(), e);
		}
		return scheduler;
	}

	@Override
	public void addAudit(SCIMAudit audit)throws SCIMException{
		final String insertSQL = "INSERT INTO SCIM_AUDIT "
			+ "(workId, workerId,workerType, resourceType,resourceId, sourceSystemId, targetSystemId, action, method, result, detail, workDate)"
			+ " VALUES ("
			+ "?,?,?,?,?,?,"
			+ "?,?,?,?,?,?)";
			
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;        
		try {
			connection = getConnection();
			statement  = connection.prepareStatement(insertSQL);
		
			statement.setString(1, audit.getWorkId());
			statement.setString(2, audit.getWorkerId());
			statement.setString(3, audit.getWorkerType());
			statement.setString(4, audit.getResourceType().toString());
			statement.setString(5, audit.getResourceId());
			statement.setString(6, audit.getSourceSystemId());
			statement.setString(7, audit.getTargetSystemId());
			statement.setString(8, audit.getAction());
			statement.setString(9, audit.getMethod());
			statement.setString(10, audit.getResult());
			statement.setString(11, audit.getDetail());
			statement.setDate(12, toSqlDate(audit.getWorkDate()));
			
			
			statement.execute();
		} catch (SQLException e) {
			throw new SCIMException("ADD DATA FAILED : " + insertSQL , e);
		}finally {
			DBCP.close(connection, statement, resultSet);
		}
	}
	@Override
	public void addSchedulerHistory(SCIMSchedulerHistory history)throws SCIMException{
		final String insertSQL = "INSERT INTO SCIM_SCHEDULER_HISTORY "
				+ "(schedulerId, workId, workerId,successCount, failCount, workDate)"
				+ " VALUES (?,?,?,?,?,?)";
				
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;        
		try {
			connection = getConnection();
			statement  = connection.prepareStatement(insertSQL);
		
			statement.setString(1, history.getSchedulerId());
			statement.setString(2, history.getWorkId());
			statement.setString(3, history.getWorkerId());
			statement.setInt(4, history.getSuccessCount());
			statement.setInt(5, history.getFailCount());
			statement.setDate(6, toSqlDate(history.getWorkDate()));
			
			statement.execute();
		} catch (SQLException e) {
			throw new SCIMException("ADD DATA FAILED : " + insertSQL , e);
		}finally {
			DBCP.close(connection, statement, resultSet);
		}
	}
	
	
	public List<SCIMAudit> getSystemAccountHistory(String systemId, String userId)throws SCIMException{
		final String selectSQL = "SELECT * FROM SCIM_AUDIT WHERE TARGETSYSTEMID=? AND RESOURCEID=? ORDER BY WORKDATE DESC";
		
		Connection connection = null;
		PreparedStatement statement = null;
	    ResultSet resultSet = null;        

	    List<SCIMAudit> audit_list = new ArrayList<SCIMAudit>();
        try {
        	connection = getConnection();
        	statement  = connection.prepareStatement(selectSQL);
        	
        	statement.setString(1, systemId);
        	statement.setString(2, userId);
        	
        	resultSet = statement.executeQuery();
        	while(resultSet.next()) {
        		audit_list.add(newAuditFromDB(resultSet));
        	}
        	
		} catch (SQLException e) {
			throw new SCIMException(selectSQL, e);
		}finally {
			DBCP.close(connection, statement, resultSet);
		}
		
		return audit_list;
	}
	
	@Override
	public List<SCIMAudit> findAuditByUserId(String userId)throws SCIMException{
		final String selectSQL = " SELECT * FROM SCIM_AUDIT WHERE userId=? Order by WorkDate Desc";
		
		Connection connection = null;
		PreparedStatement statement = null;
	    ResultSet resultSet = null;        

	    List<SCIMAudit> audit_list = new ArrayList<SCIMAudit>();
        try {
        	connection = getConnection();
        	statement  = connection.prepareStatement(selectSQL);
        	
        	statement.setString(1, userId);
        	
        	resultSet = statement.executeQuery();
        	while(resultSet.next()) {
        		audit_list.add(newAuditFromDB(resultSet));
        	}
        	
		} catch (SQLException e) {
			throw new SCIMException(selectSQL, e);
		}finally {
			DBCP.close(connection, statement, resultSet);
		}
		
		return audit_list;
	}
	private SCIMAudit newAuditFromDB(ResultSet resultSet) throws SQLException {
		SCIMAudit audit = new SCIMAudit();
		
		audit.setWorkId(resultSet.getString("workId"));
		audit.setWorkerId(resultSet.getString("workerId"));
		audit.setWorkerType(resultSet.getString("workerType"));
		audit.setResourceType(ResoureType.valueOf(resultSet.getString("resourceType")));
		audit.setResourceId(resultSet.getString("resourceId"));
		audit.setSourceSystemId(resultSet.getString("sourceSystemId"));
		audit.setTargetSystemId(resultSet.getString("targetSystemId"));
		audit.setMethod(resultSet.getString("method"));
		audit.setDetail(resultSet.getString("detail"));
		audit.setWorkDate(toJavaDate (resultSet.getDate("workDate")));
		audit.setAction(resultSet.getString("action"));
		audit.setResult(resultSet.getString("result"));
		
		return audit;
	}
	
	@Override
	public List<SCIMAudit> findAuditByWorkId(String workId)throws SCIMException{
		final String selectSQL = " SELECT * FROM SCIM_AUDIT WHERE workId=? Order by WorkDate Desc";
		
		Connection connection = null;
		PreparedStatement statement = null;
	    ResultSet resultSet = null;        

	    List<SCIMAudit> audit_list = new ArrayList<SCIMAudit>();
        try {
        	connection = getConnection();
        	statement  = connection.prepareStatement(selectSQL);
        	
        	statement.setString(1, workId);
        	
        	resultSet = statement.executeQuery();
        	while(resultSet.next()) {
        		audit_list.add(newAuditFromDB(resultSet));
        	}
        	
		} catch (SQLException e) {
			throw new SCIMException(selectSQL, e);
		}finally {
			DBCP.close(connection, statement, resultSet);
		}
		
		return audit_list;
	}
	
	@Override
	public List<SCIMSchedulerHistory> getSchedulerHistoryById(String schedulerId)throws SCIMException{
		final String selectSQL = " SELECT * FROM SCIM_SCHEDULER_HISTORY WHERE schedulerId=? Order by workDate Desc";
		
		Connection connection = null;
		PreparedStatement statement = null;
	    ResultSet resultSet = null;        

	    List<SCIMSchedulerHistory> history_list = new ArrayList<SCIMSchedulerHistory>();
        try {
        	connection = getConnection();
        	statement  = connection.prepareStatement(selectSQL);
        	
        	statement.setString(1, schedulerId);
        	
        	resultSet = statement.executeQuery();
        	while(resultSet.next()) {
        		SCIMSchedulerHistory scheduler_history = new SCIMSchedulerHistory();
        		
        		scheduler_history.setSchedulerId(schedulerId);
        		scheduler_history.setWorkId(resultSet.getString("workId"));
        		scheduler_history.setWorkerId(	resultSet.getString("workerId"));
        		scheduler_history.setSuccessCount(	resultSet.getInt("successCount"));
        		scheduler_history.setFailCount(resultSet.getInt("failCount"));
        		scheduler_history.setWorkDate( 	toJavaDate (resultSet.getDate("workDate")));
        		
        		history_list.add(scheduler_history);
        	}
		} catch (SQLException e) {
			throw new SCIMException(selectSQL, e);
		}finally {
			DBCP.close(connection, statement, resultSet);
		}
		return history_list;
	}
	
	@Override
	public List<SCIMSchedulerHistory> getSchedulerHistory(String schedulerId)throws SCIMException{
		return null;
	}
	
	
	public void createScheduler(SCIMScheduler scheduler)throws SCIMException{
		StringBuffer query_buffer = new StringBuffer();
		query_buffer.append("INSERT INTO SCIM_SCHEDULER ");
		query_buffer.append("(SCHEDULERID, SCHEDULERNAME, SCHEDULERTYPE, SCHEDULERDESC");
		query_buffer.append(",JOBCLASS, TRIGGERTYPE, DAYOFMONTH, DAYOFWEEK, HOUROFDAY, MINUTEOFHOUR");
		query_buffer.append(",EXECUTESYSTEMID, SOURCESYSTEMID,TARGETSYSTEMID,ENCODE)");
		
		query_buffer.append(" VALUES (?,?,?,?");
		query_buffer.append(" ,?,?,?,?,?,?");
		query_buffer.append(" ,?,?,?,?)");

		final String query_string = query_buffer.toString();
		
		Connection connection = null;
		PreparedStatement statement = null;
	    ResultSet resultSet = null;     
	    int result = 0;
	    try {
	    	connection = getConnection();
        	statement  = connection.prepareStatement(query_string);

        	statement.setString(1, scheduler.getSchedulerId());
        	statement.setString(2, scheduler.getSchedulerName());
        	statement.setString(3, scheduler.getSchedulerType());
        	statement.setString(4, scheduler.getSchedulerDesc());
        	statement.setString(5, scheduler.getJobClass());
        	statement.setString(6, scheduler.getTriggerType());
        	        	
        	statement.setInt(7, scheduler.getDayOfMonth());
        	statement.setInt(8, scheduler.getDayOfWeek());
        	statement.setInt(9, scheduler.getHourOfDay());
        	statement.setInt(10, scheduler.getMinuteOfHour());
        	
        	statement.setString(11, scheduler.getExecuteSystemId());
        	statement.setString(12, scheduler.getSourceSystemId());
        	statement.setString(13, scheduler.getTargetSystemId());
        	statement.setString(14, scheduler.getEncode());

        	result = statement.executeUpdate();
	    } catch (SQLException e) {
	    	logger.error(e.getMessage(),e);
	    	throw new SCIMException("ADD FAILED : ", e);
		}finally {
			logger.info("query : {}", query_string);
			logger.info("result : {}", result);
	    	DBCP.close(connection, statement, resultSet);
	    }
	}
	public void updateScheduler(SCIMScheduler scheduler)throws SCIMException{
		
		StringBuffer query_buffer = new StringBuffer();
		query_buffer.append("UPDATE SCIM_SCHEDULER ");
		query_buffer.append("SET SCHEDULERNAME=? , SCHEDULERTYPE=? ,SCHEDULERDESC=? ,JOBCLASS=?, TRIGGERTYPE=? ");
		query_buffer.append(", DAYOFMONTH=? , DAYOFWEEK=? ,HOUROFDAY=? ,MINUTEOFHOUR=? ");
		query_buffer.append(", EXECUTESYSTEMID=? , SOURCESYSTEMID=? ,TARGETSYSTEMID=? ,LASTEXECUTEDATE=? , ENCODE=?");
		query_buffer.append("WHERE SCHEDULERID=?");
		
		final String query_string = query_buffer.toString();

		Connection connection = null;
		PreparedStatement statement = null;
	    ResultSet resultSet = null;
	    int result = 0;
	    try {
	    	connection = getConnection();
        	statement  = connection.prepareStatement(query_string);
        	
        	statement.setString(1, scheduler.getSchedulerName());
        	statement.setString(2, scheduler.getSchedulerType());
        	statement.setString(3, scheduler.getSchedulerDesc());
        	statement.setString(4, scheduler.getJobClass());
        	statement.setString(5, scheduler.getTriggerType());
        	        	
        	statement.setInt(6, scheduler.getDayOfMonth());
        	statement.setInt(7, scheduler.getDayOfWeek());
        	statement.setInt(8, scheduler.getHourOfDay());
        	statement.setInt(9, scheduler.getMinuteOfHour());
        	
        	statement.setString(10, scheduler.getExecuteSystemId());
        	statement.setString(11, scheduler.getSourceSystemId());
        	statement.setString(12, scheduler.getTargetSystemId());
        	
        	statement.setDate(13,toSqlDate(scheduler.getLastExecuteDate()));

        	statement.setString(14, scheduler.getEncode());
        	
        	statement.setString(15, scheduler.getSchedulerId());

        	result = statement.executeUpdate();
        	
	    } catch (SQLException e) {
	    	logger.error(e.getMessage(),e);
	    	throw new SCIMException("UPDATE FAILED : ", e);
		}finally {
			logger.info("query : {},\n{}", query_string,scheduler.toString(true));
			logger.info("result : {}", result);
	    	DBCP.close(connection, statement, resultSet);
	    }
	}
	public void deleteScheduler(String schedulerId)throws SCIMException{
		StringBuffer query_buffer = new StringBuffer();
		query_buffer.append("DELETE FROM SCIM_SCHEDULER ");
		query_buffer.append("WHERE SCHEDULERID=?");
		
		final String query_string = query_buffer.toString();
		
		Connection connection = null;
		PreparedStatement statement = null;
	    ResultSet resultSet = null;  
	    int result = 0;
	    try {
	    	connection = getConnection();
        	statement  = connection.prepareStatement(query_string);
        	
        	statement.setString(1, schedulerId);
        	
        	statement.executeUpdate();
	    } catch (SQLException e) {
	    	logger.error(e.getMessage(),e);
	    	throw new SCIMException("DELETE FAILED : ", e);
		}finally {
			logger.info("query : {}", query_string);
			logger.info("result : {}", result);
	    	DBCP.close(connection, statement, resultSet);
	    }
	}
	
	@Override
	public List<SCIMScheduler> getSchdulerBySystemId(String systemId)throws SCIMException {
		
		final String selectSQL = "SELECT * FROM SCIM_SCHEDULER WHERE targetSystemId=? or sourceSystemId=?";
		
		Connection connection = null;
		PreparedStatement statement = null;
	    ResultSet resultSet = null;        

	    List<SCIMScheduler> scheduler_list = new ArrayList<SCIMScheduler>();
        try {
        	connection = getConnection();
        	statement  = connection.prepareStatement(selectSQL);
        	
        	statement.setString(1, systemId);
        	statement.setString(2, systemId);
        	
        	resultSet = statement.executeQuery();
        	while(resultSet.next()) {
        		SCIMScheduler scheduler = newScheduler(resultSet);
        		scheduler_list.add(scheduler);
        	}
		} catch (SQLException e) {
			throw new SCIMException(selectSQL, e);
		}finally {
			DBCP.close(connection, statement, resultSet);
		}
		return scheduler_list;
	}
	
	@Override
	public List<SCIMScheduler> getSchdulerAll() throws SCIMException{
		final String selectSQL = "SELECT * FROM SCIM_SCHEDULER";
		
		Connection connection = null;
		PreparedStatement statement = null;
	    ResultSet resultSet = null;        

	    List<SCIMScheduler> scheduler_list = new ArrayList<SCIMScheduler>();
        try {
        	connection = getConnection();
        	statement  = connection.prepareStatement(selectSQL);
        	resultSet = statement.executeQuery();
        	while(resultSet.next()) {
        		SCIMScheduler scheduler = newScheduler(resultSet);
        		scheduler_list.add(scheduler);
        	}
		} catch (SQLException e) {
			throw new SCIMException(selectSQL, e);
		}finally {
			DBCP.close(connection, statement, resultSet);
		}
		return scheduler_list;
	}

	@Override
	public void updateLoginTime(String userId)throws SCIMException{
		final String updateSQL = "UPDATE SCIM_ADMIN SET LOGINTIME=? WHERE ADMINID=?";

		
		Connection connection = null;
		PreparedStatement statement = null;
	    ResultSet resultSet = null;        
	    try {
	    	connection = getConnection();
        	statement  = connection.prepareStatement(updateSQL);
        	
        	statement.setDate(1,  toSqlDate(new Date()));
        	statement.setString(2, userId);
        	
        	statement.executeUpdate();
        	
	    } catch (SQLException e) {
	    	throw new SCIMException("UPDATE FAILED : " + updateSQL , e);
		}finally {
	    	DBCP.close(connection, statement, resultSet);
	    }
	};
	@Override
	public LoginUser getLoginUser(String id) throws SCIMException {
		//final String selectSQL = "SELECT A.adminId, B.userName,  A.adminType, A.adminPw FROM SCIM_ADMIN A, SCIM_USER B WHERE A.adminId = B.userId AND A.adminId = ?";
		final String selectSQL = "SELECT * FROM SCIM_ADMIN WHERE ADMINID=?";
				
		Connection connection = null;
		PreparedStatement statement = null;
	    ResultSet resultSet = null;        

	    LoginUserRdbImpl login_user = null;
        try {
        	connection = getConnection();
        	statement  = connection.prepareStatement(selectSQL);
        	statement.setString(1, id);
        	resultSet = statement.executeQuery();
        	if(resultSet.next()) {
        		login_user = new LoginUserRdbImpl(resultSet);
        	}
//        	}else {
//        		throw new SCIMException("USER NOT FOUND : " + id, RESULT_IS_NULL);
//        	}
		} catch (SQLException e) {
			throw new SCIMException(selectSQL, e);
		}finally {
			DBCP.close(connection, statement, resultSet);
		}
        
		return login_user;
	}

	public List<SCIMAudit> getAccountHistory(String resourceId)throws SCIMException{
		
		List<SCIMAudit> audit_list = new ArrayList<SCIMAudit>();
		final String selectSQL = "SELECT * FROM SCIM_AUDIT WHERE RESOURCEID=?  AND RESULT='SUCCESS'";
		
		Connection connection = null;
		PreparedStatement statement = null;
	    ResultSet resultSet = null;        

        try {
        	connection = getConnection();
        	statement  = connection.prepareStatement(selectSQL);
        	statement.setString(1, resourceId);
        	resultSet = statement.executeQuery();
        	
        	while(resultSet.next()) {
        		SCIMAudit audit = new SCIMAudit();
        		
        		audit.setResourceId(resultSet.getString("RESOURCEID"));        		
        		audit.setMethod(resultSet.getString("METHOD"));
        		audit.setWorkerId(resultSet.getString("WORKERID"));
        		audit.setWorkDate(resultSet.getDate("WORKDATE"));
        		
        		audit_list.add(audit);
        	}
		} catch (SQLException e) {
			throw new SCIMException(selectSQL, e);
		}finally {
			DBCP.close(connection, statement, resultSet);
		}
        
		return audit_list;
		
	}

	
//	
//
//	@Override
//	public void addOperationResult(String workId, SCIMUser user, String source,String direct, SCIMOperation operation, SCIMOperation result) throws SCIMException {
//		final String insertSQL = "INSERT INTO SCIM_AUDIT (workId, adminId,userId,sourceSystem,directSystem,method,result,detail,workDate)"
//				+ " VALUES ("
//				+ "?,?,?,?,?,"
//				+ "?,?,?,?)";
//		
//		Connection connection = null;
//		PreparedStatement statement = null;
//	    ResultSet resultSet = null;        
//	    try {
//	    	connection = getConnection();
//        	statement  = connection.prepareStatement(insertSQL);
//
//        	statement.setString(1, workId);
//        	statement.setString(2, user.getId());
//        	statement.setString(3, operation.getData().getId());
//        	statement.setString(4, source);
//        	statement.setString(5, direct);
//        	statement.setString(6, operation.getMethod());
//        	statement.setString(7, result.getStatus());
//        	if(result.getResponse() != null) {
//        		statement.setString(8, result.getResponse().getDetail());
//        	}else {
//        		statement.setString(8, null);
//        	}
//        	statement.setTimestamp(9, toSqlTimestamp(new Date()));
//        	
//        	statement.execute();
//	    } catch (SQLException e) {
//	    	throw new SCIMException("ADD AUDIT DATA FAILED : " + insertSQL , e);
////	    	System.out.println(">>>>>>> L : " +  e.getMessage());
////	    	if (e instanceof SQLIntegrityConstraintViolationException) {
////				throw new SCIMException(e.getMessage(),RESULT_DUPLICATE_ENTRY);
////			}else {
////				throw new SCIMException("ADD AUDIT DATA FAILED : " + insertSQL , e);
////			}
//		}finally {
//	    	DBCP.close(connection, statement, resultSet);
//	    }
//	}

	//********************************************************
	// SYSTEM - 
	//********************************************************
	public void deleteSystem(String systemId)throws SCIMException{
		StringBuffer query_buffer = new StringBuffer();
		query_buffer.append("DELETE FROM SCIM_SYSTEM ");
		query_buffer.append("WHERE SYSTEMID=?");
		
		final String query_string = query_buffer.toString();
		
		Connection connection = null;
		PreparedStatement statement = null;
	    ResultSet resultSet = null;  
	    int result = 0;
	    try {
	    	connection = getConnection();
        	statement  = connection.prepareStatement(query_string);
        	
        	statement.setString(1, systemId);
        	
        	statement.executeUpdate();
	    } catch (SQLException e) {
	    	logger.error(e.getMessage(),e);
	    	throw new SCIMException("DELETE FAILED : ", e);
		}finally {
			logger.info("query : {}", query_string);
			logger.info("result : {}", result);
	    	DBCP.close(connection, statement, resultSet);
	    }
	}

	public void updateSystem(SCIMSystem system)throws SCIMException{
		
		StringBuffer query_buffer = new StringBuffer();
		query_buffer.append("UPDATE SCIM_SYSTEM ");
		query_buffer.append("SET SYSTEMNAME=? , SYSTEMDESC=? ,SYSTEMURL=? ,SYSTEMTYPE=? ");
		query_buffer.append("WHERE SYSTEMID=?");
		
		final String query_string = query_buffer.toString();

		Connection connection = null;
		PreparedStatement statement = null;
	    ResultSet resultSet = null;
	    int result = 0;
	    try {
	    	connection = getConnection();
        	statement  = connection.prepareStatement(query_string);
        	
        	statement.setString(1, system.getSystemName());
        	statement.setString(2, system.getSystemDesc());
        	statement.setString(3, system.getSystemUrl());
        	statement.setString(4, system.getSystemTyp());
        	
        	statement.setString(5, system.getSystemId());

        	result = statement.executeUpdate();
        	
	    } catch (SQLException e) {
	    	logger.error(e.getMessage(),e);
	    	throw new SCIMException("UPDATE FAILED : ", e);
		}finally {
			logger.info("query : {}", query_string);
			logger.info("result : {}", result);
	    	DBCP.close(connection, statement, resultSet);
	    }
	}
	public void createSystem(SCIMSystem system)throws SCIMException{
		StringBuffer query_buffer = new StringBuffer();
		query_buffer.append("INSERT INTO SCIM_SYSTEM ");
		query_buffer.append("(SYSTEMID, SYSTEMNAME,SYSTEMDESC,SYSTEMURL,SYSTEMTYPE)");
		query_buffer.append(" VALUES (?,?,?,?,?)");

		final String query_string = query_buffer.toString();
		
		Connection connection = null;
		PreparedStatement statement = null;
	    ResultSet resultSet = null;     
	    int result = 0;
	    try {
	    	connection = getConnection();
        	statement  = connection.prepareStatement(query_string);

        	statement.setString(1, system.getSystemId());
        	statement.setString(2, system.getSystemName());
        	statement.setString(3, system.getSystemDesc());
        	statement.setString(4, system.getSystemUrl());
        	statement.setString(5, system.getSystemTyp());
        	        	
        	result = statement.executeUpdate();
	    } catch (SQLException e) {
	    	logger.error(e.getMessage(),e);
	    	throw new SCIMException("ADD FAILED : ", e);
		}finally {
			logger.info("query : {}", query_string);
			logger.info("result : {}", result);
	    	DBCP.close(connection, statement, resultSet);
	    }
	}

	@Override
	public List<SCIMSystem> getSystemAll() throws SCIMException {
		final String selectSQL = "SELECT systemId,systemName,systemDesc,systemUrl,systemType"
				+ " FROM SCIM_SYSTEM";
	
		
		Connection connection = null;
		PreparedStatement statement = null;
	    ResultSet resultSet = null;              

	    List<SCIMSystem> system_list = new ArrayList<SCIMSystem>();
        try {
        	connection = getConnection();
        	statement  = connection.prepareStatement(selectSQL);
        	
        	resultSet = statement.executeQuery();
        	while(resultSet.next()) {
        		SCIMSystem system = new SCIMSystem();
        		
        		system.setSystemId(resultSet.getString("systemId"));
        		system.setSystemName(resultSet.getString("systemName"));
        		system.setSystemDesc(resultSet.getString("systemDesc"));
        		system.setSystemUrl(resultSet.getString("systemUrl"));
        		system.setSystemType(resultSet.getString("systemType"));
        		
        		system_list.add(system);
        	}
        	
		} catch (SQLException e) {
			throw new SCIMException(selectSQL, e);
		}finally {
			DBCP.close(connection, statement, resultSet);
		}
        
		return system_list;
	}


	@Override
	public List<SCIMSystem> getSystemAll(String type) throws SCIMException {
		final String selectSQL = "SELECT systemId,systemName,systemDesc,systemUrl,systemType"
				+ " FROM SCIM_SYSTEM WHERE systemType=?";
	
		
		Connection connection = null;
		PreparedStatement statement = null;
	    ResultSet resultSet = null;              

	    List<SCIMSystem> system_list = new ArrayList<SCIMSystem>();
        try {
        	connection = getConnection();
        	statement  = connection.prepareStatement(selectSQL);
        	
        	statement.setString(1, type);
        	
        	resultSet = statement.executeQuery();
        	while(resultSet.next()) {
        		SCIMSystem system = new SCIMSystem();
        		
        		system.setSystemId(resultSet.getString("systemId"));
        		system.setSystemName(resultSet.getString("systemName"));
        		system.setSystemDesc(resultSet.getString("systemDesc"));
        		system.setSystemUrl(resultSet.getString("systemUrl"));
        		system.setSystemType(resultSet.getString("systemType"));
        		
        		system_list.add(system);
        	}
        	
		} catch (SQLException e) {
			throw new SCIMException(selectSQL, e);
		}finally {
			DBCP.close(connection, statement, resultSet);
		}
        
		return system_list;
	}
	
	@Override
	public SCIMSystem getSystemById(String systemId) throws SCIMException {
		final String selectSQL = "SELECT systemId,systemName,systemDesc,systemUrl"
				+ " FROM SCIM_SYSTEM WHERE systemId=?";
		
		Connection connection = null;
		PreparedStatement statement = null;
	    ResultSet resultSet = null; 
	    
	    SCIMSystem system = null;
	    try {
	    
        	connection = getConnection();
        	statement  = connection.prepareStatement(selectSQL);
        	statement.setString(1, systemId);
        	resultSet = statement.executeQuery();
	    	if(resultSet.next()) {
	    		system = new SCIMSystem();

	    		system.setSystemId(resultSet.getString("systemId"));
	    		system.setSystemName(resultSet.getString("systemName"));
	    		system.setSystemDesc(resultSet.getString("systemDesc"));
	    		system.setSystemUrl(resultSet.getString("systemUrl"));
		    }
	    	
	    } catch (SQLException e) {
			throw new SCIMException(selectSQL, e);
		}finally {
			DBCP.close(connection, statement, resultSet);
		}
	    
		return system;
	}	
	
	@Override
	public void updateSchdulerLastExcuteDate(String schdulerId, Date date) throws SCIMException {
		final String updateSQL = "UPDATE SCIM_SCHEDULER" + " SET LASTEXECUTEDATE=? WHERE SCHEDULERID=?";		
		Connection connection = null;
		PreparedStatement statement = null;
	    ResultSet resultSet = null;        
	    try {
	    	connection = getConnection();
        	statement  = connection.prepareStatement(updateSQL);
        	
        	statement.setDate(1,  toSqlDate(date));
        	statement.setString(2, schdulerId);
        	
        	statement.executeUpdate();
        	
	    } catch (SQLException e) {
	    	throw new SCIMException("UPDATE FAILED : " + updateSQL , e);
		}finally {
	    	DBCP.close(connection, statement, resultSet);
	    }
	}

//
//	@Override
//	public void addSchedulerHistory(String schedulerId, String workId, int req_put_count, int req_post_count,
//			int req_patch_count, int req_delete_count, int res_put_count, int res_post_count, int res_patch_count,
//			int res_delete_count) throws SCIMException {
//		
//		
//		final String insertSQL = "INSERT INTO SCIM_SCHEDULER_HISTORY ("
//				+ " schedulerId, workId,"
//				+ " reqPut,reqPost,reqPatch,reqDelete,"
//				+ " resPut,resPost,resPatch,resDelete,"
//				+ " workDate)"
//				+ " VALUES ("
//				+ "?,?,"
//				+ "?,?,?,?,"
//				+ "?,?,?,?,"
//				+ "?)";
//		
//		Connection connection = null;
//		PreparedStatement statement = null;
//	    ResultSet resultSet = null;        
//	    try {
//	    	connection = getConnection();
//        	statement  = connection.prepareStatement(insertSQL);
//
//        	statement.setString(1, schedulerId);
//        	statement.setString(2, workId);
//        	
//        	statement.setInt(3,req_put_count);
//        	statement.setInt(4,req_post_count);
//        	statement.setInt(5,req_patch_count);
//        	statement.setInt(6,req_delete_count);
//        	
//        	statement.setInt(7,res_put_count);
//        	statement.setInt(8,res_post_count);
//        	statement.setInt(9,res_patch_count);
//        	statement.setInt(10,res_delete_count);
//        	
//        	statement.setTimestamp(11, toSqlTimestamp(new Date()));
//        	
//        	statement.execute();
//	    } catch (SQLException e) {
//	    	throw new SCIMException("ADD AUDIT DATA FAILED : " + insertSQL , e);
////	    	System.out.println(">>>>>>> L : " +  e.getMessage());
////	    	if (e instanceof SQLIntegrityConstraintViolationException) {
////				throw new SCIMException(e.getMessage(),RESULT_DUPLICATE_ENTRY);
////			}else {
////				throw new SCIMException("ADD SCHEDULER HISTORY DATA FAILED : " + insertSQL , e);
////			}
//		}finally {
//	    	DBCP.close(connection, statement, resultSet);
//	    }
//	}
}
