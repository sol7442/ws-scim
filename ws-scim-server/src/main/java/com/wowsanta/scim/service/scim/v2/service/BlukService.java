package com.wowsanta.scim.service.scim.v2.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wowsanta.scim.exception.SCIMError;
import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.log.SCIMLogger;
import com.wowsanta.scim.message.SCIMBulkOperation;
import com.wowsanta.scim.message.SCIMBulkRequest;
import com.wowsanta.scim.message.SCIMBulkResponse;
import com.wowsanta.scim.obj.SCIMResource;
import com.wowsanta.scim.obj.SCIMUser;
import com.wowsanta.scim.object.Resource_Object;
import com.wowsanta.scim.repository.AbstractSCIMRepository;
import com.wowsanta.scim.repository.RepositoryInputMapper;
import com.wowsanta.scim.repository.RepositoryOutputMapper;
import com.wowsanta.scim.repository.SCIMRepositoryManager;
import com.wowsanta.scim.repository.SCIMResourceGetterRepository;
import com.wowsanta.scim.repository.impl.OracleRepository;
import com.wowsanta.scim.repository.resource.SCIMResourceRepository;
import com.wowsanta.scim.repository.system.SCIMProviderRepository;
import com.wowsanta.scim.repository.system.SCIMSystemRepository;
import com.wowsanta.scim.repository.SCIMRepositoryController;
import com.wowsanta.scim.resource.SCIMLocationFactory;
import com.wowsanta.scim.resource.SCIMResourceSetterRepository;
import com.wowsanta.scim.resource.user.LoginUser;
import com.wowsanta.scim.resource.worker.Worker;
import com.wowsanta.scim.scheduler.SCIMScheduler;
import com.wowsanta.scim.schema.SCIMConstants;
import com.wowsanta.scim.schema.SCIMDefinitions;
import com.wowsanta.scim.schema.SCIMErrorCode;
import com.wowsanta.scim.service.SCIMService;

import spark.Request;
import spark.Response;
import spark.Route;

public class BlukService {

