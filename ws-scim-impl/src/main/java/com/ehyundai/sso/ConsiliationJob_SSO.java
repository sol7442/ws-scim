package com.ehyundai.sso;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.ehyundai.object.User;
import com.wowsanta.scim.client.RESTClient;
import com.wowsanta.scim.client.RESTClientPool;
import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.log.SCIMLogger;
import com.wowsanta.scim.message.SCIMBulkOperation;
import com.wowsanta.scim.message.SCIMBulkRequest;
import com.wowsanta.scim.message.SCIMBulkResponse;
import com.wowsanta.scim.obj.SCIMSystem;
import com.wowsanta.scim.obj.SCIMUser;
import com.wowsanta.scim.resource.SCIMRepositoryManager;
import com.wowsanta.scim.resource.SCIMResourceRepository;
import com.wowsanta.scim.resource.SCIMSystemRepository;
import com.wowsanta.scim.scheduler.SCIMJob;
import com.wowsanta.scim.scheduler.SCIMScheduler;
import com.wowsanta.scim.schema.SCIMConstants;
import com.wowsanta.scim.schema.SCIMDefinitions;
import com.wowsanta.scim.util.Random;

public class ConsiliationJob_SSO extends SCIMJob {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2799856505493920258L;

	@Override
	public void beforeExecute(SCIMScheduler scheduler, SCIMUser worker) {
	}

	@Override
	public void doExecute(SCIMScheduler scheduler, SCIMUser worker) {
		SCIMLogger.proc("Consiliation to SSO : {} ", new Date());

		try {
			SCIMResourceRepository res_repo = SCIMRepositoryManager.getInstance().getResourceRepository();
			SCIMSystemRepository   sys_rep  = SCIMRepositoryManager.getInstance().getSystemRepository();
			
			Calendar cal = Calendar.getInstance();
			Date to = cal.getTime();

			SCIMBulkRequest request = new SCIMBulkRequest();
			request.setRequestId(Random.number(0,10000000));
			request.setSourecSystemId(scheduler.getSourceSystemId());
			request.setDirectSystemId(scheduler.getTargetSystemId());
			
			List<SCIMUser> user_list = res_repo.getAllUsers();;
			for (SCIMUser scimUser : user_list) {
				User sso_user = (User)scimUser;
				System.out.println(">>>>>("+user_list.size()+")" + sso_user);
				
				SCIMBulkOperation operation = new SCIMBulkOperation();
				operation.setData(sso_user);
				operation.setBulkId(Random.number(0, 1000000));
				operation.setMethod(SCIMDefinitions.MethodType.PUT.toString());
				
				request.addOperation(operation);
			}
			
			try {
				
				SCIMSystem target_system = sys_rep.getSystem(scheduler.getTargetSystemId());
				
				String bulk_url = target_system.getSystemUrl() + "/api/" + scheduler.getSourceSystemId() + "/scim" + SCIMConstants.VERSION_ENDPINT + SCIMConstants.BULK_ENDPOINT;;
				
				System.out.println("bulk_url >>" + bulk_url);

				RESTClient client = new RESTClient(worker);
				SCIMBulkResponse response = client.bulk(bulk_url, request);

				System.out.println(">>>");
				for (SCIMBulkOperation request_Operation : request.getOperations()) {
					System.out.println(request_Operation.toString());
				}
				for (SCIMBulkOperation response_Operation : response.getOperations()) {
					System.out.println(response_Operation.toString());
				}
				
				System.out.println(">>> ["+request.getOperations().size()+"]["+response.getOperations().size()+"]");
				
			} catch (SCIMException e) {
				e.printStackTrace();
			}
			
			System.out.println(">>>>>" + to);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		SCIMLogger.proc("Consiliation to SSO : {} ", new Date());
	}

	@Override
	public void afterExecute(SCIMScheduler schedulerS, SCIMUser worker) {

	}

}
