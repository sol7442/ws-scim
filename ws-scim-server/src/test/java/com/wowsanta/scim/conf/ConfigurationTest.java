package com.wowsanta.scim.conf;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;

import com.wowsanta.scim.server.Configuration;

public class ConfigurationTest {
	
	private final String config_file_name = "../config/service-configuration.json";
	
	@Test
	public void make() {
		Configuration conf = new Configuration();
		conf.setServiceConsumerPath("../config/service-consumer.json");
		conf.setServiceProviderPath("../config/service-provider.json");
		try {
			conf.save(this.config_file_name);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void read() {
		try {
			Configuration conf = Configuration.load(this.config_file_name);
			System.out.println(conf);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
}
