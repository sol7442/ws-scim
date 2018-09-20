package com.wession.scim.controller;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import spark.Request;
import spark.Response;

public class ServiceProviderConfig {
	// Singleton 설정 
	private static class Singleton {
		private static final ServiceProviderConfig instance = new ServiceProviderConfig();
	}

	public static synchronized ServiceProviderConfig getInstance() {
		return Singleton.instance;
	}

	private JSONObject obj = new JSONObject();

	
	private ServiceProviderConfig() {
		init();
		System.out.println("ServiceProviderConfig Initiated.");
	}

	public void change(String attrName, Object obj) {
		
	}

	public Object read(String attrName) {
		return obj.get(attrName);
	}
	
	public String getRefer() {
		String url = getServerConfig().getAsString("baseURL");
		JSONArray versions = (JSONArray) getServerConfig().get("version");
		if (versions == null || versions.size() == 0) {
			url = url + "v2.0";
		} else {
			url = url + versions.get(0);
		}
		return url;
	}
	
	public String getRefer(String version, String resource) {
		String url = getServerConfig().getAsString("baseURL") + version + "/" + resource;
		return url;
	}
	
	public JSONObject getServerConfig() {
		return (JSONObject) obj.get("wessionIM");
	}

	public JSONObject getAgentConfig() {
		return (JSONObject) obj.get("wessionAgent");
	}
	
	public JSONObject getDBConfig() {
		return (JSONObject) obj.get("agentSource");
	}

	private void init() {
		String file_path = "./reference/ServiceProviderConfig.json";
		try {
			obj = (JSONObject) JSONValue.parse(new FileReader(file_path));
			System.out.println("ServiceProviderConfig : \n" + obj.toJSONString());
			JSONObject serverConf = getServerConfig();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getServiceConfig(Request request, Response response) {
		String path = request.params(":path");
		if (path == null)
			return obj.toJSONString();
		
		JSONObject json = (JSONObject) obj.get(path);
		return json.toJSONString();
	}

	public String setServiceConfig() {
		String file_path = "./reference/ServiceProviderConfig.json";
		try {
			JSONValue.writeJSONString(obj, new FileWriter(file_path));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return obj.toJSONString();
	}



}
