package com.wowsanta.scim;

import java.io.FileNotFoundException;

import com.wowsanta.scim.spark.AbstractSparkService;

public class SCIMConsumerService extends AbstractSparkService{

	
	public static void main(String[] args) {
		SCIMConsumerService service = new SCIMConsumerService();
		service.initialize(System.getProperty("config.file"));
		service.setRouters();
	}

	

	private void initialize(String config_file) {
		try {
			Configuration config = Configuration.load(config_file);
			
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
		
	}
}

