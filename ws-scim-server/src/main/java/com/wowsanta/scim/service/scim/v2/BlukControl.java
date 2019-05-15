package com.wowsanta.scim.service.scim.v2;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ehyundai.object.User;
import com.wowsanta.scim.exception.SCIMError;
import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.log.SCIMLogger;
import com.wowsanta.scim.message.SCIMBulkOperation;
import com.wowsanta.scim.message.SCIMBulkRequest;
import com.wowsanta.scim.message.SCIMBulkResponse;
import com.wowsanta.scim.obj.SCIMResource;
import com.wowsanta.scim.obj.SCIMUser;
import com.wowsanta.scim.object.Resource_Object;
import com.wowsanta.scim.repository.SCIMRepositoryManager;
import com.wowsanta.scim.repository.SCIMResourceGetterRepository;
import com.wowsanta.scim.repository.SCIMRepositoryController;
import com.wowsanta.scim.resource.SCIMLocationFactory;
import com.wowsanta.scim.resource.SCIMResourceSetterRepository;
import com.wowsanta.scim.resource.user.LoginUser;
import com.wowsanta.scim.schema.SCIMConstants;
import com.wowsanta.scim.schema.SCIMDefinitions;
import com.wowsanta.scim.schema.SCIMErrorCode;
import com.wowsanta.scim.service.scim.v2.service.BlukService;
import com.wowsanta.scim.util.Random;

import spark.Request;
import spark.Response;
import spark.Route;

public class BlukControl {
	static Logger logger = LoggerFactory.getLogger(BlukControl.class);
	
	private static BlukService bulkService = new BlukService();
	
	public static Route post() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				SCIMBulkRequest  scim_bluk_request  = new SCIMBulkRequest();
				SCIMBulkResponse scim_bluk_response = new SCIMBulkResponse();

				LoginUser worker = request.session().attribute("loginUser");
				scim_bluk_request.parse(request.body());
				try {

					SCIMResourceSetterRepository resource_repository = (SCIMResourceSetterRepository)SCIMRepositoryManager.getInstance().getResourceRepository();
					List<SCIMBulkOperation> operation_result_list = new ArrayList<SCIMBulkOperation>();
					
					for (SCIMBulkOperation operation : scim_bluk_request.getOperations()) {
						String path 			= operation.getPath();
						String method 			= operation.getMethod();
						
						Resource_Object req_resource 	= operation.getData();
						SCIMBulkOperation operation_result = new SCIMBulkOperation(operation);
						
						try {
							SCIMResource res_resource = null;
							if (path.equals(SCIMConstants.USER_ENDPOINT)) {
								//SCIMUser req_user  = (SCIMUser) req_resource;
//								if (SCIMDefinitions.MethodType.PUT.toString().equals(method)) {
//									res_resource = resource_repository.createUser(req_user);
//								} else if (SCIMDefinitions.MethodType.PATCH.toString().equals(method)) {
//									res_resource = resource_repository.updateUser(req_user);
//								} else if (SCIMDefinitions.MethodType.POST.toString().equals(method)) {
//									res_resource = resource_repository.updateUser(req_user);
//								} else if (SCIMDefinitions.MethodType.DELETE.toString().equals(method)) {
//									resource_repository.deleteUser(req_user.getId());
//								} else {
//									throw new SCIMException(SCIMError.BadRequest);//"",SCIMErrorCode.e500, SCIMErrorCode.SCIMType.invalidValue);
//								}
							} else {
				
							}
							operation_result.setStatus(SCIMConstants.SatusConstants.SCUESS_CODE);
							if(res_resource != null) {
								operation_result.setLocation(SCIMLocationFactory.getInstance().get(res_resource));
							}
						} catch (Exception e) {
							e.printStackTrace();
							operation_result.setStatus("409");
							operation_result.setResponse(SCIMError.InternalServerError);
							
						}finally {
							System.out.println("bulk result >>> " + operation_result);
							operation_result_list.add(operation_result);
						}
					}
					
					scim_bluk_response.setOperations(operation_result_list);
					response.status(SCIMConstants.HtppConstants.CREATED);
					
				}catch(Exception e) {
					e.printStackTrace();
				}
				
