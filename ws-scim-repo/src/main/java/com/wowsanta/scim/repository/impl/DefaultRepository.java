package com.wowsanta.scim.repository.impl;

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

import com.wowsanta.scim.object.Resource_Object;
import com.wowsanta.scim.repo.rdb.DBCP;
import com.wowsanta.scim.repository.ResourceColumn;
import com.wowsanta.scim.repository.AttributeSchema;
import com.wowsanta.scim.repository.AttributeValue;
import com.wowsanta.scim.repository.DataMapper;
import com.wowsanta.scim.repository.RepositoryException;
import com.wowsanta.scim.repository.RepositoryInputMapper;
import com.wowsanta.scim.repository.RepositoryOutputMapper;
import com.wowsanta.scim.repository.ResourceTable;
import com.wowsanta.scim.repository.ResourceType;
import com.wowsanta.scim.repository.SCIMRepository;
import com.wowsanta.scim.repository.resource.SCIMResourceRepository;
import com.wowsanta.scim.schema.SCIMDefinitions.Uniqueness;

public abstract class DefaultRepository implements SCIMRepository, SCIMResourceRepository {
	static transient Logger logger = LoggerFactory.getLogger(DefaultRepository.class);
	protected DBCP dbcp;

	protected transient RepositoryOutputMapper userOutputMapper;
	protected transient RepositoryOutputMapper groupOutputMapper;
	protected transient RepositoryInputMapper userInputMapper;
	protected transient RepositoryInputMapper groupInputMapper;

	
	public void setUserOutputMapper(RepositoryOutputMapper output_mapper) {
		this.userOutputMapper = output_mapper;
	}
	public void setUserInputMapper(RepositoryInputMapper input_mapper) {
		this.userInputMapper = input_mapper;
	}
	public void setGrouptOutputMapper(RepositoryOutputMapper output_mapper) {
		this.groupOutputMapper = output_mapper;
	}
	
	public void setGroupInputMapper(RepositoryInputMapper input_mapper) {
		this.groupInputMapper = input_mapper;
	}
	public void setDbcp(DBCP dbcp) {
		this.dbcp = dbcp;
	}
	public DBCP getDbcp() {
		return this.dbcp;
	}
	public void initialize() throws RepositoryException {
		try {
			this.dbcp.setUp();
			logger.info("Repository initialize : {}", this.dbcp.getPoolName());
		} catch (Exception e) {
			logger.error("Repository initialize FAILED : {} - {}", e.getMessage());
			throw new RepositoryException("DBCP Setup Error ", e);
		}
	}
	
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
	public void close(Connection con, PreparedStatement pstmt, ResultSet rs) {
        try{
            try{
                if(rs != null){rs.close();}
            }catch(Exception e){}
            
            try{
                if(pstmt != null){ pstmt.close();}
            }catch(Exception e){}

            try{
                if(con != null && !con.isClosed()){
                	con.close();
                }
            }catch(Exception e){}
            
        }catch(Exception e){
        	e.printStackTrace();
        }
	}
	
