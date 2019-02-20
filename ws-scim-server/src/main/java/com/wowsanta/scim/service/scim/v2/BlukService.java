package com.wowsanta.scim.service.scim.v2;

import java.util.List;

import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.log.SCIMLogger;
import com.wowsanta.scim.message.SCIMBulkOperation;
import com.wowsanta.scim.message.SCIMBulkRequest;
import com.wowsanta.scim.message.SCIMBulkResponse;
import com.wowsanta.scim.message.SCIMError;
import com.wowsanta.scim.obj.SCIMResource;
import com.wowsanta.scim.obj.SCIMUser;
import com.wowsanta.scim.resource.SCIMLocationFactory;
import com.wowsanta.scim.resource.SCIMProviderRepository;
import com.wowsanta.scim.resource.SCIMRepository;
import com.wowsanta.scim.resource.SCIMRepositoryManager;
import com.wowsanta.scim.resource.SCIMResourceRepository;
import com.wowsanta.scim.resource.SCIMSystemRepository;
import com.wowsanta.scim.scheduler.SCIMScheduler;
import com.wowsanta.scim.schema.SCIMConstants;
import com.wowsanta.scim.schema.SCIMDefinitions;
import com.wowsanta.scim.schema.SCIMErrorCode;
import com.wowsanta.scim.service.SCIMService;

import spark.Request;
import spark.Response;
import spark.Route;

public class BlukService {

	public static Route execute() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				SCIMBulkRequest scim_bluk_request = new SCIMBulkRequest();
				SCIMBulkResponse scim_bluk_response = new SCIMBulkResponse();

				SCIMUser worker = request.session().attribute("loginUser");
				scim_bluk_request.parse(request.body());

				for (SCIMBulkOperation operation : scim_bluk_request.getOperations()) {
					String path = operation.getPath();
					String method = operation.getMethod();
					
System.out.println("bulk-request-op : " + operation);

					SCIMBulkOperation result = null;
					if (path.equals(SCIMConstants.USER_ENDPOINT)) {
						if (SCIMDefinitions.MethodType.PUT.toString().equals(method)) {
							result = addUser(operation);
						} else if (SCIMDefinitions.MethodType.PATCH.toString().equals(method)) {
							result = updateUser(operation);
						} else if (SCIMDefinitions.MethodType.POST.toString().equals(method)) {
							result = updateUser(operation);
						} else if (SCIMDefinitions.MethodType.DELETE.toString().equals(method)) {
							result = deleteUser(operation);
						} else {
							System.out.println("--------un : " + method);
						}
					} else {

					}
System.out.println("bulk result >>> " + result);
					if (result != null) {
						scim_bluk_response.addOperation(result);
					}
				}

				
				try {
					writeSchdulerHistory(worker, scim_bluk_request, scim_bluk_response);
				}catch (Exception e) {
					e.printStackTrace();
				}
				

