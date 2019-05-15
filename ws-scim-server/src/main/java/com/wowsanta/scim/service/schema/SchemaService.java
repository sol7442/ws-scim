package com.wowsanta.scim.service.schema;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wowsanta.scim.protocol.ClientReponse;
import com.wowsanta.scim.protocol.FrontResponse;
import com.wowsanta.scim.protocol.ResponseState;
import com.wowsanta.scim.repository.RepositoryInputMapper;
import com.wowsanta.scim.repository.RepositoryOutputMapper;
import com.wowsanta.scim.repository.ResourceTypeSchema;
import com.wowsanta.scim.repository.SCIMRepositoryManager;

import spark.Request;
import spark.Response;
import spark.Route;

public class SchemaService {
	static Logger logger = LoggerFactory.getLogger(SchemaService.class);
	
	public Route getSchemaFile() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				FrontResponse front_response = new FrontResponse();
				try {
					String file_name = request.params("fileName");
					String file_path = "../schema/" + file_name;
					
					ResourceTypeSchema resource_schema = ResourceTypeSchema.load(file_path);
					
					logger.info("load schema : {}", file_name );
					logger.info("{}", resource_schema.toString(true));
					
					front_response.setData(resource_schema);
					front_response.setState(ResponseState.Success);
				}catch (Exception e) {
					logger.error("{}",e.getMessage(),e);
					front_response.setState(ResponseState.Fail);
					front_response.setMessage(e.getMessage());
				}
				
				return front_response;
			}
		};
	}

	public Route getSchemaOutputMapper() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				ClientReponse client_response = new ClientReponse();
				try {
					String group_output_mapper_file_name = SCIMRepositoryManager.getInstance().getResourceRepositoryConfig().getGroupOutputMapper();
					String user_output_mapper_file_name = SCIMRepositoryManager.getInstance().getResourceRepositoryConfig().getUserOutputMapper();
					
					logger.info("group_mapper : {}",group_output_mapper_file_name);
					logger.info("user_mapper  : {}",user_output_mapper_file_name);
					
					List<RepositoryOutputMapper> output_mapper_list = new ArrayList<RepositoryOutputMapper>();
					if(group_output_mapper_file_name != null) {
						RepositoryOutputMapper group_mapper = RepositoryOutputMapper.load(group_output_mapper_file_name);
						group_mapper.setName("group_mapper");
						output_mapper_list.add(group_mapper);
					}
					
					if(user_output_mapper_file_name != null) {
						RepositoryOutputMapper user_mapper = RepositoryOutputMapper.load(user_output_mapper_file_name);
						user_mapper.setName("user_mapper");
						output_mapper_list.add(user_mapper);
					}
					
					client_response.setData(output_mapper_list);
					client_response.setState(ResponseState.Success);
				}catch (Exception e) {
					client_response.setMessage(e.getMessage());
					client_response.setState(ResponseState.Fail);
				}
				return client_response;
			}
		};
	}

	public Route getSchemaInputMapper() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				ClientReponse client_response = new ClientReponse();
				try {
					String group_input_mapper_file_name = SCIMRepositoryManager.getInstance().getResourceRepositoryConfig().getGroupInputMapper();
					String user_input_mapper_file_name = SCIMRepositoryManager.getInstance().getResourceRepositoryConfig().getUserInputMapper();
					
					logger.info("group_mapper : {}",group_input_mapper_file_name);
					logger.info("user_mapper  : {}",user_input_mapper_file_name);
					
					List<RepositoryInputMapper> output_mapper_list = new ArrayList<RepositoryInputMapper>();
					if(group_input_mapper_file_name != null) {
						RepositoryInputMapper group_mapper = RepositoryInputMapper.load(group_input_mapper_file_name);
						group_mapper.setName("group_mapper");
						output_mapper_list.add(group_mapper);
					}
					
					if(user_input_mapper_file_name != null) {
						RepositoryInputMapper user_mapper = RepositoryInputMapper.load(user_input_mapper_file_name);
						user_mapper.setName("user_mapper");
						output_mapper_list.add(user_mapper);
					}
					
					client_response.setData(output_mapper_list);
					client_response.setState(ResponseState.Success);
				}catch (Exception e) {
					client_response.setMessage(e.getMessage());
					client_response.setState(ResponseState.Fail);
				}
				return client_response;
			}
		};
	}
}