				return scim_bluk_response;
			}
		};
	}

	public static Route get() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				SCIMBulkRequest scim_bluk_request = new SCIMBulkRequest();
				

				try {
					String last_execute_date = request.params(":lasteDate");
					
					logger.info("BULK REQUEST - GET : {}", last_execute_date);

					scim_bluk_request.setRequestId(Random.number(0,10000000));
					SCIMResourceGetterRepository res_repo  = (SCIMResourceGetterRepository)SCIMRepositoryManager.getInstance().getResourceRepository();

					Date last_date = new Date(Long.valueOf(last_execute_date));
					Calendar cal = Calendar.getInstance();
					Date to = cal.getTime();
					cal.setTime(last_date);
					Date from = cal.getTime();
					
					List<SCIMUser> user_list = null;
					if(last_execute_date.equals("0")) {
						logger.info("BULK REQUEST - GET USER DATA : {}", "ALL ACTIVE USER");
						user_list = res_repo.getUsersByActive();
					}else {
						logger.info("BULK REQUEST - GET USER DATA : {} - {} ",from,to);
						user_list = res_repo.getUsersByDate(from, to);
					}
					 
					logger.info("BULK REQUEST - GET DATA SIZE : {}", user_list.size());
					
					int count_post = 0;
					int count_put = 0;
					int count_patch = 0;
					
//					for (SCIMUser scimUser : user_list) {
//						User user = (User) scimUser;
//						SCIMBulkOperation operation = new SCIMBulkOperation();
//						operation.setPath(SCIMConstants.USER_ENDPOINT);
//						operation.setData(user);
//						
//						if(last_execute_date.equals("0")) {
//							operation.setBulkId(Random.number(0, 1000000));
//							operation.setMethod(SCIMDefinitions.MethodType.PUT.toString());
//							count_put++;
//						}else {
//							if (user.getActive() == 0) {
//								operation.setMethod(SCIMDefinitions.MethodType.PATCH.toString());
//								count_patch++;
//							} else {
//								if (user.getActive() == 1 && user.getMeta().getCreated().equals(user.getMeta().getLastModified())) {
//									operation.setBulkId(Random.number(0, 1000000));
//									operation.setMethod(SCIMDefinitions.MethodType.PUT.toString());
//									count_put++;
//								} else {
//									operation.setMethod(SCIMDefinitions.MethodType.POST.toString());
//									count_post++;
//								}
//							}
//						}
//						scim_bluk_request.addOperation(operation);
//					}
					
					logger.info("BULK REQUEST - GET PUT {}, POST {}, PATCH {} - ALL {} ",count_put,count_post,count_patch,user_list.size());

				}catch (Exception e) {
					e.printStackTrace();
				}
				
				response.status(SCIMConstants.HtppConstants.CREATED);
				return scim_bluk_request;
			}
		};
	}

	public static Route getAll() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				SCIMBulkRequest scim_bluk_request = new SCIMBulkRequest();
				try {
					scim_bluk_request.setRequestId(Random.number(0,10000000));
					
					SCIMResourceGetterRepository res_repo  = (SCIMResourceGetterRepository)SCIMRepositoryManager.getInstance().getResourceRepository();
					List<SCIMUser> user_list = res_repo.getAllUsers();
					
					System.out.println("user size : " + user_list.size());
					
					for (SCIMUser scimUser : user_list) {
//						User user = (User) scimUser;
//				
//						SCIMBulkOperation operation = new SCIMBulkOperation();
//						operation.setPath(SCIMConstants.USER_ENDPOINT);
//						operation.setData(user);
//
//						operation.setBulkId(Random.number(0, 1000000));
//						operation.setMethod(SCIMDefinitions.MethodType.PUT.toString());
//						scim_bluk_request.addOperation(operation);
					}
				}catch (Exception e) {
					e.printStackTrace();
				}
				
				response.status(SCIMConstants.HtppConstants.CREATED);
				return scim_bluk_request;
			}
		};
	}

}
