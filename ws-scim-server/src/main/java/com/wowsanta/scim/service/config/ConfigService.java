package com.wowsanta.scim.service.config;

import java.io.File;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wowsanta.scim.SCIMSystemManager;
import com.wowsanta.scim.exception.SCIMError;
import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.resource.SCIMRepository;
import com.wowsanta.scim.resource.SCIMRepositoryManager;
import com.wowsanta.scim.resource.SCIMResourceRepository;

import spark.Request;
import spark.Response;
import spark.Route;

public class ConfigService {
	public Route getSystemInfo() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				
				
				return null;
			}
		};
	}

	public Route setSystemInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	public Route getRepositoryInfo() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				SCIMResourceRepository resource_repository = (SCIMResourceRepository) SCIMRepositoryManager.getInstance().getResourceRepository();
				
				System.out.println(" resource_reposityr " + resource_repository.toString());
				
				return resource_repository;
			}
		};
	}

	public Route setRepositoryInfo() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				
				try {
					JsonObject jsonObject = new JsonParser().parse(request.body()).getAsJsonObject();
					String class_name = jsonObject.get("className").getAsString();
					SCIMRepository newRepository = (SCIMRepository) Class.forName(class_name).newInstance();
					newRepository.fromJson(jsonObject);
					
					System.out.println("newRepository" + newRepository) ;
					newRepository.initialize();
					if(newRepository.validate()) {
						SCIMRepositoryManager.getInstance().setResourceRepository((SCIMResourceRepository) newRepository);
						SCIMRepositoryManager.getInstance().save();
						
					}else {
						throw new SCIMException("Repository Validate Failed",SCIMError.InternalServerError);
					}
					
					return newRepository;
				}catch (Exception e) {
					SCIMError error = SCIMError.InternalServerError;
					error.addDetail(e.getMessage());
					return error;
				}
			}
		};
	}
}
