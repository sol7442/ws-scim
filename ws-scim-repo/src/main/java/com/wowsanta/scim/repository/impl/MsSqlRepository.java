package com.wowsanta.scim.repository.impl;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.obj.SCIMResource2;
import com.wowsanta.scim.object.Resource_Object;
import com.wowsanta.scim.object.SCIM_Resource;
import com.wowsanta.scim.object.SCIM_User;
import com.wowsanta.scim.repo.rdb.DBCP;
import com.wowsanta.scim.repository.AttributeSchema;
import com.wowsanta.scim.repository.AttributeValue;
import com.wowsanta.scim.repository.RepositoryException;
import com.wowsanta.scim.repository.RepositoryOutputMapper;
import com.wowsanta.scim.repository.ResourceColumn;
import com.wowsanta.scim.repository.ResourceTable;
import com.wowsanta.scim.repository.ResourceType;
import com.wowsanta.scim.repository.SCIMRepositoryController;
import com.wowsanta.scim.schema.SCIM_Repository_Constans;

public class MsSqlRepository extends DefaultRepository implements SCIMRepositoryController {

	public static MsSqlRepository load(String json_config_file) throws RepositoryException {
		logger.info("REPOSITORY LOAD : {} ", json_config_file);
		try {
			Gson gson = new GsonBuilder().disableHtmlEscaping().create();
			JsonReader reader = new JsonReader(new FileReader(json_config_file));
			MsSqlRepository repository =  gson.fromJson(reader, MsSqlRepository.class);
			
			return repository;
		} catch (Exception e) {
			logger.error("REPOSITORY LOAD FAILED : {} - {}", json_config_file, e.getMessage());
			throw new RepositoryException("REPOSITORY LOAD FAILED ", e);
		}
	}
	
	public List<ResourceTable> getTables() throws RepositoryException {
		final String selectSQL = "select * from information_schema.tables";

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		List<ResourceTable> table_list = new ArrayList<ResourceTable>();

		try {
			connection = getConnection();
			statement = connection.prepareStatement(selectSQL);
			resultSet = statement.executeQuery();
			ResultSetMetaData meta = resultSet.getMetaData();
			while (resultSet.next()) {
				ResourceTable table = new ResourceTable();
				table.addSchema(SCIM_Repository_Constans.WOWSTAN_REPOSITORY_MSSQL_TABLE_URI);
				for (int i = 1; i <= meta.getColumnCount(); i++) {
					table.addAttribute(meta.getColumnName(i), resultSet.getString(meta.getColumnName(i)));
				}
				table.setId(resultSet.getString("TABLE_NAME"));
				table.setName(resultSet.getString("TABLE_NAME"));
				table_list.add(table);
			}
		} catch (SQLException e) {
			logger.error("validate failed : ", e);
			throw new RepositoryException(selectSQL, e);
		} finally {
			DBCP.close(connection, statement, resultSet);
		}
		logger.info("REPOSITORY VAILDATE : {} ", selectSQL);

		return table_list;
	}

	public  List<ResourceColumn> getTableColums(String tableName, String keyColumn) throws RepositoryException{
		final String selectSQL = "SELECT * FROM "+tableName+" ORDER BY "+ keyColumn +" DESC OFFSET 0 ROWS FETCH NEXT 1 ROWS ONLY ;";

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		List<ResourceColumn> column_list = new ArrayList<ResourceColumn>();
		try {
			connection = getConnection();
			statement = connection.prepareStatement(selectSQL);
			resultSet = statement.executeQuery();
			ResultSetMetaData meta = resultSet.getMetaData();
			int column_count = meta.getColumnCount();
			for(int i=1; i<=column_count; i++) {
				ResourceColumn colum = new ResourceColumn();
				
				colum.setName(meta.getColumnName(i));
				colum.setLabel(meta.getColumnLabel(i));
				colum.setPrecision(meta.getPrecision(i));
				colum.setDisplaySize(meta.getColumnDisplaySize(i));
				colum.setType(meta.getColumnType(i));
				colum.setTypeName(meta.getColumnTypeName(i));
				colum.setClassName(meta.getColumnClassName(i));
				colum.setIsNullable(meta.isNullable(i));
				colum.setAutoIncrement(meta.isAutoIncrement(i));
				colum.setCaseSensitive(meta.isCaseSensitive(i));
				colum.setCurrency(meta.isCurrency(i));
				colum.setDefinitelyWritable(meta.isDefinitelyWritable(i));
				colum.setReadOnly(meta.isReadOnly(i));
				colum.setSigned(meta.isSigned(i));
				colum.setWritable(meta.isWritable(i));
				colum.setSearchable(meta.isSearchable(i));
				
				column_list.add(colum);
			}
		} catch (SQLException e) {
			logger.error("validate failed : ", e);
			throw new RepositoryException(selectSQL, e);
		} finally {
			DBCP.close(connection, statement, resultSet);
		}
		logger.info("REPOSITORY VAILDATE : {} ", selectSQL);

		return column_list;
	}
	
//	public List<ResourceColumn> getTableColums(String tableName) throws RepositoryException {
//		final String selectSQL = "SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME=?";
//
//		Connection connection = null;
//		PreparedStatement statement = null;
//		ResultSet resultSet = null;
//
//		List<ResourceColumn> column_list = new ArrayList<ResourceColumn>();
//		try {
//			connection = getConnection();
//			statement = connection.prepareStatement(selectSQL);
//			statement.setString(1, tableName);
//
//			resultSet = statement.executeQuery();
//			ResultSetMetaData meta = resultSet.getMetaData();
//
//			while (resultSet.next()) {
//				ResourceColumn colum = new ResourceColumn();
//				colum.addSchema(SCIM_Repository_Constans.WOWSTAN_REPOSITORY_MSSQL_COlUMN_URI);
//				for (int i = 1; i <= meta.getColumnCount(); i++) {
//					colum.addAttribute(meta.getColumnName(i), resultSet.getString(meta.getColumnName(i)));
//				}
//				colum.setId(resultSet.getString("COLUMN_NAME"));
//				colum.setName(resultSet.getString("COLUMN_NAME"));
//				column_list.add(colum);
//			}
//		} catch (SQLException e) {
//			logger.error("validate failed : ", e);
//			throw new RepositoryException(selectSQL, e);
//		} finally {
//			DBCP.close(connection, statement, resultSet);
//		}
//		logger.info("REPOSITORY VAILDATE : {} ", selectSQL);
//
//		return column_list;
//	}
	
