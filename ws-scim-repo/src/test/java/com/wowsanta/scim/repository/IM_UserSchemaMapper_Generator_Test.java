package com.wowsanta.scim.repository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wowsanta.scim.object.Resource_Object;
import com.wowsanta.scim.repository.convert.DateConverter;
import com.wowsanta.scim.repository.impl.OracleRepository;
import com.wowsanta.scim.util.Random;
import com.wowsanta.scim.util.Random.DUTY;
import com.wowsanta.scim.util.Random.JOB;
import com.wowsanta.scim.util.Random.ORGANIZATION;
import com.wowsanta.scim.util.Random.POSITION;

public class IM_UserSchemaMapper_Generator_Test {

	public static final String im_repository_config_file = "../config/backup_conf_20190619/default_oracle_im_repository.json";
	public static final String user_resource_schema_file = "../config/backup_conf_20190619/default_user_schema.json";
	
	public static final String im_user_resource_output_mapper_file = "../config/backup_conf_20190619/default_oracle_im_user_resource_output_mapper.json";
	public static final String im_user_resource_output_schema_file = "../config/backup_conf_20190619/default_oracle_im_user_resource_output_schema.json";
	public static final String im_user_resource_input_mapper_file = "../config/backup_conf_20190619/default_oracle_im_user_resource_input_mapper.json";
	public static final String im_user_resource_input_schema_file = "../config/backup_conf_20190619/default_oracle_im_user_resource_input_schema.json";

