package com.wowsanta.scim.service.repository;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wowsanta.scim.protocol.ClientReponse;
import com.wowsanta.scim.protocol.ClientRequest;
import com.wowsanta.scim.protocol.ResponseState;
import com.wowsanta.scim.repository.ResourceColumn;
import com.wowsanta.scim.repository.ResourceTable;
import com.wowsanta.scim.repository.SCIMRepository;
import com.wowsanta.scim.repository.SCIMRepositoryManager;
import com.wowsanta.scim.repository.SCIMRepositoryController;

import spark.Request;
import spark.Response;
import spark.Route;

public class RepositoryService {

	Logger logger = LoggerFactory.getLogger(RepositoryService.class);
	
	public Route getTableList() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				ClientReponse client_response = new ClientReponse();
				try {
					
					logger.info("RepositoryService.getTableList : ");
					
					SCIMRepositoryController repository = (SCIMRepositoryController) SCIMRepositoryManager.getInstance().getResourceRepository();
					List<ResourceTable> table_list = repository.getTables();
					for (ResourceTable table : table_list) {
						logger.debug("table : {}", table);
					}
					
					client_response.setData(table_list);
					client_response.setState(ResponseState.Success);
					
					logger.info("RepositoryService.getTableList Result : {} ", table_list.size());
				}catch(Exception e) {
					logger.error("RepositoryService.getTableList Result : {} ",e.getMessage(), e);
					client_response.setState(ResponseState.Fail);
					client_response.setMessage(e.getMessage());
				}
				return client_response;
			}
		};
	}

	public Route getTableColumnList() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				logger.info("RepositoryService.getTableColumnList : ");
				
				ClientReponse client_response = new ClientReponse();
				try {
					String tableName = request.params(":tableName");
					String keyColumn = request.params(":keyColumn");
					
					logger.info("RepositoryService.getTableColumnList params : {} ", tableName);

					
					SCIMRepositoryController repository = (SCIMRepositoryController) SCIMRepositoryManager.getInstance().getResourceRepository();
					
					List<ResourceColumn> columns = repository.getTableColums(tableName,keyColumn);
					for (ResourceColumn column : columns) {
						logger.debug("column : {}", column);
					}
					
					client_response.setData(columns);
					client_response.setState(ResponseState.Success);
					
					logger.info("RepositoryService.getTableColumnList Result : {} ", columns.size());
				}catch(Exception e) {
					client_response.setState(ResponseState.Fail);
					client_response.setMessage(e.getMessage());
				}
				return client_response;
			}
		};
	}

	public Route getQueryResult() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				ClientReponse client_response = new ClientReponse();
				try {
					
					ClientRequest client_request = ClientRequest.parse(request.body());
					logger.info("client_request : {} ", client_request);
					String query = (String)client_request.getParam("query");
					
					SCIMRepositoryController repository = (SCIMRepositoryController) SCIMRepositoryManager.getInstance().getResourceRepository();
					
					List<Map<String,Object>> result = repository.excuteQuery(query);
					for (Map<String, Object> map : result) {
						Set<String> keys = map.keySet();
						for (String key : keys) {
							logger.debug("key : {} : {} ",key, map.get(key));
						}
					}
					
					client_response.setData(result);
					client_response.setState(ResponseState.Success);
				}catch(Exception e) {
					client_response.setState(ResponseState.Fail);
					client_response.setMessage(e.getMessage());
				}
				return client_response;
			}
		};
	}

}
