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
import com.wowsanta.scim.obj.SCIMAudit;
import com.wowsanta.scim.obj.SCIMSchedulerHistory;
import com.wowsanta.scim.obj.SCIMSystem;
import com.wowsanta.scim.obj.SCIMUser;
import com.wowsanta.scim.repo.rdb.AbstractRDBRepository;
import com.wowsanta.scim.repo.rdb.DBCP;
import com.wowsanta.scim.resource.SCIMGroup;
import com.wowsanta.scim.resource.SCIMProviderRepository;
import com.wowsanta.scim.resource.SCIMResouceFactory;
import com.wowsanta.scim.resource.SCIMResourceRepository;
import com.wowsanta.scim.resource.SCIMSystemColumn;
import com.wowsanta.scim.resource.SCIMSystemRepository;
import com.wowsanta.scim.resource.user.LoginUser;
import com.wowsanta.scim.resource.user.impl.LoginUserRdbImpl;
import com.wowsanta.scim.scheduler.SCIMScheduler;
import com.wowsanta.scim.schema.SCIMConstants;
import com.wowsanta.scim.schema.SCIMDefinitions;
import com.wowsanta.scim.schema.SCIMResourceTypeSchema;

public class IMSystemRepository extends AbstractRDBRepository implements SCIMProviderRepository{
	private static final long serialVersionUID = 1L;
	
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
//	    	
//	    	System.out.println(">>>>>>> L : " +  e.getMessage());
//	    	if (e instanceof SQLIntegrityConstraintViolationException) {
//				throw new SCIMException(e.getMessage(),RESULT_DUPLICATE_ENTRY);
//			}else {
//				throw new SCIMException("ADD AUDIT DATA FAILED : " + insertSQL , e);
//			}
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
			scheduler.setExcuteSystemId(resultSet.getString("excuteSystemId"));
			scheduler.setSourceSystemId(resultSet.getString("sourceSystemId"));
			scheduler.setTargetSystemId(resultSet.getString("targetSystemId"));
			
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
			+ "(workId, workerId,workerType, userId, sourceSystemId, targetSystemId, action, method, result, detail, workDate)"
			+ " VALUES ("
			+ "?,?,?,?,?,"
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
			statement.setString(4, audit.getUserId());
			statement.setString(5, audit.getSourceSystemId());
			statement.setString(6, audit.getTargetSystemId());
			statement.setString(7, audit.getAction());
			statement.setString(8, audit.getMethod());
			statement.setString(9, audit.getResult());
			statement.setString(10, audit.getDetail());
			statement.setDate(11, toSqlDate(audit.getWorkDate()));
			
			
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
	
	@Override
	public List<SCIMSchedulerHistory> getSchedulerHistoryById(String schedulerId)throws SCIMException{
		final String selectSQL = " SELECT * FROM SCIM_SCHEDULER_HISTORY WHERE schedulerId=?";
		
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
//		final String selectSQL = "SELECT *"				
//				+ " FROM SCIM_SCHEDULER_HISTORY WHERE schedulerId=? order by workDate desc";
//		
//		System.out.println(">>>> " + schedulerId);
//		Connection connection = null;
//		PreparedStatement statement = null;
//	    ResultSet resultSet = null;        
//
//	    List<SCIMSchedulerHistory> history_list = new ArrayList<SCIMSchedulerHistory>();
//        try {
//        	connection = getConnection();
//        	statement  = connection.prepareStatement(selectSQL);
//        	
//        	statement.setString(1, schedulerId);
//        	
//        	resultSet = statement.executeQuery();
//        	while(resultSet.next()) {
//        		SCIMSchedulerHistory scheduler_history = new SCIMSchedulerHistory();
//        		
//        		scheduler_history.setSchedulerId(	resultSet.getString("schedulerId"));
//        		scheduler_history.setWorkId(	resultSet.getString("workId"));
//        		scheduler_history.setReqPut(	resultSet.getInt("reqPut"));
//        		scheduler_history.setReqPost(	resultSet.getInt("reqPost"));
//        		scheduler_history.setReqPatch(	resultSet.getInt("reqPatch"));
//        		scheduler_history.setReqDelete(	resultSet.getInt("reqDelete"));
//        		
//        		scheduler_history.setResPut(	resultSet.getInt("resPut"));
//        		scheduler_history.setResPost(	resultSet.getInt("resPost"));
//        		scheduler_history.setResPatch(	resultSet.getInt("resPatch"));
//        		scheduler_history.setResDelete(	resultSet.getInt("resDelete"));
//        		
//        		scheduler_history.setWorkDate( 	toJavaDate (resultSet.getTimestamp("workDate")));
//        		
//        		history_list.add(scheduler_history);
//        	}
//		} catch (SQLException e) {
//			throw new SCIMException(selectSQL, e);
//		}finally {
//			DBCP.close(connection, statement, resultSet);
//		}
//		return history_list;
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
		final String updateSQL = "UPDATE SCIM_SCHEDULER"
				+ " SET LASTEXECUTEDATE=? WHERE SCHEDULERID=?";

		
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
