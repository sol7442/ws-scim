package com.wowsanta.scim.service;

import java.io.File;

import org.junit.Test;

import com.wowsanta.scim.SCIMSystemManager;
import com.wowsanta.scim.exception.SCIMException;

public class ServiceProviderTest {

	static {
		System.setProperty("logback.configurationFile", "../config/logback.xml");
		System.setProperty("logback.path", "../logs");
		System.setProperty("logback.mode", "debug");
		
		String config_file_path = "";
		File current_path  = new File(System.getProperty("user.dir"));
		config_file_path = current_path.getParent() + File.separator + "config" +File.separator + "scim-service-provider.json"; 
		
		try {
			SCIMSystemManager.getInstance().load(config_file_path);
			SCIMSystemManager.getInstance().getServiceProvider().getServer().initialize();
		} catch (SCIMException e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void load_config_test() {
//		System.out.println(SCIMSystemManager.getInstance().getServiceProvider().toString(true));
//		System.out.println(SCIMSystemManager.getInstance().getServiceProvider().getSystemInfo());
	}
	
	
}
