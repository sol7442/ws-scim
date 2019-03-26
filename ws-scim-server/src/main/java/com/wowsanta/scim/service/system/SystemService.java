package com.wowsanta.scim.service.system;

import java.util.List;

import com.wowsanta.scim.obj.SCIMSystem;
import com.wowsanta.scim.repository.SCIMRepositoryManager;
import com.wowsanta.scim.resource.SCIMSystemRepository;

import spark.Request;
import spark.Response;
import spark.Route;

public class SystemService {

	public Route getSystems() {
		return new Route() {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				SCIMSystemRepository system_repository = SCIMRepositoryManager.getInstance().getSystemRepository();

				List<SCIMSystem> system_list = system_repository.getSystemAll();
				
				return system_list;
			}
		};
	}

}
