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
import com.wowsanta.scim.object.SCIM_Resource;
import com.wowsanta.scim.object.SCIM_User;
import com.wowsanta.scim.repo.rdb.DBCP;
import com.wowsanta.scim.repository.RepositoryException;
import com.wowsanta.scim.repository.ResourceColumn;
import com.wowsanta.scim.repository.ResourceTable;
import com.wowsanta.scim.repository.SCIMResourceRepository;
import com.wowsanta.scim.repository.SCIMSchemaMapper;
import com.wowsanta.scim.repository.SchemaMapper;
import com.wowsanta.scim.schema.SCIM_WOWSATA_Constans;

public class MsSqlRepository extends DefaultRepository implements SCIMResourceRepository {

	private String userSchmeaMapperPath;
	private String userResourceMapperPath;
	private String groupSchmeaMapperPath;
	private String groupResourceMapperPath;
	
	 
	
	public static MsSqlRepository load(String json_config_file) throws RepositoryException {
		logger.info("REPOSITORY LOAD : {} ", json_config_file);
		try {
			Gson gson = new GsonBuilder().disableHtmlEscaping().create();
			JsonReader reader = new JsonReader(new FileReader(json_config_file));
			MsSqlRepository repository =  gson.fromJson(reader, MsSqlRepository.class);
			
			if(repository.userSchmeaMapperPath != null) {
				SCIMSchemaMapper userSchemMapper = SCIMSchemaMapper.load(repository.userSchmeaMapperPath);
				repository.setUserSchemaMapper(userSchemMapper);
			}
			
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
				table.addSchema(SCIM_WOWSATA_Constans.WOWSTAN_REPOSITORY_MSSQL_TABLE_URI);
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

	public List<ResourceColumn> getTableColums(String tableName) throws RepositoryException {
		final String selectSQL = "SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME=?";

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
				colum.addSchema(SCIM_WOWSATA_Constans.WOWSTAN_REPOSITORY_MSSQL_COlUMN_URI);
				for (int i = 1; i <= meta.getColumnCount(); i++) {
					colum.addAttribute(meta.getColumnName(i), resultSet.getString(meta.getColumnName(i)));
				}
				colum.setId(resultSet.getString("COLUMN_NAME"));
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
	
	public List<SCIM_Resource> findUsers(String where, String order, int start_index, int page_count)throws RepositoryException{
		ResourceTable table = this.userSchmeaMapper.getResourceTable();
		ResourceColumn id_column =  this.userSchmeaMapper.getIdColumn();
		
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append("SELECT * FROM ").append(" ");
		sqlBuffer.append(table.getName()).append(" ");
		if(where != null) {
			sqlBuffer.append("WHERE").append(" ");
			sqlBuffer.append(where).append(" ");
		}
		
		sqlBuffer.append("ORDER BY").append(" ");
		if(order != null) {
			sqlBuffer.append(order).append(" ");
		}else {
			sqlBuffer.append(id_column.getId()).append(" ");
		}
		sqlBuffer.append("DESC").append(" ");
		sqlBuffer.append("OFFSET ").append(start_index).append(" ROWS").append(" ");
		sqlBuffer.append("FETCH NEXT ").append(page_count).append(" ROWS ONLY").append(" ");
		
		Connection connection = null;
		PreparedStatement statement = null;
	    ResultSet resultSet = null;              

	    List<SCIM_Resource> user_list = new ArrayList<SCIM_Resource>();
	    
        try {
        	connection = getConnection();
        	statement  = connection.prepareStatement(sqlBuffer.toString());

        	resultSet = statement.executeQuery();
        	while(resultSet.next()) {
        		SCIM_Resource user = new SCIM_Resource();	
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
				
				user_list.add(user);
        	}
        	
		} catch (SQLException e) {
			throw new RepositoryException(sqlBuffer.toString(), e);
		}finally {
			DBCP.close(connection, statement, resultSet);
		}
        
        return user_list;
	}
	
	public Object getOutObject(ResultSet resultSet, ResourceColumn column) throws RepositoryException {
		try {
			String data_type = (String) column.getAttribute("DATA_TYPE");
			switch (data_type) {
			case "varchar":
			case "nvarchar":
			case "char":
				return resultSet.getString(column.getId());
			case "datetime":
				return resultSet.getDate(column.getId());
			default:
				return null;
			}
		}catch (Exception e) {
			throw new RepositoryException("DATA CONVERT FAILED ",e);
		}
	}
}
