package com.wowsanta.scim.conf;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;

public class ConfigurationTest {
	
	private final String config_file_name = "../ws-scim-server/config/service-provider.json";
	
	public void make() {
		ServiceProvider sp = new ServiceProvider();
		try {
			sp.save(this.config_file_name);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void read() {
		try {
			ServiceProvider sp = ServiceProvider.load(this.config_file_name);
			System.out.println(sp);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
}
