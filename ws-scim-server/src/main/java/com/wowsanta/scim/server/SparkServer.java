package com.wowsanta.scim.server;


import com.wowsanta.scim.scheduler.SCIMJob;
import com.wowsanta.scim.service.SCIMServiceServer;
import com.wowsanta.scim.service.ServerController;
import com.wowsanta.scim.service.SparkController;

import spark.Spark;

public class SparkServer implements SCIMServiceServer{
	private String baseURL;
	private int servicePort;
	private int maxThreads;
	private int minThreads;
	private int idleTimeoutMills;
	private String[] version;
	private String staticFiles;
	
	private String controllerClass;
	
	public String getBaseURL() {
		return baseURL;
	}
	public void setBaseURL(String baseURL) {
		this.baseURL = baseURL;
	}
	public int getServicePort() {
		return servicePort;
	}
	public void setServicePort(int servicePort) {
		this.servicePort = servicePort;
	}
	public int getMaxThreads() {
		return maxThreads;
	}
	public void setMaxThreads(int maxThreads) {
		this.maxThreads = maxThreads;
	}
	public int getMinThreads() {
		return minThreads;
	}
	public void setMinThreads(int minThreads) {
		this.minThreads = minThreads;
	}
	public int getIdleTimeoutMills() {
		return idleTimeoutMills;
	}
	public void setIdleTimeoutMills(int idleTimeoutMills) {
		this.idleTimeoutMills = idleTimeoutMills;
	}
	public String[] getVersion() {
		return version;
	}
	public void setVersion(String[] version) {
		this.version = version;
	}
	public String getStaticFiles() {
		return staticFiles;
	}
	public void setStaticFiles(String staticFiles) {
		this.staticFiles = staticFiles;
	}
	
	public void start() {
		Spark.awaitInitialization();
	}
	public void stop() {
		Spark.stop();
	}
	
	public String getControllerClass() {
		return controllerClass;
	}
	public void setControllerClass(String controllerClass) {
		this.controllerClass = controllerClass;
	}
	
	@Override
	public void initialize() {
		Spark.port(this.servicePort);
		Spark.threadPool(this.maxThreads, this.minThreads, this.idleTimeoutMills);
		Spark.staticFiles.registerMimeType("ico", "ico");
		Spark.staticFiles.externalLocation(this.staticFiles);

		new ServerController().control();
		
		
//		try {
//			SparkController controller =  (SparkController) Class.forName(this.controllerClass).newInstance();
//			controller.control();
//		} catch (InstantiationException e) {
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			e.printStackTrace();
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		}
		
		//new SparkController().control();
	}
	
	
}
