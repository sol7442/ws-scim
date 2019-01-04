package com.wowsanta.scim;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;

import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.log.SCIMLogger;
import com.wowsanta.scim.service.SCIMServiceProvider;
import com.wowsanta.scim.service.ServiceProvider;

public class SCIMServiceProviderServer extends Thread {

	
	public static void main(String[] args) {
		
		String config_file_name = "../config/service-provider.json";
		SCIMServiceProviderServer server = new SCIMServiceProviderServer();
		server.initialize(config_file_name);
		server.start();
		
	}
	
	private SCIMServiceProvider serviceProvider;
	private boolean run = true;
	public SCIMServiceProviderServer() {
		super("SCIMServer");
	}
	public void initialize(String config_file_name) {
		try {
			this.serviceProvider = ServiceProvider.load(config_file_name);
		} catch (SCIMException e) {
			SCIMLogger.error("SCIMServiceProvider INITIALIZE ERROR : ",e);
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
