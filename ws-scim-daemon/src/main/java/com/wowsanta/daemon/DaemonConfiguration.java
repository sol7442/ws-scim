package com.wowsanta.daemon;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.wowsata.util.json.JsonException;
import com.wowsata.util.json.WowsantaJson;

public class DaemonConfiguration extends WowsantaJson{
	
	private static DaemonConfiguration instance;
	
	private String distPath      = "../dist";
	private String libPath       = "../libs";
	private String configPath    = "../config";
	private String serviceConfig = "../config/serviceConfiguration";
	private String serviceClass  = "com.wowsata.service.impl.SparkServiceImpl";
	private List<String> librayList = new ArrayList<>();

	public static DaemonConfiguration getInstance() {
		if(instance == null) {
			instance = new DaemonConfiguration();
		}
		return instance;
	}
	
	public String getLibraryClassPath() {
		StringBuffer buffer = new StringBuffer();
		
		//System.getProperty("user.dir");
		//this.getDistPath();
		File dist_path = new File(this.distPath);
		File[] library_list = dist_path.listFiles();
		
		System.out.println(".....................");
		for (File file : library_list) {
			if(librayList.contains(file.getName())) {
				try {
					buffer.append(file.getCanonicalPath()).append(System.getProperty("path.separator"));
				} catch (IOException e) {
					e.printStackTrace();
				}
			};
		}
		System.out.println(".....................");
		return buffer.toString();
	}
	
	public List<String> getLibrayList() {
		return librayList;
	}

	public void setLibrayList(List<String> librayList) {
		this.librayList = librayList;
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
