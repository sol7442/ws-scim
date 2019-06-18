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
import com.wowsanta.scim.repository.impl.DefaultRepository;
import com.wowsanta.scim.repository.impl.MsSqlRepository;
import com.wowsanta.scim.util.Random;
import com.wowsanta.scim.util.Random.DUTY;
import com.wowsanta.scim.util.Random.JOB;
import com.wowsanta.scim.util.Random.ORGANIZATION;
import com.wowsanta.scim.util.Random.POSITION;

public class GW_UserSchemaMapper_Generator_Test {

	public static final String gw_repository_config_file = "../config/backup_conf_20190429/default_mssql_gw_repository.json";
	public static final String user_resource_schema_file = "../config/backup_conf_20190429/default_user_schema.json";
	
	
	public static final String gw_user_resource_output_mapper_file = "../config/backup_conf_20190429/default_mssql_gw_user_resource_output_mapper.json";
	public static final String gw_user_resource_output_schema_file = "../config/backup_conf_20190429/default_mssql_gw_user_resource_output_schema.json";
	public static final String gw_user_resource_input_mapper_file = "../config/backup_conf_20190429/default_mssql_gw_user_resource_input_mapper.json";
	public static final String gw_user_resource_input_schema_file = "../config/backup_conf_20190429/default_mssql_gw_user_resource_input_schema.json";

