package com.wowsanta.scim.service;

import java.io.FileNotFoundException;

import com.wowsanta.scim.server.env.EnvironmentRoute;
import com.wowsanta.scim.server.filter.AuthFilter;
import com.wowsanta.scim.service.agent.AgentService;
import com.wowsanta.scim.service.auth.AuthenticationRoute;
import com.wowsanta.scim.spark.AbstractSparkService;

import static spark.Spark.*;



public class SCIMProviderService extends AbstractSparkService{

	private Configuration config;
	
	public static void main(String[] args) {
		SCIMProviderService service = new SCIMProviderService();
		service.initialize(System.getProperty("config.file"));
		service.setRouters();
	}

	

	private void initialize(String config_file) {
		try {
			
			config = Configuration.load(config_file);
			
			ServiceProvider provider = ServiceProvider.load(config.getServiceProviderPath());
			ServiceConsumer consumer = ServiceConsumer.load(config.getServiceConsumerPath());
			
			System.out.println(config);
			System.out.println(consumer);
			initialize(provider.getWessionIM());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setRouters() {
		before("/*",new AuthFilter());
		
		path("/auth/", new AuthenticationRoute());		
		path("/agent/", new AgentService());		
		path("/scim/", new AuthenticationRoute());
		
		
	}

}
