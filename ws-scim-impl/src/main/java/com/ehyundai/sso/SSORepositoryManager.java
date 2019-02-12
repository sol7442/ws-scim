package com.ehyundai.sso;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import com.ehyundai.im.Meta;
import com.ehyundai.im.UserGroup;
import com.ehyundai.object.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.repo.rdb.AbstractRDBRepository;
import com.wowsanta.scim.repo.rdb.DBCP;
import com.wowsanta.scim.resource.SCIMGroup;
import com.wowsanta.scim.resource.SCIMResouceFactory;
import com.wowsanta.scim.resource.SCIMResourceRepository;
import com.wowsanta.scim.resource.SCIMUser;
import com.wowsanta.scim.schema.SCIMResourceTypeSchema;

public class SSORepositoryManager extends AbstractRDBRepository implements SCIMResourceRepository {

	private static final String USER_TABLE = "WA3_USER";
	
	
	private static final String insertSQL = "INSERT INTO " + USER_TABLE			
			+ "(ID, NAME, DIV_ID, ORG_ID, PATH_ID ) VALUES"
			+ "(?,?,?,?,?)";

	private static final String deleteSQL = "DELETE " + USER_TABLE			
			+ " WHERE ID = ?";
	
	private static final String selectSQL = "SELECT ID, NAME, ORG_ID FROM " + USER_TABLE			
			+ " WHERE ID = ?";
	
	public static SSORepositoryManager load(String file_name) throws FileNotFoundException {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonReader reader = new JsonReader(new FileReader(file_name));
		return gson.fromJson(reader,SSORepositoryManager.class);
	}
	
	@Override
	public void setUserSchema(SCIMResourceTypeSchema userSchema) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public SCIMUser createUser(SCIMUser user) throws SCIMException {
		//Meta meta = (Meta)user.getMeta();
		User im_user = (User) user;
		
		Connection connection = null;
		PreparedStatement statement = null;
	    ResultSet resultSet = null;        

//        try {
//        	connection = getConnection();
//        	statement  = connection.prepareStatement(insertSQL);
//        	statement.setString(1, im_user.getId());
//        	statement.setString(2, im_user.getUserName());
//        	statement.setString(3, "div_id");
//        	statement.setString(4, im_user.getGroups().get(0).getValue());
//        	statement.setString(5, "path_id");
//
//        	statement.execute();
//        	        	
//		} catch (SQLException e) {
//			throw new SCIMException(insertSQL, e);
//		}finally {
//			DBCP.close(connection, statement, resultSet);
//		}
        
		return user;
	}



	@Override
	public SCIMUser getUser(String userId) throws SCIMException {
		User selected_user = (User)SCIMResouceFactory.getInstance().createUser();;
		
		Connection connection = null;
		PreparedStatement statement = null;
	    ResultSet resultSet = null;        

//        try {
//        	connection = getConnection();
//        	statement  = connection.prepareStatement(selectSQL);
//        	statement.setString(1, userId);
//        	resultSet = statement.executeQuery();
//        	if(resultSet.next()) {
//        		selected_user.setId(resultSet.getString("ID"));
//        		selected_user.setUserName(resultSet.getString("NAME"));
//        		UserGroup group = (UserGroup)SCIMResouceFactory.getInstance().createUserGroup(resultSet.getString("ORG_ID"));
//        		selected_user.addGroup(group);
//        		
////        		selected_user.setMeta(meta);
////        		selected_user.addGroup(group);
//        	}
//		} catch (SQLException e) {
//			throw new SCIMException(selectSQL, e);
//		}finally {
//			DBCP.close(connection, statement, resultSet);
//		}
        
		return selected_user;
	}

	@Override
	public SCIMUser updateUser(SCIMUser updatedUser) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteUser(String userId) throws SCIMException {
		Connection connection = null;
		PreparedStatement statement = null;
	    ResultSet resultSet = null;        

        try {
        	connection = getConnection();
        	statement  = connection.prepareStatement(deleteSQL);
        	statement.setString(1, userId);
        	
        	statement.executeUpdate();
        	        	
		} catch (SQLException e) {
			throw new SCIMException(deleteSQL, e);
		}finally {
			DBCP.close(connection, statement, resultSet);
		}
		
	}

	@Override
	public void setGroupSchema(SCIMResourceTypeSchema groupSchema) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public SCIMGroup createGroup(SCIMGroup group) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SCIMGroup getGroup(String groupId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SCIMGroup updateGroup(SCIMGroup group) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteGroup(String groupId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<SCIMUser> getUsers(Date from, Date to) throws SCIMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SCIMUser> getUsers(String where) throws SCIMException {
		// TODO Auto-generated method stub
		return null;
	}
}
