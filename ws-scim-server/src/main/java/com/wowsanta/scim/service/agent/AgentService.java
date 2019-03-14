package com.wowsanta.scim.service.agent;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wowsanta.scim.client.RESTClient;
import com.wowsanta.scim.exception.SCIMError;
import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.obj.SCIMSystem;
import com.wowsanta.scim.obj.SCIMUser;
import com.wowsanta.scim.resource.SCIMRepository;
import com.wowsanta.scim.resource.SCIMRepositoryManager;
import com.wowsanta.scim.resource.SCIMResourceGetterRepository;
import com.wowsanta.scim.resource.user.LoginUser;
import com.wowsanta.scim.resource.worker.Worker;
import com.wowsanta.scim.schema.SCIMConstants;

import spark.Request;
import spark.Response;
import spark.Route;

public class AgentService {

	public static Route runRemoteScheduler() {
		// TODO Auto-generated method stub
		return null;
	}

	public Route getSystemInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	public Route setSystemInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	public Route getRepositoryInfo() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				try {
					String systemId = request.params(":systemId");
					SCIMSystem system = SCIMRepositoryManager.getInstance().getSystemRepository().getSystemById(systemId);
					if(system == null) {
						SCIMError error = SCIMError.invalidValue;
						error.addDetail("system ID not found : " + systemId);
						
						throw new SCIMException(error);
					}
					
					
					String system_url = system.getSystemUrl();
					String call_url   = system_url + "/config/repository";
							
					Worker worker = findWorker(request);
					RESTClient clinent = new RESTClient(worker);
					String result = clinent.get2(call_url);
					
					return SCIMRepository.load(result);
					
				}catch(SCIMException e) {
					return e.getError();
				}
			}
		};
	}

	public Route setRepositoryInfo() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				try {
					String systemId = request.params(":systemId");
					SCIMSystem system = SCIMRepositoryManager.getInstance().getSystemRepository().getSystemById(systemId);
					if(system == null) {
						SCIMError error = SCIMError.invalidValue;
						error.addDetail("system ID not found : " + systemId);
						
						throw new SCIMException(error);
					}
					System.out.println("request body : " + request.body());
					
					JsonObject jsonObject = new JsonParser().parse(request.body()).getAsJsonObject();
					
					System.out.println("change repository : " + jsonObject);
					
					String system_url = system.getSystemUrl();
					String call_url   = system_url + "/config/repository";
							
					System.out.println("post url : " + call_url);
					Worker worker = findWorker(request);
					RESTClient clinent = new RESTClient(worker);
					
					String result = clinent.post2(call_url,jsonObject);
					
					System.out.println("post result : " + result);

					
					return "OK";
					
				}catch(SCIMException e) {
					return e.getError();
				}
			}
		};
	}
	
	private Worker findWorker(Request request) {
		LoginUser login_user = request.session().attribute("loginUser");
		Worker worker = new Worker();
		worker.setWorkerId(login_user.getUserId());
		worker.setWorkerType(login_user.getType().toString());
		return worker;
	}

	public Route getLibrary() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				String systemId = request.params(":systemId");
				SCIMSystem system = SCIMRepositoryManager.getInstance().getSystemRepository().getSystemById(systemId);
				String system_url = system.getSystemUrl();
				String call_url   = system_url + "/config/library";
				
				String scim_library = System.getProperty("scim.library");
				System.out.println("scim_library" +  scim_library);
				
				Path library_path = Paths.get(scim_library);
				File library_dir  = library_path.toFile();
				for (File libray_file : library_dir.listFiles()) {
					System.out.println(libray_file.getCanonicalPath());
				}
				
				return library_dir.list();
			}
		};
	}
	public Route patchLibrary() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				String systemId = request.params(":systemId");
				SCIMSystem system = SCIMRepositoryManager.getInstance().getSystemRepository().getSystemById(systemId);
				String system_url = system.getSystemUrl();
				String call_url   = system_url + "/config/library";
				
				Worker worker = findWorker(request);
				RESTClient clinent = new RESTClient(worker);
				
				String scim_library = System.getProperty("scim.library");
				System.out.println("scim_library" +  scim_library);
				
				Path library_path = Paths.get(scim_library);
				File library_dir  = library_path.toFile();
				for (File libray_file : library_dir.listFiles()) {
					System.out.println(libray_file.getCanonicalPath());
				}
				
				clinent.patch(call_url,library_dir.listFiles());
				
				return library_dir.list();
			}
		};
	}

	public Route setLibrary() {
		// TODO Auto-generated method stub
		return null;
	}

	public Route removeLibrary() {
		// TODO Auto-generated method stub
		return null;
	}


}
