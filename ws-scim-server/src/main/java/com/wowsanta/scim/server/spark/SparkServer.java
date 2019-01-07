package com.wowsanta.scim.server.spark;

import com.wowsanta.scim.controller.AdminController;
import com.wowsanta.scim.service.SCIMServiceServer;
import com.wowsanta.scim.service.ServiceProviderController;
import com.wowsanta.scim.service.admin.AdminService;

import spark.Spark;

public class SparkServer implements SCIMServiceServer{
	private String baseURL;
	private int servicePort;
	private int maxThreads;
	private int minThreads;
	
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
	private int idleTimeoutMills;
	private String[] version;
	private String staticFiles;
	
	public void start() {
		Spark.awaitInitialization();
	}
	public void stop() {
		Spark.stop();
	}
	@Override
	public void initialize() {
		Spark.port(this.servicePort);
		Spark.threadPool(this.maxThreads, this.minThreads, this.idleTimeoutMills);
		Spark.staticFiles.registerMimeType("ico", "ico");
		Spark.staticFiles.externalLocation(this.staticFiles);
		
//		new UserController();
//		new GroupController();
		new AdminController(new AdminService());
		
//		ServiceProviderController controller = new ServiceProviderController();
//		controller.regist();
		
	}
}
