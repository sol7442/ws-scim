package com.wowsanta.scim.service.account;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wowsanta.scim.message.SCIMFindRequest;
import com.wowsanta.scim.message.SCIMListResponse;
import com.wowsanta.scim.obj.SCIMAudit;
import com.wowsanta.scim.object.Resource_Object;
import com.wowsanta.scim.protocol.FrontReqeust;
import com.wowsanta.scim.protocol.FrontResponse;
import com.wowsanta.scim.protocol.ResponseState;
import com.wowsanta.scim.repository.SCIMRepositoryManager;
import com.wowsanta.scim.repository.resource.SCIMResourceRepository;
import com.wowsanta.scim.repository.system.SCIMSystemRepository;
import com.wowsanta.scim.resource.SCIMAuditData;
import com.wowsata.util.json.WowsantaJson;

import spark.Request;
import spark.Response;
import spark.Route;

public class AccountService {

	Logger logger = LoggerFactory.getLogger(AccountService.class);
	
	public Route getSystemAccount() {
		return new Route() {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				String systemId = request.params(":systemId");
				
				SCIMResourceRepository  resource_repository  =  SCIMRepositoryManager.getInstance().getResourceRepository();
				
				List<Resource_Object> user_list = resource_repository.searchUser(null,0,0, resource_repository.getUserCount(null));
				
				System.out.println("user_list : " + user_list.size());
				
				return user_list;
			}
		};
	}

	public Route getAccountHistory() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				FrontResponse front_response = new FrontResponse();
				try {
				
					String userId = request.params(":userId");
					SCIMSystemRepository  system_repository  =  SCIMRepositoryManager.getInstance().getSystemRepository();
					List<SCIMAudit> account_history_list = system_repository.getAccountHistory(userId);
					
					front_response.setData(account_history_list);
					front_response.setState(ResponseState.Success);
					
					logger.info("account history size ", account_history_list.size());
					
				}catch (Exception e) {
					logger.error(e.getMessage(),e);
					front_response.setMessage(e.getMessage());
					front_response.setState(ResponseState.Fail);
				}
				return front_response;
			}
		};
	}

	public Route findUserList() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				FrontResponse front_response = new FrontResponse();
				try {
					logger.debug("reqeust body : \n{}",request.body());
					SCIMListResponse  result = new SCIMListResponse();
					
					SCIMFindRequest find = WowsantaJson.parse(request.body(),SCIMFindRequest.class);
					
					logger.debug("find request > {}", find);
					logger.debug("find where   > {}", find.getWhere());
					
					SCIMResourceRepository  resource_repository  =  SCIMRepositoryManager.getInstance().getResourceRepository();
					
					int total_count  = resource_repository.getUserCount(null);

					List<Resource_Object> user_list = resource_repository.searchUser(null,0,0, total_count);
					result.setTotalResults(total_count);
					result.setResources(user_list);
					
					logger.debug("find response < {}", result.getTotalResults());
										
					front_response.setState(ResponseState.Success);
					front_response.setData(result);
				}catch(Exception e) {
					front_response.setState(ResponseState.Fail);
					front_response.setMessage(e.getMessage());
					
					logger.error("Acount Error",e);
				}
				
				return front_response;
			}
		};
	}

	public Route getUserSystemList() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				FrontResponse front_response = new FrontResponse();
				try {
					String userId = request.params(":userId");
					logger.debug("getUserSystemList : {}", userId);
					
					List<Resource_Object> user_list = new ArrayList<Resource_Object>();
					SCIMResourceRepository  resource_repository  =  SCIMRepositoryManager.getInstance().getResourceRepository();
					//List<Resource_Object> user_list = resource_repository.searchUser(null,0,0, resource_repository.getUserCount(null));
					
					Resource_Object resource = resource_repository.getUser(userId);
					resource.put("systemId", "sys-scim-sso");
					user_list.add(resource);
					
					
					front_response.setState(ResponseState.Success);
					front_response.setData(user_list);
					
					logger.debug("getUserSystemList : {}", user_list.size());
					
				}catch (Exception e) {
					front_response.setState(ResponseState.Fail);
					front_response.setMessage(e.getMessage());
					
					logger.error("Acount Error",e);
				}
				
				return front_response;
			}
		};
	}

	public Route getUserState() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				
				FrontResponse front_response = new FrontResponse();

				
				try {
					
					SCIMSystemRepository  system_repository  =  SCIMRepositoryManager.getInstance().getSystemRepository();
					
					int total_count  = 21;//system_repository.getTotoalAccountCount();
					int active_count = 18;//system_repository.getInactiveAccountCount();
					
					Map<String,Integer> user_state_map = new HashMap<String,Integer>(); 
					user_state_map.put("total", total_count);
					user_state_map.put("active", active_count);
					user_state_map.put("inactive", (total_count - active_count));
					
					front_response.setState(ResponseState.Success);
					front_response.setData(user_state_map);
					
				}catch(Exception e){
					front_response.setState(ResponseState.Fail);
					front_response.setMessage(e.getMessage());
					
					logger.error("Acount Error",e);
				}

				
				return front_response;
			}
		};
	}
	
	public Route getSysUserState() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				
				FrontResponse front_response = new FrontResponse();
				try {
					String systemId = request.params(":systemId");

					SCIMSystemRepository  system_repository  =  SCIMRepositoryManager.getInstance().getSystemRepository();
					
					///SCIMProviderRepository provider_repository = (SCIMProviderRepository)SCIMRepositoryManager.getInstance().getSystemRepository();
					int total_count  = 29;//system_repository.getTotoalSystemAccountCount(systemId);
					int active_count = 23;//system_repository.getInactiveSystemAccountCount(systemId);
					
					Map<String,Integer> user_state_map = new HashMap<String,Integer>(); 
					user_state_map.put("total", total_count);
					user_state_map.put("active", active_count);
					user_state_map.put("inactive", (total_count - active_count));
					
					front_response.setState(ResponseState.Success);
					front_response.setData(user_state_map);
					
				}catch(Exception e){
					front_response.setState(ResponseState.Fail);
					front_response.setMessage(e.getMessage());
					
					logger.error("Acount Error",e);
				}

				
				return front_response;
			}
		};
	}

	public Route getUserStatus() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				FrontResponse front_response = new FrontResponse();
				try {
					SCIMSystemRepository  system_repository  =  SCIMRepositoryManager.getInstance().getSystemRepository();

//					SCIMProviderRepository provider_repository = (SCIMProviderRepository)SCIMRepositoryManager.getInstance().getSystemRepository();
					int total_count    = 31;// provider_repository.getTotoalAccountCount();
					int isolated_count = 3;//provider_repository.getIsolatatedAccountCount();
					
					Map<String,Integer> user_state_map = new HashMap<String,Integer>(); 
					user_state_map.put("total", total_count);
					
					user_state_map.put("isolatate", isolated_count);
					user_state_map.put("integrate", (total_count - isolated_count));
					
					front_response.setState(ResponseState.Success);
					front_response.setData(user_state_map);
					
				}catch(Exception e){
					front_response.setState(ResponseState.Fail);
					front_response.setMessage(e.getMessage());
					
					logger.error("Acount Error",e);
				}

				
				return front_response;
			}
		};
	}
	
	public Route getSysUserStatus() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				FrontResponse front_response = new FrontResponse();
				try {
					
					String systemId = request.params(":systemId");

					SCIMSystemRepository  system_repository  =  SCIMRepositoryManager.getInstance().getSystemRepository();

//					SCIMProviderRepository provider_repository = (SCIMProviderRepository)SCIMRepositoryManager.getInstance().getSystemRepository();
					int total_count    = 23;//provider_repository.getTotoalSystemAccountCount(systemId);
					int ghost_count =    4;//provider_repository.getGhostSysAccountCount(systemId);
					
					Map<String,Integer> user_state_map = new HashMap<String,Integer>(); 
					user_state_map.put("total", total_count);
					
					user_state_map.put("ghost", ghost_count);
					user_state_map.put("integrate", (total_count - ghost_count));
					
					front_response.setState(ResponseState.Success);
					front_response.setData(user_state_map);
					
				}catch(Exception e){
					front_response.setState(ResponseState.Fail);
					front_response.setMessage(e.getMessage());
					
					logger.error("Acount Error",e);
				}

				
				return front_response;
			}
		};
	}

	public Route findSysUserList() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				FrontResponse front_response = new FrontResponse();
				try {
					
					logger.debug("reqeust body : \n{}",request.body());
					SCIMListResponse  result = new SCIMListResponse();
					SCIMFindRequest find = WowsantaJson.parse(request.body(),SCIMFindRequest.class);

					String systemId = request.params("systemId");
//					
//					logger.debug("find system > {}", systemId);
//					logger.debug("find request > {}", find);
//					logger.debug("find where   > {}", find.getWhere());
					
					SCIMResourceRepository  resource_repository  =  SCIMRepositoryManager.getInstance().getResourceRepository();
					int total_count  = resource_repository.getUserCount(null);

					List<Resource_Object> user_list = resource_repository.searchUser(null,0,0, total_count);
					
					result.setResources(user_list);
					result.setTotalResults(total_count);
					
					
					front_response.setState(ResponseState.Success);
					front_response.setData(result);
				}catch(Exception e) {
					front_response.setState(ResponseState.Fail);
					front_response.setMessage(e.getMessage());
					
					logger.error("Acount Error",e);
				}
				
				return front_response;
			}
		};
	}



	

}
