package com.wowsanta.scim.repository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wowsanta.scim.object.Resource_Object;
import com.wowsanta.scim.repository.convert.BooleanConverter;
import com.wowsanta.scim.repository.convert.DateConverter;
import com.wowsanta.scim.repository.impl.MsSqlRepository;
import com.wowsanta.scim.util.Random;
import com.wowsanta.scim.util.Random.ORGANIZATION;

public class GW_GroupSchemaMapper_Generator_Test {

	public static final String gw_repository_config_file = "../config/backup_conf_20190429/default_mssql_gw_repository.json";
	public static final String group_resource_schema_file = "../config/backup_conf_20190429/default_group_schema.json";
	
	public static final String gw_group_resource_output_mapper_file = "../config/backup_conf_20190429/default_mssql_gw_group_resource_output_mapper.json";
	public static final String gw_group_resource_input_mapper_file = "../config/backup_conf_20190429/default_mssql_gw_group_resource_input_mapper.json";

	//@Test
	public void get_group_by_out_mapper_test() {
		try {
			GsonBuilder builder = new GsonBuilder().disableHtmlEscaping();
			//builder.setPrettyPrinting();			
			Gson gson = builder.create();
			
			RepositoryOutputMapper group_resource_output_mapper = RepositoryOutputMapper.load(gw_group_resource_output_mapper_file);
			MsSqlRepository repository = MsSqlRepository.load(gw_repository_config_file);
			repository.setGrouptOutputMapper(group_resource_output_mapper);
			repository.initialize();
			
			String groupId = "HeadOffice";
			Resource_Object group_object = repository.getGroup(groupId);
			System.out.println(gson.toJson(group_object));
			
			String from = "2019-01-23 00:00:00";
			SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date modify_date = transFormat.parse(from);
			System.out.println("modify_date : " + transFormat.format(modify_date));
			String filter = "modifyDate ge '" + transFormat.format(modify_date) + "'"; // or id eq \"803148\" ";
			System.out.println("filter : " + filter );
			
			//filter = null;
			
			System.out.println("group count : " + repository.getGroupCount(filter));
			
			List<Resource_Object> group_list = repository.searchGroup(filter, 0,5,repository.getGroupCount(filter));
			for (Resource_Object resource_Object : group_list) {
				System.out.println("resource_Object : {}" + gson.toJson(resource_Object) );
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}	
		
	}
	
	@Test
	public void set_group_by_input_mapper_test() {

		try {
			GsonBuilder builder = new GsonBuilder().disableHtmlEscaping();
			builder.setPrettyPrinting();
			Gson gson = builder.create();

			RepositoryInputMapper group_resource_input_mapper = RepositoryInputMapper.load(gw_group_resource_input_mapper_file);

			MsSqlRepository repository = MsSqlRepository.load(gw_repository_config_file);
			repository.initialize();
			repository.setGroupInputMapper(group_resource_input_mapper);

			Resource_Object group_object = new Resource_Object();
			//group_object.put("id", Random.number(100000, 999999));
			group_object.put("active", Random.yn_integer(10));

			final SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
			Date now_date = new Date();
			
			group_object.put("createDate", fmt.format(now_date));
			group_object.put("modifyDate", fmt.format(now_date));
//
//현대백화점그룹;현대캐터링시스템;푸드사업서비스사업부(핸대캐터링);프드서비스3사업부;영남운영1팀;현대중공업;			
//NNAAAA;NNCCAX;NNCCAD;NNCCBH;QE370300;
			
			//ORGANIZATION org = Random.oranization();
			group_object.put("id", "QE370300");
			group_object.put("organizationName", 		"현대중공업");
			group_object.put("organizationCode", 		"QE370300");
			group_object.put("organizationPath", 		"NNAAAA;NNCCAX;NNCCAD;NNCCBH;QE370300;");
			group_object.put("organizationParent", 		"NNCCBH");
			group_object.put("organizationDescription", "현대백화점그룹;현대캐터링시스템;푸드사업서비스사업부(핸대캐터링);프드서비스3사업부;영남운영1팀;현대중공업;");

			
//			group_object.put("id", "QE370301");
//			group_object.put("organizationName", 		"현대중공업1");
//			group_object.put("organizationCode", 		"QE370301");
//			group_object.put("organizationPath", 		"NNAAAA;NNCCAX;NNCCAD;NNCCBT;QE370301;");
//			group_object.put("organizationParent", 		"NNCCBT");
//			group_object.put("organizationDescription", "현대백화점그룹;현대캐터링시스템;푸드사업서비스사업부(핸대캐터링);프드서비스2사업부;영남운영1팀;현대중공업1;");

			System.out.println(gson.toJson(group_object));
			
			repository.createGroup(group_object);
	
			Date update_date = new Date();
			group_object.put("modifyDate", fmt.format(update_date));
			repository.updateGroup(group_object);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//@Test
	public void gen_gw_group_input_out_mapper_test() {
		try {

			ResourceTypeSchema group_schema = ResourceTypeSchema.load(group_resource_schema_file);

			MsSqlRepository repository = MsSqlRepository.load(gw_repository_config_file);
			repository.initialize();
			RepositoryInputMapper group_resource_input_mapper = new RepositoryInputMapper();

			List<ResourceTable> table_list = repository.getTables();
			System.out.println("table list=====");
			for (ResourceTable table : table_list) {
				//System.out.println(table);
				if (table.getName().equals("GW_ORG_INFO")) {
					table.setIndex(0);			
					List<ResourceColumn> columns = repository.getTableColums("GW_ORG_INFO");
					for (ResourceColumn column : columns) {
						System.out.println("colum name " + column.getName());
						if(column.getName().equals("GROUPID")) {
							AttributeSchema attribute = group_schema.getAttribute("id");
							column.setAttributeSchema(attribute);
							column.setType(ResourceType.PrimaryColumn);
						}else if(column.getName().equals("GROUPNAME")) {
							AttributeSchema attribute = group_schema.getAttribute("organizationName");
							column.setAttributeSchema(attribute);
							column.setDefaultValue("현대백화점그룹");
							
						}else if(column.getName().equals("GROUPDESC")) {
							AttributeSchema attribute = group_schema.getAttribute("organizationDescription");
							column.setAttributeSchema(attribute);
						}else if(column.getName().equals("PARRENTID")) {
							AttributeSchema attribute = group_schema.getAttribute("organizationParent");
							column.setAttributeSchema(attribute);
							column.setDefaultValue("AAAA");
							
						}else if(column.getName().equals("ACTIVE")) {
							AttributeSchema attribute = group_schema.getAttribute("active");
							
							DataMapper dataMapper = new DataMapper();
							dataMapper.setClassName(BooleanConverter.class.getCanonicalName());
							dataMapper.setMethodName("integerToYnDefaultY");
							column.setDataMapper(dataMapper);
							
							column.setAttributeSchema(attribute);
						}else if(column.getName().equals("CREATEDATE")) {
							AttributeSchema attribute = group_schema.getAttribute("createDate");
							column.setAttributeSchema(attribute);
							
							DataMapper dataMapper = new DataMapper();
							dataMapper.setClassName(DateConverter.class.getCanonicalName());
							dataMapper.setMethodName("stringToSqlTimestampDefaultCurrentTime");
							column.setDataMapper(dataMapper);
						}else if(column.getName().equals("MODIFYDATE")) {
							AttributeSchema attribute = group_schema.getAttribute("modifyDate");
							column.setAttributeSchema(attribute);
							
							DataMapper dataMapper = new DataMapper();
							dataMapper.setClassName(DateConverter.class.getCanonicalName());
							dataMapper.setMethodName("stringToSqlTimestampDefaultCurrentTime");
							column.setDataMapper(dataMapper);
						}else if(column.getName().equals("GROUPPATH")) {
							AttributeSchema attribute = group_schema.getAttribute("organizationPath");
							column.setAttributeSchema(attribute);
						}else {
							System.out.println("not used colum name " + column.getName());
						}
					}
					table.setColumns(columns);
					group_resource_input_mapper.addTable(table);
					
				}
			}
			System.out.println(group_resource_input_mapper.toString(false));
			group_resource_input_mapper.save(gw_group_resource_input_mapper_file);
			
			
			Set<String> key_set = group_schema.getAttributes().keySet();
			ResourceTable table = group_resource_input_mapper.getTable("GW_ORG_INFO");
			RepositoryOutputMapper group_resource_output_mapper = new RepositoryOutputMapper();
			
			group_resource_output_mapper.setTable(table);
			
			for (String key : key_set) {
				System.out.println("key : " + key);
				
				AttributeSchema attribute  = group_schema.getAttribute(key);
				if(key.equals("id")) {
					ResourceColumn column = table.getColumn("GROUPID");
					column.setAttributeSchema(null);
					
					attribute.setResourceColumn(column);
				}else if(key.equals("active")) {
					ResourceColumn column = table.getColumn("ACTIVE");
					column.setAttributeSchema(null);
					
					DataMapper dataMapper = new DataMapper();
					dataMapper.setClassName(BooleanConverter.class.getCanonicalName());
					dataMapper.setMethodName("ynToIntegerDefaultZero");
					column.setDataMapper(dataMapper);
					
					attribute.setResourceColumn(column);
				}else if(key.equals("organizationPath")) {
					ResourceColumn column = table.getColumn("GROUPPATH");
					column.setAttributeSchema(null);
					
					attribute.setResourceColumn(column);
					attribute.setDefaultValue("AAAA");
					
				}else if(key.equals("organizationCode")) {
					ResourceColumn column = table.getColumn("GROUPID");
					column.setAttributeSchema(null);
					attribute.setResourceColumn(column);
					
				}else if(key.equals("organizationName")) {
					ResourceColumn column = table.getColumn("GROUPNAME");
					column.setAttributeSchema(null);
					attribute.setResourceColumn(column);
					attribute.setDefaultValue("현대백화점그룹");

				}else if(key.equals("organizationParent")) {
					ResourceColumn column = table.getColumn("PARRENTID");
					column.setAttributeSchema(null);
					attribute.setResourceColumn(column);
					attribute.setDefaultValue("AAAA");
					
				}else if(key.equals("organizationDescription")) {
					ResourceColumn column = table.getColumn("GROUPDESC");
					column.setAttributeSchema(null);
					attribute.setResourceColumn(column);
					attribute.setDefaultValue("AAAA");
					
				}else if(key.equals("createDate")) {
					ResourceColumn column = table.getColumn("CREATEDATE");
					column.setAttributeSchema(null);

					attribute.setResourceColumn(column);
					DataMapper dataMapper = new DataMapper();
					dataMapper.setClassName(DateConverter.class.getCanonicalName());
					dataMapper.setMethodName("sqlTimestampToStringDefaultCurrenttime");
					
					attribute.setDataMapper(dataMapper);
				}else if(key.equals("modifyDate")) {
					ResourceColumn column = table.getColumn("MODIFYDATE");
					column.setAttributeSchema(null);

					
					attribute.setResourceColumn(column);
					DataMapper dataMapper = new DataMapper();
					dataMapper.setClassName(DateConverter.class.getCanonicalName());
					dataMapper.setMethodName("sqlTimestampToStringDefaultCurrenttime");
					
					attribute.setDataMapper(dataMapper);
					
				}else {
					System.out.println("not used : " + key);
				}
				
				group_resource_output_mapper.putAttribute(key, attribute);
			}
			
			System.out.println(group_resource_output_mapper.toString(false));
			group_resource_output_mapper.save(gw_group_resource_output_mapper_file);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	//@Test
//	public void gen_gw_group_output_mapper_test() {
//		try {
//			GsonBuilder builder = new GsonBuilder().disableHtmlEscaping();
//			builder.setPrettyPrinting();
//			Gson gson = builder.create();
//
//			RepositoryOutputMapper group_resource_output_mapper = new RepositoryOutputMapper();
//
//			MsSqlRepository repository = MsSqlRepository.load(gw_repository_config_file);
//			repository.initialize();
//
//			List<ResourceTable> table_list = repository.getTables();
//			for (ResourceTable table : table_list) {
//				System.out.println("table : " + gson.toJson(table));
//				if (table.getName().equals("GW_ORG_INFO")) {
//					table.setColumns(repository.getTableColums("GW_ORG_INFO"));
//					table.getColumn("GROUPID").setType(ResourceType.PrimaryColumn);
//
//					DataMapper id = new DataMapper();
//					id.setId("id");
//					table.getColumn("GROUPID").setDataMapper(id);
//
//					group_resource_output_mapper.setTable(table);
//					System.out.println(gson.toJson(table));
//				}
//			}
//
//			FileWriter writer = new FileWriter(gw_group_resource_output_schema_file);
//			writer.write(gson.toJson(group_resource_output_mapper));
//			writer.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
}
