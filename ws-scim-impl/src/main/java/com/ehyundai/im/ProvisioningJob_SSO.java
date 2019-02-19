package com.ehyundai.im;

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

public class ProvisioningJob_SSO extends SCIMJob {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2799856505493920258L;

	@Override
	public void beforeExecute(SCIMScheduler scheduler, SCIMUser worker) {
	}

	@Override
	public void doExecute(SCIMScheduler scheduler, SCIMUser worker) {
		SCIMLogger.proc("ProvisionJob_SSO : {} ", new Date());

		try {
			SCIMResourceRepository res_repo = SCIMRepositoryManager.getInstance().getResourceRepository();
			SCIMSystemRepository   sys_rep  = SCIMRepositoryManager.getInstance().getSystemRepository();
			
			
			List<SCIMUser> user_list = null;
			

			Calendar cal = Calendar.getInstance();
			Date to = cal.getTime();
			
			Date last_execute_date = scheduler.getLastExecuteDate();
			if(last_execute_date != null) {
				cal.setTime(last_execute_date);
				Date from = cal.getTime();
				user_list = res_repo.getUsers(from, to);
			}else {
				user_list = res_repo.getAllActiveUsers();
			}
			
			
			
			SCIMBulkRequest request = new SCIMBulkRequest();
			request.setRequestId(Random.number(0,10000000));
			request.setSourecSystemId(scheduler.getSourceSystemId());
			request.setDirectSystemId(scheduler.getTargetSystemId());
			
			SCIMSystem target_system = sys_rep.getSystem(scheduler.getTargetSystemId());
			String bulk_url = target_system.getSystemUrl() + "/scim" + SCIMConstants.VERSION_ENDPINT + SCIMConstants.BULK_ENDPOINT;;			
			System.out.println("bulk_url : " + bulk_url);
			
			for (SCIMUser scimUser : user_list) {
				User user = (User) scimUser;

				SCIMBulkOperation operation = new SCIMBulkOperation();
				operation.setData(user);
				if (user.getActive() == 0) {
					operation.setMethod(SCIMDefinitions.MethodType.PATCH.toString());
				} else {
					if(last_execute_date == null) {
						operation.setBulkId(Random.number(0, 1000000));
						operation.setMethod(SCIMDefinitions.MethodType.PUT.toString());
					}else {
						if (user.getActive() == 1 && user.getMeta().getCreated().equals(user.getMeta().getLastModified())) {
							operation.setBulkId(Random.number(0, 1000000));
							operation.setMethod(SCIMDefinitions.MethodType.PUT.toString());
						} else {
							operation.setMethod(SCIMDefinitions.MethodType.POST.toString());
						}
					}
				}
				request.addOperation(operation);
			}
			
			try {

				RESTClient client = new RESTClient(worker);
				SCIMBulkResponse response = client.bulk(bulk_url, request);
				
				addAudit(scheduler, worker, request, response);
				addSchudlerHistory(scheduler, request.getRequestId(), response);

				System.out.println(">>> ["+request.getOperations().size()+"]["+response.getOperations().size()+"]");
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			//sys_rep.updateSchdulerLastExcuteDate(scheduler.getSchedulerId(), to);
		} catch (Exception e) {
			e.printStackTrace();
		}
		SCIMLogger.proc("ProvisionJob_SSO : {} ", new Date());

	}

	private void addSchudlerHistory(SCIMScheduler scheduler, String workId, SCIMBulkResponse response) throws SCIMException {
		
		SCIMSystemRepository sys_rep = SCIMRepositoryManager.getInstance().getSystemRepository();
		
		int req_put_count = 0;
		int req_post_count = 0;
		int req_patch_count = 0;
		int req_delete_count = 0;
		
		int res_put_count = 0;
		int res_post_count = 0;
		int res_patch_count = 0;
		int res_delete_count = 0;
		
		for (SCIMBulkOperation response_operation : response.getOperations()) {
			if(response_operation.getMethod().equals(SCIMDefinitions.MethodType.PUT.toString())){
				req_put_count++;
				if(response_operation.getResponse() == null) {
					res_put_count++;
				}
			}else if(response_operation.getMethod().equals(SCIMDefinitions.MethodType.POST.toString())){
				req_post_count++;
				if(response_operation.getResponse() == null) {
					res_post_count++;
				}
			}else if(response_operation.getMethod().equals(SCIMDefinitions.MethodType.PATCH.toString())){
				req_patch_count++;
				if(response_operation.getResponse() == null) {
					res_patch_count++;
				}
			}else if(response_operation.getMethod().equals(SCIMDefinitions.MethodType.DELETE.toString())){
				req_delete_count++;
				if(response_operation.getResponse() == null) {
					res_delete_count++;
				}
			}
		}
		
		sys_rep.addSchedulerHistory(scheduler.getSchedulerId(),workId,
				req_put_count,
				req_post_count,
				req_patch_count,
				req_delete_count,
				res_put_count,
				res_post_count,
				res_patch_count,
				res_delete_count
				);
		
	}

	private void addAudit(SCIMScheduler scheduler, SCIMUser worker,
			SCIMBulkRequest request, SCIMBulkResponse response) throws SCIMException {
		
		SCIMSystemRepository sys_rep = SCIMRepositoryManager.getInstance().getSystemRepository();
		
		List<SCIMBulkOperation> response_opersations = response.getOperations();
		for (SCIMBulkOperation request_operation : request.getOperations()) {
			SCIMBulkOperation response_operation = findResonseOperation(response_opersations, request_operation);
			
			sys_rep.addOperationResult(
				request.getRequestId(),
				worker, 
				scheduler.getSourceSystemId(), scheduler.getTargetSystemId(),
				request_operation, response_operation);
		}
	}

	private SCIMBulkOperation findResonseOperation(List<SCIMBulkOperation> response_opersations, SCIMBulkOperation request_operation) {
		for (SCIMBulkOperation response_operation : response_opersations) {
			if(response_operation.getLocation().indexOf(request_operation.getData().getId()) > 0) {
				return response_operation;
			}
		}
		return null;
	}

	@Override
	public void afterExecute(SCIMScheduler schedulerS, SCIMUser worker) {

	}

}
