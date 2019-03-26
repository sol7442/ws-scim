package com.wowsanta.daemon;


import com.wowsata.util.json.JsonException;
import com.wowsata.util.json.WowsantaJson;

public class DaemonConfiguration extends WowsantaJson{
	
	private static DaemonConfiguration instance;
	
	private String distPath      = "../dist";
	private String libPath       = "../libs";
	private String configPath    = "../config";
	private String serviceConfig = "../config/serviceConfiguration";
	private String serviceClass  = "com.wowsata.service.impl.SparkServiceImpl";
	
	public static DaemonConfiguration getInstance() {
		if(instance == null) {
			instance = new DaemonConfiguration();
		}
		return instance;
	}
	private DaemonConfiguration() {
		this.jsonClass =  DaemonConfiguration.class.getCanonicalName();
	}
	
	public String getDistPath() {
		return distPath;
	}
	public String getServiceConfig() {
		return serviceConfig;
	}
	public String getServiceClass() {
		return serviceClass;
	}
	public void setDistPath(String distPath) {
		this.distPath = distPath;
	}
	public void setServiceConfig(String serviceConfig) {
		this.serviceConfig = serviceConfig;
	}
	public void setServiceClass(String serviceClass) {
		this.serviceClass = serviceClass;
	}
	public String getLibPath() {
		return this.libPath;
	}
	public String getConfigPath() {
		return this.configPath;
	}
	
	public static DaemonConfiguration loadFromFile(String file_name) throws JsonException{
		instance = (DaemonConfiguration)WowsantaJson.loadFromFile(file_name);
		return instance;
	}
	
	

}
