package com.wowsanta.scim.repository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wowsanta.scim.object.Resource_Object;
import com.wowsanta.scim.repository.convert.DateConverter;
import com.wowsanta.scim.repository.impl.DefaultRepository;
import com.wowsanta.scim.repository.impl.OracleRepository;
import com.wowsanta.scim.util.Random;
import com.wowsanta.scim.util.Random.ORGANIZATION;

public class IM_GroupSchemaMapper_Generator_Test {

	public static final String im_repository_config_file = "../config/backup_conf_20190619/default_oracle_im_repository.json";
	public static final String group_resource_schema_file = "../config/backup_conf_20190619/default_group_schema.json";
	
	public static final String im_group_resource_output_mapper_file = "../config/backup_conf_20190619/default_oracle_im_group_resource_output_mapper.json";
	public static final String im_group_resource_input_mapper_file = "../config/backup_conf_20190619/default_oracle_im_group_resource_input_mapper.json";

	private Logger logger = LoggerFactory.getLogger(IM_GroupSchemaMapper_Generator_Test.class);
	
	//@Test
	public void get_group_by_out_mapper_test() {
		try {
			
			SCIMRepositoryManager repository_manager = SCIMRepositoryManager.load(im_repository_config_file);
			repository_manager.initailze();
			
			OracleRepository repository = (OracleRepository) repository_manager.getResourceRepository();
			
			RepositoryOutputMapper group_resource_output_mapper = RepositoryOutputMapper.load(im_group_resource_output_mapper_file);
			RepositoryInputMapper group_resource_input_mapper = RepositoryInputMapper.load(im_group_resource_input_mapper_file);
			
			repository.setGrouptOutputMapper(group_resource_output_mapper);
			repository.setGroupInputMapper(group_resource_input_mapper);
			repository.initialize();
			
			
			
			String groupId = "6747603";
			Resource_Object group_object = repository.getGroup(groupId);
			
			logger.info("{}",group_object.toString(true));
			
			String from = "19-04-21 21:16:15";
			SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date modify_date = transFormat.parse(from);
			String filter = "modifyDate ge \"" + transFormat.format(modify_date) + "\"";// or id eq \"803148\" ";
			System.out.println("filter : " + filter );
			
			System.out.println("group count : " + repository.getGroupCount(filter));
			
//			List<Resource_Object> group_list = repository.searchGroup(filter, 0,5,repository.getGroupCount(filter));
//			for (Resource_Object resource_Object : group_list) {
//				logger.info("{}",resource_Object);
//			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}	
		
	}
	
	//@Test
	public void set_group_by_input_mapper_test() {

		try {
			

			
			SCIMRepositoryManager repository_manager = SCIMRepositoryManager.load(im_repository_config_file);
			repository_manager.initailze();
			
			OracleRepository repository = (OracleRepository) repository_manager.getResourceRepository();
			
			RepositoryOutputMapper group_resource_output_mapper = RepositoryOutputMapper.load(im_group_resource_output_mapper_file);
			RepositoryInputMapper group_resource_input_mapper = RepositoryInputMapper.load(im_group_resource_input_mapper_file);
			
			repository.setGrouptOutputMapper(group_resource_output_mapper);
			repository.setGroupInputMapper(group_resource_input_mapper);
			repository.initialize();

			
			Resource_Object group_object = new Resource_Object();
			group_object.put("active", Random.yn_integer(90));

			Date now_date = new Date();
			final SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
			
			group_object.put("createDate", fmt.format(now_date));
			group_object.put("modifyDate", fmt.format(now_date));

			ORGANIZATION org = Random.oranization();
			group_object.put("id", Random.number(100000, 9999999));
			group_object.put("organizationName", org.getKorean());
			group_object.put("organizationCode", String.valueOf(org.ordinal()));
			group_object.put("organizationPath", org.getEnglish());
			group_object.put("organizationParent", 		Random.oranization().toString());
			group_object.put("organizationDescription", group_object.get("organizationName") + " description");

			logger.info("group",group_object.toString(true));
			
			repository.createGroup(group_object);
	
			Date update_date = new Date();
			group_object.put("modifyDate", fmt.format(update_date));
			repository.updateGroup(group_object);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void gen_im_group_input_out_mapper_test() {
		try {
			ResourceTypeSchema group_schema = ResourceTypeSchema.load(group_resource_schema_file);
			
			SCIMRepositoryManager.load(im_repository_config_file).initailze();
			SCIMRepositoryController repository = (SCIMRepositoryController)SCIMRepositoryManager.getInstance().getResourceRepository();
			
			RepositoryInputMapper group_resource_input_mapper = new RepositoryInputMapper();

			List<ResourceTable> table_list = repository.getTables();
			System.out.println("table list=====");
			for (ResourceTable table : table_list) {
				//System.out.println(table.getName());
				if (table.getName().equals("SCIM_GROUP")) {
					table.setIndex(0);			
					List<ResourceColumn> columns = repository.getTableColums("SCIM_GROUP","GROUPID");
					for (ResourceColumn column : columns) {
						//System.out.println("colum name " + column.getName());
						if(column.getName().equals("GROUPID")) {
							AttributeSchema attribute = group_schema.getAttribute("id");
							column.setAttributeSchema(attribute.getName());;
							
							logger.info("set {}-{}",table.getName(),column.getName() );
						}else if(column.getName().equals("GROUPNAME")) {
							AttributeSchema attribute = group_schema.getAttribute("organizationName");
							column.setAttributeSchema(attribute.getName());;
							logger.info("set {}-{}",table.getName(),column.getName() );
						}else {
							logger.info("skip {}-{}",table.getName(),column.getName() );
						}
					}
					table.setColumns(columns);
					group_resource_input_mapper.addTable(table);
					System.out.println("set " + table.getName());
				}else if (table.getName().equals("SCIM_GROUP_META")) {
					table.setIndex(1);			
					List<ResourceColumn> columns = repository.getTableColums("SCIM_GROUP_META","GROUPID");
					for (ResourceColumn column : columns) {
						if(column.getName().equals("GROUPID")) {
							AttributeSchema attribute = group_schema.getAttribute("id");
							column.setAttributeSchema(attribute.getName());;
							logger.info("set {}-{}",table.getName(),column.getName() );
						}else if(column.getName().equals("EXPIREDATE")) {
							AttributeSchema attribute = group_schema.getAttribute("expireDate");
							column.setAttributeSchema(attribute.getName());;
							
							DataMapper dataMapper = new DataMapper();
							dataMapper.setClassName(DateConverter.class.getCanonicalName());
							dataMapper.setMethodName("stringToSqlDateDefaultNull");
							column.setDataMapper(dataMapper);
							logger.info("set {}-{}",table.getName(),column.getName() );
						}else if(column.getName().equals("EXPIREDATE")) {
							AttributeSchema attribute = group_schema.getAttribute("expireDate");
							column.setAttributeSchema(attribute.getName());;
							
							DataMapper dataMapper = new DataMapper();
							dataMapper.setClassName(DateConverter.class.getCanonicalName());
							dataMapper.setMethodName("stringToSqlDateDefaultNull");
							column.setDataMapper(dataMapper);
							logger.info("set {}-{}",table.getName(),column.getName() );
						}else if(column.getName().equals("CREATEDATE")) {
							AttributeSchema attribute = group_schema.getAttribute("createDate");
							column.setAttributeSchema(attribute.getName());;
							
							DataMapper dataMapper = new DataMapper();
							dataMapper.setClassName(DateConverter.class.getCanonicalName());
							dataMapper.setMethodName("stringToSqlTimestampDefaultCurrentTime");
							column.setDataMapper(dataMapper);
							logger.info("set {}-{}",table.getName(),column.getName() );
						}else if(column.getName().equals("MODIFYDATE")) {
							AttributeSchema attribute = group_schema.getAttribute("modifyDate");
							column.setAttributeSchema(attribute.getName());;
							
							DataMapper dataMapper = new DataMapper();
							dataMapper.setClassName(DateConverter.class.getCanonicalName());
							dataMapper.setMethodName("stringToSqlTimestampDefaultCurrentTime");
							column.setDataMapper(dataMapper);
							
							logger.info("set {}-{}",table.getName(),column.getName() );
						}else if(column.getName().equals("ACTIVE")) {
							AttributeSchema attribute = group_schema.getAttribute("active");
							column.setAttributeSchema(attribute.getName());;
							
							logger.info("set {}-{}",table.getName(),column.getName() );
						}else {
							logger.info("skip {}-{}",table.getName(),column.getName() );
						}
					}
					
					
					table.setColumns(columns);
					group_resource_input_mapper.addTable(table);
					System.out.println("set " + table.getName());
				}else if(table.getName().equals("SCIM_GROUP_PROFILE")) {
					table.setIndex(1);			
					List<ResourceColumn> columns = repository.getTableColums("SCIM_GROUP_PROFILE","GROUPID");
					for (ResourceColumn column : columns) {
						if(column.getName().equals("GROUPID")) {
							AttributeSchema attribute = group_schema.getAttribute("id");
							column.setAttributeSchema(attribute.getName());;
							logger.info("set {}-{}",table.getName(),column.getName() );

						}else if(column.getName().equals("GROUPPATH")) {
							AttributeSchema attribute = group_schema.getAttribute("organizationPath");
							column.setAttributeSchema(attribute.getName());;
							logger.info("set {}-{}",table.getName(),column.getName() );

						}else if(column.getName().equals("GROUPDESC")) {
							AttributeSchema attribute = group_schema.getAttribute("organizationDescription");
							column.setAttributeSchema(attribute.getName());;
							logger.info("set {}-{}",table.getName(),column.getName() );

						}else if(column.getName().equals("PARENTID")) {
							AttributeSchema attribute = group_schema.getAttribute("organizationParent");
							column.setAttributeSchema(attribute.getName());;
							logger.info("set {}-{}",table.getName(),column.getName() );

						}else if(column.getName().equals("GROUPCODE")) {
							AttributeSchema attribute = group_schema.getAttribute("organizationCode");
							column.setAttributeSchema(attribute.getName());;
							logger.info("set {}-{}",table.getName(),column.getName() );

						}else {
							logger.info("skip {}-{}",table.getName(),column.getName() );
						}
					}
					table.setColumns(columns);
					group_resource_input_mapper.addTable(table);
					System.out.println("set " + table.getName());
				}else {
					//System.out.println("skip table : " + table.getName() );
				}
			}
			System.out.println(group_resource_input_mapper.toString(false));
			group_resource_input_mapper.save(im_group_resource_input_mapper_file);
			
			group_schema = ResourceTypeSchema.load(group_resource_schema_file);
			Set<String> key_set = group_schema.getAttributes().keySet();
			
			ResourceTable out_table = null;
			table_list = repository.getTables();
			System.out.println("table list=====");
			for (ResourceTable table : table_list) {
				if (table.getName().equals("SCIM_GROUP_INFO")) {
					table.setIndex(0);	
					List<ResourceColumn> columns = repository.getTableColums("SCIM_GROUP_INFO","GROUPID");
					for (ResourceColumn colmun : columns) {
						logger.info("{}-{}", table.getName(),colmun.getName());
					}
					
					table.setColumns(columns);
					out_table = table;
				}
			}
					
			RepositoryOutputMapper group_resource_output_mapper = new RepositoryOutputMapper();
			group_resource_output_mapper.setTable(out_table);
			
			for (String key : key_set) {
				AttributeSchema attribute  = group_schema.getAttribute(key);
				logger.info("attribute {} " ,attribute.getName());
				
				if(key.equals("id")) {
					ResourceColumn column = out_table.getColumn("GROUPID");
					attribute.setResourceColumn(column);
				}else if(key.equals("active")) {
					ResourceColumn column = out_table.getColumn("ACTIVE");
					attribute.setResourceColumn(column);
				}else if(key.equals("organizationName")) {
					ResourceColumn column = out_table.getColumn("GROUPNAME");
					attribute.setResourceColumn(column);
				}else if(key.equals("organizationParent")) {
					ResourceColumn column = out_table.getColumn("PARENTID");
					attribute.setResourceColumn(column);
				}else if(key.equals("organizationCode")) {
					ResourceColumn column = out_table.getColumn("GROUPID");
					attribute.setResourceColumn(column);
				}else if(key.equals("organizationPath")) {
					ResourceColumn column = out_table.getColumn("GROUPPATH");
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
				}else if(key.equals("expireDate")) {
					ResourceColumn column = out_table.getColumn("EXPIREDATE");
					attribute.setResourceColumn(column);
					
					DataMapper dataMapper = new DataMapper();
					dataMapper.setClassName(DateConverter.class.getCanonicalName());
					dataMapper.setMethodName("sqlDateToStringDefaultNull");
					
					attribute.setDataMapper(dataMapper);
				}
				else {
					logger.info("skip : {}",key);
				}
				
				group_resource_output_mapper.putAttribute(key, attribute);
			}
			
			System.out.println(group_resource_output_mapper.toString(false));
			group_resource_output_mapper.save(im_group_resource_output_mapper_file);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