	//@Test
	public void get_user_by_out_mapper_test() {
		try {
			GsonBuilder builder = new GsonBuilder().disableHtmlEscaping();
			//builder.setPrettyPrinting();			
			Gson gson = builder.create();
			
			RepositoryOutputMapper user_resource_output_mapper = RepositoryOutputMapper.load(im_user_resource_output_mapper_file);
			
			SCIMRepositoryManager repository_manager = SCIMRepositoryManager.load(im_repository_config_file);
			repository_manager.initailze();
			OracleRepository repository = (OracleRepository) repository_manager.getResourceRepository();//OracleRepository.load(im_repository_config_file);
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
//			filter = null;
			System.out.println("user count : " + repository.getUserCount(filter));
			
			List<Resource_Object> user_list = repository.searchUser(filter, 0,100,repository.getUserCount(filter));
			for (Resource_Object resource_Object : user_list) {
				System.out.println("resource : " + gson.toJson(resource_Object) );
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}	
		
	}
	
	//@Test
	public void set_user_by_input_mapper_test() {

		try {
			GsonBuilder builder = new GsonBuilder().disableHtmlEscaping();
			builder.setPrettyPrinting();
			Gson gson = builder.create();

			RepositoryInputMapper user_resource_input_mapper = RepositoryInputMapper
					.load(im_user_resource_input_mapper_file);

			OracleRepository repository = OracleRepository.load(im_repository_config_file);
			repository.initialize();
			repository.setUserInputMapper(user_resource_input_mapper);

			Resource_Object user_object = new Resource_Object();
			user_object.put("id"	,Random.number(100000,999999));
			user_object.put("name"	,Random.name());
			user_object.put("employeeNumber"	,user_object.get("id"));
			
			int acive_num = Random.yn_integer(90);
			user_object.put("active"			,acive_num);

			final SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
			Date join_date = Random.beforeYears(10);
			
			
			Date now_date = new Date();
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
			user_object.put("organizationPath"	, "AAA;QQQ;CCCC");
						
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

	@Test
	public void gen_im_user_input_out_mapper_test() {
		try {
			ResourceTypeSchema user_schema = ResourceTypeSchema.load(user_resource_schema_file);
			
			SCIMRepositoryManager.load(im_repository_config_file).initailze();
			SCIMRepositoryController repository = (SCIMRepositoryController)SCIMRepositoryManager.getInstance().getResourceRepository();
			RepositoryInputMapper user_resource_input_mapper = new RepositoryInputMapper();

			List<ResourceTable> table_list = repository.getTables();
			System.out.println("table list=====");
			for (ResourceTable table : table_list) {
				//System.out.println(table);
				if (table.getName().equals("SCIM_USER")) {
					table.setIndex(0);			
					List<ResourceColumn> columns = repository.getTableColums("SCIM_USER","USERID");
					for (ResourceColumn column : columns) {
						System.out.println("colum name " + column.getName());
						if(column.getName().equals("USERID")) {
							AttributeSchema attribute = user_schema.getAttribute("id");
							column.setAttributeSchema(attribute.getName());
							
						}else if(column.getName().equals("USERNAME")) {
							AttributeSchema attribute = user_schema.getAttribute("name");
							column.setAttributeSchema(attribute.getName());
						}else {
							System.out.println("not used colum name " + column.getName());
						}
					}
					table.setColumns(columns);
					user_resource_input_mapper.addTable(table);
				}else if (table.getName().equals("SCIM_USER_META")) {
					table.setIndex(1);			
					List<ResourceColumn> columns = repository.getTableColums("SCIM_USER_META","USERID");
					for (ResourceColumn column : columns) {
						System.out.println("colum name " + column.getName());
						if(column.getName().equals("USERID")) {
							AttributeSchema attribute = user_schema.getAttribute("id");
							column.setAttributeSchema(attribute.getName());
							
						}else if(column.getName().equals("EXPIREDATE")) {
							AttributeSchema attribute = user_schema.getAttribute("expireDate");
							column.setAttributeSchema(attribute.getName());
							
							DataMapper dataMapper = new DataMapper();
							dataMapper.setClassName(DateConverter.class.getCanonicalName());
							dataMapper.setMethodName("stringToSqlDateDefaultNull");
							column.setDataMapper(dataMapper);
							
						}else if(column.getName().equals("EXPIREDATE")) {
							AttributeSchema attribute = user_schema.getAttribute("expireDate");
							column.setAttributeSchema(attribute.getName());
							
							DataMapper dataMapper = new DataMapper();
							dataMapper.setClassName(DateConverter.class.getCanonicalName());
							dataMapper.setMethodName("stringToSqlDateDefaultNull");
							column.setDataMapper(dataMapper);
							
						}else if(column.getName().equals("CREATEDATE")) {
							AttributeSchema attribute = user_schema.getAttribute("createDate");
							column.setAttributeSchema(attribute.getName());
							
							DataMapper dataMapper = new DataMapper();
							dataMapper.setClassName(DateConverter.class.getCanonicalName());
							dataMapper.setMethodName("stringToSqlTimestampDefaultCurrentTime");
							column.setDataMapper(dataMapper);
							
						}else if(column.getName().equals("MODIFYDATE")) {
							AttributeSchema attribute = user_schema.getAttribute("modifyDate");
							column.setAttributeSchema(attribute.getName());
							
							DataMapper dataMapper = new DataMapper();
							dataMapper.setClassName(DateConverter.class.getCanonicalName());
							dataMapper.setMethodName("stringToSqlTimestampDefaultCurrentTime");
							column.setDataMapper(dataMapper);
						}else if(column.getName().equals("ACTIVE")) {
							AttributeSchema attribute = user_schema.getAttribute("active");
							column.setAttributeSchema(attribute.getName());
							
//							DataMapper dataMapper = new DataMapper();
//							dataMapper.setClassName(BooleanConverter.class.getCanonicalName());
//							dataMapper.setMethodName("ynToIntegerDefaultZero");
//							column.setDataMapper(dataMapper);
							
						}else {
							System.out.println("not used colum name " + column.getName());
						}
					}
					
					
					table.setColumns(columns);
					user_resource_input_mapper.addTable(table);
				}else if(table.getName().equals("SCIM_USER_PROFILE")) {
					table.setIndex(1);			
					List<ResourceColumn> columns = repository.getTableColums("SCIM_USER_PROFILE","USERID");
					for (ResourceColumn column : columns) {
						System.out.println("colum name " + column.getName());
						if(column.getName().equals("USERID")) {
							AttributeSchema attribute = user_schema.getAttribute("id");
							column.setAttributeSchema(attribute.getName());
							
						}else if(column.getName().equals("RANKNAME")) {
							AttributeSchema attribute = user_schema.getAttribute("rankName");
							column.setAttributeSchema(attribute.getName());
						}else if(column.getName().equals("RANKCODE")) {
							AttributeSchema attribute = user_schema.getAttribute("rankCode");
							column.setAttributeSchema(attribute.getName());
						}else if(column.getName().equals("POSITIONNAME")) {
							AttributeSchema attribute = user_schema.getAttribute("positionName");
							column.setAttributeSchema(attribute.getName());
						}else if(column.getName().equals("POSITIONCODE")) {
							AttributeSchema attribute = user_schema.getAttribute("positionCode");
							column.setAttributeSchema(attribute.getName());
						}else if(column.getName().equals("JOBNAME")) {
							AttributeSchema attribute = user_schema.getAttribute("jobName");
							column.setAttributeSchema(attribute.getName());
						}else if(column.getName().equals("JOBCODE")) {
							AttributeSchema attribute = user_schema.getAttribute("jobCode");
							column.setAttributeSchema(attribute.getName());
						}else if(column.getName().equals("ORGANIZATIONNAME")) {
							AttributeSchema attribute = user_schema.getAttribute("organizationName");
							column.setAttributeSchema(attribute.getName());
							column.setDefaultValue("현대백화점그룹");
						}else if(column.getName().equals("ORGANIZATIONCODE")) {
							AttributeSchema attribute = user_schema.getAttribute("organizationCode");
							column.setAttributeSchema(attribute.getName());
							column.setDefaultValue("AAAA");
						}else if(column.getName().equals("ORGANIZATIONPATH")) {
							AttributeSchema attribute = user_schema.getAttribute("organizationPath");
							column.setAttributeSchema(attribute.getName());
							column.setDefaultValue("AAAA");
						}else if(column.getName().equals("EMPLOYEENUMBER")) {
							AttributeSchema attribute = user_schema.getAttribute("employeeNumber");
							column.setAttributeSchema(attribute.getName());
						}else if(column.getName().equals("RETIREDATE")) {
							AttributeSchema attribute = user_schema.getAttribute("retireDate");
							column.setAttributeSchema(attribute.getName());
							
							DataMapper dataMapper = new DataMapper();
							dataMapper.setClassName(DateConverter.class.getCanonicalName());
							dataMapper.setMethodName("stringToSqlTimestampDefaultNull");
							column.setDataMapper(dataMapper);
							
						}else if(column.getName().equals("ENTERDATE")) {
							AttributeSchema attribute = user_schema.getAttribute("joinDate");
							column.setAttributeSchema(attribute.getName());
							
							DataMapper dataMapper = new DataMapper();
							dataMapper.setClassName(DateConverter.class.getCanonicalName());
							dataMapper.setMethodName("stringToSqlTimestampDefaultNull");
							column.setDataMapper(dataMapper);
						}
					}
					table.setColumns(columns);
					user_resource_input_mapper.addTable(table);
				}else {
					System.out.println("skip table : " + table.getName() );
				}
				
				
				
			}
			System.out.println(user_resource_input_mapper.toString(false));
			user_resource_input_mapper.save(im_user_resource_input_mapper_file);
			
			
			user_schema = ResourceTypeSchema.load(user_resource_schema_file);
			Set<String> key_set = user_schema.getAttributes().keySet();
			
			
			ResourceTable out_table = null;// user_resource_input_mapper.getTable("SSO_ORG_PERSON");
			table_list = repository.getTables();
			System.out.println("table list=====");
			for (ResourceTable table : table_list) {
				if (table.getName().equals("SCIM_USER_INFO")) {
					table.setIndex(0);	
					List<ResourceColumn> columns = repository.getTableColums("SCIM_USER_INFO","USERID");
					table.setColumns(columns);
					out_table = table;
				}
			}
					
			RepositoryOutputMapper user_resource_output_mapper = new RepositoryOutputMapper();
			user_resource_output_mapper.setTable(out_table);
			
			for (String key : key_set) {
				AttributeSchema attribute  = user_schema.getAttribute(key);
				
				if(key.equals("id")) {
					ResourceColumn column = out_table.getColumn("USERID");
					attribute.setResourceColumn(column);
				}else if(key.equals("name")) {
					ResourceColumn column = out_table.getColumn("USERNAME");
					attribute.setResourceColumn(column);
				}else if(key.equals("employeeNumber")) {
					ResourceColumn column = out_table.getColumn("EMPLOYEENUMBER");
					attribute.setResourceColumn(column);
				}else if(key.equals("active")) {
					ResourceColumn column = out_table.getColumn("ACTIVE");
					attribute.setResourceColumn(column);
				}else if(key.equals("rankName")) {
					ResourceColumn column = out_table.getColumn("RANKNAME");
					attribute.setResourceColumn(column);
				}else if(key.equals("rankCode")) {
					ResourceColumn column = out_table.getColumn("RANKCODE");
					attribute.setResourceColumn(column);
				}else if(key.equals("jobName")) {
					ResourceColumn column = out_table.getColumn("JOBNAME");
					attribute.setResourceColumn(column);
				}else if(key.equals("jobCode")) {
					ResourceColumn column = out_table.getColumn("JOBCODE");
					attribute.setResourceColumn(column);
				}else if(key.equals("positionName")) {
					ResourceColumn column = out_table.getColumn("POSITIONNAME");
					attribute.setResourceColumn(column);
				}else if(key.equals("positionCode")) {
					ResourceColumn column = out_table.getColumn("POSITIONCODE");
					attribute.setResourceColumn(column);
				}else if(key.equals("organizationName")) {
					ResourceColumn column = out_table.getColumn("ORGANIZATIONNAME");
					attribute.setResourceColumn(column);
				}else if(key.equals("organizationCode")) {
					ResourceColumn column = out_table.getColumn("ORGANIZATIONCODE");
					attribute.setResourceColumn(column);
				}else if(key.equals("organizationPath")) {
					ResourceColumn column = out_table.getColumn("ORGANIZATIONPATH");
					attribute.setResourceColumn(column);
				}else if(key.equals("modifyDate")) {
					ResourceColumn column = out_table.getColumn("MODIFYDATE");
					attribute.setResourceColumn(column);
					
					DataMapper dataMapper = new DataMapper();
					dataMapper.setClassName(DateConverter.class.getCanonicalName());
					dataMapper.setMethodName("sqlTimestampToStringDefaultCurrenttime");
					
					attribute.setDataMapper(dataMapper);
				}else if(key.equals("createDate")) {
					ResourceColumn column = out_table.getColumn("CREATEDATE");
					attribute.setResourceColumn(column);
					
					DataMapper dataMapper = new DataMapper();
					dataMapper.setClassName(DateConverter.class.getCanonicalName());
					dataMapper.setMethodName("sqlTimestampToStringDefaultCurrenttime");
					
					attribute.setDataMapper(dataMapper);
					
				}else if(key.equals("retireDate")) {
					ResourceColumn column = out_table.getColumn("RETIREDATE");
					attribute.setResourceColumn(column);

					DataMapper dataMapper = new DataMapper();
					dataMapper.setClassName(DateConverter.class.getCanonicalName());
					dataMapper.setMethodName("sqlTimestampToStringDefaultNull");
					
					attribute.setDataMapper(dataMapper);
				}else if(key.equals("joinDate")) {
					ResourceColumn column = out_table.getColumn("ENTERDATE");
					attribute.setResourceColumn(column);
					
					DataMapper dataMapper = new DataMapper();
					dataMapper.setClassName(DateConverter.class.getCanonicalName());
					dataMapper.setMethodName("sqlTimestampToStringDefaultNull");
					
					attribute.setDataMapper(dataMapper);
				}else if(key.equals("expireDate")) {
					ResourceColumn column = out_table.getColumn("EXPIREDATE");
					attribute.setResourceColumn(column);
					
					DataMapper dataMapper = new DataMapper();
					dataMapper.setClassName(DateConverter.class.getCanonicalName());
					dataMapper.setMethodName("sqlDateToStringDefaultNull");
					
					attribute.setDataMapper(dataMapper);
				}
				else {
					System.out.println("not used : " + key);
				}
				user_resource_output_mapper.putAttribute(key, attribute);
			}
			
			System.out.println(user_resource_output_mapper.toString(false));
			user_resource_output_mapper.save(im_user_resource_output_mapper_file);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
