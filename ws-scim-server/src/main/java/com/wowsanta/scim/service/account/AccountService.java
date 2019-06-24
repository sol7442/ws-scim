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
import com.wowsanta.scim.protocol.FrontSearchRequest;
import com.wowsanta.scim.protocol.ResponseState;
import com.wowsanta.scim.repository.AttributeValue;
import com.wowsanta.scim.repository.RepositoryException;
import com.wowsanta.scim.repository.RepositoryInputMapper;
import com.wowsanta.scim.repository.RepositoryOutputMapper;
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
					SCIMListResponse result = new SCIMListResponse();
					FrontSearchRequest search = WowsantaJson.parse(request.body(),FrontSearchRequest.class);
					
					logger.debug("search request > {}", search);
					logger.debug("search where   > {}", search.getWhere());
					
					SCIMResourceRepository resource_repository = SCIMRepositoryManager.getInstance().getResourceRepository();
					String filter = makeFilter(search.getAttributes());

					int total_count = resource_repository.getUserCount(filter);
					List<Resource_Object> user_list = resource_repository.searchUser(filter, search.getStartIndex(),search.getCount(), total_count);
					
					
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

			private String makeFilter(List<AttributeValue> attributes) {
				if(attributes == null) {
					return null;
				}
				
				StringBuffer buffer = new StringBuffer();
				for (AttributeValue attributeValue : attributes) {
					buffer.append(attributeValue.getName());
					buffer.append(" ").append("eq").append(" ");
					buffer.append(attributeValue.getValue());
				}
				
				
				return buffer.toString();
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
					
					List<Resource_Object> user_list = null;
					
					SCIMSystemRepository system_repository  =  SCIMRepositoryManager.getInstance().getSystemRepository();
					user_list = system_repository.getSystemAccountListByUserId(userId);
					
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
					
					SCIMResourceRepository resource_repository  =  SCIMRepositoryManager.getInstance().getResourceRepository();
					
					int total_count  = resource_repository.getUserCount(null);
					int active_count = resource_repository.getUserCount("active eq 1");
					
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
					
					int total_count  = system_repository.getSystemAccountCount(systemId);
					int active_count = system_repository.getSystemActiveAccountCount(systemId);
					
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
					
					SCIMResourceRepository resource_repository  =  SCIMRepositoryManager.getInstance().getResourceRepository();
					SCIMSystemRepository  system_repository  =  SCIMRepositoryManager.getInstance().getSystemRepository();
					
					int total_count    = resource_repository.getUserCount(null);
					int isolated_count = system_repository.getIsolatatedAccountCount();
										
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

					int total_count    = system_repository.getSystemAccountCount(systemId);
					int ghost_count =    system_repository.getSystemGhostAccountCount(systemId);
					
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
					FrontSearchRequest search = WowsantaJson.parse(request.body(),FrontSearchRequest.class);
					
					SCIMListResponse  result = new SCIMListResponse();
					String systemId = request.params("systemId");
					
					SCIMSystemRepository system_repository = SCIMRepositoryManager.getInstance().getSystemRepository();
					SCIMResourceRepository resource_repository = SCIMRepositoryManager.getInstance().getResourceRepository();
					
					
					
					String system_user_input_mapper_file = SCIMRepositoryManager.getInstance().getResourceRepositoryConfig().getSystemUserInputMapper();
					String system_user_output_mapper_file = SCIMRepositoryManager.getInstance().getResourceRepositoryConfig().getSystemUseroutputMapper();
					logger.info("system_user_input_mapper : {} ", system_user_input_mapper_file);
					logger.info("system_user_output_mapper_file : {} ", system_user_output_mapper_file);
					RepositoryOutputMapper system_user_resource_output_mapper = null;
					RepositoryInputMapper  system_user_resource_input_mapper = null;
					try {
						system_user_resource_input_mapper  = RepositoryInputMapper.load(system_user_input_mapper_file);
						system_user_resource_output_mapper = RepositoryOutputMapper.load(system_user_output_mapper_file);
					} catch (Exception e) {
						logger.error(e.getMessage(),e);
					}
					
					int total_count = system_repository.getSystemAccountCount(systemId);
					
					List<AttributeValue> attribute_list = search.getAttributes();
					AttributeValue system_id_value = new AttributeValue("systemId",systemId);
					attribute_list.add(system_id_value);
					
					List<Resource_Object> user_list = resource_repository.searchSystemUser(system_user_resource_output_mapper, attribute_list, search.getStartIndex(), search.getCount(), total_count);
					
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

	public Route getSystemAccountHistory() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				FrontResponse front_response = new FrontResponse();
				try {
					
					String systemId = request.params("systemId");
					String userId = request.params("userId");
					
					logger.debug("find systemId < {}", systemId);
					logger.debug("find userId < {}", userId);
					
					
					SCIMSystemRepository system_repository = SCIMRepositoryManager.getInstance().getSystemRepository(); 

					List<SCIMAudit> audit_list = system_repository.getSystemAccountHistory(systemId,userId);
										
					front_response.setData(audit_list);
					front_response.setState(ResponseState.Success);
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
