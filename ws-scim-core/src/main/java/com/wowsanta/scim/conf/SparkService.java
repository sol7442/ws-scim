package com.wowsanta.scim.conf;

public class SparkService {
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
	
}
