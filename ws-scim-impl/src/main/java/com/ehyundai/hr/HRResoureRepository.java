package com.ehyundai.hr;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import com.wowsanta.scim.resource.SCIMResourceGetterRepository;
import com.wowsanta.scim.resource.SCIMResourceRepository;
import com.wowsanta.scim.schema.SCIMResourceTypeSchema;
import com.wowsanta.scim.util.Random;

public class HRResoureRepository extends AbstractRDBRepository implements SCIMResourceGetterRepository{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5170670467974453815L;

	public HRResoureRepository() {
		super();
		setClassName(HRResoureRepository.class.getCanonicalName());
	}
	
	//SELECT UR_CODE, MODIFYDATE FROM BASE_OBJECT_UR WHERE MODIFYDATE > '2019-01-14 23:17:36'

	@Override
	public void setUserSchema(SCIMResourceTypeSchema userSchema) {
		// TODO Auto-generated method stub
		
	}

	public SCIMUser createUser(SCIMUser user) throws SCIMException {
		User gw_user = (User)user;
		
		final String insertSQL = "INSERT INTO GW_USER (UR_Code,GR_Code,EmpNo,DisplayName,ExGroupName"
				+ ",JobPositionCode,ExJobPositionName"
				+ ",JobTitleCode,ExJobTitleName"
				+ ",JobLevelCode,ExJobLevelName"
				+ ",IsUse"
				+ ",Ex_PrimaryMail"
				+ ",EnterDate,RetireDate,RegistDate,ModifyDate)"
				+ " VALUES ("
				+ "?,?,?,?,?,"
				+ "?,?,?,?,?,"
				+ "?,?,?,?,?,"
				+ "?,?)";
		
		Connection connection = null;
		PreparedStatement statement = null;
	    ResultSet resultSet = null;        
	    try {
			
	    	connection = getConnection();
        	statement  = connection.prepareStatement(insertSQL);
        	statement.setString(1, gw_user.getId());
        	statement.setString(2, gw_user.getCompanyCode());
        	
        	statement.setString(3, gw_user.getEmployeeNumber());
        	statement.setString(4, gw_user.getUserName());
        	statement.setString(5, gw_user.getDivision());
        	
        	statement.setString(6, gw_user.getPositionCode());
        	statement.setString(7, gw_user.getPosition());
        	statement.setString(8, gw_user.getJobCode());
        	statement.setString(9, gw_user.getJob());
        	statement.setString(10, gw_user.getRankCode());
        	statement.setString(11, gw_user.getRank());
        	statement.setString(12, toYN(gw_user.isActive()));
        	statement.setString(13, gw_user.geteMail());
        	
        	statement.setDate(14, toSqlDate(gw_user.getJoinDate()));
        	statement.setDate(15, toSqlDate(gw_user.getRetireDate()));
        	
        	if(gw_user.getMeta() != null) {
        		statement.setDate(16, toSqlDate(gw_user.getMeta().getCreated()));
            	statement.setDate(17, toSqlDate(gw_user.getMeta().getLastModified()));
        	}else {
        		statement.setDate(16, toSqlDate(new Date()));
            	statement.setDate(17, toSqlDate(new Date()));
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
				+ "FROM GW_USER WHERE UR_Code = ?";
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
        		throw new SCIMException("USER NOT FOUND : " + userId, RESULT_IS_NULL);
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
		final String selectSQL = "SELECT UR_Code, GR_Code,EmpNo,DisplayName,ExGroupName"
				+ ",JobPositionCode,ExJobPositionName"
				+ ",JobTitleCode,ExJobTitleName"
				+ ",JobLevelCode,ExJobLevelName"
				+ ",IsUse"
				+ ",Ex_PrimaryMail"
				+ ",EnterDate,RetireDate,RegistDate,ModifyDate "
				+ "FROM GW_USER WHERE IsUse=?";
	
		Connection connection = null;
		PreparedStatement statement = null;
	    ResultSet resultSet = null;              

	    List<SCIMUser> user_list = new ArrayList<SCIMUser>();
        try {
        	connection = getConnection();
        	statement  = connection.prepareStatement(selectSQL);
        	
        	statement.setString(1, "Y");
        	
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
		final String selectSQL = "SELECT UR_Code, GR_Code,EmpNo,DisplayName,ExGroupName"
				+ ",JobPositionCode,ExJobPositionName"
				+ ",JobTitleCode,ExJobTitleName"
				+ ",JobLevelCode,ExJobLevelName"
				+ ",IsUse"
				+ ",Ex_PrimaryMail"
				+ ",EnterDate,RetireDate,RegistDate,ModifyDate "
				+ "FROM GW_USER WHERE ModifyDate BETWEEN ? AND ?";
	
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
	public List<SCIMUser> getUsersByWhere(String where) {
		final String selectSQL = "SELECT UR_Code, DN_ID, DN_Code, GR_Code, DisplayName, RegistDate, ModifyDate FROM BASE_OBJECT_UR WHERE UR_Code = ?";
		Connection connection = null;
		PreparedStatement statement = null;
	    ResultSet resultSet = null;  
		
		return null;
	}
	


	@Override
	public void setGroupSchema(SCIMResourceTypeSchema groupSchema) throws SCIMException {
		// TODO Auto-generated method stub
		
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
    		
    		gw_user.setEmployeeNumber(resultSet.getString("UR_Code"));
    		gw_user.setUserName(resultSet.getString("DisplayName"));
    		
    		gw_user.setOrganization(resultSet.getString("GR_Code"));
    		gw_user.setDivision(resultSet.getString("ExGroupName"));
    		
    		gw_user.setDepartment("defaultvalue");
    		
    		gw_user.setPositionCode(resultSet.getString("JobPositionCode"));
    		gw_user.setPosition(resultSet.getString("ExJobPositionName"));
    		gw_user.setJobCode(resultSet.getString("JobTitleCode"));
    		gw_user.setJob(resultSet.getString("ExJobTitleName"));
    		gw_user.setRankCode(resultSet.getString("JobLevelCode"));
    		gw_user.setRank(resultSet.getString("ExJobLevelName"));
    		gw_user.setActive(toBoolean(resultSet.getString("IsUse")));
    		gw_user.seteMail(resultSet.getString("Ex_PrimaryMail"));
    		
    		gw_user.setJoinDate(resultSet.getDate("EnterDate"));
    		gw_user.setRetireDate(resultSet.getDate("RetireDate"));
    		
    		gw_user.setMeta(new SCIMUserMeta());
    		gw_user.getMeta().setCreated(		toJavaDate(resultSet.getDate("RegistDate")));
    		gw_user.getMeta().setLastModified(	toJavaDate(resultSet.getDate("ModifyDate")));
    		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return gw_user;
	}

}
