package com.wowsanta.scim.server;

import java.io.FileNotFoundException;

import com.wowsanta.scim.server.auth.AuthenticationRoute;
import com.wowsanta.scim.server.env.EnvironmentRoute;
import com.wowsanta.scim.server.filter.AuthFilter;
import com.wowsanta.scim.spark.AbstractSparkService;

import static spark.Spark.*;



public class SCIMService extends AbstractSparkService{

	private Configuration config;
	
	public static void main(String[] args) {
		SCIMService service = new SCIMService();
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
		path("/env/", new EnvironmentRoute());		
		path("/scim/", new AuthenticationRoute());
		
		
	}

}
