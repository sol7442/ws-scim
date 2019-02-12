package com.ehyundai.gw;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ehyundai.im.Admin;
import com.ehyundai.object.User;
import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.obj.DefaultUserMeta;
import com.wowsanta.scim.obj.SCIMAdmin;
import com.wowsanta.scim.obj.SCIMUser;
import com.wowsanta.scim.repo.rdb.AbstractRDBRepository;
import com.wowsanta.scim.repo.rdb.DBCP;
import com.wowsanta.scim.resource.SCIMResourceRepository;
import com.wowsanta.scim.resource.SCIMSystemRepository;
import com.wowsanta.scim.schema.SCIMResourceTypeSchema;

public class GWRepository extends AbstractRDBRepository implements SCIMResourceRepository, SCIMSystemRepository{

	//SELECT UR_Code, DN_ID, DN_Code, GR_Code, DisplayName, RegistDate, ModifyDate FROM BASE_OBJECT_UR WHERE IsUse = 'Y' AND ModifyDate > '21:40:00'
	
	public GWRepository() {
		super();
		setClassName(GWRepository.class.getCanonicalName());
	}
	
	//SELECT UR_CODE, MODIFYDATE FROM BASE_OBJECT_UR WHERE MODIFYDATE > '2019-01-14 23:17:36'

	@Override
	public void setUserSchema(SCIMResourceTypeSchema userSchema) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public SCIMUser createUser(SCIMUser user) throws SCIMException {
		User gw_user = (User)user;
		
		
		final String insertSQL = "INSERT INTO BASE_OBJECT_UR (UR_Code,DN_Code,GR_Code,EmpNo,DisplayName,ExGroupName"
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
				+ "?,?,?)";
		
		Connection connection = null;
		PreparedStatement statement = null;
	    ResultSet resultSet = null;        
	    try {
	    	connection = getConnection();
        	statement  = connection.prepareStatement(insertSQL);
        	statement.setString(1, gw_user.getId());
        	statement.setString(2, gw_user.getCompanyCode());
        	statement.setString(3, gw_user.getGroupCode());
        	statement.setString(4, gw_user.getEmployeeNumber());
        	statement.setString(5, gw_user.getUserName());
        	statement.setString(6, gw_user.getGroupName());
        	statement.setString(7, gw_user.getPositionCode());
        	statement.setString(8, gw_user.getPosition());
        	statement.setString(9, gw_user.getJobCode());
        	statement.setString(10, gw_user.getJob());
        	statement.setString(11, gw_user.getRankCode());
        	statement.setString(12, gw_user.getRank());
        	statement.setBoolean(13, gw_user.isActive());
        	statement.setString(14, gw_user.geteMail());
        	
        	statement.setDate(15, toSqlDate(gw_user.getJoinDate()));
        	statement.setDate(16, toSqlDate(gw_user.getRetireDate()));
        	statement.setDate(17, toSqlDate(gw_user.getMeta().getCreated()));
        	statement.setDate(18, toSqlDate(gw_user.getMeta().getLastModified()));
        	
        	statement.execute();
        	connection.commit();
	    }catch(Exception e) {
	    	throw new SCIMException(insertSQL, e);
	    }finally {
	    	DBCP.close(connection, statement, resultSet);
	    }
		
		return gw_user;
	}

