package com.wowsanta.scim.service.scim.v2;



import com.wowsanta.scim.message.SCIMBulkOperation;
import com.wowsanta.scim.message.SCIMBulkRequest;
import com.wowsanta.scim.message.SCIMBulkResponse;
import com.wowsanta.scim.obj.SCIMUser;
import com.wowsanta.scim.resource.SCIMRepositoryManager;
import com.wowsanta.scim.resource.SCIMSystemRepository;
import com.wowsanta.scim.schema.SCIMConstants;
import com.wowsanta.scim.service.SCIMService;

import spark.Request;
import spark.Response;
import spark.Route;

public class BulkController implements Route {

	private SCIMService service;
	public BulkController(SCIMService service) {
		this.service = service;
	}
	
	@Override
	public Object handle(Request request, Response response) throws Exception {
		SCIMBulkRequest scim_bluk_request = new SCIMBulkRequest();
		SCIMBulkResponse scim_bluk_response = new SCIMBulkResponse();
		
		SCIMSystemRepository systemRepository = SCIMRepositoryManager.getInstance().getSystemRepository();
		SCIMUser user = request.session().attribute("user");
		if(user == null) {
			user = new SCIMUser();
			user.setId("sys-scim-admin");
		}
		
		scim_bluk_request.parse(request.body());
		for (SCIMBulkOperation operation : scim_bluk_request.getOperations()) {
			SCIMBulkOperation result = (SCIMBulkOperation) this.service.execute(operation);
			scim_bluk_response.addOperation(result);
			systemRepository.addOperationResult(user,scim_bluk_request.getSourecSystemId(),scim_bluk_request.getDirectSystemId(),operation,result);
		}
		response.status(SCIMConstants.HtppConstants.CREATED);
		return scim_bluk_response;
	}

}
