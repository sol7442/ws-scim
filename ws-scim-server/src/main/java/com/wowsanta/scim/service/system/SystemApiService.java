package com.wowsanta.scim.service.system;

import java.util.List;

import com.wowsanta.scim.obj.SCIMSystem;
import com.wowsanta.scim.resource.SCIMRepositoryManager;
import com.wowsanta.scim.resource.SCIMSystemRepository;

import spark.Request;
import spark.Response;
import spark.Route;

public class SystemApiService {

	public static Route getAllSystems() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				SCIMSystemRepository system_repository = SCIMRepositoryManager.getInstance().getSystemRepository();
				
				List<SCIMSystem> system_list = system_repository.getSystemAll();
				
				System.out.println("system_list : " + system_list);
				return system_list;
			}
		};
	}

	public static Route getSystem() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				String systemId = request.params(":id");
				System.out.println("systemId : " + systemId);
				
				return "get-system";
			}
		};
	}

}
