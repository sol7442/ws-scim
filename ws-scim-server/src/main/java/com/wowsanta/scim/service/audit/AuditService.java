package com.wowsanta.scim.service.audit;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wowsanta.scim.obj.SCIMAudit;
import com.wowsanta.scim.repository.SCIMRepositoryManager;
import com.wowsanta.scim.repository.system.SCIMProviderRepository;

import spark.Request;
import spark.Response;
import spark.Route;

public class AuditService {
	Logger logger = LoggerFactory.getLogger(AuditService.class);
	
	public Route getUserAuditList() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				String userId = request.params(":userId");
				SCIMProviderRepository provider_repository = (SCIMProviderRepository) SCIMRepositoryManager.getInstance().getSystemRepository();
				
				logger.info("getUserAuditListByUserId : {}", userId);
				
				List<SCIMAudit> audit_list = provider_repository.findAuditByUserId(userId);

				logger.info("getUserAuditListByUserId : {} < {} ", userId, audit_list.size());

				return audit_list;
				
			}
		};
	}

}
