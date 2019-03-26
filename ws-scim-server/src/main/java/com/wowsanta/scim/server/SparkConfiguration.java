package com.wowsanta.scim.server;


import com.wowsanta.scim.ServiceConfiguration;
import com.wowsata.util.json.JsonException;
import com.wowsata.util.json.WowsantaJson;

public class SparkConfiguration extends ServiceConfiguration{
	private String staticFiles;
	private String keyStorePath;
	private String keyStorePassword;
	private static SparkConfiguration instance = null;
	
	public static SparkConfiguration getInstance() {
		if(instance == null) {
			instance = new SparkConfiguration();
		}
		return instance;
	}
	private SparkConfiguration() {
		this.jsonClass = SparkConfiguration.class.getCanonicalName();
	}
	public String getStaticFiles() {
		return staticFiles;
	}

	public void setStaticFiles(String staticFiles) {
		this.staticFiles = staticFiles;
	}
	
	public static SparkConfiguration loadFromFile(String file_name) throws JsonException{
		return (SparkConfiguration)WowsantaJson.loadFromFile(file_name);
	}
	
	public String getKeyStorePath() {
		return this.keyStorePath;
	}
	public String getKeyStorePassword() {
		return this.keyStorePassword;
	}
}
