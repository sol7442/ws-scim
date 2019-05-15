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

public class SSO_GroupSchemaMapper_Generator_Test {

	public static final String sso_repository_config_file = "../config/backup_conf_20190429/default_oracle_sso_repository.json";
	public static final String group_resource_schema_file = "../config/backup_conf_20190429/default_group_schema.json";
	
	public static final String sso_group_resource_output_mapper_file = "../config/backup_conf_20190429/default_oracle_sso_group_resource_output_mapper.json";
	public static final String sso_group_resource_input_mapper_file = "../config/backup_conf_20190429/default_oracle_sso_group_resource_input_mapper.json";

	private Logger logger = LoggerFactory.getLogger(SSO_GroupSchemaMapper_Generator_Test.class);
	
	//@Test
	public void get_group_by_out_mapper_test() {
		try {
			RepositoryOutputMapper group_resource_output_mapper = RepositoryOutputMapper.load(sso_group_resource_output_mapper_file);
			DefaultRepository repository = OracleRepository.load(sso_repository_config_file);
			repository.setGrouptOutputMapper(group_resource_output_mapper);
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
			
			List<Resource_Object> group_list = repository.searchGroup(filter, 0,5,repository.getGroupCount(filter));
			for (Resource_Object resource_Object : group_list) {
				logger.info("{}",resource_Object);
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}	
		
	}
	
	//@Test
	public void set_group_by_input_mapper_test() {

		try {
			RepositoryInputMapper group_resource_input_mapper = RepositoryInputMapper
					.load(sso_group_resource_input_mapper_file);

			DefaultRepository repository = OracleRepository.load(sso_repository_config_file);
			repository.initialize();
			repository.setGroupInputMapper(group_resource_input_mapper);

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
	public void gen_sso_group_input_out_mapper_test() {
		try {
			ResourceTypeSchema group_schema = ResourceTypeSchema.load(group_resource_schema_file);
			//System.out.println(group_schema.toString(true));
			
			OracleRepository repository = OracleRepository.load(sso_repository_config_file);
			repository.initialize();
			RepositoryInputMapper group_resource_input_mapper = new RepositoryInputMapper();

			List<ResourceTable> table_list = repository.getTables();
			System.out.println("table list=====");
			for (ResourceTable table : table_list) {
				//System.out.println(table.getName());
				if (table.getName().equals("WA3_ORG")) {
					table.setIndex(0);			
					List<ResourceColumn> columns = repository.getTableColums("WA3_ORG");
					for (ResourceColumn column : columns) {
						//System.out.println("colum name " + column.getName());
						if(column.getName().equals("ID")) {
							AttributeSchema attribute = group_schema.getAttribute("id");
							column.setAttributeSchema(attribute);
							column.setType(ResourceType.PrimaryColumn);
							
						}else if(column.getName().equals("NAME")) {
							AttributeSchema attribute = group_schema.getAttribute("organizationName");
							column.setAttributeSchema(attribute);
						
						}else if(column.getName().equals("CREATOR")) {
							column.setDefaultValue("im-system");
						
						}else if(column.getName().equals("MODIFIER")) {
							column.setDefaultValue("im-system");
						
						}else if(column.getName().equals("PARENT_ID")) {
							AttributeSchema attribute = group_schema.getAttribute("organizationParent");
							column.setAttributeSchema(attribute);
						
						}else if(column.getName().equals("INFO")) {
							AttributeSchema attribute = group_schema.getAttribute("organizationDescription");
							column.setAttributeSchema(attribute);
						
						}else if(column.getName().equals("PATH_ID")) {
							AttributeSchema attribute = group_schema.getAttribute("organizationPath");
							column.setAttributeSchema(attribute);
							
							DataMapper dataMapper = new DataMapper();
							dataMapper.setClassName("com.wowsanta.scim.repository.convert.CustomConverter");
							dataMapper.setMethodName("OrgPathConverter");
							column.setDataMapper(dataMapper);
						
						}else if(column.getName().equals("CREATE_TIME")) {
							AttributeSchema attribute = group_schema.getAttribute("createDate");
							column.setAttributeSchema(attribute);
							
							DataMapper dataMapper = new DataMapper();
							dataMapper.setClassName(DateConverter.class.getCanonicalName());
							dataMapper.setMethodName("stringToLongDefaultCurrenttime");
							column.setDataMapper(dataMapper);
						}else if(column.getName().equals("MODIFY_TIME")) {
							AttributeSchema attribute = group_schema.getAttribute("modifyDate");
							column.setAttributeSchema(attribute);
							
							DataMapper dataMapper = new DataMapper();
							dataMapper.setClassName(DateConverter.class.getCanonicalName());
							dataMapper.setMethodName("stringToLongDefaultCurrenttime");
							column.setDataMapper(dataMapper);
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
			group_resource_input_mapper.save(sso_group_resource_input_mapper_file);
			
			
//			boolean bb = true;
//			if(bb)return;
			
			group_schema = ResourceTypeSchema.load(group_resource_schema_file);
			Set<String> key_set = group_schema.getAttributes().keySet();
			
			ResourceTable out_table = null;
			table_list = repository.getTables();
			System.out.println("table list=====");
			for (ResourceTable table : table_list) {
				if (table.getName().equals("WA3_ORG")) {
					table.setIndex(0);	
					List<ResourceColumn> columns = repository.getTableColums("WA3_ORG");
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
					ResourceColumn column = out_table.getColumn("ID");
					attribute.setResourceColumn(column);
				}
				else {
					logger.info("skip : {}",key);
				}
				
				group_resource_output_mapper.putAttribute(key, attribute);
			}
			
			System.out.println(group_resource_output_mapper.toString(false));
			group_resource_output_mapper.save(sso_group_resource_output_mapper_file);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
