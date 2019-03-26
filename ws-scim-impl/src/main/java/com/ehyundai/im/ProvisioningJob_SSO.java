package com.ehyundai.im;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import com.wowsanta.scim.repository.SCIMRepositoryManager;
import com.wowsanta.scim.repository.SCIMResourceGetterRepository;
import com.wowsanta.scim.repository.SCIMServerResourceRepository;
import com.wowsanta.scim.resource.SCIMLocationFactory;
import com.wowsanta.scim.resource.SCIMSystemRepository;
import com.wowsanta.scim.resource.worker.Worker;
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

	Logger logger = LoggerFactory.getLogger(ProvisioningJob_SSO.class);
	Logger audit_logger  = LoggerFactory.getLogger("audit");
	
	@Override
	public Object run(SCIMScheduler scheduler, Worker worker) throws SCIMException {
		
		try {
			
			SCIMSystemRepository system_repository = SCIMRepositoryManager.getInstance().getSystemRepository();
		
			String target_system_id = scheduler.getTargetSystemId();
			
			SCIMAudit audit = makeAuditObject(scheduler, worker);
			SCIMSchedulerHistory history = makeHistoryObject(scheduler, audit);
			
			SCIMBulkRequest scim_bluk_request = new SCIMBulkRequest();
			scim_bluk_request.setRequestId(audit.getWorkId());
			
			SCIMServerResourceRepository resource_repository = (SCIMServerResourceRepository) SCIMRepositoryManager.getInstance().getResourceRepository();
			List<SCIMUser> user_list  = getProvisionResource(scheduler);
			
			logger.info("Provision : {} > {} : {} ",audit.getWorkId(), target_system_id, user_list.size());
			for (SCIMUser scimUser : user_list) {
				
				SCIMUser target_system_user = resource_repository.getSystemUser(target_system_id, scimUser.getId());
				SCIMBulkOperation operation = new SCIMBulkOperation();
				operation.setPath(SCIMConstants.USER_ENDPOINT);
				operation.setData(scimUser);
				
				operation.setLocation(SCIMLocationFactory.getInstance().get(operation.getData()));

				if(target_system_user == null) {
					operation.setBulkId(Random.number(0, 1000000));
					operation.setMethod(SCIMDefinitions.MethodType.PUT.toString());
					
				}else {
					operation.setMethod(SCIMDefinitions.MethodType.POST.toString());
				}
				scim_bluk_request.addOperation(operation);
			}
			
			SCIMSystem target_system = system_repository.getSystemById(target_system_id);
			String bluk_request_url = target_system.getSystemUrl() + "/scim/v2.0/Bulk";
			
			logger.info("Provision Request: {} > {} : {} ",audit.getWorkId(), bluk_request_url, scim_bluk_request.getOperations().size());

			
			RESTClient client = new RESTClient(worker);
			String response_str = client.post2(bluk_request_url, scim_bluk_request);
			
			SCIMBulkResponse scim_bluk_response = new SCIMBulkResponse();
			scim_bluk_response.parse(response_str);
			logger.info("Provision Response : {} < {} : {} ",audit.getWorkId(), bluk_request_url, scim_bluk_response.getOperations().size());
			
			for (SCIMBulkOperation request_operation : scim_bluk_request.getOperations()) {
				SCIMBulkOperation response_operation = findResponseOperation(request_operation,scim_bluk_response.getOperations());
				
				audit.setUserId(request_operation.getData().getId());
				
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

				if(response_operation.getStatus().equals("200")) {
					
					if(request_operation.getMethod().equals(SCIMDefinitions.MethodType.PUT.toString())){
						resource_repository.createSystemUser(target_system_id, (SCIMUser) request_operation.getData());
					}else {
						resource_repository.updateSystemUser(target_system_id, (SCIMUser) request_operation.getData());
					}
				}
				//logger.info("Provision Response : {} > {} : {} ",audit.getWorkId(),request_operation );
				audit_logger.info("Provisioning SSO : {}", audit);
				system_repository.addAudit(audit);
				history.addAudit(audit);
				logger.info("Provision Response : {} < {} : {} ",audit.getWorkId(),response_operation );
			}
			
			system_repository.addSchedulerHistory(history);
			system_repository.updateSchdulerLastExcuteDate(scheduler.getSchedulerId(),new Date());
			
			logger.info("Provisioning SSO Result : {} ", history);

			return history;
			
		}catch (SCIMException e) {
			logger.error("Syn Failed : ",e);
			throw e;
		}catch (Exception e) {
			e.printStackTrace();
			throw new SCIMException(e.getMessage(), SCIMError.InternalServerError, e);
		}
	}

	private SCIMBulkOperation findResponseOperation(SCIMBulkOperation request_operation,
			List<SCIMBulkOperation> operations) {
		
		for (SCIMBulkOperation response_operation : operations) {
			if(response_operation.getLocation().endsWith(request_operation.getData().getId())) {
				return response_operation;
			}
		}
		
		return null;
	}

	private List<SCIMUser> getProvisionResource(SCIMScheduler scheduler) throws SCIMException {
		SCIMResourceGetterRepository resource_getter = (SCIMResourceGetterRepository) SCIMRepositoryManager.getInstance().getResourceRepository();
		Date last_execute_date = scheduler.getLastExecuteDate();
		System.out.println("last_execute_date : " +  last_execute_date);
		if(last_execute_date == null) {
			return resource_getter.getUsersByActive();
		}else {
			return resource_getter.getUsersByDate(last_execute_date, new Date());
		}
	}
	
