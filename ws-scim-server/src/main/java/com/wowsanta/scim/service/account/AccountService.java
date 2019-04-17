package com.wowsanta.scim.service.account;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wowsanta.scim.message.SCIMFindRequest;
import com.wowsanta.scim.message.SCIMListResponse;
import com.wowsanta.scim.obj.SCIMResource2;
import com.wowsanta.scim.obj.SCIMUser;
import com.wowsanta.scim.protocol.FrontResponse;
import com.wowsanta.scim.protocol.ResponseState;
import com.wowsanta.scim.repository.SCIMRepositoryManager;
import com.wowsanta.scim.repository.SCIMServerResourceRepository;
import com.wowsanta.scim.resource.SCIMAuditData;
import com.wowsanta.scim.resource.SCIMProviderRepository;
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
				
				SCIMServerResourceRepository  resource_repository = (SCIMServerResourceRepository) SCIMRepositoryManager.getInstance().getResourceRepository();
				
				List<SCIMUser> user_list = resource_repository.getSystemUsersBysystemIdWidthPage(systemId);
				
				System.out.println("user_list : " + user_list.size());
				
				return user_list;
			}
		};
	}

	public Route getAccountHistory() {
		//userId
		// TODO Auto-generated method stub
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				String userId = request.params(":userId");
				
				SCIMServerResourceRepository  resource_repository = (SCIMServerResourceRepository) SCIMRepositoryManager.getInstance().getResourceRepository();
				
				List<SCIMAuditData> account_history_list = resource_repository.getAccountHistoryByUsrIdWidthPage(userId);
				
				System.out.println("account_history_list : " + account_history_list.size());
				
				return account_history_list;
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
					
					
					SCIMProviderRepository provider_repository = (SCIMProviderRepository)SCIMRepositoryManager.getInstance().getSystemRepository();
					int total_count  = provider_repository.getTotoalAccountCount();
					int page_count   = find.getCount();
					int page_index   = find.getStartIndex();
							
					int total_page = total_count / page_count;
					if (total_count % page_count > 0) {
						total_page++;
					}
					
					int start_index = page_count * page_index + 1;
					int end_index   = page_count * (page_index + 1);
					
					logger.debug("find request > {}-{}", start_index, end_index);

//					List<SCIMResource2> user_list = provider_repository.getUsersByWhere(find.getWhere(),find.getOrder(), start_index, end_index);
//					for (SCIMResource2 scimUser : user_list) {
//						result.addReource(scimUser);
//					}
					result.setTotalResults(total_count);
					
					logger.debug("find response < {}", result.getTotalResults());
					
					//response.status(200);
					
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
					
					SCIMProviderRepository provider_repository = (SCIMProviderRepository)SCIMRepositoryManager.getInstance().getSystemRepository();

					
					List<SCIMResource2> user_list = provider_repository.getUserSystemList(userId);
					
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
					SCIMProviderRepository provider_repository = (SCIMProviderRepository)SCIMRepositoryManager.getInstance().getSystemRepository();
					int total_count  = provider_repository.getTotoalAccountCount();
					int active_count = provider_repository.getInactiveAccountCount();
					
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

					SCIMProviderRepository provider_repository = (SCIMProviderRepository)SCIMRepositoryManager.getInstance().getSystemRepository();
					int total_count  = provider_repository.getTotoalSystemAccountCount(systemId);
					int active_count = provider_repository.getInactiveSystemAccountCount(systemId);
					
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
					SCIMProviderRepository provider_repository = (SCIMProviderRepository)SCIMRepositoryManager.getInstance().getSystemRepository();
					int total_count    = provider_repository.getTotoalAccountCount();
					int isolated_count = provider_repository.getIsolatatedAccountCount();
					
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

					SCIMProviderRepository provider_repository = (SCIMProviderRepository)SCIMRepositoryManager.getInstance().getSystemRepository();
					int total_count    = provider_repository.getTotoalSystemAccountCount(systemId);
					int ghost_count = provider_repository.getGhostSysAccountCount(systemId);
					
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
					
					logger.debug("find system > {}", systemId);
					logger.debug("find request > {}", find);
					logger.debug("find where   > {}", find.getWhere());
					
					
					SCIMProviderRepository provider_repository = (SCIMProviderRepository)SCIMRepositoryManager.getInstance().getSystemRepository();
					int total_count  = provider_repository.getTotoalSystemAccountCount(systemId);
					int page_count   = find.getCount();
					int page_index   = find.getStartIndex();
							
					int total_page = total_count / page_count;
					if (total_count % page_count > 0) {
						total_page++;
					}
					
					int start_index = page_count * page_index + 1;
					int end_index   = page_count * (page_index + 1);
					
					logger.debug("find request > {}-{}", start_index, end_index);

//					List<SCIMResource2> user_list = provider_repository.getSystemUsersByWhere(systemId,find.getWhere(),find.getOrder(), start_index, end_index);
//					for (SCIMResource2 scimUser : user_list) {
//						result.addReource(scimUser);
//					}
					result.setTotalResults(total_count);
					
					logger.debug("find response < {}", result.getTotalResults());
					
					//response.status(200);
					
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