	//@Test
	public void get_user_by_out_mapper_test() {
		try {
			GsonBuilder builder = new GsonBuilder().disableHtmlEscaping();
			//builder.setPrettyPrinting();			
			Gson gson = builder.create();
			
			RepositoryOutputMapper user_resource_output_mapper = RepositoryOutputMapper.load(gw_user_resource_output_mapper_file);
			MsSqlRepository repository = MsSqlRepository.load(gw_repository_config_file);
			repository.setUserOutputMapper(user_resource_output_mapper);
			repository.initialize();
			
			String userId = "717999";
			Resource_Object user_object = repository.getUser(userId);
			System.out.println(user_object.toString(true));
			
			String from = "2019-04-28 21:16:15";
			SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date modify_date = transFormat.parse(from);
			System.out.println("modify_date : " + transFormat.format(modify_date));
			String filter = "modifyDate ge '" + transFormat.format(modify_date) + "'"; // or id eq \"803148\" ";
			System.out.println("filter : " + filter );
			//filter = null;
			System.out.println("user count : " + repository.getUserCount(filter));
			
			List<Resource_Object> user_list = repository.searchUser(filter, 0,100,repository.getUserCount(filter));
			for (Resource_Object resource_Object : user_list) {
				System.out.println("resource_Object : {}" + gson.toJson(resource_Object) );
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}	
		
	}
	@Test
	public void set_user_by_input_mapper_test() {

		try {
			GsonBuilder builder = new GsonBuilder().disableHtmlEscaping();
			builder.setPrettyPrinting();
			Gson gson = builder.create();

			RepositoryInputMapper user_resource_input_mapper = RepositoryInputMapper
					.load(gw_user_resource_input_mapper_file);

			DefaultRepository repository = MsSqlRepository.load(gw_repository_config_file);
			repository.initialize();
			repository.setUserInputMapper(user_resource_input_mapper);

			Resource_Object user_object = new Resource_Object();
			user_object.put("id"	,Random.number(100000,999999));
			user_object.put("name"	,Random.name());
			user_object.put("employeeNumber"	,user_object.get("id"));
			
			int acive_num = Random.yn_integer(10);
			user_object.put("active"			,acive_num);

			final SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
			Date join_date = Random.beforeYears(10);
			
			
			Date now_date = new Date();
//			String join_date_str = fmt.format(join_date);
//			String now_date_str  = fmt.format(now_date);

			user_object.put("joinDate"			, fmt.format(join_date));
			if(acive_num == 0) {
				Date retire_date = Random.date(join_date ,now_date);
				user_object.put("retireDate"	, fmt.format(retire_date));
			}
			//Date now_date = new Date();
			user_object.put("createDate"		, fmt.format(now_date));
			user_object.put("modifyDate"		, fmt.format(now_date));
			
			ORGANIZATION org = Random.oranization();
			user_object.put("organizationName"	, org.getKorean());
			user_object.put("organizationCode"	, String.valueOf(org.ordinal()));
			user_object.put("organizationPath"	, "AAA;QQQ;CCCC;");
						
			POSITION position = Random.position();
			user_object.put("positionName"		, position.getKorean());
			user_object.put("positionCode"		, String.valueOf(position.ordinal()));
						
			DUTY duty = Random.duty();
			user_object.put("rankName"			, duty.getKorean());
			user_object.put("rankCode"			, String.valueOf(duty.ordinal()));
			
			JOB job = Random.job();
			user_object.put("jobName"			, job.getKorean());
			user_object.put("jobCode"			, String.valueOf(job.ordinal()));
			
			
			System.out.println(gson.toJson(user_object));
			
			repository.createUser(user_object);
			
			System.out.println("update ---------------" + user_object.get("name"));
			
			Date update_date = new Date();
			user_object.put("modifyDate"		, fmt.format(update_date));
			repository.updateUser(user_object);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//@Test
	public void gen_gw_user_input_out_mapper_test() {
		try {
			ResourceTypeSchema user_schema = ResourceTypeSchema.load(user_resource_schema_file);
			//System.out.println(user_schema.toString(true));
			
			MsSqlRepository repository = MsSqlRepository.load(gw_repository_config_file);
			repository.initialize();
			RepositoryInputMapper user_resource_input_mapper = new RepositoryInputMapper();

			List<ResourceTable> table_list = repository.getTables();
			//System.out.println("table list=====");
			for (ResourceTable table : table_list) {
				//System.out.println(table);
				if (table.getName().equals("SSO_ORG_PERSON")) {
					table.setIndex(0);			
					List<ResourceColumn> columns = repository.getTableColums("SSO_ORG_PERSON");
					for (ResourceColumn column : columns) {
						//System.out.println("colum name " + column.getName());
						if(column.getName().equals("UR_Code")) {
							AttributeSchema attribute = user_schema.getAttribute("id");
							column.setAttributeSchema(attribute);
							column.setType(ResourceType.PrimaryColumn);
							
						}else if(column.getName().equals("DisplayName")) {
							AttributeSchema attribute = user_schema.getAttribute("name");
							column.setAttributeSchema(attribute);
						}else if(column.getName().equals("ExGroupName")) {
							AttributeSchema attribute = user_schema.getAttribute("organizationName");

							column.setAttributeSchema(attribute);
							column.setDefaultValue("현대백화점그룹");

						}else if(column.getName().equals("ExGroupPath")) {
							AttributeSchema attribute = user_schema.getAttribute("organizationPath");
							
							column.setAttributeSchema(attribute);
							column.setDefaultValue("AAAA");
						}else if(column.getName().equals("JobPositionCode")) {
							AttributeSchema attribute = user_schema.getAttribute("positionCode");
							column.setAttributeSchema(attribute);
						}else if(column.getName().equals("ExJobPositionName")) {
							AttributeSchema attribute = user_schema.getAttribute("positionName");
							column.setAttributeSchema(attribute);
						}else if(column.getName().equals("JobTitleCode")) {
							AttributeSchema attribute = user_schema.getAttribute("jobCode");
							column.setAttributeSchema(attribute);
						}else if(column.getName().equals("ExJobTitleName")) {
							AttributeSchema attribute = user_schema.getAttribute("jobName");
							column.setAttributeSchema(attribute);
						}else if(column.getName().equals("JobLevelCode")) {
							AttributeSchema attribute = user_schema.getAttribute("rankCode");
							column.setAttributeSchema(attribute);
						}else if(column.getName().equals("ExJobLevelName")) {
							AttributeSchema attribute = user_schema.getAttribute("rankName");
							column.setAttributeSchema(attribute);
						}else if(column.getName().equals("IsUse")) {
							AttributeSchema attribute = user_schema.getAttribute("active");
							column.setAttributeSchema(attribute);
							
							DataMapper dataMapper = new DataMapper();
							dataMapper.setClassName(BooleanConverter.class.getCanonicalName());
							dataMapper.setMethodName("integerToYnDefaultY");
							column.setDataMapper(dataMapper);

						}else if(column.getName().equals("EnterDate")) {
							AttributeSchema attribute = user_schema.getAttribute("joinDate");
							column.setAttributeSchema(attribute);
							
							DataMapper dataMapper = new DataMapper();
							dataMapper.setClassName(DateConverter.class.getCanonicalName());
							dataMapper.setMethodName("stringToSqlTimestampDefaultNull");
							column.setDataMapper(dataMapper);
							
						}else if(column.getName().equals("RetireDate")) {
							AttributeSchema attribute = user_schema.getAttribute("retireDate");
							column.setAttributeSchema(attribute);
							
							DataMapper dataMapper = new DataMapper();
							dataMapper.setClassName(DateConverter.class.getCanonicalName());
							dataMapper.setMethodName("stringToSqlTimestampDefaultNull");
							column.setDataMapper(dataMapper);
							
						}else if(column.getName().equals("RegistDate")) {
							AttributeSchema attribute = user_schema.getAttribute("createDate");
							column.setAttributeSchema(attribute);
							
							DataMapper dataMapper = new DataMapper();
							dataMapper.setClassName(DateConverter.class.getCanonicalName());
							dataMapper.setMethodName("stringToSqlTimestampDefaultCurrentTime");
							column.setDataMapper(dataMapper);
							
						}else if(column.getName().equals("ModifyDate")) {
							AttributeSchema attribute = user_schema.getAttribute("modifyDate");
							column.setAttributeSchema(attribute);
							
							DataMapper dataMapper = new DataMapper();
							dataMapper.setClassName(DateConverter.class.getCanonicalName());
							dataMapper.setMethodName("stringToSqlTimestampDefaultCurrentTime");
							column.setDataMapper(dataMapper);
							
						}else if(column.getName().equals("ExGroupCode")) {
							AttributeSchema attribute = user_schema.getAttribute("organizationCode");
							
							column.setAttributeSchema(attribute);
							column.setDefaultValue("AAAA");
						}else {
							System.out.println("not used colum name " + column.getName());
						}
					}
					
					table.setColumns(columns);
					user_resource_input_mapper.addTable(table);
//					table.getColumn("UR_Code").setType(ResourceType.PrimaryColumn);
				}
			}
			System.out.println(user_resource_input_mapper.toString(false));
			user_resource_input_mapper.save(gw_user_resource_input_mapper_file);
			
			
			Set<String> key_set = user_schema.getAttributes().keySet();
			ResourceTable table = user_resource_input_mapper.getTable("SSO_ORG_PERSON");
			RepositoryOutputMapper user_resource_output_mapper = new RepositoryOutputMapper();
			
			user_resource_output_mapper.setTable(table);
			
			for (String key : key_set) {
				AttributeSchema attribute  = user_schema.getAttribute(key);
				if(key.equals("id")) {
					ResourceColumn column = table.getColumn("UR_Code");
					column.setAttributeSchema(null);
					
					attribute.setResourceColumn(column);
				}else if(key.equals("name")) {
					ResourceColumn column = table.getColumn("DisplayName");
					column.setAttributeSchema(null);
					
					attribute.setResourceColumn(column);
				}else if(key.equals("employeeNumber")) {
					ResourceColumn column = table.getColumn("UR_Code");
					column.setAttributeSchema(null);
					
					attribute.setResourceColumn(column);
				}else if(key.equals("organizationPath")) {
					ResourceColumn column = table.getColumn("ExGroupPath");
					column.setAttributeSchema(null);
					
					attribute.setResourceColumn(column);
					attribute.setDefaultValue("AAAA");
				}else if(key.equals("organizationCode")) {
					ResourceColumn column = table.getColumn("ExGroupCode");
					column.setAttributeSchema(null);
					
					attribute.setResourceColumn(column);
					attribute.setDefaultValue("AAAA");
				}else if(key.equals("organizationName")) {
					ResourceColumn column = table.getColumn("ExGroupName");
					column.setAttributeSchema(null);
					
					attribute.setResourceColumn(column);
					attribute.setDefaultValue("현대백화점그룹");
				}else if(key.equals("positionCode")) {
					ResourceColumn column = table.getColumn("JobPositionCode");
					column.setAttributeSchema(null);
					
					attribute.setResourceColumn(column);
				}else if(key.equals("positionName")) {
					ResourceColumn column = table.getColumn("ExJobPositionName");
					column.setAttributeSchema(null);
					
					attribute.setResourceColumn(column);
				}else if(key.equals("jobCode")) {
					ResourceColumn column = table.getColumn("JobTitleCode");
					column.setAttributeSchema(null);
					
					attribute.setResourceColumn(column);
				}else if(key.equals("jobName")) {
					ResourceColumn column = table.getColumn("ExJobTitleName");
					column.setAttributeSchema(null);
					
					attribute.setResourceColumn(column);
				}else if(key.equals("rankCode")) {
					ResourceColumn column = table.getColumn("JobLevelCode");
					column.setAttributeSchema(null);
					
					attribute.setResourceColumn(column);
				}else if(key.equals("rankName")) {
					ResourceColumn column = table.getColumn("ExJobLevelName");
					column.setAttributeSchema(null);
					
					attribute.setResourceColumn(column);
				}else if(key.equals("active")) {
					ResourceColumn column = table.getColumn("IsUse");
					column.setAttributeSchema(null);
					
					DataMapper dataMapper = new DataMapper();
					dataMapper.setClassName(BooleanConverter.class.getCanonicalName());
					dataMapper.setMethodName("ynToIntegerDefaultZero");
					attribute.setDataMapper(dataMapper);
					
					attribute.setResourceColumn(column);
				}else if(key.equals("joinDate")) {
					ResourceColumn column = table.getColumn("EnterDate");
					column.setAttributeSchema(null);
					
					attribute.setResourceColumn(column);
					DataMapper dataMapper = new DataMapper();
					dataMapper.setClassName(DateConverter.class.getCanonicalName());
					dataMapper.setMethodName("sqlTimestampToStringDefaultCurrenttime");
					
					attribute.setDataMapper(dataMapper);
					
				}else if(key.equals("retireDate")) {
					ResourceColumn column = table.getColumn("RetireDate");
					attribute.setResourceColumn(column);
					column.setAttributeSchema(null);
					
					DataMapper dataMapper = new DataMapper();
					dataMapper.setClassName(DateConverter.class.getCanonicalName());
					dataMapper.setMethodName("sqlTimestampToStringDefaultNull");
					
					attribute.setDataMapper(dataMapper);
					
				}else if(key.equals("createDate")) {
					ResourceColumn column = table.getColumn("RegistDate");
					attribute.setResourceColumn(column);
					column.setAttributeSchema(null);
					
					DataMapper dataMapper = new DataMapper();
					dataMapper.setClassName(DateConverter.class.getCanonicalName());
					dataMapper.setMethodName("sqlTimestampToStringDefaultNull");
					
					attribute.setDataMapper(dataMapper);
					
				}else if(key.equals("modifyDate")) {
					ResourceColumn column = table.getColumn("ModifyDate");
					attribute.setResourceColumn(column);
					column.setAttributeSchema(null);
					
					DataMapper dataMapper = new DataMapper();
					dataMapper.setClassName(DateConverter.class.getCanonicalName());
					dataMapper.setMethodName("sqlTimestampToStringDefaultCurrenttime");
					attribute.setDataMapper(dataMapper);
					
				}else {
					System.out.println("not used : " + key);
				}
				
				user_resource_output_mapper.putAttribute(key, attribute);
			}
			
			System.out.println(user_resource_output_mapper.toString(false));
			user_resource_output_mapper.save(gw_user_resource_output_mapper_file);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	//@Test
//	public void gen_gw_user_output_mapper_test() {
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
//				if (table.getName().equals("SSO_ORG_PERSON")) {
//					table.setColumns(repository.getTableColums("SSO_ORG_PERSON"));
//					table.getColumn("UR_Code").setType(ResourceType.PrimaryColumn);
//
//					DataMapper id = new DataMapper();
//					id.setId("id");
//					table.getColumn("UR_Code").setDataMapper(id);
//
//					group_resource_output_mapper.setTable(table);
//					System.out.println(gson.toJson(table));
//				}
//			}
//
//			FileWriter writer = new FileWriter(gw_user_resource_output_schema_file);
//			writer.write(gson.toJson(group_resource_output_mapper));
//			writer.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
}
