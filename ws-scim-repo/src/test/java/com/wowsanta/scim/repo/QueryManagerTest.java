package com.wowsanta.scim.repo;

import java.io.IOException;

import org.junit.Test;

import com.wowsanta.scim.repo.rdb.RDBColumn;
import com.wowsanta.scim.repo.rdb.RDBQuery;
import com.wowsanta.scim.repo.rdb.RDBQueryDefinitions;
import com.wowsanta.scim.repo.rdb.UserQueryManager;


public class QueryManagerTest {

	private final String query_schema_file = "../config/query_schmea.json";
	
	@Test
	public void createQeurySchema() {
		UserQueryManager user_query = new UserQueryManager();
		
		RDBQuery select = new RDBQuery();
		select.setTable("USER");
		select.addColum(new RDBColumn(RDBQueryDefinitions.ColumType.SCIMATTRIBUTE,"name"));
		select.addColum(new RDBColumn(RDBQueryDefinitions.ColumType.SCIMATTRIBUTE,"age"));
		user_query.setSelect(select);
		
		RDBQuery insert = new RDBQuery();
		insert.setTable("USER");
		insert.addColum(new RDBColumn(RDBQueryDefinitions.ColumType.SCIMATTRIBUTE,"name"));
		insert.addColum(new RDBColumn(RDBQueryDefinitions.ColumType.SCIMATTRIBUTE,"name"));
		
		user_query.addInsert(insert);
		
		
		System.out.println(user_query.toJson());
		
		try {
			user_query.save(query_schema_file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
