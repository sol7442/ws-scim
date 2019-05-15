package com.wowsanta.scim.repository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wowsanta.scim.object.Resource_Object;
import com.wowsanta.scim.repository.impl.OracleRepository;

public class IM_SystemUser_SchemaMapper_Generator_Test {

	public static final String im_repository_config_file = "../config/backup_conf_20190429/default_oracle_im_repository.json";
	public static final String user_resource_schema_file = "../config/backup_conf_20190429/default_user_schema.json";
	
	public static final String im_system_user_resource_output_mapper_file = "../config/backup_conf_20190429/default_oracle_im_system_user_resource_output_mapper.json";
	public static final String im_system_user_resource_output_schema_file = "../config/backup_conf_20190429/default_oracle_im_system_user_resource_output_schema.json";
	public static final String im_system_user_resource_input_mapper_file = "../config/backup_conf_20190429/default_oracle_im_system_user_resource_input_mapper.json";
	public static final String im_system_user_resource_input_schema_file = "../config/backup_conf_20190429/default_oracle_im_system_user_resource_input_schema.json";

	
	
	@Test
	public void get_user_by_out_mapper_test() {
		try {
			GsonBuilder builder = new GsonBuilder().disableHtmlEscaping();
			//builder.setPrettyPrinting();			
			Gson gson = builder.create();
			
			RepositoryOutputMapper system_user_resource_output_mapper = RepositoryOutputMapper.load(im_system_user_resource_output_mapper_file);
			
			SCIMRepositoryManager repository_manager = SCIMRepositoryManager.load(im_repository_config_file);
			repository_manager.initailze();
			OracleRepository repository = (OracleRepository) repository_manager.getResourceRepository();//OracleRepository.load(im_repository_config_file);
			
			
			
			AttributeValue system_id_value = new AttributeValue("systemId","sys-scim-sso");
			AttributeValue user_id_value = new AttributeValue("id","69258042");
			
			List<AttributeValue> attribute_list = new ArrayList<AttributeValue>();
			attribute_list.add(system_id_value);
			attribute_list.add(user_id_value);
			
			Resource_Object user_object  = repository.getSystemUser(system_user_resource_output_mapper, attribute_list);
			System.out.println(user_object.toString());
			
			String filter = "systemId eq sys-scim-sso";
			int count = repository.getSystemUserCount(system_user_resource_output_mapper, filter);
			System.out.println(count);

			List<Resource_Object> user_list = repository.searchSystemUser(system_user_resource_output_mapper, filter, 0,100,count);
			for (Resource_Object resource_Object : user_list) {
				System.out.println("resource : " + gson.toJson(resource_Object) );
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}	
		
	}
	//@Test
	public void gen_im_systemuser_input_out_mapper_test() {
		
		try {
			ResourceTypeSchema user_schema = ResourceTypeSchema.load(user_resource_schema_file);
			//System.out.println(user_schema.toString(true));
			SCIMRepositoryManager repository_mgr = SCIMRepositoryManager.load(im_repository_config_file).initailze();
			OracleRepository repository = (OracleRepository) repository_mgr.getResourceRepository();
			
			RepositoryInputMapper systemuser_resource_input_mapper = new RepositoryInputMapper();

			List<ResourceTable> table_list = repository.getTables();
			System.out.println("table list=====");
			for (ResourceTable table : table_list) {
				if (table.getName().equals("SCIM_SYSTEM_USER")) {
					table.setIndex(0);			
					List<ResourceColumn> columns = repository.getTableColums("SCIM_SYSTEM_USER");
					for (ResourceColumn column : columns) {
						if(column.getName().equals("USERID")) {
							AttributeSchema attribute = user_schema.getAttribute("id");
							column.setAttributeSchema(attribute);
							column.setType(ResourceType.PrimaryColumn);
							
						}else if( column.getName().equals("SYSTEMID")) {
							AttributeSchema attribute = user_schema.getAttribute("systemId");
							column.setAttributeSchema(attribute);
							column.setType(ResourceType.PrimaryColumn);
						}else if( column.getName().equals("USERNAME")) {
							AttributeSchema attribute = user_schema.getAttribute("name");
							column.setAttributeSchema(attribute);
						}else if( column.getName().equals("EXTERNALID")) {
							AttributeSchema attribute = user_schema.getAttribute("externalId");
							column.setAttributeSchema(attribute);
						}else if( column.getName().equals("ACTIVE")) {
							AttributeSchema attribute = user_schema.getAttribute("active");
							column.setAttributeSchema(attribute);
						}else if( column.getName().equals("CREATEDATE")) {
							AttributeSchema attribute = user_schema.getAttribute("createDate");
							column.setAttributeSchema(attribute);
						}else if( column.getName().equals("MODIFYDATE")) {
							AttributeSchema attribute = user_schema.getAttribute("modifyDate");
							column.setAttributeSchema(attribute);
						}else if( column.getName().equals("LASTACCESSDATE")) {
							AttributeSchema attribute = user_schema.getAttribute("lastAccessDate");
							column.setAttributeSchema(attribute);
						}else if( column.getName().equals("PROVISIONDATE")) {
							AttributeSchema attribute = user_schema.getAttribute("provisionDate");
							column.setAttributeSchema(attribute);
						}else {
							System.out.println("not used colum name " + column.getName());
						}
					}
					table.setColumns(columns);
					systemuser_resource_input_mapper.addTable(table);
				}
			}
			
			System.out.println(systemuser_resource_input_mapper.toString(false));
			systemuser_resource_input_mapper.save(im_system_user_resource_input_mapper_file);
			
			ResourceTable out_table = null;// user_resource_input_mapper.getTable("SSO_ORG_PERSON");
			table_list = repository.getTables();
			System.out.println("table list=====");
			for (ResourceTable table : table_list) {
				if (table.getName().equals("SCIM_SYSTEM_USER")) {
					table.setIndex(0);	
					List<ResourceColumn> columns = repository.getTableColums("SCIM_SYSTEM_USER");
					table.setColumns(columns);
					out_table = table;
				}
			}
			
			
			RepositoryOutputMapper system_user_resource_output_mapper = new RepositoryOutputMapper();
			system_user_resource_output_mapper.setTable(out_table);
			
			Set<String> key_set = user_schema.getAttributes().keySet();
			for (String key : key_set) {
				AttributeSchema attribute  = user_schema.getAttribute(key);
				if(key.equals("id")) {
					ResourceColumn column = out_table.getColumn("USERID");
					attribute.setResourceColumn(column);
				}else if(key.equals("name")) {
					ResourceColumn column = out_table.getColumn("USERNAME");
					attribute.setResourceColumn(column);
				}else if(key.equals("externalId")) {
					ResourceColumn column = out_table.getColumn("EXTERNALID");
					attribute.setResourceColumn(column);
				}else if(key.equals("systemId")) {
					ResourceColumn column = out_table.getColumn("SYSTEMID");
					attribute.setResourceColumn(column);
				}else if(key.equals("active")) {
					ResourceColumn column = out_table.getColumn("ACTIVE");
					attribute.setResourceColumn(column);
				}else if(key.equals("createDate")) {
					ResourceColumn column = out_table.getColumn("CREATEDATE");
					attribute.setResourceColumn(column);
				}else if(key.equals("modifyDate")) {
					ResourceColumn column = out_table.getColumn("MODIFYDATE");
					attribute.setResourceColumn(column);
				}else if(key.equals("lastAccessDate")) {
					ResourceColumn column = out_table.getColumn("LASTACCESSDATE");
					attribute.setResourceColumn(column);
				}else if(key.equals("provisionDate")) {
					ResourceColumn column = out_table.getColumn("PROVISIONDATE");
					attribute.setResourceColumn(column);
				}else {
					System.out.println("not used : " + key);
				}
				
				system_user_resource_output_mapper.putAttribute(key, attribute);
			}
			
			System.out.println(system_user_resource_output_mapper.toString(false));
			system_user_resource_output_mapper.save(im_system_user_resource_output_mapper_file);
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
