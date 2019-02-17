package com.ehyundai.gw;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.ehyundai.object.User;
import com.wowsanta.scim.client.RESTClient;
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

public class ReconciliationJob_GW extends SCIMJob {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2799856505493920258L;

	@Override
	public void beforeExecute(SCIMScheduler scheduler) {
	}

	@Override
	public void doExecute(SCIMScheduler scheduler) {
		SCIMLogger.proc("ReconciliationJob_GW : {} ", new Date());

		try {
			SCIMResourceRepository res_repo = SCIMRepositoryManager.getInstance().getResourceRepository();
			SCIMSystemRepository   sys_rep  = SCIMRepositoryManager.getInstance().getSystemRepository();
			
			
			Calendar cal = Calendar.getInstance();
			Date to = cal.getTime();
			cal.add(Calendar.DATE, -10);
			Date from = cal.getTime();

			SCIMBulkRequest request = new SCIMBulkRequest();
			request.setSourecSystemId(scheduler.getSourceSystemId());
			request.setDirectSystemId(scheduler.getTargetSystemId());
			
			SCIMSystem target_system = sys_rep.getSystem(scheduler.getTargetSystemId());

			List<SCIMUser> user_list = res_repo.getUsers(from, to);
			for (SCIMUser scimUser : user_list) {
				User user = (User) scimUser;
				SCIMBulkOperation operation = new SCIMBulkOperation();
				operation.setData(user);

				if (user.getActive() == 0) {
					operation.setMethod(SCIMDefinitions.MethodType.PATCH.toString());
				} else {
					if (user.getActive() == 1 && user.getMeta().getCreated().equals(user.getMeta().getLastModified())) {
						operation.setBulkId(Random.number(0, 1000000));
						operation.setMethod(SCIMDefinitions.MethodType.PUT.toString());
					} else {
						operation.setMethod(SCIMDefinitions.MethodType.POST.toString());
					}
				}

				request.addOperation(operation);
			}
			
			try {
				
				
				String bulk_url = target_system.getSystemUrl() + "/scim" + SCIMConstants.VERSION_ENDPINT + SCIMConstants.BULK_ENDPOINT;;
				RESTClient client = new RESTClient();
				SCIMBulkResponse response = client.bulk(bulk_url, request);
				System.out.println(response.toString(true));
				
				
			} catch (SCIMException e) {
				e.printStackTrace();
			}
			
			
			sys_rep.updateSchdulerLastExcuteDate(scheduler.getSchedulerId(), to);
		} catch (Exception e) {
			e.printStackTrace();
		}
		SCIMLogger.proc("ReconciliationJob_GW : {} ", new Date());

	}

	@Override
	public void afterExecute(SCIMScheduler scheduler) {

	}

}