	public List<Resource_Object> searchSystemUser(RepositoryOutputMapper outMapper,	List<AttributeValue> attribute_list, int startIndex, int pageCount, int totalCount)  throws RepositoryException{
		return null;
	}
	public List<Resource_Object> searchSystemUser(RepositoryOutputMapper outMapper, String filter,int startIndex, int pageCount, int totalCount)throws RepositoryException{
		if(pageCount <= 0) {
			pageCount = totalCount;
		}
		
		if(totalCount == 0) {
			return new ArrayList<Resource_Object>();
		}
		
		ResourceColumn primaryColumn = null;
		AttributeSchema key_attribute = outMapper.getKeyAttribte();
		if(key_attribute != null) {
			primaryColumn = key_attribute.getResourceColumn();
		}
		
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append(outMapper.getSearchQuery(filter)).append(" ");
		
		sqlBuffer.append("ORDER BY").append(" ");
		sqlBuffer.append(primaryColumn.getName()).append(" ");
		
		sqlBuffer.append("DESC").append(" ");
		sqlBuffer.append("OFFSET ").append(startIndex).append(" ROWS").append(" ");
		sqlBuffer.append("FETCH NEXT ").append(pageCount).append(" ROWS ONLY").append(" ");
		
		return searchResource( outMapper, sqlBuffer.toString());
	}
	@Override
	public List<Resource_Object> searchUser(String filter, int startIndex, int pageCount, int totalCount) throws RepositoryException {

		if(pageCount <= 0) {
			pageCount = totalCount;
		}
		
		if(totalCount == 0) {
			return new ArrayList<Resource_Object>();
		}
		
		ResourceColumn primaryColumn = null;
		AttributeSchema key_attribute = this.userOutputMapper.getKeyAttribte();
		if(key_attribute != null) {
			primaryColumn = key_attribute.getResourceColumn();
		}
		
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append(this.userOutputMapper.getSearchQuery(filter)).append(" ");
		
		sqlBuffer.append("ORDER BY").append(" ");
		sqlBuffer.append(primaryColumn.getName()).append(" ");
		
		sqlBuffer.append("DESC").append(" ");
		sqlBuffer.append("OFFSET ").append(startIndex).append(" ROWS").append(" ");
		sqlBuffer.append("FETCH NEXT ").append(pageCount).append(" ROWS ONLY").append(" ");
		
		return searchResource( this.userOutputMapper, sqlBuffer.toString());
	}

	private List<Resource_Object> searchResource(RepositoryOutputMapper outputMapper, final String query_string) throws RepositoryException {
		List<Resource_Object> resource_list = new ArrayList<Resource_Object>();

		Connection connection = null;
		PreparedStatement statement = null;
	    ResultSet resultSet = null; 
	    
		try {
        	connection = getConnection();
        	statement  = connection.prepareStatement(query_string);
        	resultSet = statement.executeQuery();
			ResultSetMetaData meta = resultSet.getMetaData();
        	while(resultSet.next()) {
        		resource_list.add(newResourceFromResultSet(outputMapper, resultSet, meta));
        	}
        	
		} catch (SQLException e) {
			throw new RepositoryException(query_string, e);
		}finally {
			close(connection, statement, resultSet);
			logger.info("query  : {}", query_string);
			logger.info("result : {}", resource_list.size());
		}
		
		return resource_list;
	}

	@Override
	public List<Resource_Object> searchGroup(String filter, int startIndex, int pageCount, int totalCount) throws RepositoryException {
		if(pageCount <= 0) {
			pageCount = totalCount;
		}
		
		if(totalCount == 0) {
			return new ArrayList<Resource_Object>();
		}
		
	    ResourceColumn primaryColumn = null;
		AttributeSchema key_attribute = this.groupOutputMapper.getKeyAttribte();
		if(key_attribute != null) {
			primaryColumn = key_attribute.getResourceColumn();
		}
		
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append(this.groupOutputMapper.getSearchQuery(filter)).append(" ");
		
		sqlBuffer.append("ORDER BY").append(" ");
		sqlBuffer.append(primaryColumn.getId()).append(" ");
		
		sqlBuffer.append("DESC").append(" ");
		sqlBuffer.append("OFFSET ").append(startIndex).append(" ROWS").append(" ");
		sqlBuffer.append("FETCH NEXT ").append(pageCount).append(" ROWS ONLY").append(" ");
		
		return searchResource( this.groupOutputMapper, sqlBuffer.toString());
	}
}
