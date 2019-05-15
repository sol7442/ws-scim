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
import com.wowsanta.scim.object.Resource_Object;
import com.wowsanta.scim.object.SCIM_Object;
import com.wowsanta.scim.object.SCIM_Resource;
import com.wowsanta.scim.object.SCIM_User;
import com.wowsanta.scim.repository.RepositoryInputMapper;
import com.wowsanta.scim.repository.RepositoryOutputMapper;
import com.wowsanta.scim.repository.SCIMRepositoryManager;
import com.wowsanta.scim.repository.SCIMResourceGetterRepository;
import com.wowsanta.scim.repository.impl.MsSqlRepository;
import com.wowsanta.scim.repository.impl.OracleRepository;
import com.wowsanta.scim.repository.resource.SCIMResourceRepository;
import com.wowsanta.scim.repository.system.SCIMProviderRepository;
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
					
					SCIMFindRequest find = SCIMFindRequest.parse(request.body());
					logger.info("find request > {}", find.toString(true));

					SCIMResourceRepository resource_repository = SCIMRepositoryManager.getInstance().getResourceRepository();
					
//					/******************		//local test ********************/			
//					final String gw_repository_config_file = "../config/backup_conf_20190429/default_mssql_gw_repository.json";
//					final String gw_user_resource_output_mapper_file = "../config/backup_conf_20190429/default_mssql_gw_user_resource_output_mapper.json";
//					final String gw_group_resource_output_mapper_file = "../config/backup_conf_20190429/default_mssql_gw_group_resource_output_mapper.json";
//
//					
//					//RepositoryInputMapper user_resource_input_mapper = RepositoryInputMapper.load(gw_user_resource_input_mapper_file);
//					RepositoryOutputMapper user_resource_output_mapper = RepositoryOutputMapper.load(gw_user_resource_output_mapper_file);
//					RepositoryOutputMapper group_resource_output_mapper = RepositoryOutputMapper.load(gw_group_resource_output_mapper_file);
//					//RepositoryInputMapper group_resource_input_mapper = RepositoryInputMapper.load(gw_group_resource_input_mapper_file);
//					
//					MsSqlRepository resource_repository = MsSqlRepository.load(gw_repository_config_file);
//					resource_repository.initialize();
//					//resource_repository.setUserInputMapper(user_resource_input_mapper);
//					resource_repository.setUserOutputMapper(user_resource_output_mapper);
//					//resource_repository.setGroupInputMapper(group_resource_input_mapper);
//					resource_repository.setGrouptOutputMapper(group_resource_output_mapper);
//					/******************		//local test ********************/
					
					
					int total_count  = resource_repository.getUserCount(find.getWhere());					
					int page_count   = find.getCount();
					int start_index   = find.getStartIndex();
					
					List<Resource_Object> user_list = resource_repository.searchUser(find.getWhere(), start_index, page_count, total_count);
					logger.info("find user size : {}", user_list.size());
					for (Resource_Object scim_User : user_list) {
						System.out.println("find user >> " + scim_User);//TODO 변환.
					}
					
					result.setResources(user_list); //TODO
					result.setTotalResults(total_count);
					
					response.status(200);
					return result;
					
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
