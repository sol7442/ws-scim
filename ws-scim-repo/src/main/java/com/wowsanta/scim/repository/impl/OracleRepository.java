package com.wowsanta.scim.repository.impl;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.wowsanta.scim.object.Resource_Object;
import com.wowsanta.scim.repo.rdb.DBCP;
import com.wowsanta.scim.repository.AttributeSchema;
import com.wowsanta.scim.repository.AttributeValue;
import com.wowsanta.scim.repository.DataMapper;
import com.wowsanta.scim.repository.RepositoryException;
import com.wowsanta.scim.repository.RepositoryOutputMapper;
import com.wowsanta.scim.repository.ResourceColumn;
import com.wowsanta.scim.repository.ResourceTable;
import com.wowsanta.scim.repository.ResourceType;
import com.wowsanta.scim.repository.SCIMRepositoryController;
import com.wowsanta.scim.schema.SCIM_Repository_Constans;

import oracle.net.aso.c;

public class OracleRepository extends DefaultRepository implements SCIMRepositoryController{

	public static OracleRepository load(String json_config_file) throws RepositoryException {
		logger.info("REPOSITORY LOAD : {} ", json_config_file);
		try {
			Gson gson = new GsonBuilder().disableHtmlEscaping().create();
			JsonReader reader = new JsonReader(new FileReader(json_config_file));
			OracleRepository repository =  gson.fromJson(reader, OracleRepository.class);
			
			return repository;
			
		} catch (Exception e) {
			logger.error("REPOSITORY LOAD FAILED : {} - {}", json_config_file, e.getMessage());
			throw new RepositoryException("REPOSITORY LOAD FAILED ", e);
		}
	}
	
	public List<ResourceTable> getTables() throws RepositoryException{
		final String selectSQL = "SELECT * FROM TAB";
		
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
				table.addSchema(SCIM_Repository_Constans.WOWSTAN_REPOSITORY_ORACLE_TABLE_URI);
				for (int i = 1; i <= meta.getColumnCount(); i++) {
					table.addAttribute(meta.getColumnName(i), resultSet.getString(meta.getColumnName(i)));
				}
				table.setId(resultSet.getString("TNAME"));
				table.setName(resultSet.getString("TNAME"));
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

	public  List<ResourceColumn> getTableColums(String tableName) throws RepositoryException{
		final String selectSQL = "SELECT * FROM COLS WHERE TABLE_NAME=?";

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		List<ResourceColumn> column_list = new ArrayList<ResourceColumn>();
		try {
			connection = getConnection();
			statement = connection.prepareStatement(selectSQL);
			statement.setString(1, tableName);

			resultSet = statement.executeQuery();
			ResultSetMetaData meta = resultSet.getMetaData();

			while (resultSet.next()) {
				ResourceColumn colum = new ResourceColumn();
				colum.addSchema(SCIM_Repository_Constans.WOWSTAN_REPOSITORY_ORACLE_COlUMN_URI);
				for (int i = 1; i <= meta.getColumnCount(); i++) {
					colum.addAttribute(meta.getColumnName(i), resultSet.getString(meta.getColumnName(i)));
				}
				colum.setId(resultSet.getString("COLUMN_NAME"));
				colum.setName(resultSet.getString("COLUMN_NAME"));
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
	
	
	public List<Resource_Object> searchSystemUser(RepositoryOutputMapper outMapper, String filter,int startIndex, int pageCount, int totalCount)throws RepositoryException{
		startIndex++; // oracle -- start index = 1;			
		if(pageCount <= 0) {
			pageCount = totalCount;
		}
		
		if(totalCount == 0) {
			return new ArrayList<Resource_Object>();
		}
		
		int startCount = (startIndex - 1) * pageCount + 1;   
		int endCount = startIndex * pageCount; 				 
		if(endCount >totalCount) {
			endCount = totalCount;
		}
		
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT * FROM ");
		buffer.append("(SELECT ROWNUM AS NUM , A.* FROM (");
		buffer.append(outMapper.getSearchQuery(filter));
		buffer.append(") A").append(" ");
		buffer.append("WHERE ROWNUM <=?)");
		buffer.append(" B WHERE B.NUM>=?");
		
		return searchResource(outMapper, startCount, endCount, buffer.toString());
	}
	
	@Override
	public List<Resource_Object> searchGroup(String filter,int startIndex, int pageCount, int totalCount)throws RepositoryException  {

		startIndex++; // oracle -- start index = 1;			
		if(pageCount <= 0) {
			pageCount = totalCount;
		}
		
		if(totalCount == 0) {
			return new ArrayList<Resource_Object>();
		}
		
		int startCount = (startIndex - 1) * pageCount + 1;   
		int endCount = startIndex * pageCount; 				 
		if(endCount >totalCount) {
			endCount = totalCount;
		}
		
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT * FROM ");
		buffer.append("(SELECT ROWNUM AS NUM , A.* FROM (");
		buffer.append(groupOutputMapper.getSearchQuery(filter));
		buffer.append(") A").append(" ");
		buffer.append("WHERE ROWNUM <=?)");
		buffer.append(" B WHERE B.NUM>=?");
		
		return searchResource(this.groupOutputMapper, startCount, endCount, buffer.toString());
	}

	@Override
	public List<Resource_Object> searchUser(String filter,int startIndex, int pageCount, int totalCount)throws RepositoryException  {
		
		startIndex++; // oracle -- start index = 1;			
		if(pageCount <= 0) {
			pageCount = totalCount;
		}
		
		if(totalCount == 0) {
			return new ArrayList<Resource_Object>();
		}
		
		int startCount = (startIndex - 1) * pageCount + 1;   
		int endCount = startIndex * pageCount; 				 
		if(endCount >totalCount) {
			endCount = totalCount;
		}

		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT * FROM ");
		buffer.append("(SELECT ROWNUM AS NUM , A.* FROM (");
		buffer.append(userOutputMapper.getSearchQuery(filter));
		buffer.append(") A").append(" ");
		buffer.append("WHERE ROWNUM <=?)");
		buffer.append(" B WHERE B.NUM>=?");

		return searchResource(this.userOutputMapper,startCount, endCount, buffer.toString());
	}
	
	private List<Resource_Object> searchResource(RepositoryOutputMapper outputMapper, int startCount, int endCount, final String query_string) throws RepositoryException {
		List<Resource_Object> resource_list = new ArrayList<Resource_Object>();
		
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = getConnection();
			statement  = connection.prepareStatement(query_string);
		    statement.setInt(1, endCount);
		    statement.setInt(2, startCount);
		    
		    resultSet = statement.executeQuery();
			ResultSetMetaData meta = resultSet.getMetaData();
			while (resultSet.next()) {
				resource_list.add(newResourceFromResultSet(outputMapper, resultSet, meta));
			}

        	statement.execute();
        	statement.close();
        	
		}catch (Exception e) {
			throw new RepositoryException(e.getMessage(), e);
		}finally {
			close(connection, statement, resultSet);
			logger.info("query  : {}", query_string);
			logger.info("result : {}", resource_list.size());
		}
		
		return resource_list;
	}
	
}