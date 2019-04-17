package com.wowsanta.scim.service.scim.v2.service;


import java.util.List;
import java.util.Map;

import org.eclipse.jetty.security.PropertyUserStore.UserListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wowsanta.scim.exception.SCIMError;
import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.log.SCIMLogger;
import com.wowsanta.scim.message.SCIMFindRequest;
import com.wowsanta.scim.message.SCIMListResponse;
import com.wowsanta.scim.message.SCIMSearchRequest;
import com.wowsanta.scim.obj.SCIMResource2;
import com.wowsanta.scim.obj.SCIMUser;
import com.wowsanta.scim.object.SCIM_Resource;
import com.wowsanta.scim.object.SCIM_User;
import com.wowsanta.scim.repository.SCIMRepositoryManager;
import com.wowsanta.scim.repository.SCIMResourceGetterRepository;
import com.wowsanta.scim.repository.SCIMUserResourceGetter;
import com.wowsanta.scim.resource.SCIMProviderRepository;
import com.wowsanta.scim.resource.user.LoginUser;

import spark.Request;
import spark.Response;
import spark.Route;

import static com.wowsanta.scim.server.JsonUtil.json_parse;

public class UserService {

	Logger logger = LoggerFactory.getLogger(UserService.class);
	Logger audit_logger  = LoggerFactory.getLogger("audit");
	
	public Route getUser() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				String userId = request.params(":userId");

				System.out.println("getUserById : " + userId);
				SCIMResourceGetterRepository resource_repository = (SCIMResourceGetterRepository)SCIMRepositoryManager.getInstance().getResourceRepository();
				SCIMUser user = resource_repository.getUser(userId);
				System.out.println("getUserById : " + user);
				
				return user;
			}
		};
	}

	public Route find() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				try {
					
					logger.debug("reqeust body : \n{}",request.body());
					SCIMListResponse  result = new SCIMListResponse();
					SCIMFindRequest find = json_parse(request.body(),SCIMFindRequest.class);
					
					logger.debug("find request > {}", find);
					logger.debug("find where   > {}", find.getWhere());
					
					
					//SCIMProviderRepository provider_repository = (SCIMProviderRepository)SCIMRepositoryManager.getInstance().getSystemRepository();
					//SCIMResourceGetterRepository resource_repository = (SCIMResourceGetterRepository)SCIMRepositoryManager.getInstance().getResourceRepository();
					
					SCIMUserResourceGetter repository =  (SCIMUserResourceGetter)SCIMRepositoryManager.getInstance().getRepository();
					
					int total_count  = repository.getTotoalUserCount(find.getWhere());
					int page_count   = find.getCount();
					int start_index   = find.getStartIndex();
					
					List<SCIM_Resource> user_list = repository.findUsers(find.getWhere(),find.getOrder(), start_index, page_count);
					logger.info("find user size : {}", user_list.size());
					for (SCIM_Resource scim_User : user_list) {
						System.out.println("find user >> " + scim_User.getId());
					}
					
					result.setResources(user_list);
					result.setTotalResults(total_count);
//					if(page_count <=0) {
//						page_count = total_count;
//					}
//					
//					int total_page = total_count / page_count;
//					if (total_count % page_count > 0) {
//						total_page++;
//					}
//					
//					int start_index = page_count * page_index + 1;
//					int end_index   = page_count * (page_index + 1);
//					
//					logger.debug("find request > {}-{}-{}", start_index, end_index,total_page);

//					
//					List<SCIMResource2> user_list = resource_repository.getUsersByWhere(find.getWhere());
//					for (SCIMResource2 scimUser : user_list) {
//						result.addReource(scimUser);
//					}
//					result.setTotalResults(user_list.size());
//					
//					logger.debug("find response < {}", result.getTotalResults());
//					
					response.status(200);
					return result;
				}catch(SCIMException e) {
					e.printStackTrace();
					response.status(e.getError().getStatus());
					return e.getError();
					
				}catch (Exception e) {
					e.printStackTrace();
					logger.error("FIND PROCESS Exception : -- : {} ", e.getMessage());
					response.status(SCIMError.InternalServerError.getStatus());
					return SCIMError.InternalServerError;
				}
				finally {
					///SCIMLogger.audit("LOGIN  : {} ", user);
				}
			}
		};
	}
	
	public Route search() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return SCIMError.NotImplemented;
			}
		};
	}

	public Route create() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return SCIMError.NotImplemented;
			}
		};
	}

	public Route updateUser() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return SCIMError.NotImplemented;
			}
		};
	}

	public Route patch() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return SCIMError.NotImplemented;
			}
		};
	}

}
