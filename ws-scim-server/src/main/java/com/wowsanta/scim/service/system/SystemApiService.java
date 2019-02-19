package com.wowsanta.scim.service.system;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.plaf.synth.SynthSpinnerUI;

import com.ehyundai.sso.ConsiliationJob_SSO;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wowsanta.scim.client.RESTClient;
import com.wowsanta.scim.message.SCIMBulkResponse;
import com.wowsanta.scim.obj.SCIMSystem;
import com.wowsanta.scim.obj.SCIMUser;
import com.wowsanta.scim.resource.SCIMRepositoryManager;
import com.wowsanta.scim.resource.SCIMSystemRepository;
import com.wowsanta.scim.scheduler.SCIMJob;
import com.wowsanta.scim.scheduler.SCIMScheduler;
import com.wowsanta.scim.schema.SCIMConstants;

import spark.Request;
import spark.Response;
import spark.Route;

public class SystemApiService {

	public static Route getAllSystems() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				SCIMSystemRepository system_repository = SCIMRepositoryManager.getInstance().getSystemRepository();
				
				List<SCIMSystem> system_list = system_repository.getSystemAll();
				
				System.out.println("system_list : " + system_list);
				return system_list;
			}
		};
	}

	public static Route getConsumerSystems() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				SCIMSystemRepository system_repository = SCIMRepositoryManager.getInstance().getSystemRepository();
				
				List<SCIMSystem> system_list = system_repository.getSystemAll("CONSUMER");
				
				System.out.println("system_list : " + system_list);
				return system_list;
			}
		};
	}
	
	public static Route getProviderSystems() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				SCIMSystemRepository system_repository = SCIMRepositoryManager.getInstance().getSystemRepository();
				
				List<SCIMSystem> system_list = system_repository.getSystemAll("RESOURCE");
				
				System.out.println("system_list : " + system_list);
				return system_list;
			}
		};
	}
	
	public static Route getSystem() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				String systemId = request.params(":id");
				System.out.println("systemId : " + systemId);
				
				return "get-system";
			}
		};
	}

	public static Route getSystemScheduler() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				String systemId = request.params(":systemId");
				
				SCIMSystemRepository system_repository = SCIMRepositoryManager.getInstance().getSystemRepository();
				List<SCIMScheduler> scheduler_list = system_repository.getSchdulerBySystemId(systemId);
				
				System.out.println("scheduler_list : " + scheduler_list.size());
				
				return scheduler_list;
			}
			
		};
	}
	public static Route getSystemScheduler2() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				String systemId = request.params(":id");
				
				System.out.println(">>> : " + systemId);
				
				SCIMSystemRepository system_repository = SCIMRepositoryManager.getInstance().getSystemRepository();
				SCIMSystem target_system = system_repository.getSystem(systemId);
				System.out.println(">>> : " + target_system);
				if(target_system == null) {
					return system_repository.getSchdulerAll();
				}else {
					
					String call_url = target_system.getSystemUrl() + "/scheduler/";
					
					List<SCIMScheduler> scheduler_list = new ArrayList<SCIMScheduler>();
					SCIMUser user = request.session().attribute("loginUser");
					try {
					
						RESTClient client = new RESTClient(user);
						String call_reslut = client.call(call_url);
						try {
							JsonParser parser = new JsonParser();
							JsonArray json_array = parser.parse(call_reslut).getAsJsonArray();
							for (JsonElement jsonElement : json_array) {
								JsonObject object = jsonElement.getAsJsonObject();
								SCIMScheduler schuduler = SCIMScheduler.load(object,SCIMScheduler.class);
								scheduler_list.add(schuduler);
							}
						}catch(Exception e1) {
							System.err.println(">>>>" + e1.getMessage());
						}
						return scheduler_list;
					}catch(Exception e) {
						System.err.println(">>>" +  e.getMessage());
						return scheduler_list;
					}
				}
			}
		};
	}

	public static Route runSystemScheduler() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				String systemId 	= request.params(":systemId");
				String schedulerId 	= request.params(":schedulerId");
				
				
				System.out.println("systemId : ==>> " + systemId);
				System.out.println("schedulerId : ==>> " + schedulerId);
				
				SCIMSystemRepository system_repository = SCIMRepositoryManager.getInstance().getSystemRepository();
				SCIMSystem target_system = system_repository.getSystem(systemId);
				
				System.out.println("target_system : " +  target_system);
				
				SCIMUser user = request.session().attribute("loginUser");
				
				if(target_system != null) {
					String run_url = target_system.getSystemUrl() + "/scheduler/run/" + schedulerId;
					
					System.out.println("run_url : " +  run_url);
					
					RESTClient client = new RESTClient(user);
					String call_reslut = client.run(run_url);
					System.out.println("call_reslut : " + call_reslut);
				}else {
					SCIMScheduler scheduler = system_repository.getSchdulerById(schedulerId);
					System.out.println("run scheduler : " +  scheduler);
					
					SCIMJob job =  (SCIMJob) Class.forName(scheduler.getJobClass()).newInstance();
					
					System.out.println("run SCIMJob : " +  job);
					
					job.doExecute(scheduler, user);
					
					
				}
				return null;
			}
		};
	}

	public static Route getSchedulerHistory() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				String schedulerId = request.params(":schedulerId");
				SCIMSystemRepository system_repository = SCIMRepositoryManager.getInstance().getSystemRepository();
				return system_repository.getSchedulerHistory(schedulerId);
			}
		};
	}



	

}