	Logger logger = LoggerFactory.getLogger(BlukService.class);

	
	public Route post() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				try {
					LoginUser login_user = request.session().attribute("loginUser");
				
					SCIMResourceRepository resource_repository = SCIMRepositoryManager.getInstance().getResourceRepository();
////					/******************		//local test ********************/			
//					final String sso_repository_config_file = "../config/backup_conf_20190429/default_oracle_sso_repository.json";
//					final String sso_user_resource_output_mapper_file = "../config/backup_conf_20190429/default_oracle_sso_user_resource_output_mapper.json";
//					final String sso_user_resource_input_mapper_file = "../config/backup_conf_20190429/default_oracle_sso_user_resource_input_mapper.json";
//
//					final String sso_group_resource_output_mapper_file = "../config/backup_conf_20190429/default_oracle_sso_group_resource_output_mapper.json";
//					final String sso_group_resource_input_mapper_file = "../config/backup_conf_20190429/default_oracle_sso_group_resource_input_mapper.json";
//
//					
//					RepositoryInputMapper user_resource_input_mapper = RepositoryInputMapper.load(sso_user_resource_input_mapper_file);
//					RepositoryOutputMapper user_resource_output_mapper = RepositoryOutputMapper.load(sso_user_resource_output_mapper_file);
//					RepositoryOutputMapper group_resource_output_mapper = RepositoryOutputMapper.load(sso_group_resource_output_mapper_file);
//					RepositoryInputMapper group_resource_input_mapper = RepositoryInputMapper.load(sso_group_resource_input_mapper_file);
//					
//					OracleRepository resource_repository = OracleRepository.load(sso_repository_config_file);
//					resource_repository.initialize();
//					resource_repository.setUserInputMapper(user_resource_input_mapper);
//					resource_repository.setUserOutputMapper(user_resource_output_mapper);
//					resource_repository.setGroupInputMapper(group_resource_input_mapper);
//					resource_repository.setGrouptOutputMapper(group_resource_output_mapper);
////					/******************		//local test ********************/
					
					
					Worker worker = new Worker();
					worker.setWorkerId(login_user.getUserId());
					worker.setWorkerType(login_user.getType().toString());
				
					SCIMBulkRequest scim_bluk_request = SCIMBulkRequest.parse(request.body());
					logger.info("Bluk Request [{}][{}] start > {} ", scim_bluk_request.getRequestId(),scim_bluk_request.getOperations().size(), worker);

					List<SCIMBulkOperation> operation_result_list = new ArrayList<SCIMBulkOperation>();
					for (SCIMBulkOperation operation : scim_bluk_request.getOperations()) {
						SCIMBulkOperation operation_result = new SCIMBulkOperation(operation);
						Resource_Object resource = operation.getData();
						
						logger.debug("Bluk operation : {}", operation);
						
						try {
							if(operation.getPath().equals("/Groups")) {
								Resource_Object old_group = resource_repository.getGroup(resource.getId());
								if(old_group == null) {
									operation_result.setMethod("CREATE");
									resource_repository.createGroup(resource);
								}else {
									operation_result.setMethod("UPDATE");
									resource_repository.updateGroup(resource);
								}
							}else if(operation.getPath().equals("/Users")) {
								Resource_Object old_user = resource_repository.getUser(resource.getId());
								if(old_user == null) {
									operation_result.setMethod("CREATE");
									resource_repository.createUser(resource);
								}else {
									operation_result.setMethod("UPDATE");
									resource_repository.updateUser(resource);
								}
							}else {
								logger.error("Bluk unkown : \n{}", operation.toString());
							}
							
							operation_result.setStatus(SCIMConstants.SatusConstants.SCUESS_CODE);
						}catch (Exception e) {
							operation_result.setStatus("409");
							SCIMError error = SCIMError.InternalServerError;
							error.addDetail(e.getMessage());
							operation_result.setResponse(error);
							
							logger.error("Bluk ERROR : {} {} ", e.getMessage(),resource.toString(true),e);
						}
						
						//operation_result.setLocation(SCIMLocationFactory.getInstance().get(operation.getData()));
						logger.info("Bluk result : {}", operation_result );
						operation_result_list.add(operation_result);
					}

					logger.info("Bluk Request [{}][{}] end > {} ", scim_bluk_request.getRequestId(),scim_bluk_request.getOperations().size(), worker);
					
					SCIMBulkResponse scim_bluk_response = new SCIMBulkResponse();
					scim_bluk_response.setOperations(operation_result_list);
					response.status(SCIMConstants.HtppConstants.CREATED);
					return scim_bluk_response;
					
				}catch (Exception e) {
					logger.error("SERVER ERROR : {} ",e.getMessage(),e);
					return SCIMError.InternalServerError;
				}
			}
		};
	}
	
//	private UserService userService = new UserService();
	