				response.status(SCIMConstants.HtppConstants.CREATED);
				return scim_bluk_response;
			}

			private void writeSchdulerHistory(SCIMUser worker, SCIMBulkRequest request, SCIMBulkResponse response) throws SCIMException {
				
				SCIMSystemRepository system_repository = SCIMRepositoryManager.getInstance().getSystemRepository();
				int req_put_count = 0;
				int req_post_count = 0;
				int req_patch_count = 0;
				int req_delete_count = 0;
				
				int res_put_count = 0;
				int res_post_count = 0;
				int res_patch_count = 0;
				int res_delete_count = 0;
				
				List<SCIMBulkOperation> response_opersations = null;//
				if(response != null) {
					response_opersations = response.getOperations();
				}
				System.out.println("response_opersations >>> " +  response_opersations);
				
				for (SCIMBulkOperation request_operation : request.getOperations()) {
					
					SCIMBulkOperation response_operation = findResonseOperation(response_opersations, request_operation);

					System.out.println("response : " + response_operation);
					
					system_repository.addOperationResult(
							request.getRequestId(),
							worker,
							request.getSourceSystemId(),
							request.getTargetSystemId(),
							request_operation, response_operation);
					
					if(request_operation.getMethod().equals(SCIMDefinitions.MethodType.PUT.toString())){
						req_put_count++;
						if(response_operation.getResponse() == null) {
							res_put_count++;
						}
					}else if(request_operation.getMethod().equals(SCIMDefinitions.MethodType.POST.toString())){
						req_post_count++;
						if(response_operation.getResponse() == null) {
							res_post_count++;
						}
					}else if(request_operation.getMethod().equals(SCIMDefinitions.MethodType.PATCH.toString())){
						req_patch_count++;
						if(response_operation.getResponse() == null) {
							res_patch_count++;
						}
					}else if(request_operation.getMethod().equals(SCIMDefinitions.MethodType.DELETE.toString())){
						req_delete_count++;
						if(response_operation.getResponse() == null) {
							res_delete_count++;
						}
					}
				}
				
				system_repository.addSchedulerHistory(request.getSchedulerId(),request.getRequestId(),
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

			private SCIMBulkOperation findResonseOperation(List<SCIMBulkOperation> response_opersations, SCIMBulkOperation request_operation) {
				if(response_opersations == null) {
					SCIMBulkOperation errer_operation =  new SCIMBulkOperation();
					errer_operation.setStatus(SCIMErrorCode.SCIMType.serverError.toString());
					errer_operation.setResponse(SCIMErrorCode.e408);
					return errer_operation;
				}

				for (SCIMBulkOperation response_operation : response_opersations) {
					if(response_operation.getLocation().indexOf(request_operation.getData().getId()) > 0) {
						return response_operation;
					}
				}
				
				return null;
			}
		};
	}

	private static SCIMBulkOperation updateUser(SCIMBulkOperation operation) {
		SCIMBulkOperation result = new SCIMBulkOperation(operation);

		SCIMResource resource = operation.getData();

		SCIMResourceRepository resource_repository = SCIMRepositoryManager.getInstance().getResourceRepository();
		try {
			SCIMUser user = (SCIMUser) resource;
			resource_repository.updateUser(user);
			result.setLocation(SCIMLocationFactory.getInstance().get(user));
			result.setStatus(SCIMConstants.SatusConstants.SCUESS_CODE);
		} catch (SCIMException e) {
			SCIMError error = new SCIMError();
			if (e.getCode() == SCIMRepository.RESULT_DUPLICATE_ENTRY) {
				result.setStatus(SCIMConstants.SatusConstants.uniqueness);

				error.setStatus(SCIMConstants.SatusConstants.uniqueness);
				error.setScimType(SCIMDefinitions.ErrorType.uniqueness.toString());
				error.setDetail(SCIMConstants.SatusConstants.uniqueness_DETAIL);
			} else {
				// bulk_resp.addError(bulkId, method, "400", null, "invalidSyntax", "Request is
				// unparsable, syntactically incorrect, or violates schema.");
				result.setStatus(SCIMConstants.SatusConstants.invalidSyntax);

				error.setStatus(SCIMConstants.SatusConstants.invalidSyntax);
				error.setScimType(SCIMDefinitions.ErrorType.invalidSyntax.toString());
				error.setDetail(SCIMConstants.SatusConstants.invalidSyntax_DETAIL);
			}

			result.setResponse(error);

		} finally {

		}

		return result;
	}

	private static SCIMBulkOperation deleteUser(SCIMBulkOperation operation) {
		SCIMBulkOperation result = new SCIMBulkOperation(operation);
		SCIMResource resource = operation.getData();
		SCIMResourceRepository resource_repository = SCIMRepositoryManager.getInstance().getResourceRepository();

		try {
			SCIMUser user = (SCIMUser) resource;
			resource_repository.deleteUser(user.getId());
			result.setLocation(SCIMLocationFactory.getInstance().get(user));
			result.setStatus(SCIMConstants.SatusConstants.SCUESS_CODE);

		} catch (SCIMException e) {
			SCIMError error = new SCIMError();

			if (e.getCode() == SCIMRepository.RESULT_DUPLICATE_ENTRY) {
				result.setStatus(SCIMConstants.SatusConstants.uniqueness);

				error.setStatus(SCIMConstants.SatusConstants.uniqueness);
				error.setScimType(SCIMDefinitions.ErrorType.uniqueness.toString());
				error.setDetail(SCIMConstants.SatusConstants.uniqueness_DETAIL);

			} else {
				// bulk_resp.addError(bulkId, method, "400", null, "invalidSyntax", "Request is
				// unparsable, syntactically incorrect, or violates schema.");
				result.setStatus(SCIMConstants.SatusConstants.invalidSyntax);

				error.setStatus(SCIMConstants.SatusConstants.invalidSyntax);
				error.setScimType(SCIMDefinitions.ErrorType.invalidSyntax.toString());
				error.setDetail(SCIMConstants.SatusConstants.invalidSyntax_DETAIL);
			}

			result.setResponse(error);

		} finally {

		}

		return result;
	}

	private static SCIMBulkOperation addSystemUser(String systemId, SCIMBulkOperation operation) {
		SCIMBulkOperation result = new SCIMBulkOperation(operation);
		SCIMResource resource = operation.getData();

		SCIMResourceRepository resource_repository = SCIMRepositoryManager.getInstance().getResourceRepository();
		result.setLocation(SCIMLocationFactory.getInstance().get(resource));

		try {
			resource_repository.createSystemUser(systemId, (SCIMUser) resource);
			result.setStatus(SCIMConstants.SatusConstants.SCUESS_CODE);

		} catch (SCIMException e) {
			if (e.getCode() == SCIMRepository.RESULT_DUPLICATE_ENTRY) {
				result.setStatus(SCIMConstants.SatusConstants.uniqueness);
				SCIMError error = new SCIMError();
				error.setStatus(SCIMConstants.SatusConstants.uniqueness);
				error.setScimType(SCIMDefinitions.ErrorType.uniqueness.toString());
				error.setDetail(SCIMConstants.SatusConstants.uniqueness_DETAIL);

				result.setResponse(error);
			} else {
				e.printStackTrace();
			}
		} finally {

		}

		return result;
	}

	private static SCIMBulkOperation addUser(SCIMBulkOperation operation) {
		SCIMBulkOperation result = new SCIMBulkOperation(operation);
		SCIMResource resource = operation.getData();

		SCIMResourceRepository resource_repository = SCIMRepositoryManager.getInstance().getResourceRepository();
		result.setLocation(SCIMLocationFactory.getInstance().get(resource));

		try {
			resource_repository.createUser((SCIMUser) resource);
			result.setStatus(SCIMConstants.SatusConstants.SCUESS_CODE);

		} catch (SCIMException e) {
			//SCIMErrorCode.e409
			result.setResponse(SCIMErrorCode.e409);
			e.printStackTrace();
		} finally {

		}

		return result;
	}

}
