package com.wowsanta.scim.service.schudler;

import java.util.List;

import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.message.SCIMError;
import com.wowsanta.scim.resource.SCIMRepositoryManager;
import com.wowsanta.scim.resource.SCIMSystemRepository;
import com.wowsanta.scim.scheduler.SCIMScheduler;
import com.wowsanta.scim.schema.SCIMErrorCode;

import spark.Request;
import spark.Response;
import spark.Route;

public class SchdulerService {

	public static Route getSystemScheduler() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				try {
					String systemId = request.params(":systemId");
					SCIMSystemRepository system_repository = SCIMRepositoryManager.getInstance().getSystemRepository();
					
					
					List<SCIMScheduler> scheduler_list = system_repository.getSchdulerBySystemId(systemId);
					System.out.println("scheduler_list : " + scheduler_list.size());
					
					return scheduler_list;
				}catch(SCIMException e) {
					return new SCIMError(SCIMErrorCode.e500,e.getLocalizedMessage());
				}
			}
		};
	}

}
