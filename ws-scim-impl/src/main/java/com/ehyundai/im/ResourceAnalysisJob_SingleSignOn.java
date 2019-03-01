package com.ehyundai.im;

import java.util.Date;
import java.util.List;

import org.quartz.JobExecutionContext;

import com.wowsanta.scim.client.RESTClient;
import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.log.SCIMLogger;
import com.wowsanta.scim.message.SCIMBulkOperation;
import com.wowsanta.scim.message.SCIMBulkRequest;
import com.wowsanta.scim.message.SCIMBulkResponse;
import com.wowsanta.scim.message.SCIMError;
import com.wowsanta.scim.obj.SCIMSystem;
import com.wowsanta.scim.obj.SCIMUser;
import com.wowsanta.scim.resource.SCIMLocationFactory;
import com.wowsanta.scim.resource.SCIMProviderRepository;
import com.wowsanta.scim.resource.SCIMRepositoryManager;
import com.wowsanta.scim.resource.SCIMServerResourceRepository;
import com.wowsanta.scim.resource.SCIMSystemRepository;
import com.wowsanta.scim.scheduler.SCIMJob;
import com.wowsanta.scim.scheduler.SCIMScheduler;
import com.wowsanta.scim.schema.SCIMConstants;
import com.wowsanta.scim.schema.SCIMDefinitions;
import com.wowsanta.scim.schema.SCIMErrorCode;

public class ResourceAnalysisJob_SingleSignOn extends SCIMJob {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2799856505493920258L;

	@Override
	public void beforeExecute(JobExecutionContext context) throws SCIMException {
		
		SCIMScheduler scheduler = (SCIMScheduler) context.getJobDetail().getJobDataMap().get("schedulerInfo");
		SCIMUser worker = (SCIMUser) context.getJobDetail().getJobDataMap().get("worker");
		
		SCIMLogger.proc(" {} START : {}  ", scheduler.getSchedulerId(), worker);
				
		SCIMSystem source_system = SCIMRepositoryManager.getInstance().getSystemRepository().getSystemById(scheduler.getSourceSystemId());
		//Date last_execute_date = scheduler.getLastExecuteDate();
	
		boolean data_clear = true;
		Date last_execute_date = new Date(0);
		
		String bluk_request_url = source_system.getSystemUrl() + "/scim/v2.0/Bulk/" + last_execute_date.getTime();
		SCIMLogger.proc(" {} CALL BULK REQUEST : {}  ", scheduler.getSchedulerId(), bluk_request_url);
		
		RESTClient client = new RESTClient(worker);
		String response_string = client.call(bluk_request_url);
		SCIMBulkRequest scim_bluk_request = new SCIMBulkRequest();
		scim_bluk_request.parse(response_string);
		
		SCIMLogger.proc(" {} CALL BULK RESPONSE SIZE : {}  ", scheduler.getSchedulerId(), scim_bluk_request.getOperations().size());
		
		context.getJobDetail().getJobDataMap().put("blukRequest", scim_bluk_request);
		context.getJobDetail().getJobDataMap().put("data_clear", data_clear);
		//context.getJobDetail().getJobDataMap().put("worker", worker);
	}

	@Override
	public void doExecute(JobExecutionContext context) throws SCIMException{
		SCIMServerResourceRepository resource_repository = (SCIMServerResourceRepository) SCIMRepositoryManager.getInstance().getResourceRepository();
		//SCIMProviderRepository system_repository =  (SCIMProviderRepository) SCIMRepositoryManager.getInstance().getSystemRepository();
		
		SCIMScheduler scheduler = (SCIMScheduler) context.getJobDetail().getJobDataMap().get("schedulerInfo");
		SCIMUser worker = (SCIMUser) context.getJobDetail().getJobDataMap().get("worker");
		SCIMBulkRequest scim_bluk_request = (SCIMBulkRequest) context.getJobDetail().getJobDataMap().get("blukRequest");
		boolean data_clear = (Boolean) context.getJobDetail().getJobDataMap().get("data_clear");
		
		SCIMBulkResponse scim_bluk_response = new SCIMBulkResponse();
		String system_id = scheduler.getSourceSystemId();
		
		SCIMLogger.proc(" {} {} START : {} ", scheduler.getSchedulerId(), scheduler.getSchedulerType(), worker);

		List<SCIMBulkOperation> operation_list = scim_bluk_request.getOperations();
		if(data_clear) {
			resource_repository.clearSystemUser(system_id);
			resource_repository.clearSystemUserProfile(system_id);
			SCIMLogger.proc(" {} {} OLD DATA CLEARE : {} - {} ", scheduler.getSchedulerId(), scheduler.getSchedulerType(), system_id, data_clear);
		}
		
		
		for (SCIMBulkOperation operation : operation_list) {
			System.out.println("operation >> " + operation);
			
			String path = operation.getPath();
			String method = operation.getMethod();
			
			SCIMBulkOperation result = new SCIMBulkOperation(operation);
			try {
				if (path.equals(SCIMConstants.USER_ENDPOINT)) {
					SCIMUser user = (SCIMUser) operation.getData();
					result.setLocation(SCIMLocationFactory.getInstance().get(user));
					
					if(data_clear) {
						resource_repository.createSystemDummyUser(system_id, user);
						SCIMLogger.proc(" {} {} RESOURCE CREATE : {} - {} ", scheduler.getSchedulerId(), scheduler.getSchedulerType(), system_id, user.getId());
					}else {
						
					}
					result.setStatus(SCIMConstants.SatusConstants.SCUESS_CODE);
				}	
			}catch (SCIMException e) {
				e.printStackTrace();
				result.setStatus(e.getError().getStatus());
				result.setResponse(e.getError());
			}finally {
				scim_bluk_response.addOperation(result);
			}
		}
		
		context.getJobDetail().getJobDataMap().put("blukResponse", scim_bluk_response);
	}

	@Override
	public void afterExecute(JobExecutionContext context) throws SCIMException {
		System.out.println("run before afterExecute >>>");
		SCIMProviderRepository system_repository =  (SCIMProviderRepository) SCIMRepositoryManager.getInstance().getSystemRepository();
		SCIMServerResourceRepository resource_repository = (SCIMServerResourceRepository) SCIMRepositoryManager.getInstance().getResourceRepository();
		
		SCIMScheduler scheduler = (SCIMScheduler) context.getJobDetail().getJobDataMap().get("schedulerInfo");
		SCIMUser worker = (SCIMUser) context.getJobDetail().getJobDataMap().get("worker");
		SCIMBulkRequest scim_bluk_request  = (SCIMBulkRequest) context.getJobDetail().getJobDataMap().get("blukRequest");
		SCIMBulkResponse scim_bluk_response = (SCIMBulkResponse) context.getJobDetail().getJobDataMap().get("blukResponse");
		boolean data_clear = (Boolean) context.getJobDetail().getJobDataMap().get("data_clear");

		SCIMLogger.proc(" {} {} START ANALYSIS : {} - {} ", scheduler.getSchedulerId(), scheduler.getSchedulerType(), scheduler.getSourceSystemId());
		SCIMLogger.proc(" {} {} ANALYSIS : {} - {} ----------", scheduler.getSchedulerId(), scheduler.getSchedulerType(), scheduler.getSourceSystemId());
		SCIMLogger.proc(" {} {} END ANALYSIS : {} - {} ", scheduler.getSchedulerId(), scheduler.getSchedulerType(), scheduler.getSourceSystemId());
		

		system_repository.updateSchdulerLastExcuteDate(scheduler.getSchedulerId(), new Date());
		
	}
}