	protected Object getOutObject(int index, ResultSet resultSet, ResultSetMetaData meta) throws RepositoryException {
		Object object = null;
		try {
			switch (meta.getColumnType(index)) {
			case java.sql.Types.CHAR:
			case java.sql.Types.VARCHAR:
			case java.sql.Types.LONGVARCHAR:
			case java.sql.Types.NCHAR:
			case java.sql.Types.NVARCHAR:
			case java.sql.Types.LONGNVARCHAR:
				object = resultSet.getString(index);
				break;
			case java.sql.Types.NUMERIC:
			case java.sql.Types.DECIMAL:
				object = resultSet.getBigDecimal(index);
				break;
			case java.sql.Types.BIT:
				object =  resultSet.getBoolean(index);
				break;
			case java.sql.Types.TINYINT:
				object = resultSet.getByte(index);
				break;
			case java.sql.Types.SMALLINT:
				object = resultSet.getShort(index);
				break;
			case java.sql.Types.INTEGER:
				object = resultSet.getInt(index);
				break;
			case java.sql.Types.BIGINT:
				object = resultSet.getLong(index);
				break;
			case java.sql.Types.REAL:
				object = resultSet.getFloat(index);
				break;
			case java.sql.Types.FLOAT:
			case java.sql.Types.DOUBLE:
				object = resultSet.getDouble(index);
				break;
			case java.sql.Types.BINARY:
			case java.sql.Types.VARBINARY:
			case java.sql.Types.LONGVARBINARY:
				object = resultSet.getBytes(index);
				break;
			case java.sql.Types.DATE:
				object = resultSet.getDate(index);
				break;
			case java.sql.Types.TIME:
				object = resultSet.getTime(index);
				break;
			case java.sql.Types.TIMESTAMP:
				object = resultSet.getTimestamp(index);
				break;
			case java.sql.Types.CLOB:
			case java.sql.Types.NCLOB:
				object = resultSet.getClob(index);
				break;
			case java.sql.Types.BLOB:
				object = resultSet.getBlob(index);
				break;
			default:
				logger.error("unkonwn type {},{},{},{}", index, meta.getColumnName(index), meta.getColumnTypeName(index), meta.getColumnType(index));
				break;
			}
			
			logger.debug("resultSet : {}-{}",object, meta.getColumnName(index) );

		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return object;
	}
	
	protected void setStatement(int index, PreparedStatement statement, Object set_data) throws RepositoryException{
		try {
			if (set_data instanceof java.lang.String) {
				statement.setString(index, (java.lang.String) set_data);
			}else if(set_data instanceof java.math.BigDecimal) {
				statement.setBigDecimal(index, (java.math.BigDecimal) set_data);
			}else if(set_data instanceof java.lang.Boolean) {
				statement.setBoolean(index, (java.lang.Boolean) set_data);
			}else if(set_data instanceof java.lang.Byte) {
				statement.setByte(index, (java.lang.Byte) set_data);
			}else if(set_data instanceof java.lang.Short) {
				statement.setShort(index, (java.lang.Short) set_data);
			}else if(set_data instanceof java.lang.Integer) {
				statement.setInt(index, (java.lang.Integer) set_data);
			}else if(set_data instanceof java.lang.Long) {
				statement.setLong(index, (java.lang.Long) set_data);
			}else if(set_data instanceof java.lang.Float) {
				statement.setFloat(index, (java.lang.Float) set_data);
			}else if(set_data instanceof java.lang.Double) {
				statement.setDouble(index, (java.lang.Double) set_data);
			}else if(set_data instanceof byte[]) {
				statement.setBytes(index, (byte[]) set_data);
			}else if(set_data instanceof java.sql.Date) {
				statement.setDate(index, (java.sql.Date) set_data);
			}else if(set_data instanceof java.sql.Time) {
				statement.setTime(index, (java.sql.Time) set_data);
			}else if(set_data instanceof java.sql.Timestamp) {
				statement.setTimestamp(index, (java.sql.Timestamp) set_data);
			}else if(set_data instanceof java.sql.Clob) {
				statement.setClob(index, (java.sql.Clob) set_data);
			}else if(set_data instanceof java.sql.Blob) {
				statement.setBlob(index, (java.sql.Blob) set_data);
			}else {
				statement.setObject(index, set_data);
			}
			
			logger.debug("setStatement ({}): {}-{}",index, set_data, set_data.getClass().getName());
		}catch (Exception e) {
			logger.error("setStatement ({}): {}-{}",index, set_data, e.getMessage(), e);			
			throw new RepositoryException("setStatement ERROR :" + e.getMessage(), e);
		}
	}
	
	
	public int getGroupCount(String filter) throws RepositoryException{
		return getResourceCount(this.groupOutputMapper, filter);
	}
	public int getResourceCount(RepositoryOutputMapper outMapper, String filter) throws RepositoryException{
		int count = 0;
		
		String queryString = outMapper.getCountQuery(filter);
		
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = getConnection();
		    statement  = connection.prepareStatement(queryString);
        	resultSet = statement.executeQuery();
			if (resultSet.next()) {
				count = resultSet.getInt(1);
			}
        	statement.execute();
        	statement.close();
			
        	logger.info("query  : {}", queryString);
			logger.info("result : {}", count);
			
		}catch (Exception e) {
			logger.info("query  : {}", queryString);
			logger.info("filter : {}", filter);
			
			throw new RepositoryException(e.getMessage(), e);
		}finally {
			close(connection, statement, resultSet);
		}
		return count;
	}
	@Override
	public int getUserCount(String filter)throws RepositoryException{
		return getResourceCount(this.userOutputMapper, filter);
	}
	
