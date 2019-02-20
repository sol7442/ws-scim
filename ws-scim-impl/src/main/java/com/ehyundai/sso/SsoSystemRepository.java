package com.ehyundai.sso;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.message.SCIMOperation;
import com.wowsanta.scim.obj.SCIMSystem;
import com.wowsanta.scim.obj.SCIMUser;
import com.wowsanta.scim.repo.rdb.AbstractRDBRepository;
import com.wowsanta.scim.repo.rdb.DBCP;
import com.wowsanta.scim.resource.SCIMSystemRepository;
import com.wowsanta.scim.scheduler.SCIMScheduler;
import com.wowsanta.scim.scheduler.SCIMSchedulerHistory;

public class SsoSystemRepository extends AbstractRDBRepository implements SCIMSystemRepository {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public SCIMSystem getSystemById(String systemId)throws SCIMException{
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
	public SCIMUser getLoginUser(String id) throws SCIMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SCIMSystem> getSystemAll() throws SCIMException {
		// TODO Auto-generated method stub
		return null;
	}

	/******************************************************************
	 * 
	 ********************************************************************/
	public void updateSchdulerLastExcuteDate(String schdulerId, Date date) throws SCIMException {
		final String updateSQL = "UPDATE SCIM_SCHEDULER SET lastExecuteDate=? "
				+ " WHERE schedulerId=?";

		Connection connection = null;
		PreparedStatement statement = null;
	    ResultSet resultSet = null;        
	    try {
	    	connection = getConnection();
        	statement  = connection.prepareStatement(updateSQL);

        	statement.setString(1, toString(date));
        	statement.setString(2, schdulerId);
        	
        	statement.executeUpdate();
	    } catch (SQLException e) {
	    	throw new SCIMException("UPDATE USER FAILED : " + updateSQL , e);
		}finally {
	    	DBCP.close(connection, statement, resultSet);
	    }
	}
	
	
	@Override
	public List<SCIMScheduler> getSchdulerAll() throws SCIMException {
		final String selectSQL = "SELECT schedulerId, schedulerName, schedulerDesc, schedulerType,"
				+ "jobClass, triggerType, dayOfMonth, dayOfWeek, hourOfDay, minuteOfHour,"
				+ "sourceSystemId, targetSystemId, lastExecuteDate "
				+ " FROM SCIM_SCHEDULER";
		
		Connection connection = null;
		PreparedStatement statement = null;
	    ResultSet resultSet = null;        

	    List<SCIMScheduler> scheduler_list = new ArrayList<SCIMScheduler>();
        try {
        	connection = getConnection();
        	statement  = connection.prepareStatement(selectSQL);
        	resultSet = statement.executeQuery();
        	while(resultSet.next()) {
        		SCIMScheduler scheduler = new SCIMScheduler();
        		
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
        		
        		scheduler.setSourceSystemId(resultSet.getString("sourceSystemId"));
        		scheduler.setTargetSystemId(resultSet.getString("targetSystemId"));
        		
        		scheduler.setLastExecuteDate(toDate(resultSet.getString("lastExecuteDate")));
        		
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
	public List<SCIMSystem> getSystemAll(String type) throws SCIMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SCIMScheduler> getSchdulerBySystemId(String systemId) throws SCIMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SCIMSchedulerHistory> getSchedulerHistory(String schedulerId) throws SCIMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SCIMScheduler getSchdulerById(String schedulerId) throws SCIMException {
		final String selectSQL = "SELECT schedulerId, schedulerName, schedulerDesc, schedulerType,"
				+ "jobClass, triggerType, dayOfMonth, dayOfWeek, hourOfDay, minuteOfHour,"
				+ "sourceSystemId, targetSystemId, lastExecuteDate "
				+ " FROM SCIM_SCHEDULER WHERE schedulerId=? ";
		
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
        		scheduler = new SCIMScheduler();
        		
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
        		
        		scheduler.setSourceSystemId(resultSet.getString("sourceSystemId"));
        		scheduler.setTargetSystemId(resultSet.getString("targetSystemId"));
        		
        		scheduler.setLastExecuteDate(toDate(resultSet.getString("lastExecuteDate")));
        	}
		} catch (SQLException e) {
			throw new SCIMException(selectSQL, e);
		}finally {
			DBCP.close(connection, statement, resultSet);
		}
		return scheduler;
	}

	@Override
	public void addOperationResult(String workId, SCIMUser user, String source, String direct, SCIMOperation operation,
			SCIMOperation result) throws SCIMException {
	}

	@Override
	public void addSchedulerHistory(String schedulerId, String workId, int req_put_count, int req_post_count,
			int req_patch_count, int req_delate_count, int res_put_count, int res_post_count, int res_patch_count,
			int res_delate_count) throws SCIMException {
	}

}