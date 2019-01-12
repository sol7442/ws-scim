package com.wowsanta.scim.repo.rdb;


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.resource.SCIMRepository;
import com.wowsanta.scim.resource.SCIMResourceRepository;
import com.wowsanta.scim.schema.SCIMResourceTypeSchema;

public abstract class AbstractRDBRepository implements SCIMRepository {
	private String repositoryClass;
	private String repositoryType;
	
	private DBCP dbcp;
	
	@Override
	public void initialize() throws SCIMException {
		try {
			this.dbcp.setUp();
		} catch (Exception e) {
			throw new SCIMException("DBCP Setup Error ",e);
		}
	}
	
	public void setRepositoryClass(String className) {
		this.repositoryClass = className;
	}
	public String getRepositoryClass() {
		return this.repositoryClass;
	}
	public void setRepositoryType(String type) {
		this.repositoryType = type;
	}
	public String getRepositoryType() {
		return this.repositoryType;
	}
	
	public void initDBCP(DBCP dbcp) {
		this.dbcp = dbcp;
	}
	
	public Connection getConnection() {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(this.dbcp.getPoolName());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connection;
	}


//	@Override
//	public User createUser(User user) {
//		System.out.println("======createUser==========================");
//		Connection connection = null;
//		try {
//			 connection =  DriverManager.getConnection("jdbc:apache:commons:dbcp:cp");
//			 
//			 
//			 List<RDBQuery> query_list = this.queryManager.getInsert();
//			 for (RDBQuery query : query_list) {
//				 query.getTable();
//				 
//			}
//			 
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		
//		System.out.println("======createUser==========================");
//		return null;
//	}
//
//	@Override
//	public User getUser(String userId) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public User updateUser(User updatedUser) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public void deleteUser(String userId) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void setGroupSchema(SCIMResourceTypeSchema groupSchema) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public Group createGroup(Group group) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public Group getGroup(String groupId) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public Group updateGroup(Group group) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public void deleteGroup(String groupId) {
//		// TODO Auto-generated method stub
//		
//	}
//
//
//	public void save(String file_name) throws IOException {
//		OutputStreamWriter writer = new OutputStreamWriter(
//				new FileOutputStream(
//						new File(file_name)),StandardCharsets.UTF_8);
//		
//		Gson gson = new GsonBuilder().setPrettyPrinting().create();
//		gson.toJson(this,writer);
//		writer.flush();
//		writer.close();
//	}
//	
//	public String toJson() {
//		Gson gson = new GsonBuilder().setPrettyPrinting().create();
//		return gson.toJson(this);
//	}
//	
//	public String toString() {
//		return toJson();
//	}
//	
	public static AbstractRDBRepository load(String file_name) throws FileNotFoundException {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonReader reader = new JsonReader(new FileReader(file_name));
		return gson.fromJson(reader,AbstractRDBRepository.class);
	}

}