	public int getSystemUserCount(RepositoryOutputMapper outMapper, String filter) throws RepositoryException{
		return getResourceCount(outMapper, filter);
	}
	
	public void createSystemUser(RepositoryInputMapper inMapper,Resource_Object resource)  throws RepositoryException{
		createResource(inMapper.getTables(), resource);
	}
	public void updateSystemUser(RepositoryInputMapper inMapper,Resource_Object resource)  throws RepositoryException{
		updateResource(inMapper.getTables(), resource);
	}
	
	public Resource_Object getSystemUser(RepositoryOutputMapper outMapper,	List<AttributeValue> attribute_list)  throws RepositoryException{
		Resource_Object resource = null;
		
		final String query_string = outMapper.getSelectQuery(attribute_list);
		
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		
		try {
			connection = getConnection();
			statement  = connection.prepareStatement(query_string);
//			int setIndex = 1;
//			for(int i=0; i<attribute_list.size(); i++) {
//				AttributeValue value   = attribute_list.get(i);
//				AttributeSchema schema = outMapper.getAttribute(value.getName());
//				DataMapper dataMapper = schema.getDataMapper();
//				
//				Object set_data = null;
//				if(dataMapper != null) {
//					set_data = dataMapper.convert(value.getValue());
//				}else {
//					set_data = value.getValue();
//				}
//
//				if(set_data != null) {
//					setStatement(setIndex,statement, set_data);
//					setIndex++;
//				}
//			}
			
        	resultSet = statement.executeQuery();
			ResultSetMetaData meta = resultSet.getMetaData();
			if (resultSet.next()) {
				resource = newResourceFromResultSet(outMapper, resultSet, meta);
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			logger.info("query  : {}", query_string);
			logger.info("attribtue  : {}", attribute_list);
			logger.info("resource : {}", resource);
			
			close(connection, statement, resultSet);
		}	
		
		return resource;
	}
	private Resource_Object getResource(RepositoryOutputMapper outMapper, String id) throws RepositoryException{
		Resource_Object resource = null;

		final String query_string = outMapper.getSelectQuery();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			logger.info("query  : {}", query_string);					
			logger.info("params  : {}", id);
			
			connection = getConnection();
			statement  = connection.prepareStatement(query_string);
        	statement.setString(1, id);
			
        	resultSet = statement.executeQuery();
			ResultSetMetaData meta = resultSet.getMetaData();
			if (resultSet.next()) {
				resource = newResourceFromResultSet(outMapper, resultSet, meta);
			}
			
			logger.info("resource : {}", resource);
        	statement.execute();
		}catch (Exception e) {
			logger.info("query  : {}", query_string);
			logger.info("resource : {}", id);
			
			throw new RepositoryException("", e);
		}finally {
			close(connection, statement, resultSet);
		}
		
		return resource;
	}
	public Resource_Object getGroup(String groupId) throws RepositoryException {
		return getResource(this.groupOutputMapper, groupId);
	}
	public Resource_Object getUser(String userId) throws RepositoryException {
		return getResource(this.userOutputMapper, userId);
	}
	
	public void createUser(Resource_Object user_object) throws RepositoryException {
		List<ResourceTable> tables = this.userInputMapper.getTables();
		createResource(tables, user_object);
	}
	
