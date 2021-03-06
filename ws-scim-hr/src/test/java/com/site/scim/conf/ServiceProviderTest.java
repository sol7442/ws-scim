package com.site.scim.conf;


import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;


public class ServiceProviderTest {
	
	private final String config_file_name = "../config/service-provider.json";
	
	//@Test
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
