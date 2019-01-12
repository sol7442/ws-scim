package com.wowsanta.scim;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;

import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.log.SCIMLogger;
import com.wowsanta.scim.resource.ServiceProvider;
import com.wowsanta.scim.service.SCIMServiceProvider;

public class SCIMServer extends Thread {

	
	public static void main(String[] args) {
		
		String config_file_name = "../config/service-provider.json";
		SCIMServer server = new SCIMServer();
		server.initialize(config_file_name);
		server.start();
		
	}
	
	private SCIMServiceProvider serviceProvider;
	private boolean run = true;
	public SCIMServer() {
		super("SCIMServer");
	}
	public void initialize(String config_file_name) {
		try {
			this.serviceProvider = ServiceProvider.load(config_file_name);
			
			
			String file_name = "../config/scim_system_config.json";
			SystemManager mgr = SystemManager.getInstance();
			mgr.load(file_name);
			mgr.initialize();
			
		} catch (SCIMException e) {
			SCIMLogger.error("SCIMServer INITIALIZE ERROR : ",e);
		}
	}
	public void run() {
		SCIMLogger.sys("SCIMServer start : [{}]================================", new Date());
		
		this.serviceProvider.getServer().initialize();
		
		while(this.run) {
			try {
				System.out.println("Enter Command -- wait : ");
				BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
				String command = reader.readLine();
				
				if(command.equals("exit")) {
					this.serviceProvider.getServer().stop();
					this.run = false;
				}
				
			} catch (Exception e) {
				SCIMLogger.error("SCIMServiceProvider Main Thread ERROR : ",e);
			}
		}
		
		SCIMLogger.sys("SCIMServer stop  : [{}]================================", new Date());
	}
}
