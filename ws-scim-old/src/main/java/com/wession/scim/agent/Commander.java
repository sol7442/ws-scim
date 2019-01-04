package com.wession.scim.agent;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;

import com.wession.scim.agent.site.DemoIMSAdpator;
import com.wession.scim.agent.site.DemoIMSScim;
import com.wession.scim.controller.ServiceProviderConfig;

import net.minidev.json.JSONObject;
import spark.Request;
import spark.Response;

public class Commander { 
	private static ServiceProviderConfig conf = ServiceProviderConfig.getInstance();
	
	public String process(Request request, Response response) {
		JSONObject agent_config = conf.getAgentConfig();
		
		JSONObject ret = new JSONObject();
		
		String ver = request.params(":version");
		String cmd = request.params(":command");
		
		System.out.println(ver + ":" + cmd);
		
		if ("v1.0".equals(ver) && "sync".equals(cmd)) {
			DemoIMSScim scim = new DemoIMSScim();
			ret = scim.Sync();
			
		} else if ("v1.0".equals(ver) && "dropout".equals(cmd)) {
			JSONObject sync_config = (JSONObject) conf.getDBConfig().get("dropout");
			String userid = request.params(":userid");
			if (userid == null || "".equals(userid)) {
				
			} else {
				// 사용자가 있는 경우 기존 DB에서 내용을 찾아 해당 부분을 처리함
				String jarName = sync_config.getAsString("dropout-jar");
				String viewPackage = sync_config.getAsString("dropout-class");
				String methodName = sync_config.getAsString("method");
				
				System.out.println("jarName : " + jarName);
				System.out.println("viewPackage : " + viewPackage);
				System.out.println("dropoutMethodName : " + methodName);
				System.out.println("dropout data : " + userid);
				
				JarClassLoader jLoader;
				try {
					Class[] methodParamClass = new Class[] {String.class};
					Object[] methodParamObject = new Object[] {userid};
					
					jLoader = new JarClassLoader(jarName);
					Object obj = jLoader.loadClass(viewPackage).newInstance();
					Method method = obj.getClass().getMethod(methodName, methodParamClass);
					ret = (JSONObject) method.invoke(obj, methodParamObject);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
				return ret.toJSONString();
				
			}
			
		} else if ("v1.0".equals(ver) && "join".equals(cmd)) {
			JSONObject sync_config = (JSONObject) conf.getDBConfig().get("join");
			String userid = request.params(":userid");
			if (userid == null || "".equals(userid)) {
				
			} else {
				// 사용자가 있는 경우 기존 DB에서 내용을 찾아 해당 부분을 처리함
				String jarName = sync_config.getAsString("join-jar");
				String viewPackage = sync_config.getAsString("join-class");
				String methodName = sync_config.getAsString("method");
				
				System.out.println("jarName : " + jarName);
				System.out.println("viewPackage : " + viewPackage);
				System.out.println("joinMethodName : " + methodName);
				System.out.println("join data : " + userid);
				
				JarClassLoader jLoader;
				try {
					Class[] methodParamClass = new Class[] {String.class};
					Object[] methodParamObject = new Object[] {userid};
					
					jLoader = new JarClassLoader(jarName);
					Object obj = jLoader.loadClass(viewPackage).newInstance();
					Method method = obj.getClass().getMethod(methodName, methodParamClass);
					ret = (JSONObject) method.invoke(obj, methodParamObject);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
				return ret.toJSONString();
				
			}
			
				
		} else if ("v1.1".equals(ver) && "sync".equals(cmd)) {
			JSONObject sync_config = (JSONObject) conf.getDBConfig().get("sync");
			try {
				String jarName = sync_config.getAsString("sync-jar");
				String viewPackage = sync_config.getAsString("sync-class");
				String syncMethodName = sync_config.getAsString("method");
				
				System.out.println("jarName : " + jarName);
				System.out.println("viewPackage : " + viewPackage);
				System.out.println("syncMethodName : " + syncMethodName);
				
				JarClassLoader jLoader = new JarClassLoader(jarName);
				Object obj = jLoader.loadClass(viewPackage).newInstance();
				Method method = obj.getClass().getMethod(syncMethodName);
				
				ret = (JSONObject) method.invoke(obj, (Object[]) null);

			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
		return ret.toJSONString();
	}

}
