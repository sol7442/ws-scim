package com.wowsanta.scim.service.audit;

import java.util.List;

import com.wowsanta.scim.log.SCIMLogger;
import com.wowsanta.scim.obj.SCIMAudit;
import com.wowsanta.scim.resource.SCIMProviderRepository;
import com.wowsanta.scim.resource.SCIMRepositoryManager;

import spark.Request;
import spark.Response;
import spark.Route;

public class AuditService {

	public Route getUserAuditList() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				String userId = request.params(":userId");
				SCIMProviderRepository provider_repository = (SCIMProviderRepository) SCIMRepositoryManager.getInstance().getSystemRepository();
				
				SCIMLogger.proc("getUserAuditListByUserId : {}", userId);
				
				List<SCIMAudit> audit_list = provider_repository.findAuditByUserId(userId);

				SCIMLogger.proc("getUserAuditListByUserId : {} < {} ", userId, audit_list.size());

				return audit_list;
				
			}
		};
	}

}
