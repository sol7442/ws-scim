package com.wowsanta.scim.service.system;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.internal.LinkedTreeMap;
import com.wowsanta.scim.obj.SCIMAdmin;
import com.wowsanta.scim.obj.SCIMSystem;
import com.wowsanta.scim.protocol.FrontReqeust;
import com.wowsanta.scim.protocol.FrontResponse;
import com.wowsanta.scim.protocol.ResponseState;
import com.wowsanta.scim.repository.SCIMRepositoryManager;
import com.wowsanta.scim.repository.system.SCIMSystemRepository;

import spark.Request;
import spark.Response;
import spark.Route;

public class SystemService {

	static Logger logger = LoggerFactory.getLogger(SystemService.class); 
	public Route getSystems() {
		return new Route() {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				SCIMSystemRepository system_repository = SCIMRepositoryManager.getInstance().getSystemRepository();

				List<SCIMSystem> system_list = system_repository.getSystemAll();
				
				return system_list;
			}
		};
	}

	public Route createSystem() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				FrontResponse front_response = new FrontResponse();
				try {
					FrontReqeust front_request = FrontReqeust.parse(request.body());
					Object system_object = front_request.getParams().get("system");
					SCIMSystem system = SCIMSystem.parse((LinkedTreeMap)system_object);
					
					logger.info("create system : {} ", system.toString(true));
					
					SCIMSystemRepository system_repository = SCIMRepositoryManager.getInstance().getSystemRepository();
					if(system_repository.getSystemById(system.getSystemId()) == null) {
						system_repository.createSystem(system);
					}
					front_response.setState(ResponseState.Success);
				}catch (Exception e) {
					logger.error(e.getMessage(),e);
					front_response.setMessage(e.getMessage());;
					front_response.setState(ResponseState.Fail);
				}
				return front_response;
			}
		};
	}

	public Route updateSystem() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				FrontResponse front_response = new FrontResponse();
				try {
					FrontReqeust front_request = FrontReqeust.parse(request.body());
					Object system_object_string = front_request.getParams().get("system");
					SCIMSystem system = SCIMSystem.parse((LinkedTreeMap) system_object_string);
					
					logger.info("update system : {} ", system.toString(true));
					
					SCIMSystemRepository system_repository = SCIMRepositoryManager.getInstance().getSystemRepository();
					if(system_repository.getSystemById(system.getSystemId()) != null) {
						system_repository.updateSystem(system);
					}
					front_response.setState(ResponseState.Success);
				}catch (Exception e) {
					logger.error(e.getMessage(),e);
					front_response.setMessage(e.getMessage());;
					front_response.setState(ResponseState.Fail);
				}
				return front_response;
			}
		};
	}

	public Route deleteSystem() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				FrontResponse front_response = new FrontResponse();
				try {
					String systemId = request.params("systemId");
					
					logger.info("delete system : {} ", systemId);
					
					SCIMSystemRepository system_repository = SCIMRepositoryManager.getInstance().getSystemRepository();
					if(system_repository.getSystemById(systemId) != null) {
						system_repository.deleteSystem(systemId);
					}
					front_response.setState(ResponseState.Success);
				}catch (Exception e) {
					logger.error(e.getMessage(),e);
					front_response.setMessage(e.getMessage());;
					front_response.setState(ResponseState.Fail);
				}
				return front_response;
			}
		};
	}

}
