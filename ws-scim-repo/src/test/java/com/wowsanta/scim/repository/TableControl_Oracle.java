package com.wowsanta.scim.repository;

import java.util.List;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wowsanta.scim.repository.impl.MsSqlRepository;
import com.wowsanta.scim.repository.impl.OracleRepository;
import com.wowsanta.scim.repository.resource.SCIMResourceRepository;

public class TableControl_Oracle {
	
	Logger logger = LoggerFactory.getLogger(TableControl_Oracle.class);
	
	public static final String im_repository_config_file = "../config/backup_conf_20190429/default_oracle_im_repository.json";
	public static final String gw_repository_config_file = "../config/backup_conf_20190429/default_mssql_gw_repository.json";

	@Test
	public void test_getTableInfo() {
		try {
		
//			SCIMRepositoryManager.getInstance().load(im_repository_config_file);
//			SCIMRepositoryManager.getInstance().initailze();
			//SCIMRepositoryController repository = (SCIMRepositoryController)SCIMRepositoryManager.getInstance().getResourceRepository();
			
			
			MsSqlRepository repository = MsSqlRepository.load(gw_repository_config_file);
			repository.initialize();
			
			List<ResourceTable> table_list = repository.getTables();
			for (ResourceTable table : table_list) {
				logger.debug("table name : {} ",table.getName());
			}
			
			List<ResourceColumn> columns = repository.getTableColums("SSO_ORG_PERSON", "UR_Code");
			for (ResourceColumn column : columns) {
				logger.debug("SCIM_USER_INFO.COLUMN : {} ",column);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
