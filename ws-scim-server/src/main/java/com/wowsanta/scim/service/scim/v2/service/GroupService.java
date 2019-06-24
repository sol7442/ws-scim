package com.wowsanta.scim.service.scim.v2.service;

import static com.wowsanta.scim.server.JsonUtil.json_parse;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wowsanta.scim.exception.SCIMError;
import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.message.SCIMFindRequest;
import com.wowsanta.scim.message.SCIMListResponse;
import com.wowsanta.scim.object.Resource_Object;
import com.wowsanta.scim.repository.RepositoryOutputMapper;
import com.wowsanta.scim.repository.SCIMRepositoryManager;
import com.wowsanta.scim.repository.impl.MsSqlRepository;
import com.wowsanta.scim.repository.impl.OracleRepository;
import com.wowsanta.scim.repository.resource.SCIMResourceRepository;

import spark.Request;
import spark.Response;
import spark.Route;

public class GroupService {

	Logger logger = LoggerFactory.getLogger(GroupService.class);
	
	public Route getGroup() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return SCIMError.NotImplemented;
			}
		};
	}

	public Route createGroup() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return SCIMError.NotImplemented;
			}
		};
	}

	public Route updateGroup() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return SCIMError.NotImplemented;
			}
		};
	}

	public Route searchGroup() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return SCIMError.NotImplemented;
			}
		};
	}

	public Route findGroup() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				try {
					
					logger.debug("reqeust body : \n{}",request.body());
					SCIMListResponse  result = new SCIMListResponse();
					SCIMFindRequest find = json_parse(request.body(),SCIMFindRequest.class);
					
					logger.debug("find request > {}", find);
					logger.debug("find where   > {}", find.getWhere());

					SCIMResourceRepository resource_repository = SCIMRepositoryManager.getInstance().getResourceRepository();
////					/******************		//local test ********************/			
//					final String gw_repository_config_file = "../config/default_mssql_gw_repository.json";
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
////					/******************		//local test ********************/
					
					
					int total_count   = resource_repository.getGroupCount(find.getWhere());					
					int page_count    = find.getCount();
					int start_index   = find.getStartIndex();
					
					List<Resource_Object> group_list = resource_repository.searchGroup(find.getWhere(), start_index, page_count, total_count);
					logger.info("find group size : {}", group_list.size());
//					for (Resource_Object group : group_list) {
//					}
					
					result.setResources(group_list); //TODO
					result.setTotalResults(total_count);
					
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

	public Route patchGroup() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return SCIMError.NotImplemented.NotImplemented;
			}
		};
	}

}
