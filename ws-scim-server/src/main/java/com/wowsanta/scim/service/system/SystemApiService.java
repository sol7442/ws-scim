package com.wowsanta.scim.service.system;

import static com.wowsanta.scim.server.JsonUtil.json_parse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.plaf.synth.SynthSpinnerUI;

import com.ehyundai.sso.ConsiliationJob_SSO;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wowsanta.scim.SCIMSystemInfo;
import com.wowsanta.scim.SCIMSystemManager;
import com.wowsanta.scim.client.RESTClient;
import com.wowsanta.scim.message.SCIMBulkResponse;
import com.wowsanta.scim.obj.SCIMSystem;
import com.wowsanta.scim.obj.SCIMUser;
import com.wowsanta.scim.resource.SCIMProviderRepository;
import com.wowsanta.scim.resource.SCIMRepositoryManager;
import com.wowsanta.scim.resource.SCIMSystemRepository;
import com.wowsanta.scim.scheduler.SCIMJob;
import com.wowsanta.scim.scheduler.SCIMScheduler;
import com.wowsanta.scim.schema.SCIMConstants;
import com.wowsanta.scim.schema.SCIMErrorCode;

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

	public static Route getSchedulerBySystemId() {
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
	public static Route getSchedulerById() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return SCIMErrorCode.e501;
			}
			
		};
	}
//	public static Route getSystemScheduler2() {
//		return new Route() {
//			@Override
//			public Object handle(Request request, Response response) throws Exception {
//				String systemId = request.params(":id");
//				
//				System.out.println(">>> : " + systemId);
//				
//				SCIMSystemRepository system_repository = SCIMRepositoryManager.getInstance().getSystemRepository();
//				SCIMSystem target_system = system_repository.getSystem(systemId);
//				System.out.println(">>> : " + target_system);
//				if(target_system == null) {
//					return system_repository.getSchdulerAll();
//				}else {
//					
//					String call_url = target_system.getSystemUrl() + "/scheduler/";
//					
//					List<SCIMScheduler> scheduler_list = new ArrayList<SCIMScheduler>();
//					SCIMUser user = request.session().attribute("loginUser");
//					try {
//					
//						RESTClient client = new RESTClient(user);
//						String call_reslut = client.call(call_url);
//						try {
//							JsonParser parser = new JsonParser();
//							JsonArray json_array = parser.parse(call_reslut).getAsJsonArray();
//							for (JsonElement jsonElement : json_array) {
//								JsonObject object = jsonElement.getAsJsonObject();
//								SCIMScheduler schuduler = SCIMScheduler.load(object,SCIMScheduler.class);
//								scheduler_list.add(schuduler);
//							}
//						}catch(Exception e1) {
//							System.err.println(">>>>" + e1.getMessage());
//						}
//						return scheduler_list;
//					}catch(Exception e) {
//						System.err.println(">>>" +  e.getMessage());
//						return scheduler_list;
//					}
//				}
//			}
//		};
//	}

	public static Route runScheduler() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return SCIMErrorCode.e501;
			}
		};
	}
	public static Route runSystemScheduler() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				JsonObject request_json = json_parse(request.body());
				String schedulerId = request_json.get("schedulerId").getAsString();
				
				System.out.println("schedulerId : ==>> " + schedulerId);
				
				SCIMSystemInfo local_system_info = SCIMSystemManager.getInstance().getServiceProvider().getSystemInfo();
				SCIMSystemRepository system_repository = SCIMRepositoryManager.getInstance().getSystemRepository();
				SCIMScheduler scheduler 	= system_repository.getSchdulerById(schedulerId);
				
				
				SCIMUser login_user = request.session().attribute("loginUser");
				
				System.out.println("is local system  : " + local_system_info.getSystemId().equals(scheduler.getSourceSystemId()) );
				
				if( local_system_info.getSystemId().equals(scheduler.getSourceSystemId())) {
					SCIMJob job =  (SCIMJob) Class.forName(scheduler.getJobClass()).newInstance();
					job.doExecute(scheduler, login_user);
					
				}else {
					String run_url = local_system_info.getSystemUrl() + "/scheduler/run/" + schedulerId;
					RESTClient client = new RESTClient(login_user);
					
					JsonObject params = new JsonObject();
					params.addProperty("schedulerId",schedulerId);
					String call_reslut = client.run(run_url, params);
					
					System.out.println(call_reslut);
				}
				
				return "OK";
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

	public static Route getSystemColumnBySystemId() {
		return new Route() {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				String systemId = request.params(":systemId");
				SCIMProviderRepository provider_repository = (SCIMProviderRepository) SCIMRepositoryManager.getInstance().getSystemRepository();
				return provider_repository.getSystemColumnsBySystemId(systemId);
			}
		};
	}



	

}
