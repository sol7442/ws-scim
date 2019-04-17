package com.wowsanta.scim.service.repository;

import com.wowsanta.scim.protocol.ClientReponse;
import com.wowsanta.scim.protocol.ResponseState;
import com.wowsanta.scim.repository.SCIMRepository;
import com.wowsanta.scim.repository.SCIMRepositoryManager;
import com.wowsanta.scim.repository.SCIMResourceRepository;

import spark.Request;
import spark.Response;
import spark.Route;

public class RepositoryService {

	public Route getTableList() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {

				ClientReponse client_response = new ClientReponse();
				try {
					
					SCIMResourceRepository repository = (SCIMResourceRepository) SCIMRepositoryManager.getInstance().getRepository();
					client_response.setData(repository.getTables());
					client_response.setState(ResponseState.Success);
				}catch(Exception e) {
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
				ClientReponse client_response = new ClientReponse();
				try {
					String tableName = request.params(":tableName");
					
					SCIMResourceRepository repository = (SCIMResourceRepository) SCIMRepositoryManager.getInstance().getRepository();
					client_response.setData(repository.getTableColums(tableName));
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
