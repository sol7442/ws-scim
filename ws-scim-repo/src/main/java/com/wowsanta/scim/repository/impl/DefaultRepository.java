package com.wowsanta.scim.repository.impl;

import java.io.FileReader;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.obj.SCIMResource2;
import com.wowsanta.scim.object.SCIM_Meta;
import com.wowsanta.scim.object.SCIM_User;
import com.wowsanta.scim.repo.rdb.DBCP;
import com.wowsanta.scim.repository.ResourceColumn;
import com.wowsanta.scim.repository.DataMapper;
import com.wowsanta.scim.repository.RepositoryException;
import com.wowsanta.scim.repository.SCIMSchemaMapper;
import com.wowsanta.scim.repository.SCIMUserResourceGetter;
import com.wowsanta.scim.repository.SchemaMapper;
import com.wowsanta.scim.repository.ResourceTable;
import com.wowsanta.scim.repository.SCIMRepository;

public abstract class DefaultRepository implements SCIMRepository, SCIMUserResourceGetter {
	static Logger logger = LoggerFactory.getLogger(DefaultRepository.class);

	private DBCP dbcp;
	protected SCIMSchemaMapper userSchmeaMapper;

	public void initialize() throws RepositoryException {
		try {
			this.dbcp.setUp();
			logger.info("Repository initialize : {}", this.dbcp.getPoolName());
		} catch (Exception e) {
			logger.error("REPOSITORY initialize FAILED : {} - {}", e.getMessage());
			throw new RepositoryException("DBCP Setup Error ", e);
		}
	}

	public abstract List<ResourceTable> getTables() throws RepositoryException;
	public abstract List<ResourceColumn> getTableColums(String tableName) throws RepositoryException;
	

	public void close() {
		logger.info("CLOSE DBCP {} ", this.dbcp.getPoolName());
	}
	public Connection getConnection() throws RepositoryException {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(this.dbcp.getPoolName());
		} catch (SQLException e) {
			logger.info("{}, - RETRY ", e.getMessage());
			initialize();
		}
		return connection;
	}

	public void setUserSchemaMapper(SCIMSchemaMapper user_schema_mapper) {
		this.userSchmeaMapper = user_schema_mapper;
	}
	
	public int getTotoalUserCount(String where) throws RepositoryException {
		ResourceTable table = this.userSchmeaMapper.getResourceTable();
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append("SELECT count(*) as USER_COUNT FROM").append(" ");
		sqlBuffer.append(table.getName()).append(" ");
		if(where != null) {
			sqlBuffer.append("WHERE").append(" ");
			sqlBuffer.append(where);
		}
		
		int user_count = 0;		
		Connection connection = null;
		PreparedStatement statement = null;
	    ResultSet resultSet = null;              

        try {
        	connection = getConnection();
        	statement  = connection.prepareStatement(sqlBuffer.toString());

        	resultSet = statement.executeQuery();
        	if(resultSet.next()) {
        		user_count = resultSet.getInt("USER_COUNT");
        	}
        	
		} catch (SQLException e) {
			throw new RepositoryException(sqlBuffer.toString(), e);
		}finally {
			DBCP.close(connection, statement, resultSet);
		}
		return user_count;
	}
	
	
//	public void setResourceMapper(SCIMResposeMapper resource_mapper) {
//		this.resourceMapper = resource_mapper;
//	}
	
	public SCIM_User getUser(String userId) throws RepositoryException {
		
		SCIM_User user = null;
		ResourceTable table = this.userSchmeaMapper.getResourceTable();
		ResourceColumn id_column =  this.userSchmeaMapper.getIdColumn();
		
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append("SELECT * FROM").append(" ");
		sqlBuffer.append(table.getName()).append(" ");
		sqlBuffer.append("WHERE").append(" ");
		sqlBuffer.append(id_column.getId()).append("=?");
		

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {
			connection = getConnection();
			statement = connection.prepareStatement(sqlBuffer.toString());
			statement.setString(1, userId);
			resultSet = statement.executeQuery();
			if (resultSet.next()) {
				user = new SCIM_User();	
				
				String id = (String) mappingOutData(getOutObject(resultSet,id_column),id_column.getMapper());
				user.setId(id);
				
				Map<String, SchemaMapper> mapper_map = userSchmeaMapper.getSchemaMapper();
				Set<String> map_key = mapper_map.keySet();
				for (String mapper_key : map_key) {
					SchemaMapper mapper = mapper_map.get(mapper_key);
					ResourceColumn mapper_column = mapper.getColunm();
					Object data = mappingOutData(getOutObject(resultSet,mapper_column),mapper_column.getMapper());
					user.addAttribute(mapper_key, data);
				}
				
			} else {
				// not found exception
			}
		} catch (SQLException e) {
			throw new RepositoryException(sqlBuffer.toString(), e);
		} finally {
			DBCP.close(connection, statement, resultSet);
		}
		
		SCIM_Meta meta = getUserMeata(userId);
		user.setMeta(meta);
		
		return user;
	}
	
	public SCIM_Meta getUserMeata(String userId) throws RepositoryException {
//		ResourceMapper user_mata_mapper = this.resourceMapper.getUserMataMapper();
//		final String selectSQL = "SELECT * FROM " + this.tableName + " WHERE UR_Code=?";
//		Connection connection = null;
//		PreparedStatement statement = null;
//		ResultSet resultSet = null;
//
//		SCIM_User user = null;
//		try {
//			connection = getConnection();
//			statement = connection.prepareStatement(selectSQL);
//			statement.setString(1, userId);
//			resultSet = statement.executeQuery();
//			ResultSetMetaData meta = resultSet.getMetaData();
//			
//			if (resultSet.next()) {
//				user = new SCIM_User();		
//				Column id_column = user_resource_mapper.getIdColumn();
//				String id = (String) mappingOutData(getOutObject(resultSet,id_column),id_column.getMapper());
//				user.setId(id);
//				
//				for (int i = 1; i <= meta.getColumnCount(); i++) {
//					Column column = user_resource_mapper.getColumn(meta.getColumnName(i));
//					DataMapper mapper = column.getMapper();
//					if(mapper != null) {
//						Object data = getOutObject(resultSet,column);
//						user.addAttribute(mapper.getId(), mappingOutData(data,mapper));
//					}		
//				}
//				
//			} else {
//				
//			}
//		} catch (SQLException e) {
//			throw new RepositoryException(selectSQL, e);
//		} finally {
//			DBCP.close(connection, statement, resultSet);
//		}
//		
		
		
		return null;
	}
	

	protected Object mappingOutData(Object data, DataMapper mapper) {
		if(mapper == null || mapper.getClassName() == null) {
			return data;
		}
		
		try {
			Class convert_class = Class.forName(mapper.getClassName());			
			Object covert_object = convert_class.newInstance();
			
			Class[] covert_params = {Object.class};
			Method method = convert_class.getMethod(mapper.getOutMethod(), covert_params);
			return method.invoke(covert_object, data);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return data;
	}


}
