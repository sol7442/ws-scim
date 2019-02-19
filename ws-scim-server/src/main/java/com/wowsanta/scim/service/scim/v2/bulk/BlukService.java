package com.wowsanta.scim.service.scim.v2.bulk;

import java.util.List;

import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.json.SCIMJsonObject;
import com.wowsanta.scim.log.SCIMLogger;
import com.wowsanta.scim.message.SCIMBulkOperation;
import com.wowsanta.scim.message.SCIMBulkRequest;
import com.wowsanta.scim.message.SCIMBulkResponse;
import com.wowsanta.scim.message.SCIMError;
import com.wowsanta.scim.obj.SCIMResource;
import com.wowsanta.scim.obj.SCIMUser;
import com.wowsanta.scim.resource.SCIMLocationFactory;
import com.wowsanta.scim.resource.SCIMRepository;
import com.wowsanta.scim.resource.SCIMRepositoryManager;
import com.wowsanta.scim.resource.SCIMResourceRepository;
import com.wowsanta.scim.resource.SCIMSystemRepository;
import com.wowsanta.scim.scheduler.SCIMScheduler;
import com.wowsanta.scim.schema.SCIMConstants;
import com.wowsanta.scim.schema.SCIMDefinitions;
import com.wowsanta.scim.service.SCIMService;

import spark.Request;
import spark.Response;
import spark.Route;

public class BlukService {
	
	public static Route execute() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				
				String system_id = request.params(":id");
				System.out.println(">>> system_id : " + system_id);
				
				if(system_id != null) {
					return systemBulk(system_id, request,response);
				}else {
					return localBulk(request,response);
				}
			}

			private Object localBulk(Request request, Response response) {
				SCIMBulkRequest scim_bluk_request = new SCIMBulkRequest();
				SCIMBulkResponse scim_bluk_response = new SCIMBulkResponse();
				
				SCIMSystemRepository systemRepository = SCIMRepositoryManager.getInstance().getSystemRepository();
				SCIMUser user = request.session().attribute("loginUser");
				scim_bluk_request.parse(request.body());
				
				for (SCIMBulkOperation operation : scim_bluk_request.getOperations()) {
					String path = operation.getPath();
					String method = operation.getMethod();
					
					SCIMBulkOperation result = null;
					if(path.equals(SCIMConstants.USER_ENDPOINT)) {
						if(SCIMDefinitions.MethodType.PUT.toString().equals(method)) {
							result = addUser(operation);
						}
						else if(SCIMDefinitions.MethodType.PATCH.toString().equals(method)) {
				        	System.out.println("PATCH : " + operation.toString());
							result = updateUser(operation);
						}
						else if(SCIMDefinitions.MethodType.POST.toString().equals(method)) {
							result = updateUser(operation);
						}
						else if(SCIMDefinitions.MethodType.DELETE.toString().equals(method)) {
							result = deleteUser(operation);
						}else {
							System.out.println("--------un : " + method);
						}
					}else {
					
					}
					
					if(result != null) {
						scim_bluk_response.addOperation(result);
					}
					
					try {
						
						systemRepository.addOperationResult(
								scim_bluk_request.getRequestId(),
								user,scim_bluk_request.getSourecSystemId(),scim_bluk_request.getDirectSystemId(),
								operation,result);
						
//						addAudit(scheduler, user, request, response);
//						addSchudlerHistory(scheduler, request.getRequestId(), response);
						
					}catch(Exception e) {
						SCIMLogger.error("BlukService ERROR", e);
					}
				}
				response.status(SCIMConstants.HtppConstants.CREATED);
				return scim_bluk_response;
				
			}

			
			private Object systemBulk(String systemId, Request request, Response response) {
				SCIMBulkRequest scim_bluk_request = new SCIMBulkRequest();
				SCIMBulkResponse scim_bluk_response = new SCIMBulkResponse();
				
				SCIMSystemRepository systemRepository = SCIMRepositoryManager.getInstance().getSystemRepository();
				SCIMResourceRepository resource_repository = SCIMRepositoryManager.getInstance().getResourceRepository();
				
				SCIMUser user = request.session().attribute("loginUser");
				scim_bluk_request.parse(request.body());
				
				
				try {
					resource_repository.clearSystemUser(systemId);
				} catch (SCIMException e1) {
					e1.printStackTrace();
				}
				
				for (SCIMBulkOperation operation : scim_bluk_request.getOperations()) {
					String path = operation.getPath();
					String method = operation.getMethod();
					
					SCIMBulkOperation result = null;
					if(path.equals(SCIMConstants.USER_ENDPOINT)) {
						if(SCIMDefinitions.MethodType.PUT.toString().equals(method)) {
							result = addSystemUser(systemId, operation);
						}
					}else {
					
					}
					
					if(result != null) {
						scim_bluk_response.addOperation(result);
					}
					
					try {
						systemRepository.addOperationResult(
								scim_bluk_request.getRequestId(),
								user,scim_bluk_request.getSourecSystemId(),scim_bluk_request.getDirectSystemId(),
								operation,result);
					}catch(Exception e) {
						SCIMLogger.error("BlukService ERROR", e);
					}
				}
				response.status(SCIMConstants.HtppConstants.CREATED);
				return scim_bluk_response;
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
				//bulk_resp.addError(bulkId, method, "400", null, "invalidSyntax", "Request is unparsable, syntactically incorrect, or violates schema.");
				result.setStatus(SCIMConstants.SatusConstants.invalidSyntax);
				
				error.setStatus(SCIMConstants.SatusConstants.invalidSyntax);
				error.setScimType(SCIMDefinitions.ErrorType.invalidSyntax.toString());
				error.setDetail(SCIMConstants.SatusConstants.invalidSyntax_DETAIL);
			}
			
			result.setResponse(error);
			
		}finally {
			
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
				//bulk_resp.addError(bulkId, method, "400", null, "invalidSyntax", "Request is unparsable, syntactically incorrect, or violates schema.");
				result.setStatus(SCIMConstants.SatusConstants.invalidSyntax);
				
				error.setStatus(SCIMConstants.SatusConstants.invalidSyntax);
				error.setScimType(SCIMDefinitions.ErrorType.invalidSyntax.toString());
				error.setDetail(SCIMConstants.SatusConstants.invalidSyntax_DETAIL);
			}
			
			result.setResponse(error);
			
		}finally {
			
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
		}finally {
			
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
		}finally {
			
		}	
		
		return result;
	}

}