	public void createGroup(Resource_Object group_object) throws RepositoryException{
		List<ResourceTable> tables = this.groupInputMapper.getTables();
		createResource(tables,group_object );
	}
	private void createResource(List<ResourceTable> tables, Resource_Object resource) throws RepositoryException{
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;  
		
		try {
			connection = getConnection();
			for (ResourceTable table : tables) {
				final String query_string = table.getInsertQuery(resource);
				
				logger.info("query  : {}", query_string);
				logger.info("resource : {}", resource);
				
				try {
					statement = connection.prepareStatement(query_string);
					List<ResourceColumn> columns = table.getColumns();
					int setIndex = 1;
					for(int i=0; i<columns.size(); i++) {
						ResourceColumn column = columns.get(i);
						Object set_data = column.convertMappingData(resource);
						if( set_data != null) {
							setStatement(setIndex,statement, set_data);
							setIndex++;
						}
					}
					statement.execute();					
				}catch (Exception e) {
					logger.info("query  : {}", query_string);
					logger.info("resource : {}", resource.toString(true));
					throw new RepositoryException(e.getMessage(), e);
				}finally {
		        	statement.close();
				}
			}
			
		}catch (Exception e) {
			logger.error("{}",e.getMessage(),e);
			throw new RepositoryException(e.getMessage(),e);
		}finally {
			close(connection, statement, resultSet);
		}
	}
	public void updateGroup(Resource_Object group_object)throws RepositoryException{
		List<ResourceTable> tables = this.groupInputMapper.getTables();
		updateResource(tables,group_object);
	}
	private void updateResource(List<ResourceTable> tables, Resource_Object resource)throws RepositoryException{

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;  

		try {
			connection = getConnection();
			for (ResourceTable table : tables) {
				final String query_string =  table.getUpdateQuery(resource);				
				logger.info("query  : {}", query_string);				
				try {
					statement = connection.prepareStatement(query_string);
					List<ResourceColumn> columns = table.getColumns();
					int setIndex = 1;
					for(int i=0; i<columns.size(); i++) {
						ResourceColumn column = columns.get(i);
						Object set_data = column.convertMappingData(resource);
						if(set_data != null) {
							setStatement(setIndex,statement, set_data);
							setIndex++;
						}
					}
					
					List<ResourceColumn> column_list = table.getPrimaryColumns();
					for (ResourceColumn column : column_list) {
						setStatement(setIndex,statement, column.convertMappingData(resource));
						setIndex++;
					}
					
					statement.executeUpdate();
					
				}catch (Exception e) {					
					logger.info("resource : {}", resource.toString(true));
					
					throw new RepositoryException(e.getMessage(),e);
				}finally {
					logger.info("query  : {}", query_string);
					logger.info("resource : {}", resource);
					
					statement.close();
				}
			}
		}catch (Exception e) {
			throw new RepositoryException("",e);
		}finally {
			close(connection, statement, resultSet);
		}
	}
	public void updateUser(Resource_Object user_object) throws RepositoryException {
		if(this.userInputMapper == null) {
			logger.error("userInputMapper is null ---- ");
		}
		List<ResourceTable> tables = this.userInputMapper.getTables();
		updateResource(tables,user_object );
	}

	protected Resource_Object newResourceFromResultSet(RepositoryOutputMapper outpuMapper, ResultSet resultSet, ResultSetMetaData meta) throws SQLException, RepositoryException {
		Resource_Object resource = new Resource_Object();
		
		Map<String,AttributeSchema> attributes = outpuMapper.getAttributes();
		Set<String> key_set = attributes.keySet();
		for (String key : key_set) {
			AttributeSchema attribute = attributes.get(key);
			ResourceColumn column = attribute.getResourceColumn();
			if(column != null) {
				int index = findMetaIndex(meta, column.getName());

				Object data = getOutObject(index, resultSet,meta);
				if(attribute.getDataMapper() != null) {
					data = attribute.getDataMapper().convert(data);
				}
				resource.put(key,data);
				
				if(attribute.getUniqueness() == Uniqueness.SERVER) {
					resource.setId((String) data);
				}
			}
		}

		return resource;
	}
	private int findMetaIndex(ResultSetMetaData meta, String name) throws SQLException {
		for(int index=1; index<=meta.getColumnCount(); index++) {
			if(meta.getColumnName(index).equals(name)) {
				return index;
			}
		}
		return -1;
	}
}
