package com.ehyundai.im;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.quartz.JobExecutionContext;

import com.ehyundai.object.User;
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
import com.wowsanta.scim.scheduler.SCIMJob;
import com.wowsanta.scim.scheduler.SCIMScheduler;
import com.wowsanta.scim.schema.SCIMConstants;
import com.wowsanta.scim.schema.SCIMDefinitions;
import com.wowsanta.scim.schema.SCIMErrorCode;
import com.wowsanta.scim.util.Random;

public class ReconciliationJob_HR extends SCIMJob {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2799856505493920258L;

	@Override
	public void beforeExecute(JobExecutionContext context) throws SCIMException {

		SCIMBulkRequest scim_bluk_request = new SCIMBulkRequest();
		try {

			SCIMServerResourceRepository resource_repository = (SCIMServerResourceRepository) SCIMRepositoryManager
					.getInstance().getResourceRepository();

			SCIMScheduler scheduler = (SCIMScheduler) context.getJobDetail().getJobDataMap().get("schedulerInfo");
			SCIMUser worker = (SCIMUser) context.getJobDetail().getJobDataMap().get("worker");

			System.out.println("run before exceute : " + worker);
			SCIMSystem source_system = SCIMRepositoryManager.getInstance().getSystemRepository()
					.getSystemById(scheduler.getSourceSystemId());
			Date last_execute_date = scheduler.getLastExecuteDate();
			boolean data_clear = false;
			if (last_execute_date == null) {
				last_execute_date = new Date(0);
				data_clear = true;
			}

			Calendar cal = Calendar.getInstance();
			Date to = cal.getTime();
			cal.setTime(last_execute_date);
			Date from = cal.getTime();

			// get system user
			String system_id = scheduler.getSourceSystemId();

			List<SCIMUser> user_list = resource_repository.getSystemUserByDate(system_id, from, to);

			for (SCIMUser system_user : user_list) {
				System.out.println(">>>" + system_user);

				User user = (User) system_user;
				SCIMBulkOperation operation = new SCIMBulkOperation();
				operation.setPath(SCIMConstants.USER_ENDPOINT);
				operation.setData(user);
				if (user.getActive() == 0) {
					operation.setMethod(SCIMDefinitions.MethodType.PATCH.toString());
				} else {
					if (last_execute_date == null) {
						operation.setBulkId(Random.number(0, 1000000));
						operation.setMethod(SCIMDefinitions.MethodType.PUT.toString());
					} else {
						if (user.getActive() == 1
								&& user.getMeta().getCreated().equals(user.getMeta().getLastModified())) {
							operation.setBulkId(Random.number(0, 1000000));
							operation.setMethod(SCIMDefinitions.MethodType.PUT.toString());
						} else {
							operation.setMethod(SCIMDefinitions.MethodType.POST.toString());
						}
					}
				}
				scim_bluk_request.addOperation(operation);
			}

			context.getJobDetail().getJobDataMap().put("blukRequest", scim_bluk_request);
			context.getJobDetail().getJobDataMap().put("data_clear", data_clear);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void doExecute(JobExecutionContext context) throws SCIMException {
		System.out.println("run before doExecute");

		SCIMServerResourceRepository resource_repository = (SCIMServerResourceRepository) SCIMRepositoryManager
				.getInstance().getResourceRepository();
		SCIMProviderRepository system_repository = (SCIMProviderRepository) SCIMRepositoryManager.getInstance()
				.getSystemRepository();

		SCIMScheduler scheduler = (SCIMScheduler) context.getJobDetail().getJobDataMap().get("schedulerInfo");
		SCIMUser worker = (SCIMUser) context.getJobDetail().getJobDataMap().get("worker");
		SCIMBulkRequest scim_bluk_request = (SCIMBulkRequest) context.getJobDetail().getJobDataMap().get("blukRequest");
		boolean data_clear = (Boolean) context.getJobDetail().getJobDataMap().get("data_clear");

		SCIMBulkResponse scim_bluk_response = new SCIMBulkResponse();
		String system_id = scheduler.getSourceSystemId();

		if (data_clear) {
			resource_repository.clearUser();
			resource_repository.clearUserProfile();
		}

		List<SCIMBulkOperation> operation_list = scim_bluk_request.getOperations();
		for (SCIMBulkOperation operation : operation_list) {
			System.out.println("operation >> " + operation);

			String path = operation.getPath();
			String method = operation.getMethod();

			SCIMBulkOperation result = new SCIMBulkOperation(operation);
			try {

				if (path.equals(SCIMConstants.USER_ENDPOINT)) {
					SCIMUser user = (SCIMUser) operation.getData();
					result.setLocation(SCIMLocationFactory.getInstance().get(user));

					if (data_clear) {
						resource_repository.createUser(user);
					} else {
						if (SCIMDefinitions.MethodType.PUT.toString().equals(method)) {
							resource_repository.createUser(user);
						} else if (SCIMDefinitions.MethodType.PATCH.toString().equals(method)) {
							resource_repository.updateUser(user);
						} else if (SCIMDefinitions.MethodType.POST.toString().equals(method)) {
							resource_repository.updateUser(user);
						} else if (SCIMDefinitions.MethodType.DELETE.toString().equals(method)) {
							resource_repository.updateUser(user);
						}
					}
				}
				result.setStatus(SCIMConstants.SatusConstants.SCUESS_CODE);
			} catch (Exception e) {
				e.printStackTrace();
				result.setStatus(SCIMErrorCode.SCIMType.uniqueness.toString());
				result.setResponse(SCIMErrorCode.e409);
			} finally {

//				system_repository.addOperationResult(
//						scim_bluk_request.getRequestId(),
//						worker,
//						scheduler.getSourceSystemId(),
//						scheduler.getTargetSystemId(),
//						operation, result);

				scim_bluk_response.addOperation(result);
			}
		}

		context.getJobDetail().getJobDataMap().put("blukResponse", scim_bluk_response);
	}

	@Override
	public void afterExecute(JobExecutionContext context) throws SCIMException {
		System.out.println("run before afterExecute");

		SCIMProviderRepository system_repository = (SCIMProviderRepository) SCIMRepositoryManager.getInstance()
				.getSystemRepository();

		SCIMScheduler scheduler = (SCIMScheduler) context.getJobDetail().getJobDataMap().get("schedulerInfo");
		SCIMUser worker = (SCIMUser) context.getJobDetail().getJobDataMap().get("worker");
		SCIMBulkRequest scim_bluk_request = (SCIMBulkRequest) context.getJobDetail().getJobDataMap().get("blukRequest");
		SCIMBulkResponse scim_bluk_response = (SCIMBulkResponse) context.getJobDetail().getJobDataMap()
				.get("blukResponse");

		System.out.println("auditing......");
		System.out.println(scim_bluk_request.getOperations().size());
		System.out.println(scim_bluk_response.getOperations().size());
		System.out.println("auditing......");

		int req_put_count = 0;
		int req_post_count = 0;
		int req_patch_count = 0;
		int req_delete_count = 0;

		int res_put_count = 0;
		int res_post_count = 0;
		int res_patch_count = 0;
		int res_delete_count = 0;

		List<SCIMBulkOperation> response_opersations = null;//
		if (scim_bluk_response != null) {
			response_opersations = scim_bluk_response.getOperations();
		}
		System.out.println("response_opersations >>> " + response_opersations);

		for (SCIMBulkOperation request_operation : scim_bluk_request.getOperations()) {

			SCIMBulkOperation response_operation = findResonseOperation(response_opersations, request_operation);

			System.out.println("response : " + response_operation);

			system_repository.addOperationResult(scim_bluk_request.getRequestId(), worker,
					scheduler.getSourceSystemId(), scheduler.getTargetSystemId(), request_operation,
					response_operation);

			if (request_operation.getMethod().equals(SCIMDefinitions.MethodType.PUT.toString())) {
				req_put_count++;
				if (response_operation.getResponse() == null) {
					res_put_count++;
				}
			} else if (request_operation.getMethod().equals(SCIMDefinitions.MethodType.POST.toString())) {
				req_post_count++;
				if (response_operation.getResponse() == null) {
					res_post_count++;
				}
			} else if (request_operation.getMethod().equals(SCIMDefinitions.MethodType.PATCH.toString())) {
				req_patch_count++;
				if (response_operation.getResponse() == null) {
					res_patch_count++;
				}
			} else if (request_operation.getMethod().equals(SCIMDefinitions.MethodType.DELETE.toString())) {
				req_delete_count++;
				if (response_operation.getResponse() == null) {
					res_delete_count++;
				}
			}
		}

		system_repository.addSchedulerHistory(scheduler.getSchedulerId(), scim_bluk_request.getRequestId(),
				req_put_count, req_post_count, req_patch_count, req_delete_count, res_put_count, res_post_count,
				res_patch_count, res_delete_count);
	}

	private SCIMBulkOperation findResonseOperation(List<SCIMBulkOperation> response_opersations,
			SCIMBulkOperation request_operation) {
		if (response_opersations == null) {
			SCIMBulkOperation errer_operation = new SCIMBulkOperation();
			errer_operation.setStatus(SCIMErrorCode.SCIMType.serverError.toString());
			errer_operation.setResponse(SCIMErrorCode.e408);
			return errer_operation;
		}

		for (SCIMBulkOperation response_operation : response_opersations) {
			if (response_operation.getLocation().indexOf(request_operation.getData().getId()) > 0) {
				return response_operation;
			}
		}

		return null;
	}

}
