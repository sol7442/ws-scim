package com.wowsanta.scim.repository;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wowsanta.scim.object.SCIM_User;
import com.wowsanta.scim.repository.impl.DefaultRepository;
import com.wowsanta.scim.repository.impl.MsSqlRepository;
import com.wowsanta.scim.schema.SCIMAttributeSchema;
import com.wowsanta.scim.schema.SCIMResourceTypeSchema;
import com.wowsanta.scim.schema.SCIM_WOWSATA_Constans;
import com.wowsanta.scim.schema.SCIMDefinitions.DataType;

public class RepositoryTest {

	public static final String repository_config_file = "../config/default_mssql_repository.json";
	public static final String resource_schema_file = "../config/default_user_schema.json";
	public static final String schema_mapper_file = "../config/default_resource_shema_mapper.json";

	
	
	//@Test
	public void repository_schema_mapper_create_test() {
		
		try {
			
			GsonBuilder builder = new GsonBuilder().disableHtmlEscaping();
			builder.setPrettyPrinting();			
			Gson gson = builder.create();
			
			SCIMResourceTypeSchema user_schema = SCIMResourceTypeSchema.load(this.resource_schema_file);
			
			SCIMSchemaMapper user_schema_mapper = new SCIMSchemaMapper();
			user_schema_mapper.addSchema(SCIM_WOWSATA_Constans.RESOURCE_MAPPER_Groupware_USER_URI);
//			schem_mapper.setUserResourceSchema(user_schema);
			
			
			SCIMResposeMapper resource_mapper = SCIMResposeMapper.load("../config/default_mssql_repository_mapper.json");
			ResourceMapper user_mapper = resource_mapper.getUserResourceMapper("IM_ACCOUNT");
			
			Map<String,SCIMAttributeSchema> schem_attribute_list = user_schema.getAttributes();//.entrySet();
			Set<String> key_set = schem_attribute_list.keySet();
			for (String key : key_set) {
				//System.out.println("key_set : " + key);
				
				SCIMAttributeSchema attribute = user_schema.getAttribute(key);
				if(attribute.getType().equals(DataType.COMPLEX)) {
//					System.out.println(gson.toJson(attribute.getName()));
//					ArrayList<SCIMAttributeSchema> sub_attribute = attribute.getSubAttributes();
//					
//					System.out.println(gson.toJson(sub_attribute));
//					for (SCIMAttributeSchema meta_attribute : sub_attribute) {
//						
//					}
					
				}else {
					List<ResourceColumn> columns = user_mapper.getColumns();
					for(int i=0; i<columns.size();i++) {
						ResourceColumn column = columns.get(i);
						if(key.equals(column.getMapper().getId())) {
							SchemaMapper schema_mapper = new SchemaMapper(attribute,column);
							user_schema_mapper.addSchemaMapper(schema_mapper);
							//schem_mapper.addUserSchemaMapper(user_schema_mapper);
							//System.out.println(gson.toJson(schema_mapper));
						}else {
							//System.out.println(key + " >> " + column.getMapper().getId());
						}
						
					}
				}
			}
			user_schema_mapper.setResourceTable(user_mapper.getTable());
			user_schema_mapper.setIdColumn(user_mapper.getIdColumn());
			System.out.println(gson.toJson(user_schema_mapper));
			
			FileWriter writer = new FileWriter(schema_mapper_file);
			writer.write(gson.toJson(user_schema_mapper));
			writer.close();
			
			//System.out.println(gson.toJson(user_schem_mapper));
			
//			
//			SCIMAttributeSchema meta_schema = user_schema.getAttribute("meta");
//			
//			//System.out.println(meta_schema.getType());
//			SCIMAttributeSchema attribute_meata_create = meta_schema.findSubAttribute("created");
//			ResourceColumn column_regist = user_mapper.getColumn("RegistDate");
//			
//			
////			System.out.println(gson.toJson(attribute_meata_create));
////			System.out.println(gson.toJson(column_regist));
//
//			
//			
//			
//			SCIMSchemaMapper schem_mapper = new SCIMSchemaMapper();
//			SchemaMapper meta_schema_mapper = new SchemaMapper(meta_schema);
//			
//			//meta_schema_mapper.add(meta_schema,null);
//			
//			schem_mapper.addSchemaMapper(meta_schema_mapper);
//			
//			System.out.println(gson.toJson(schem_mapper));
//			
//			//System.out.println(gson.toJson(meta_schema));
//			
			
			
			
//			System.out.println(gson.toJson(schem_mapper));
//			System.out.println(gson.toJson(user_mapper));
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	//@Test
	public void repository_reload_test() {
		try {
			
			GsonBuilder builder = new GsonBuilder().disableHtmlEscaping();
			builder.setPrettyPrinting();			
			Gson gson = builder.create();
			
			
			DefaultRepository repository = MsSqlRepository.load(repository_config_file);
			repository.initialize();
			SCIMResposeMapper resource_mapper = SCIMResposeMapper.load("../config/default_mssql_repository_mapper.json");
			
			
			ResourceMapper user_mapper = resource_mapper.getUserResourceMapper("IM_ACCOUNT");
			
			ResourceMapper meta_mapper = new ResourceMapper();//.getUserResourceMapper("IM_ACCOUNT");
			meta_mapper.setTable(user_mapper.getTable());
			
			meta_mapper.addColumn(user_mapper.getColumn("ModifyDate"));
			meta_mapper.addColumn(user_mapper.getColumn("RegistDate"));
			meta_mapper.addColumn(user_mapper.getColumn("EnterDate"));
			meta_mapper.addColumn(user_mapper.getColumn("RetireDate"));

			resource_mapper.setUserMetaMapper(meta_mapper);
			
			System.out.println(gson.toJson(meta_mapper));
			
			//System.out.println(gson.toJson(resource_mapper));

			
			
			
//			ResourceMapper table_mapper = new ResourceMapper(); 
//
////			SCIMResposeMapper resource_mapper = new SCIMResposeMapper();
////			resource_mapper.addSchema(SCIM_WOWSATA_Constans.RESOURCE_MAPPER_URI);
////			resource_mapper.addUserResourceMapper(table_mapper);
//			
//			
//			resource_mapper.setUserMetaMapper(table_mapper);
//			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void repository_getUser_test() {
		try {
			DefaultRepository repository = MsSqlRepository.load(repository_config_file);
			repository.initialize();
//			SCIMResposeMapper resource_mapper = SCIMResposeMapper.load("../config/default_mssql_repository_mapper.json");
			SCIMSchemaMapper user_schema_mapper = SCIMSchemaMapper.load(schema_mapper_file);
			
//
//			GsonBuilder builder = new GsonBuilder().disableHtmlEscaping();
//			builder.setPrettyPrinting();			
//			Gson gson = builder.create();
//			System.out.println(gson.toJson(resource_mapper));
			
			
			
			repository.setUserSchemaMapper(user_schema_mapper);

			//repository.setResourceMapper(resource_mapper);
			
			SCIM_User user = repository.getUser("69258042");
			
			System.out.println(user);
			
			
			
		} catch (RepositoryException e) {
			e.printStackTrace();
		}
	}
	
	//@Test
	public void get_table_colum_test() {
		try {
			DefaultRepository repository = MsSqlRepository.load(repository_config_file);
			repository.initialize();
			ResourceMapper table_mapper = new ResourceMapper(); 
			
			List<ResourceTable> table_list = repository.getTables();
			System.out.println("table list=====");
			for (ResourceTable table : table_list) {
				System.out.println(table);
				if(table.getAttribute("TABLE_NAME").equals("IM_ACCOUNT")) {
					table_mapper.setTable(table);
					table_mapper.setColumns(repository.getTableColums("IM_ACCOUNT"));
					//table_mapper.setIdColum()
				}
			}
			
			ResourceColumn id_column = table_mapper.getColumn("UR_Code");
			table_mapper.setIdColumn(id_column);
			
			System.out.println("table list=====");
			System.err.println(table_mapper);
			
			
			SCIMResposeMapper resource_mapper = new SCIMResposeMapper();
			resource_mapper.addSchema(SCIM_WOWSATA_Constans.RESOURCE_MAPPER_URI);
			resource_mapper.addUserResourceMapper(table_mapper);
			
			
			ResourceMapper table_mapper_zero = resource_mapper.getUserResourceMapper("IM_ACCOUNT");
			System.out.println(table_mapper_zero);
			ResourceColumn colum_dn_code = table_mapper_zero.getColumn("DN_Code");
			System.out.println(colum_dn_code);
			
			DataMapper emp_mapper = new DataMapper();
			emp_mapper.setId("employeeNumber");
			colum_dn_code.setMapper(emp_mapper);
			
			ResourceColumn colum_exgrouppath = table_mapper_zero.getColumn("ExGroupName");
			System.out.println(colum_dn_code);
			
			DataMapper gr_path_mapper = new DataMapper();
			gr_path_mapper.setId("DIV");
			gr_path_mapper.setClassName("com.wowsanta.scim.repository.impl.DefaultDataMapper");
			gr_path_mapper.setInMethod("divIn");
			gr_path_mapper.setOutMethod("divOut");
			colum_exgrouppath.setMapper(gr_path_mapper);
			
			
//			
			GsonBuilder builder = new GsonBuilder().disableHtmlEscaping();
			builder.setPrettyPrinting();			
			Gson gson = builder.create();
			System.out.println(gson.toJson(resource_mapper));
//
//			FileWriter writer = new FileWriter("../config/default_mssql_repository_mapper.json");
//			writer.write(gson.toJson(resource_mapper));
//			writer.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
