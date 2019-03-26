package com.wowsanta.scim;


import com.wowsata.util.json.WowsantaJson;

public abstract class ServiceConfiguration extends WowsantaJson {
	private int servicePort = 5000;
	private int maxThreads  = 5;
	private int minThreads  = 2;
	private int idleTimeoutMills = 1000;
	private String routerClass;
	
	private String repositoryConfig;
	private String serviceProviderConfig;
	
	public String getServiceProviderConfig() {
		return this.serviceProviderConfig;
	}
	public void setServiceProviderConfig(String config) {
		this.serviceProviderConfig = config;
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
	public String getRouterClass() {
		return routerClass;
	}
	public void setRouterClass(String routerClass) {
		this.routerClass = routerClass;
	}
	public String getRepositoryConfig() {
		return repositoryConfig;
	}
	public void setRepositoryConfig(String repositoryConfig) {
		this.repositoryConfig = repositoryConfig;
	}
}
