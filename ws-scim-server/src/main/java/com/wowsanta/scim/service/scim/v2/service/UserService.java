package com.wowsanta.scim.service.scim.v2.service;


import java.util.List;
import java.util.Map;

import org.eclipse.jetty.security.PropertyUserStore.UserListener;

import com.wowsanta.scim.exception.SCIMError;
import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.log.SCIMLogger;
import com.wowsanta.scim.message.SCIMFindRequest;
import com.wowsanta.scim.message.SCIMListResponse;
import com.wowsanta.scim.message.SCIMSearchRequest;
import com.wowsanta.scim.obj.SCIMResource2;
import com.wowsanta.scim.obj.SCIMUser;
import com.wowsanta.scim.resource.SCIMRepositoryManager;
import com.wowsanta.scim.resource.SCIMResourceGetterRepository;
import com.wowsanta.scim.resource.user.LoginUser;

import spark.Request;
import spark.Response;
import spark.Route;

import static com.wowsanta.scim.server.JsonUtil.json_parse;

public class UserService {

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
					SCIMListResponse  result = new SCIMListResponse();
					SCIMFindRequest find = json_parse(request.body(),SCIMFindRequest.class);
					
					SCIMResourceGetterRepository resource_repository = (SCIMResourceGetterRepository)SCIMRepositoryManager.getInstance().getResourceRepository();
					
					List<SCIMResource2> user_list = resource_repository.getUsersByWhere(find.getWhere());
					for (SCIMResource2 scimUser : user_list) {
						System.out.println("user_list - " + scimUser);
						result.addReource(scimUser);
					}
					
					result.setTotalResults(user_list.size());
					response.status(200);
					return result;
				}catch(SCIMException e) {
					e.printStackTrace();
					response.status(e.getError().getStatus());
					return e.getError();
					
				}catch (Exception e) {
					e.printStackTrace();
					SCIMLogger.audit("FIND PROCESS Exception : -- : {} ", e.getMessage());
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
