package com.wession.scim.agent.test;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Set;

import com.wession.common.JarClassLoader;

import net.minidev.json.JSONObject;

public class DynamicLoader {
	private static String viewPackage = "com.wession.scim.agent.site.DemoIMSScim"; 

	public static void main(String[] args) {
		System.out.println("Start");
		JSONObject ret = new JSONObject();
		try {
			JarClassLoader jLoader = new JarClassLoader("D:\\DEV_05\\SCIM_Agent_BuddyOne.jar");
			Object obj = jLoader.loadClass(viewPackage).newInstance();
			Method method = obj.getClass().getMethod("Sync");
			ret = (JSONObject) method.invoke(obj, (Object[]) null);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println(ret.toJSONString());
	}
}
