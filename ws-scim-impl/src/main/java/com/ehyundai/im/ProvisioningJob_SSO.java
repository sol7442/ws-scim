package com.ehyundai.im;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wowsanta.scim.client.RESTClient;
import com.wowsanta.scim.exception.SCIMError;
import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.log.SCIMLogger;
import com.wowsanta.scim.message.SCIMBulkOperation;
import com.wowsanta.scim.message.SCIMBulkRequest;
import com.wowsanta.scim.message.SCIMBulkResponse;
import com.wowsanta.scim.obj.SCIMAudit;
import com.wowsanta.scim.obj.SCIMSchedulerHistory;
import com.wowsanta.scim.obj.SCIMSystem;
import com.wowsanta.scim.obj.SCIMUser;
import com.wowsanta.scim.object.Resource_Object;
import com.wowsanta.scim.repository.SCIMRepositoryManager;
import com.wowsanta.scim.repository.SCIMResourceGetterRepository;
import com.wowsanta.scim.repository.SCIMServerResourceRepository;
import com.wowsanta.scim.repository.resource.SCIMResourceRepository;
import com.wowsanta.scim.repository.system.SCIMSystemRepository;
import com.wowsanta.scim.resource.SCIMLocationFactory;
import com.wowsanta.scim.resource.worker.Worker;
import com.wowsanta.scim.scheduler.SCIMJob;
import com.wowsanta.scim.scheduler.SCIMScheduler;
import com.wowsanta.scim.schema.SCIMConstants;
import com.wowsanta.scim.schema.SCIMDefinitions;
import com.wowsanta.scim.schema.SCIMDefinitions.ResoureType;
import com.wowsanta.scim.util.Random;

import oracle.net.aso.g;

public class ProvisioningJob_SSO extends SCIMJob {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2799856505493920258L;

	Logger logger = LoggerFactory.getLogger(ProvisioningJob_SSO.class);
	Logger audit_logger  = LoggerFactory.getLogger("audit");
	
	@Override
	public Object run(SCIMScheduler scheduler, Worker worker) throws SCIMException {
		SCIMSchedulerHistory history = null;
		try {
			
			SCIMSystemRepository system_repository = SCIMRepositoryManager.getInstance().getSystemRepository();
			String target_system_id = scheduler.getTargetSystemId();
			
			SCIMAudit audit = makeAuditObject(scheduler, worker);
			 history = makeHistoryObject(scheduler, audit);
			
			SCIMBulkRequest scim_bluk_request = new SCIMBulkRequest();
			scim_bluk_request.setRequestId(audit.getWorkId());

			SCIMResourceRepository resource_repository = SCIMRepositoryManager.getInstance().getResourceRepository();

			String filter = null;
			Date last_exec_date = scheduler.getLastExecuteDate();
			if(last_exec_date !=null) {
				SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				filter = "modifyDate ge '" + transFormat.format(last_exec_date) + "'"; // or id eq \"803148\" ";
			}
			logger.info("Bulk Filter -Grop : {} ", filter);
			
			int totoal_group_count = resource_repository.getGroupCount(filter);
			List<Resource_Object> group_list = resource_repository.searchGroup(filter,0,0,totoal_group_count);
			for (Resource_Object group : group_list) {
				SCIMBulkOperation operation = new SCIMBulkOperation();
				operation.setPath(SCIMConstants.GROUP_ENDPOINT);
				operation.setData(group);
				operation.setBulkId(Random.number(0, 1000000));
				operation.setMethod(SCIMDefinitions.MethodType.PUT.toString());
				
				scim_bluk_request.addOperation(operation);
				
				logger.info("operation : {}",operation);
			}
			logger.info("Bulk Size -Grop : {} ", group_list.size());
			
			int totoal_user_count = resource_repository.getUserCount(filter);
			List<Resource_Object> user_list = resource_repository.searchUser(filter,0,0,totoal_user_count);
			for (Resource_Object user : user_list) {
				SCIMBulkOperation operation = new SCIMBulkOperation();
				operation.setPath(SCIMConstants.USER_ENDPOINT);
				operation.setData(user);
				operation.setBulkId(Random.number(0, 1000000));
				operation.setMethod(SCIMDefinitions.MethodType.PUT.toString());
				scim_bluk_request.addOperation(operation);
				
				logger.info("operation : {}",operation);
			}
			logger.info("Bulk Size -User : {} ", user_list.size());
			
			SCIMSystem target_system = system_repository.getSystemById(target_system_id);

			String bluk_request_url = target_system.getSystemUrl() + "/scim/v2.0/Bulk";
//			String bluk_request_url = "https://127.0.0.1:5000" + "/scim/v2.0/Bulk";
			
			RESTClient client = new RESTClient(worker);
			SCIMBulkResponse scim_bluk_response = client.post_bulk(bluk_request_url, scim_bluk_request, scheduler.getEncode());

			logger.info("SCIMBulkResponse : {} " , scim_bluk_response.toString(false));
			
			for (SCIMBulkOperation request_operation : scim_bluk_request.getOperations()) {

				SCIMBulkOperation response_operation = findResponseOperation(request_operation,scim_bluk_response.getOperations());
				audit.setResourceId(request_operation.getData().getId());

				if(SCIMConstants.GROUP_ENDPOINT.equals(request_operation.getPath())) {
					audit.setResourceType(ResoureType.GROUP);
				}else {
					audit.setResourceType(ResoureType.USER);
				}
				
				if(request_operation.getMethod().equals(SCIMDefinitions.MethodType.PUT.toString())) {
					audit.setMethod("CREATE");
				}else {
					audit.setMethod("UPDATE");
				}
				
				if(response_operation.getStatus().equals("200")) {
					audit.setResult("SUCCESS");
				}else {
					audit.setResult("FAILED");
					audit.setDetail(response_operation.getResponse().getDetail());
				}

//				if(response_operation.getStatus().equals("200")) {
//					if(request_operation.getMethod().equals(SCIMDefinitions.MethodType.PUT.toString())){
//						resource_repository.createSystemUser(target_system_id, (SCIMUser) request_operation.getData());
//					}else {
//						resource_repository.updateSystemUser(target_system_id, (SCIMUser) request_operation.getData());
//					}
//				}
				//logger.info("Provision Response : {} > {} : {} ",audit.getWorkId(),request_operation );
				audit_logger.info("Provisioning SSO : {}", audit);
				system_repository.addAudit(audit);
				history.addAudit(audit);
				logger.info("Provision Response : {} < {} : {} ",audit.getWorkId(),response_operation );
			}
			
			system_repository.addSchedulerHistory(history);
			if(history.getFailCount() == 0) {
				system_repository.updateSchdulerLastExcuteDate(scheduler.getSchedulerId(),new Date());
			}

			return history;
			
		}catch (Exception e) {
			logger.error("Syn Failed : {} ",e.getMessage(), e);
		}
		
		return history;
	}

	private SCIMBulkOperation findResponseOperation(SCIMBulkOperation request_operation,
			List<SCIMBulkOperation> operations) {
		for (SCIMBulkOperation response_operation : operations) {
			if(response_operation.getBulkId().equals(request_operation.getBulkId())) {
				return 	response_operation;
			}
		}
		return null;
	}

}
