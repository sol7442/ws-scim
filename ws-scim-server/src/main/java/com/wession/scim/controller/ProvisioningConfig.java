package com.wession.scim.controller;

import java.io.FileNotFoundException;
import java.io.FileReader;

import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

/*
 * 스케쥴링 부분을 관리하는 곳
 */
public class ProvisioningConfig {
	// Singleton 설정 
	private static class Singleton {
		private static final ProvisioningConfig instance = new ProvisioningConfig();
	}

	public static synchronized ProvisioningConfig getInstance() {
		return Singleton.instance; 
	}
	
	private JSONObject obj = new JSONObject();
	
	public ProvisioningConfig() {
		init();
		System.out.println("Provisioning Initiated.");
	}
	
	public String getProvisioningConfig() {
		// TODO Auto-generated method stub
		return obj.toJSONString();
	}

	public Object read(String attrName) {
		return obj.get(attrName);
	}


	private void init() {
		String file_path = "./reference/ProvisioningConfig.json";
		try {
			obj = (JSONObject) JSONValue.parse(new FileReader(file_path));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
