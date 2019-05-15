package com.wowsanta.scim.service;

import static com.wowsanta.scim.server.JsonUtil.json_parse;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wowsanta.scim.exception.SCIMError;
import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.obj.SCIMAdmin;
import com.wowsanta.scim.policy.impl.DefaultPasswordPoilcy;
import com.wowsanta.scim.protocol.FrontReqeust;
import com.wowsanta.scim.protocol.FrontResponse;
import com.wowsanta.scim.protocol.ResponseState;
import com.wowsanta.scim.repository.SCIMRepositoryManager;
import com.wowsanta.scim.repository.resource.SCIMResourceRepository;
import com.wowsanta.scim.repository.system.SCIMSystemRepository;
import com.wowsanta.scim.resource.user.LoginUserType;

import spark.Request;
import spark.Response;
import spark.Route;

public class EnvironmentService {
	static transient Logger logger = LoggerFactory.getLogger(EnvironmentService.class);

	public Route getAllAdmin() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				FrontResponse front_response = new FrontResponse();
				try {
					SCIMSystemRepository system_repository =SCIMRepositoryManager.getInstance().getSystemRepository();
					
					List<SCIMAdmin> admin_list = system_repository.getAdminList();
					for (SCIMAdmin admin : admin_list) {
						admin.setPassword("********");
					}
					front_response.setData(admin_list);
					front_response.setState(ResponseState.Success);
					
				}catch (Exception e) {
					logger.error(e.getMessage(),e);
					front_response.setMessage(e.getMessage());
					front_response.setState(ResponseState.Fail);
				}
				return front_response;
			}
		};
	}

	public Route getAdmin() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				// TODO Auto-generated method stub
				return null;
			}
		};
	}

	public Route createAdmin() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				FrontResponse front_response = new FrontResponse();
				try {
					FrontReqeust front_request = FrontReqeust.parse(request.body());
					
					
					Object admin_object_string = front_request.getParams().get("admin");
					SCIMAdmin admin = SCIMAdmin.parse(admin_object_string.toString());
					
					logger.info("create admin : {} ", admin);
					SCIMSystemRepository system_repository = SCIMRepositoryManager.getInstance().getSystemRepository();
					if(system_repository.getAdmin(admin.getAdminId()) == null) {
						system_repository.createAdmin(admin);
					}
					
					front_response.setState(ResponseState.Success);
				}catch (Exception e) {
					front_response.setMessage(e.getMessage());;
					front_response.setState(ResponseState.Fail);
				}
				return front_response;
			}
		};
	}

	public  Route updateAdmin() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				FrontResponse front_response = new FrontResponse();
				try {
					FrontReqeust front_request = FrontReqeust.parse(request.body());
					
					
					Object admin_object_string = front_request.getParams().get("admin");
					SCIMAdmin admin = SCIMAdmin.parse(admin_object_string.toString());
					
					logger.info("update admin : {} ", admin);
					SCIMSystemRepository system_repository = SCIMRepositoryManager.getInstance().getSystemRepository();
					if(system_repository.getAdmin(admin.getAdminId()) != null) {
						system_repository.updateAdmin(admin);
					}
					
					front_response.setState(ResponseState.Success);
				}catch (Exception e) {
					front_response.setMessage(e.getMessage());;
					front_response.setState(ResponseState.Fail);
				}
				return front_response;
			}
		};
	}

	public  Route deleteAdmin() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				FrontResponse front_response = new FrontResponse();
				try {
					
					String admin_id = request.params("adminId");
					SCIMSystemRepository system_repository = SCIMRepositoryManager.getInstance().getSystemRepository();
					SCIMAdmin admin = system_repository.getAdmin(admin_id) ;
					logger.info("remove admin : {} ", admin);
					if(admin != null) {
						system_repository.deleteAdmin(admin_id);
					}
					
					front_response.setState(ResponseState.Success);
				}catch (Exception e) {
					front_response.setMessage(e.getMessage());;
					front_response.setState(ResponseState.Fail);
				}
				return front_response;
			}
		};
	}
}
