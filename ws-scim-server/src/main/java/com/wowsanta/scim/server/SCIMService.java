package com.wowsanta.scim.server;

import java.io.FileNotFoundException;

import com.wowsanta.scim.conf.ServiceProvider;
import com.wowsanta.scim.server.filter.AdminFilter;
import com.wowsanta.scim.server.router.admin.AdminRouteGroup;
import com.wowsanta.scim.spark.AbstractSparkService;

import static spark.Spark.*;



public class SCIMService extends AbstractSparkService{

	private ServiceProvider config;
	
	public static void main(String[] args) {
		SCIMService service = new SCIMService();
		service.initialize(System.getProperty("config.file"));
		service.setRouters();
	}

	

	private void initialize(String config_file) {
		try {
			config = ServiceProvider.load(config_file);
			System.out.println(config);
			initialize(config.getWessionIM());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setRouters() {
		before("/admin/*",new AdminFilter());
		path("/admin/", new AdminRouteGroup());
		
		
	}

}