//	
//	@Override
//	public void beforeExecute(JobExecutionContext context) throws SCIMException {
//
//		SCIMBulkRequest scim_bluk_request = new SCIMBulkRequest();
//		try {
//
//			SCIMResourceGetterRepository resource_getter = (SCIMResourceGetterRepository) SCIMRepositoryManager
//					.getInstance().getResourceRepository();
//
//			SCIMScheduler scheduler = (SCIMScheduler) context.getJobDetail().getJobDataMap().get("schedulerInfo");
//			SCIMUser worker = (SCIMUser) context.getJobDetail().getJobDataMap().get("worker");
//
//			System.out.println("run before exceute : " + worker);
//			SCIMSystem source_system = SCIMRepositoryManager.getInstance().getSystemRepository()
//					.getSystemById(scheduler.getSourceSystemId());
//			Date last_execute_date = scheduler.getLastExecuteDate();
//			if (last_execute_date == null) {
//				last_execute_date = new Date(0);
//			}
//
//			Calendar cal = Calendar.getInstance();
//			Date to = cal.getTime();
//			cal.setTime(last_execute_date);
//			Date from = cal.getTime();
//
//			// get system user
//			String system_id = scheduler.getSourceSystemId();
//			List<SCIMUser> user_list = resource_getter.getUsersByDate(from, to);
//			
//			boolean proc_break = true;
//			if(proc_break) {
//				logger.info("process break by --- developer ", "");
//				return;
//			}
//			
//			for (SCIMUser system_user : user_list) {
//
//				System.out.println(">>>" + system_user);
//
//				User user = (User) system_user;
//				SCIMBulkOperation operation = new SCIMBulkOperation();
//				operation.setPath(SCIMConstants.USER_ENDPOINT);
//				operation.setData(user);
//				if (user.getActive() == 0) {
//					operation.setMethod(SCIMDefinitions.MethodType.PATCH.toString());
//				} else {
//					if (last_execute_date == null) {
//						operation.setBulkId(Random.number(0, 1000000));
//						operation.setMethod(SCIMDefinitions.MethodType.PUT.toString());
//					} else {
//						if (user.getActive() == 1
//								&& user.getMeta().getCreated().equals(user.getMeta().getLastModified())) {
//							operation.setBulkId(Random.number(0, 1000000));
//							operation.setMethod(SCIMDefinitions.MethodType.PUT.toString());
//						} else {
//							operation.setMethod(SCIMDefinitions.MethodType.POST.toString());
//						}
//					}
//				}
//				scim_bluk_request.addOperation(operation);
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		context.getJobDetail().getJobDataMap().put("blukRequest", scim_bluk_request);
//
//	}
//
//	@Override
//	public void doExecute(JobExecutionContext context) throws SCIMException {
//		logger.info("ProvisionJob_SSO : {} ", new Date());
//
//		SCIMServerResourceRepository resource_repository = (SCIMServerResourceRepository) SCIMRepositoryManager
//				.getInstance().getResourceRepository();
//		SCIMProviderRepository system_repository = (SCIMProviderRepository) SCIMRepositoryManager.getInstance()
//				.getSystemRepository();
//
//		SCIMScheduler scheduler = (SCIMScheduler) context.getJobDetail().getJobDataMap().get("schedulerInfo");
//		SCIMUser worker = (SCIMUser) context.getJobDetail().getJobDataMap().get("worker");
//		SCIMBulkRequest scim_bluk_request = (SCIMBulkRequest) context.getJobDetail().getJobDataMap().get("blukRequest");
//
//		SCIMSystem target_system = SCIMRepositoryManager.getInstance().getSystemRepository()
//				.getSystemById(scheduler.getTargetSystemId());
//
//		String bluk_request_url = target_system.getSystemUrl() + "/scim/v2.0/Bulk";
//		System.out.println("get bulk url " + bluk_request_url);
//
//		boolean proc_break = true;
//		if(proc_break) {
//			logger.info("process break by --- developer ", "");
//			return;
//		}
//		
//		RESTClient client = new RESTClient(worker);
//
//		SCIMBulkResponse scim_bluk_response = client.post_bulk(bluk_request_url, scim_bluk_request);
//
//		context.getJobDetail().getJobDataMap().put("blukResponse", scim_bluk_response);
//		logger.info("ProvisionJob_SSO : {} ", new Date());
//
//	}
//
//	@Override
//	public void afterExecute(JobExecutionContext context) throws SCIMException {
//		System.out.println("run before afterExecute");
//
//		SCIMProviderRepository system_repository = (SCIMProviderRepository) SCIMRepositoryManager.getInstance()
//				.getSystemRepository();
//
//		SCIMScheduler scheduler = (SCIMScheduler) context.getJobDetail().getJobDataMap().get("schedulerInfo");
//		SCIMUser worker = (SCIMUser) context.getJobDetail().getJobDataMap().get("worker");
//		SCIMBulkRequest scim_bluk_request = (SCIMBulkRequest) context.getJobDetail().getJobDataMap().get("blukRequest");
//		SCIMBulkResponse scim_bluk_response = (SCIMBulkResponse) context.getJobDetail().getJobDataMap()
//				.get("blukResponse");
//
//		System.out.println("auditing......");
//		System.out.println(scim_bluk_request.getOperations().size());
//		System.out.println(scim_bluk_response.getOperations().size());
//		System.out.println("auditing......");
//
//		
//		boolean proc_break = true;
//		if(proc_break) {
//			logger.info("process break by --- developer ", "");
//			return;
//		}
//		
//		int req_put_count = 0;
//		int req_post_count = 0;
//		int req_patch_count = 0;
//		int req_delete_count = 0;
//
//		int res_put_count = 0;
//		int res_post_count = 0;
//		int res_patch_count = 0;
//		int res_delete_count = 0;
//
//		List<SCIMBulkOperation> response_opersations = null;//
//		if (scim_bluk_response != null) {
//			response_opersations = scim_bluk_response.getOperations();
//		}
//		System.out.println("response_opersations >>> " + response_opersations);
//
//		for (SCIMBulkOperation request_operation : scim_bluk_request.getOperations()) {
//
//			SCIMBulkOperation response_operation = findResonseOperation(response_opersations, request_operation);
//
//			System.out.println("response : " + response_operation);
//
//			system_repository.addOperationResult(scim_bluk_request.getRequestId(), worker,
//					scheduler.getSourceSystemId(), scheduler.getTargetSystemId(), request_operation,
//					response_operation);
//
//			if (request_operation.getMethod().equals(SCIMDefinitions.MethodType.PUT.toString())) {
//				req_put_count++;
//				if (response_operation.getResponse() == null) {
//					res_put_count++;
//				}
//			} else if (request_operation.getMethod().equals(SCIMDefinitions.MethodType.POST.toString())) {
//				req_post_count++;
//				if (response_operation.getResponse() == null) {
//					res_post_count++;
//				}
//			} else if (request_operation.getMethod().equals(SCIMDefinitions.MethodType.PATCH.toString())) {
//				req_patch_count++;
//				if (response_operation.getResponse() == null) {
//					res_patch_count++;
//				}
//			} else if (request_operation.getMethod().equals(SCIMDefinitions.MethodType.DELETE.toString())) {
//				req_delete_count++;
//				if (response_operation.getResponse() == null) {
//					res_delete_count++;
//				}
//			}
//		}
//
//		system_repository.addSchedulerHistory(scheduler.getSchedulerId(), scim_bluk_request.getRequestId(),
//				req_put_count, req_post_count, req_patch_count, req_delete_count, res_put_count, res_post_count,
//				res_patch_count, res_delete_count);
//	}
//
//	private SCIMBulkOperation findResonseOperation(List<SCIMBulkOperation> response_opersations,
//			SCIMBulkOperation request_operation) {
//		if (response_opersations == null) {
//			SCIMBulkOperation errer_operation = new SCIMBulkOperation();
//			errer_operation.setStatus(SCIMError.InternalServerError.getStatus());//;;.SCIMType.serverError.toString());
//			errer_operation.setResponse(SCIMError.InternalServerError);
//			return errer_operation;
//		}
//
//		for (SCIMBulkOperation response_operation : response_opersations) {
//			if (response_operation.getLocation().indexOf(request_operation.getData().getId()) > 0) {
//				return response_operation;
//			}
//		}
//
//		return null;
//	}

}
