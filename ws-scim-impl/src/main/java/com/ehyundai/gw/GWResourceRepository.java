package com.ehyundai.gw;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ehyundai.object.Resource;
import com.ehyundai.object.User;
import com.google.gson.JsonObject;
import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.log.SCIMLogger;
import com.wowsanta.scim.obj.SCIMResource2;
import com.wowsanta.scim.obj.SCIMUser;
import com.wowsanta.scim.obj.SCIMUserMeta;
import com.wowsanta.scim.repo.rdb.AbstractRDBRepository;
import com.wowsanta.scim.repo.rdb.DBCP;
import com.wowsanta.scim.repository.SCIMResourceGetterRepository;
import com.wowsanta.scim.repository.SCIMResourceRepository;
import com.wowsanta.scim.resource.SCIMGroup;
import com.wowsanta.scim.resource.SCIMSystemColumn;
import com.wowsanta.scim.schema.SCIMResourceTypeSchema;
import com.wowsanta.scim.util.Random;

public class GWResourceRepository extends AbstractRDBRepository implements SCIMResourceGetterRepository{
	private transient Logger logger = LoggerFactory.getLogger(GWResourceRepository.class);
	private transient static final long serialVersionUID = 5170670467974453815L;

	public GWResourceRepository() {
		super();
		//setClassName(GWResourceRepository.class.getCanonicalName());
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
        		column.setAllowNull(resultSet.getBoolean("IS_NULLABLE"));
        		
        		logger.info("COLUMNS : {} ", column.tojson(false));
        	}
		} catch (SQLException e) {
			logger.error("validate failed : ", e);
			throw new SCIMException(selectSQL, e);
		}finally {
			DBCP.close(connection, statement, resultSet);
		}
        logger.info("REPOSITORY VAILDATE : {} ", selectSQL);	
        
        try {
        	int count = 0;
	        List<SCIMResource2> user_list_all = getUsersByWhere("IsUse='Y'");
	        for (SCIMResource2 scimResource2 : user_list_all) {
	            logger.info("REPOSITORY VAILDATE  2: {} ", scimResource2);
	            count++;
	            if(count > 10) {
	            	break;
	            }
			}
        }catch (Exception e) {
			logger.error("REPOSITORY VAILDATE 2 failed : ", e);
		}
        
        try {
        	String where = "ModifyDate BETWEEN '2018-01-01 00:00:00' AND '"+ toString(new Date())+"'";
	        List<SCIMResource2> user_list_all = getUsersByWhere(where);
	        int count = 0;
	        for (SCIMResource2 scimResource2 : user_list_all) {
	            logger.info("REPOSITORY VAILDATE  3: {} ", scimResource2);
	            count++;
	            if(count > 10) {
	            	break;
	            }
			}
        }catch (Exception e) {
			logger.error("REPOSITORY VAILDATE 3 failed : ", e);
		}
        
		return true;
	}
	
	//SELECT COLUMN_NAME, IS_NULLABLE, DATA_TYPE  FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'IM_ACCOUNT'
	//SELECT UR_CODE, MODIFYDATE FROM BASE_OBJECT_UR WHERE MODIFYDATE > '2019-01-14 23:17:36'

	@Override
	public void setUserSchema(SCIMResourceTypeSchema userSchema) {
		// TODO Auto-generated method stub
		
	}
	

	@Override
	public void setGroupSchema(SCIMResourceTypeSchema groupSchema) throws SCIMException {
		// TODO Auto-generated method stub
		
	}
	

	public SCIMUser createUser(SCIMUser user) throws SCIMException {
		User gw_user = (User)user;
		
		final String insertSQL = "INSERT INTO " + this.tableName
				+ " (UR_Code,DisplayName"
				+ ",DN_Code,ExGroupName"
				+ ",JobPositionCode,ExJobPositionName"
				+ ",JobTitleCode,ExJobTitleName"
				+ ",JobLevelCode,ExJobLevelName"
				+ ",IsUse"				
				+ ",EnterDate,RetireDate,RegistDate,ModifyDate)"
				+ " VALUES ("
				+ "?,?,"
				+ "?,?,"
				+ "?,?,"
				+ "?,?,"
				+ "?,?,"
				+ "?,"
				+ "?,?,?,?)";
		
		Connection connection = null;
		PreparedStatement statement = null;
	    ResultSet resultSet = null;        
	    try {
			
	    	connection = getConnection();
        	statement  = connection.prepareStatement(insertSQL);
        	
        	statement.setString(1, gw_user.getId());
        	statement.setString(2, gw_user.getUserName());   
        	
        	statement.setString(3, gw_user.getCompanyCode());        	
        	statement.setString(4, gw_user.getDivision());
        	
        	statement.setString(5, gw_user.getPositionCode());
        	statement.setString(6, gw_user.getPosition());
        	statement.setString(7, gw_user.getJobCode());
        	statement.setString(8, gw_user.getJob());
        	statement.setString(9, gw_user.getRankCode());
        	statement.setString(10, gw_user.getRank());
        	
        	statement.setString(11, toYN(gw_user.isActive()));
        	
        	statement.setString(12, toStringDay(gw_user.getJoinDate()));
        	statement.setString(13,  toStringDay(gw_user.getRetireDate()));
        	
        	if(gw_user.getMeta() != null) {
        		statement.setDate(14, toSqlDate(gw_user.getMeta().getCreated()));
            	statement.setDate(15, toSqlDate(gw_user.getMeta().getLastModified()));
        	}else {
        		statement.setDate(14, toSqlDate(new Date()));
            	statement.setDate(14, toSqlDate(new Date()));
        	}
        	
        	statement.execute();
	    }catch(Exception e) {
	    	throw new SCIMException(insertSQL, e);
	    }finally {
	    	DBCP.close(connection, statement, resultSet);
	    }
		
		return gw_user;
	}

	@Override
	public SCIMUser getUser(String userId) throws SCIMException {
		final String selectSQL = "SELECT UR_Code,GR_Code,EmpNo,DisplayName,ExGroupName"
				+ ",JobPositionCode,ExJobPositionName"
				+ ",JobTitleCode,ExJobTitleName"
				+ ",JobLevelCode,ExJobLevelName"
				+ ",IsUse"
				+ ",Ex_PrimaryMail"
				+ ",EnterDate,RetireDate,RegistDate,ModifyDate "
				+ "FROM "+this.tableName+" WHERE UR_Code = ?";
		Connection connection = null;
		PreparedStatement statement = null;
	    ResultSet resultSet = null;              

	    User gw_user = null;
	    
        try {
        	connection = getConnection();
        	statement  = connection.prepareStatement(selectSQL);
        	statement.setString(1, userId);
        	resultSet = statement.executeQuery();
        	if(resultSet.next()) {
        		gw_user = newUserFromDB(resultSet);
        	}else {
        		//throw new SCIMException("USER NOT FOUND : " + userId, RESULT_IS_NULL);
        	}
        	
		} catch (SQLException e) {
			throw new SCIMException(selectSQL, e);
		}finally {
			DBCP.close(connection, statement, resultSet);
		}
        
		return gw_user;
	}
	
	@Override
	public List<SCIMUser> getUsersByActive() throws SCIMException{
		final String selectSQL = "SELECT * "
				+ "FROM " + this.tableName
				+ " WHERE IsUse='Y'";
	
		Connection connection = null;
		PreparedStatement statement = null;
	    ResultSet resultSet = null;              

	    List<SCIMUser> user_list = new ArrayList<SCIMUser>();
        try {
        	connection = getConnection();
        	statement  = connection.prepareStatement(selectSQL);
        	
        	resultSet = statement.executeQuery();
        	while(resultSet.next()) {
        		user_list.add(newUserFromDB(resultSet));
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
		final String selectSQL = "SELECT * "
				+ "FROM " + this.tableName
				+ " WHERE ModifyDate BETWEEN ? AND ?";
	
		Connection connection = null;
		PreparedStatement statement = null;
	    ResultSet resultSet = null;              

	    List<SCIMUser> user_list = new ArrayList<SCIMUser>();
        try {
        	connection = getConnection();
        	statement  = connection.prepareStatement(selectSQL);
        	
        	statement.setDate(1, toSqlDate(from));
        	statement.setDate(2, toSqlDate(to));
        	
        	resultSet = statement.executeQuery();
        	while(resultSet.next()) {
        		user_list.add(newUserFromDB(resultSet));
        	}
        	
		} catch (SQLException e) {
			throw new SCIMException(selectSQL, e);
		}finally {
			DBCP.close(connection, statement, resultSet);
		}
        
		return user_list;
	}
	@Override
	public List<SCIMResource2> getUsersByWhere(String where) throws SCIMException{
		final String selectSQL = "SELECT * "
				+ " FROM " + this.tableName
				+ " WHERE " + where;
		Connection connection = null;
		PreparedStatement statement = null;
	    ResultSet resultSet = null;  
		
	    List<SCIMResource2> user_list = new ArrayList<SCIMResource2>();
        try {
        	connection = getConnection();
        	statement  = connection.prepareStatement(selectSQL);
        	
        	resultSet = statement.executeQuery();
        	while(resultSet.next()) {
        		user_list.add(newResourceFromDB(resultSet));
        	}
        	
		} catch (SQLException e) {
			throw new SCIMException(selectSQL, e);
		}finally {
			DBCP.close(connection, statement, resultSet);
		}
        
		return user_list;
	}
	

	private SCIMResource2 newResourceFromDB(ResultSet resultSet) {
		Resource gw_user_resource = new Resource();
		try {
			gw_user_resource.setId(resultSet.getString("UR_Code"));
    		
			gw_user_resource.setUserName(resultSet.getString("DisplayName"));
			
			gw_user_resource.setEmployeeNumber(resultSet.getString("UR_Code"));
			
			gw_user_resource.setOrganization(resultSet.getString("DN_Code"));
			gw_user_resource.setDivision(resultSet.getString("ExGroupName"));
			//gw_user_resource.setDepartment(resultSet.getString("ExGroupPath"));
    		
			gw_user_resource.setPositionCode(resultSet.getString("JobPositionCode"));
			gw_user_resource.setPosition(resultSet.getString("ExJobPositionName"));
			gw_user_resource.setJobCode(resultSet.getString("JobTitleCode"));
			gw_user_resource.setJob(resultSet.getString("ExJobTitleName"));
			gw_user_resource.setRankCode(resultSet.getString("JobLevelCode"));
			gw_user_resource.setRank(resultSet.getString("ExJobLevelName"));
			gw_user_resource.setActive(toBoolean(resultSet.getString("IsUse")));
			
			gw_user_resource.setJoinDate(dayToJavaDate(resultSet.getString("EnterDate")));
			gw_user_resource.setRetireDate(dayToJavaDate(resultSet.getString("RetireDate")));
    		
			gw_user_resource.setCreated(	toJavaDate(resultSet.getDate("RegistDate")));
			gw_user_resource.setLastModified(toJavaDate(resultSet.getDate("ModifyDate")));
    		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return gw_user_resource;
	}

	@Override
	public SCIMGroup getGroup(String groupId) throws SCIMException {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public List<SCIMUser> getAllUsers() throws SCIMException {
		// TODO Auto-generated method stub
		return null;
	}

	private User newUserFromDB(ResultSet resultSet) {
		User gw_user = new User();
		try {
    		gw_user.setId(resultSet.getString("UR_Code"));
    		
    		gw_user.setUserName(resultSet.getString("DisplayName"));
    		
    		gw_user.setOrganization(resultSet.getString("DN_Code"));
    		gw_user.setDivision(resultSet.getString("ExGroupName"));
    		//gw_user.setDepartment(resultSet.getString("ExGroupPath"));
    		
    		gw_user.setPositionCode(resultSet.getString("JobPositionCode"));
    		gw_user.setPosition(resultSet.getString("ExJobPositionName"));
    		gw_user.setJobCode(resultSet.getString("JobTitleCode"));
    		gw_user.setJob(resultSet.getString("ExJobTitleName"));
    		gw_user.setRankCode(resultSet.getString("JobLevelCode"));
    		gw_user.setRank(resultSet.getString("ExJobLevelName"));
    		gw_user.setActive(toBoolean(resultSet.getString("IsUse")));
    		
    		gw_user.setJoinDate(dayToJavaDate(resultSet.getString("EnterDate")));
    		gw_user.setRetireDate(dayToJavaDate(resultSet.getString("RetireDate")));
    		
    		gw_user.setMeta(new SCIMUserMeta());
    		gw_user.getMeta().setCreated(		toJavaDate(resultSet.getDate("RegistDate")));
    		gw_user.getMeta().setLastModified(	toJavaDate(resultSet.getDate("ModifyDate")));
    		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return gw_user;
	}

//	@Override
//	public void fromJson(JsonObject jsonObject) throws SCIMException {
//		// TODO Auto-generated method stub
//		
//	}


}
