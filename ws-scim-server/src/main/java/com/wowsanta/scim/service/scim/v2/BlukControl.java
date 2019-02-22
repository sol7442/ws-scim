package com.wowsanta.scim.service.scim.v2;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.ehyundai.object.User;
import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.message.SCIMBulkOperation;
import com.wowsanta.scim.message.SCIMBulkRequest;
import com.wowsanta.scim.message.SCIMBulkResponse;
import com.wowsanta.scim.obj.SCIMResource;
import com.wowsanta.scim.obj.SCIMUser;
import com.wowsanta.scim.resource.SCIMLocationFactory;
import com.wowsanta.scim.resource.SCIMRepositoryManager;
import com.wowsanta.scim.resource.SCIMResourceRepository;
import com.wowsanta.scim.schema.SCIMConstants;
import com.wowsanta.scim.schema.SCIMDefinitions;
import com.wowsanta.scim.schema.SCIMErrorCode;
import com.wowsanta.scim.service.scim.v2.service.BlukService;
import com.wowsanta.scim.service.scim.v2.service.SchedulerServer;
import com.wowsanta.scim.util.Random;

import spark.Request;
import spark.Response;
import spark.Route;

public class BlukControl {

	private static BlukService bulkService = new BlukService();
	private static SchedulerServer schedulerService = new SchedulerServer();
	
	public static Route post() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				SCIMBulkRequest  scim_bluk_request  = new SCIMBulkRequest();
				SCIMBulkResponse scim_bluk_response = new SCIMBulkResponse();

				SCIMUser worker = request.session().attribute("loginUser");
				scim_bluk_request.parse(request.body());

				try {
				
					///List<SCIMBulkOperation> operation_result_list = bulkService.excute(scim_bluk_request.getOperations());
					SCIMResourceRepository resource_repository = SCIMRepositoryManager.getInstance().getResourceRepository();
					List<SCIMBulkOperation> operation_result_list = new ArrayList<SCIMBulkOperation>();
					for (SCIMBulkOperation operation : scim_bluk_request.getOperations()) {
						String path 			= operation.getPath();
						String method 			= operation.getMethod();
						
						SCIMResource req_resource 	= operation.getData();
						SCIMBulkOperation operation_result = new SCIMBulkOperation(operation);
						
						try {
							SCIMResource res_resource = null;
							if (path.equals(SCIMConstants.USER_ENDPOINT)) {
								SCIMUser req_user  = (SCIMUser) req_resource;
								if (SCIMDefinitions.MethodType.PUT.toString().equals(method)) {
									res_resource = resource_repository.createUser(req_user);
								} else if (SCIMDefinitions.MethodType.PATCH.toString().equals(method)) {
									res_resource = resource_repository.updateUser(req_user);
								} else if (SCIMDefinitions.MethodType.POST.toString().equals(method)) {
									res_resource = resource_repository.updateUser(req_user);
								} else if (SCIMDefinitions.MethodType.DELETE.toString().equals(method)) {
									resource_repository.deleteUser(req_user.getId());
								} else {
									throw new SCIMException("",SCIMErrorCode.e500, SCIMErrorCode.SCIMType.invalidValue);
								}
							} else {
				
							}
							operation_result.setStatus(SCIMConstants.SatusConstants.SCUESS_CODE);
							if(res_resource != null) {
								operation_result.setLocation(SCIMLocationFactory.getInstance().get(res_resource));
							}
						} catch (Exception e) {
							e.printStackTrace();
							operation_result.setStatus("409");
							operation_result.setResponse(SCIMErrorCode.e409);
							
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
					System.out.println("bulk get call....: " + last_execute_date);
					
					scim_bluk_request.setRequestId(Random.number(0,10000000));
					
					SCIMResourceRepository res_repo  = SCIMRepositoryManager.getInstance().getResourceRepository();

					
					Date last_date = new Date(Long.valueOf(last_execute_date));//.getLastExecuteDate();
					
					Calendar cal = Calendar.getInstance();
					Date to = cal.getTime();
					cal.setTime(last_date);
					Date from = cal.getTime();
					
					
					List<SCIMUser> user_list = res_repo.getUsersByDate(from, to);
					
					System.out.println("user size : " + user_list.size());
					
					for (SCIMUser scimUser : user_list) {
						User user = (User) scimUser;
						
				//System.out.println("bulk get by syste --- system_user>>>"+ user.toString(true));
				
						SCIMBulkOperation operation = new SCIMBulkOperation();
						operation.setPath(SCIMConstants.USER_ENDPOINT);
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
						scim_bluk_request.addOperation(operation);
					}
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
System.out.println("====> call bulk all");
				try {
					scim_bluk_request.setRequestId(Random.number(0,10000000));
					
					SCIMResourceRepository res_repo  = SCIMRepositoryManager.getInstance().getResourceRepository();
					List<SCIMUser> user_list = res_repo.getAllUsers();
					
					System.out.println("user size : " + user_list.size());
					
					for (SCIMUser scimUser : user_list) {
						User user = (User) scimUser;
				
						SCIMBulkOperation operation = new SCIMBulkOperation();
						operation.setPath(SCIMConstants.USER_ENDPOINT);
						operation.setData(user);

						operation.setBulkId(Random.number(0, 1000000));
						operation.setMethod(SCIMDefinitions.MethodType.PUT.toString());
						
						scim_bluk_request.addOperation(operation);
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