//	public static Route execute() {
//		return new Route() {
//			@Override
//			public Object handle(Request request, Response response) throws Exception {
//				SCIMBulkRequest scim_bluk_request = new SCIMBulkRequest();
//				SCIMBulkResponse scim_bluk_response = new SCIMBulkResponse();
//
//				SCIMUser worker = request.session().attribute("loginUser");
//				scim_bluk_request.parse(request.body());
//
//				for (SCIMBulkOperation operation : scim_bluk_request.getOperations()) {
//					String path = operation.getPath();
//					String method = operation.getMethod();
//					
//
//					SCIMBulkOperation result = null;
//					if (path.equals(SCIMConstants.USER_ENDPOINT)) {
//						if (SCIMDefinitions.MethodType.PUT.toString().equals(method)) {
//							result = addUser(operation);
//						} else if (SCIMDefinitions.MethodType.PATCH.toString().equals(method)) {
//							result = updateUser(operation);
//						} else if (SCIMDefinitions.MethodType.POST.toString().equals(method)) {
//							result = updateUser(operation);
//						} else if (SCIMDefinitions.MethodType.DELETE.toString().equals(method)) {
//							result = deleteUser(operation);
//						} else {
//						}
//					} else {
//
//					}
//					if (result != null) {
//						scim_bluk_response.addOperation(result);
//					}
//				}
//
//				
//				try {
//					writeSchdulerHistory(worker, scim_bluk_request, scim_bluk_response);
//				}catch (Exception e) {
//					e.printStackTrace();
//				}
//				
//
//				response.status(SCIMConstants.HtppConstants.CREATED);
//				return scim_bluk_response;
//			}
//
//			private void writeSchdulerHistory(SCIMUser worker, SCIMBulkRequest request, SCIMBulkResponse response) throws SCIMException {
//				
//				SCIMSystemRepository system_repository = SCIMRepositoryManager.getInstance().getSystemRepository();
//				int req_put_count = 0;
//				int req_post_count = 0;
//				int req_patch_count = 0;
//				int req_delete_count = 0;
//				
//				int res_put_count = 0;
//				int res_post_count = 0;
//				int res_patch_count = 0;
//				int res_delete_count = 0;
//				
//				List<SCIMBulkOperation> response_opersations = null;//
//				if(response != null) {
//					response_opersations = response.getOperations();
//				}
//				
//				for (SCIMBulkOperation request_operation : request.getOperations()) {
//					
//					SCIMBulkOperation response_operation = findResonseOperation(response_opersations, request_operation);
//
//					
////					system_repository.addOperationResult(
////							request.getRequestId(),
////							worker,
////							request.getSourceSystemId(),
////							request.getTargetSystemId(),
////							request_operation, response_operation);
////					
////					if(request_operation.getMethod().equals(SCIMDefinitions.MethodType.PUT.toString())){
////						req_put_count++;
////						if(response_operation.getResponse() == null) {
////							res_put_count++;
////						}
////					}else if(request_operation.getMethod().equals(SCIMDefinitions.MethodType.POST.toString())){
////						req_post_count++;
////						if(response_operation.getResponse() == null) {
////							res_post_count++;
////						}
////					}else if(request_operation.getMethod().equals(SCIMDefinitions.MethodType.PATCH.toString())){
////						req_patch_count++;
////						if(response_operation.getResponse() == null) {
////							res_patch_count++;
////						}
////					}else if(request_operation.getMethod().equals(SCIMDefinitions.MethodType.DELETE.toString())){
////						req_delete_count++;
////						if(response_operation.getResponse() == null) {
////							res_delete_count++;
////						}
////					}
//				}
//				
////				system_repository.addSchedulerHistory(request.getSchedulerId(),request.getRequestId(),
////						req_put_count,
////						req_post_count,
////						req_patch_count,
////						req_delete_count,
////						res_put_count,
////						res_post_count,
////						res_patch_count,
////						res_delete_count
////						);
//				
//			}
//
//			private SCIMBulkOperation findResonseOperation(List<SCIMBulkOperation> response_opersations, SCIMBulkOperation request_operation) {
//				if(response_opersations == null) {
//					SCIMBulkOperation errer_operation =  new SCIMBulkOperation();
//					
//					errer_operation.setStatus(SCIMErrorCode.SCIMType.serverError.toString());
//					errer_operation.setResponse(SCIMError.InternalServerError);
//					
//					return errer_operation;
//				}
//
//				for (SCIMBulkOperation response_operation : response_opersations) {
//					if(response_operation.getLocation().indexOf(request_operation.getData().getId()) > 0) {
//						return response_operation;
//					}
//				}
//				
//				return null;
//			}
//		};
//	}
//
//	private static SCIMBulkOperation updateUser(SCIMBulkOperation operation) {
//		SCIMBulkOperation result = new SCIMBulkOperation(operation);
//
//		SCIMResource resource = operation.getData();
//
//		SCIMResourceSetterRepository resource_repository = (SCIMResourceSetterRepository)SCIMRepositoryManager.getInstance().getResourceRepository();
//		try {
//			SCIMUser user = (SCIMUser) resource;
//			resource_repository.updateUser(user);
//			result.setLocation(SCIMLocationFactory.getInstance().get(user));
//			result.setStatus(SCIMConstants.SatusConstants.SCUESS_CODE);
//		} catch (SCIMException e) {
//			
////			
////			
////			SCIMError error = new SCIMError();
////			if (e.getCode() == SCIMRepository.RESULT_DUPLICATE_ENTRY) {
////				result.setStatus(SCIMConstants.SatusConstants.uniqueness);
////
////				error.setStatus(SCIMConstants.SatusConstants.uniqueness);
////				error.setScimType(SCIMDefinitions.ErrorType.uniqueness.toString());
////				error.setDetail(SCIMConstants.SatusConstants.uniqueness_DETAIL);
////			} else {
////				// bulk_resp.addError(bulkId, method, "400", null, "invalidSyntax", "Request is
////				// unparsable, syntactically incorrect, or violates schema.");
////				result.setStatus(SCIMConstants.SatusConstants.invalidSyntax);
////
////				error.setStatus(SCIMConstants.SatusConstants.invalidSyntax);
////				error.setScimType(SCIMDefinitions.ErrorType.invalidSyntax.toString());
////				error.setDetail(SCIMConstants.SatusConstants.invalidSyntax_DETAIL);
////			}
//
//			result.setResponse(SCIMError.BadRequest);
//
//		} finally {
//
//		}
//
//		return result;
//	}
//
//	private static SCIMBulkOperation deleteUser(SCIMBulkOperation operation) {
//		SCIMBulkOperation result = new SCIMBulkOperation(operation);
//		SCIMResource resource = operation.getData();
//		SCIMResourceSetterRepository resource_repository = (SCIMResourceSetterRepository)SCIMRepositoryManager.getInstance().getResourceRepository();
//
//		try {
//			SCIMUser user = (SCIMUser) resource;
//			resource_repository.deleteUser(user.getId());
//			result.setLocation(SCIMLocationFactory.getInstance().get(user));
//			result.setStatus(SCIMConstants.SatusConstants.SCUESS_CODE);
//
//		} catch (SCIMException e) {
////			SCIMError error = new SCIMError();
////
////			if (e.getCode() == SCIMRepository.RESULT_DUPLICATE_ENTRY) {
////				result.setStatus(SCIMConstants.SatusConstants.uniqueness);
////
////				error.setStatus(SCIMConstants.SatusConstants.uniqueness);
////				error.setScimType(SCIMDefinitions.ErrorType.uniqueness.toString());
////				error.setDetail(SCIMConstants.SatusConstants.uniqueness_DETAIL);
////
////			} else {
////				// bulk_resp.addError(bulkId, method, "400", null, "invalidSyntax", "Request is
////				// unparsable, syntactically incorrect, or violates schema.");
////				result.setStatus(SCIMConstants.SatusConstants.invalidSyntax);
////
////				error.setStatus(SCIMConstants.SatusConstants.invalidSyntax);
////				error.setScimType(SCIMDefinitions.ErrorType.invalidSyntax.toString());
////				error.setDetail(SCIMConstants.SatusConstants.invalidSyntax_DETAIL);
////			}
//
//			result.setResponse(SCIMError.BadRequest);
//
//		} finally {
//
//		}
//
//		return result;
//	}
//
//	private static SCIMBulkOperation addSystemUser(String systemId, SCIMBulkOperation operation) {
//		SCIMBulkOperation result = new SCIMBulkOperation(operation);
//		SCIMResource resource = operation.getData();
//
////		SCIMResourceRepository resource_repository = SCIMRepositoryManager.getInstance().getResourceRepository();
////		result.setLocation(SCIMLocationFactory.getInstance().get(resource));
////
////		try {
////			resource_repository.createSystemUser(systemId, (SCIMUser) resource);
////			result.setStatus(SCIMConstants.SatusConstants.SCUESS_CODE);
////
////		} catch (SCIMException e) {
////			if (e.getCode() == SCIMRepository.RESULT_DUPLICATE_ENTRY) {
////				result.setStatus(SCIMConstants.SatusConstants.uniqueness);
////				SCIMError error = new SCIMError();
////				error.setStatus(SCIMConstants.SatusConstants.uniqueness);
////				error.setScimType(SCIMDefinitions.ErrorType.uniqueness.toString());
////				error.setDetail(SCIMConstants.SatusConstants.uniqueness_DETAIL);
////
////				result.setResponse(error);
////			} else {
////				e.printStackTrace();
////			}
////		} finally {
////
////		}
//
//		return result;
//	}
//
//	private static SCIMBulkOperation addUser(SCIMBulkOperation operation) {
//		SCIMBulkOperation result = new SCIMBulkOperation(operation);
//		SCIMResource resource = operation.getData();
//
//		SCIMResourceSetterRepository resource_repository = (SCIMResourceSetterRepository)SCIMRepositoryManager.getInstance().getResourceRepository();
//		result.setLocation(SCIMLocationFactory.getInstance().get(resource));
//
//		try {
//			resource_repository.createUser((SCIMUser) resource);
//			result.setStatus(SCIMConstants.SatusConstants.SCUESS_CODE);
//
//		} catch (SCIMException e) {
//			//SCIMErrorCode.e409
//			result.setResponse(SCIMError.BadRequest);
//			e.printStackTrace();
//		} finally {
//
//		}
//
//		return result;
//	}
//
//
//	public List<SCIMBulkOperation> excute(List<SCIMBulkOperation> request_operations) throws SCIMException {
//		
//		SCIMResourceSetterRepository resource_repository = (SCIMResourceSetterRepository)SCIMRepositoryManager.getInstance().getResourceRepository();
//		
//		List<SCIMBulkOperation> operation_result_list = new ArrayList<SCIMBulkOperation>();
//		for (SCIMBulkOperation operation : request_operations) {
//			
//			String path 			= operation.getPath();
//			String method 			= operation.getMethod();
//			
//			SCIMResource req_resource 	= operation.getData();
//			SCIMBulkOperation operation_result = new SCIMBulkOperation(operation);
//			
//			try {
//				SCIMResource res_resource = null;
//				if (path.equals(SCIMConstants.USER_ENDPOINT)) {
//					SCIMUser req_user  = (SCIMUser) req_resource;
//					if (SCIMDefinitions.MethodType.PUT.toString().equals(method)) {
//						res_resource = resource_repository.createUser(req_user);
//					} else if (SCIMDefinitions.MethodType.PATCH.toString().equals(method)) {
//						res_resource = resource_repository.updateUser(req_user);
//					} else if (SCIMDefinitions.MethodType.POST.toString().equals(method)) {
//						res_resource = resource_repository.updateUser(req_user);
//					} else if (SCIMDefinitions.MethodType.DELETE.toString().equals(method)) {
//						resource_repository.deleteUser(req_user.getId());
//					} else {
//						throw new SCIMException(SCIMError.BadRequest);
//					}
//				} else {
//	
//				}
//				operation_result.setStatus(SCIMConstants.SatusConstants.SCUESS_CODE);
//				if(res_resource != null) {
//					operation_result.setLocation(SCIMLocationFactory.getInstance().get(res_resource));
//				}
//			} catch (SCIMException e) {
//				e.printStackTrace();
//
//				operation_result.setStatus(e.getError().getStatus());
//				operation_result.setResponse(e.getError());
//				
//			}finally {
//				operation_result_list.add(operation_result);
//			}
//		}
//		
//		return operation_result_list;
//	}


}