	@Override
	public SCIMUser getUser(String userId) throws SCIMException {
		final String selectSQL = "SELECT UR_Code,DN_Code,GR_Code,EmpNo,DisplayName,ExGroupName"
				+ ",JobPositionCode,ExJobPositionName"
				+ ",JobTitleCode,ExJobTitleName"
				+ ",JobLevelCode,ExJobLevelName"
				+ ",IsUse"
				+ ",Ex_PrimaryMail"
				+ ",EnterDate,RetireDate,RegistDate,ModifyDate "
				+ "FROM BASE_OBJECT_UR WHERE UR_Code = ?";
		
	
		
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
        		gw_user = new User();
        		
        		gw_user.setId(resultSet.getString("UR_Code"));
        		gw_user.setCompanyCode(resultSet.getString("DN_Code"));
        		gw_user.setGroupCode(resultSet.getString("GR_Code"));
        		gw_user.setEmployeeNumber(resultSet.getString("EmpNo"));
        		gw_user.setUserName(resultSet.getString("DisplayName"));
        		gw_user.setGroupName(resultSet.getString("ExGroupName"));
        		gw_user.setPositionCode(resultSet.getString("JobPositionCode"));
        		gw_user.setPosition(resultSet.getString("ExJobPositionName"));
        		gw_user.setJobCode(resultSet.getString("JobTitleCode"));
        		gw_user.setJob(resultSet.getString("ExJobTitleName"));
        		gw_user.setRankCode(resultSet.getString("JobLevelCode"));
        		gw_user.setRank(resultSet.getString("ExJobLevelName"));
        		gw_user.setActive(resultSet.getString("IsUse"));
        		gw_user.seteMail(resultSet.getString("Ex_PrimaryMail"));
        		
        		gw_user.setJoinDate(resultSet.getDate("EnterDate"));
        		gw_user.setRetireDate(resultSet.getDate("RetireDate"));
        		((DefaultUserMeta)gw_user.getMeta()).setCreated(resultSet.getDate("RegistDate"));
        		((DefaultUserMeta)gw_user.getMeta()).setLastModified(resultSet.getDate("ModifyDate"));
        	}
        	
//        	if(gw_user == null) {
//        		throw new SCIMException("RESOURCE NOT FOUND : " + userId);
//        	}
		} catch (SQLException e) {
			throw new SCIMException(selectSQL, e);
		}finally {
			DBCP.close(connection, statement, resultSet);
		}
        
		return gw_user;
	}

	@Override
	public List<SCIMUser> getUsers(Date from, Date to)throws SCIMException{
		final String selectSQL = "SELECT UR_Code,DN_Code,GR_Code,EmpNo,DisplayName,ExGroupName"
				+ ",JobPositionCode,ExJobPositionName"
				+ ",JobTitleCode,ExJobTitleName"
				+ ",JobLevelCode,ExJobLevelName"
				+ ",IsUse"
				+ ",Ex_PrimaryMail"
				+ ",EnterDate,RetireDate,RegistDate,ModifyDate "
				+ "FROM BASE_OBJECT_UR WHERE ModifyDate BETWEEN ? AND ?";
	
		
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
        		User gw_user = new User();
        		
        		gw_user.setId(resultSet.getString("UR_Code"));
        		gw_user.setCompanyCode(resultSet.getString("DN_Code"));
        		gw_user.setGroupCode(resultSet.getString("GR_Code"));
        		gw_user.setEmployeeNumber(resultSet.getString("EmpNo"));
        		gw_user.setUserName(resultSet.getString("DisplayName"));
        		gw_user.setGroupName(resultSet.getString("ExGroupName"));
        		gw_user.setPositionCode(resultSet.getString("JobPositionCode"));
        		gw_user.setPosition(resultSet.getString("ExJobPositionName"));
        		gw_user.setJobCode(resultSet.getString("JobTitleCode"));
        		gw_user.setJob(resultSet.getString("ExJobTitleName"));
        		gw_user.setRankCode(resultSet.getString("JobLevelCode"));
        		gw_user.setRank(resultSet.getString("ExJobLevelName"));
        		gw_user.setActive(resultSet.getString("IsUse"));
        		gw_user.seteMail(resultSet.getString("Ex_PrimaryMail"));
        		
        		gw_user.setJoinDate(resultSet.getDate("EnterDate"));
        		gw_user.setRetireDate(resultSet.getDate("RetireDate"));
        		((DefaultUserMeta)gw_user.getMeta()).setCreated(resultSet.getDate("RegistDate"));
        		((DefaultUserMeta)gw_user.getMeta()).setLastModified(resultSet.getDate("ModifyDate"));
        		
        		user_list.add(gw_user);
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
		final String selectSQL = "SELECT UR_Code, DN_ID, DN_Code, GR_Code, DisplayName, RegistDate, ModifyDate FROM BASE_OBJECT_UR WHERE UR_Code = ?";
		Connection connection = null;
		PreparedStatement statement = null;
	    ResultSet resultSet = null;  
		
		return null;
	}
	
	@Override
	public SCIMUser updateUser(SCIMUser updatedUser) throws SCIMException {
		// TODO Auto-generated method stub
		return null;
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
	public SCIMAdmin getAdmin(String id) throws SCIMException {
		// TODO Auto-generated method stub
		return null;
	}






}
