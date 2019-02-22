package com.wowsanta.scim.service.system;

import static com.wowsanta.scim.server.JsonUtil.json_parse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.plaf.synth.SynthSpinnerUI;

import com.ehyundai.sso.ConciliationJob_SSO;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wowsanta.scim.SCIMSystemInfo;
import com.wowsanta.scim.SCIMSystemManager;
import com.wowsanta.scim.client.RESTClient;
import com.wowsanta.scim.log.SCIMLogger;
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
import com.wowsanta.scim.schema.SCIMScuessCode;

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
	
	public static Route runRemoteScheduler() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
			
//				JsonObject request_json = json_parse(request.body());
//				String schedulerId = request_json.get("schedulerId").getAsString();
//
//				System.out.println("remote-schedulerId : " + schedulerId);
//				
//				SCIMSystemRepository system_repository = SCIMRepositoryManager.getInstance().getSystemRepository();
//				SCIMScheduler scheduler 	= system_repository.getSchdulerById(schedulerId);
//				
//				SCIMUser login_user = request.session().attribute("loginUser");
//				
//				System.out.println("remote-schedulerId run >>: " + schedulerId);
//				SCIMJob job =  (SCIMJob) Class.forName(scheduler.getJobClass()).newInstance();
//				job.doExecute(scheduler, login_user, false);
//				System.out.println("remote-schedulerId run <<: " + schedulerId);
				
				return SCIMErrorCode.e501;//"OK";
			}
		};
	}
	public static Route runSystemScheduler() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return SCIMErrorCode.e501;//"OK";
			}
//				JsonObject request_json = json_parse(request.body());
//				String schedulerId = request_json.get("schedulerId").getAsString();
//				
//				SCIMLogger.proc("system scheduler call : {}",schedulerId);
//				
//				SCIMSystemInfo local_system_info = SCIMSystemManager.getInstance().getServiceProvider().getSystemInfo();
//				SCIMSystemRepository system_repository = SCIMRepositoryManager.getInstance().getSystemRepository();
//				SCIMScheduler scheduler 	= system_repository.getSchdulerById(schedulerId);
//				
//				SCIMUser login_user = request.session().attribute("loginUser");
//				
//				SCIMLogger.proc("system scheduler call : {}",scheduler);
//				
////				boolean wait_return = true;
////				if(wait_return) {
////					return SCIMScuessCode.OK;
////				}
//				
//				System.out.println("local system id : " + local_system_info.getSystemId());
//				System.out.println("excute system id : " + scheduler.getExcuteSystemId());
//				
//				if( local_system_info.getSystemId().equals(scheduler.getExcuteSystemId())) {
//			
//					try {
//						SCIMLogger.proc("local scheduler call : {}", schedulerId);
//						
//						SCIMJob job =  (SCIMJob) Class.forName(scheduler.getJobClass()).newInstance();
//
//						SCIMLogger.proc("local scheduler class : start {}", job.getClass().getCanonicalName());						
//						
//						job.doExecute(scheduler, login_user, false);
//						
//						SCIMLogger.proc("local scheduler class : end {}", job.getClass().getCanonicalName());
//						return SCIMScuessCode.OK;
//						
//					}catch (Exception e) {
//						return SCIMErrorCode.e501;
//					}
//				}else {
//					try {
//
//						
//						SCIMSystem remote_system = system_repository.getSystemById(scheduler.getSourceSystemId());
//						String run_url = remote_system.getSystemUrl() + "/scheduler/run/remote";
//						
//						SCIMLogger.proc("remote scheduler call : {}",run_url);
//						
//						RESTClient client = new RESTClient(login_user);
//						JsonObject params = new JsonObject();
//						params.addProperty("schedulerId",schedulerId);
//						
//						System.out.println("remote-call -> schedulerId : " + schedulerId);
//						String call_reslut = client.run(run_url, params);
//						
//						System.out.println(call_reslut);
//						
//						SCIMLogger.proc("system scheduler result : {}", "OK");
//						
//						return SCIMScuessCode.OK;
//					}catch(Exception e) {
//		System.out.println(">>>>eexcep" + e.getLocalizedMessage());
//						return SCIMErrorCode.e404;
//					}
//				}
//			}
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
