package com.wowsanta.scim.service.system;

import static com.wowsanta.scim.server.JsonUtil.json_parse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.plaf.synth.SynthSpinnerUI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wowsanta.scim.SCIMSystemInfo;
import com.wowsanta.scim.SCIMSystemManager;
import com.wowsanta.scim.client.RESTClient;
import com.wowsanta.scim.exception.SCIMError;
import com.wowsanta.scim.log.SCIMLogger;
import com.wowsanta.scim.message.SCIMBulkResponse;
import com.wowsanta.scim.obj.SCIMSystem;
import com.wowsanta.scim.obj.SCIMUser;
import com.wowsanta.scim.repository.SCIMRepositoryManager;
import com.wowsanta.scim.repository.system.SCIMProviderRepository;
import com.wowsanta.scim.repository.system.SCIMSystemRepository;
import com.wowsanta.scim.scheduler.SCIMJob;
import com.wowsanta.scim.scheduler.SCIMScheduler;
import com.wowsanta.scim.schema.SCIMConstants;
import com.wowsanta.scim.schema.SCIMErrorCode;
import com.wowsanta.scim.schema.SCIMScuessCode;

import spark.Request;
import spark.Response;
import spark.Route;

public class SystemApiService {

	private static Logger logger = LoggerFactory.getLogger(SystemApiService.class);
	
	public static Route getAllSystems() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				SCIMSystemRepository system_repository = SCIMRepositoryManager.getInstance().getSystemRepository();
				
				List<SCIMSystem> system_list = system_repository.getSystemAll();
				
				logger.debug("system_list : " + system_list);
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
				
				logger.debug("system_list : " + system_list);
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
				
				logger.debug("system_list : " + system_list);
				return system_list;
			}
		};
	}
	
	public static Route getSystem() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				String systemId = request.params(":id");
				logger.debug("systemId : " + systemId);
				
				return "get-system";
			}
		};
	}


	public static Route getSchedulerById() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return SCIMError.NotImplemented;
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
//				logger.debug("remote-schedulerId : " + schedulerId);
//				
//				SCIMSystemRepository system_repository = SCIMRepositoryManager.getInstance().getSystemRepository();
//				SCIMScheduler scheduler 	= system_repository.getSchdulerById(schedulerId);
//				
//				SCIMUser login_user = request.session().attribute("loginUser");
//				
//				logger.debug("remote-schedulerId run >>: " + schedulerId);
//				SCIMJob job =  (SCIMJob) Class.forName(scheduler.getJobClass()).newInstance();
//				job.doExecute(scheduler, login_user, false);
//				logger.debug("remote-schedulerId run <<: " + schedulerId);
				
				return SCIMError.NotImplemented;//"OK";
			}
		};
	}
	public static Route runSystemScheduler() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return SCIMError.NotImplemented;//"OK";
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
//				logger.debug("local system id : " + local_system_info.getSystemId());
//				logger.debug("excute system id : " + scheduler.getExcuteSystemId());
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
//						logger.debug("remote-call -> schedulerId : " + schedulerId);
//						String call_reslut = client.run(run_url, params);
//						
//						logger.debug(call_reslut);
//						
//						SCIMLogger.proc("system scheduler result : {}", "OK");
//						
//						return SCIMScuessCode.OK;
//					}catch(Exception e) {
//		logger.debug(">>>>eexcep" + e.getLocalizedMessage());
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

	public static Route getSystemRepository() {
		return new Route() {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				String systemId = request.params(":systemId");
				SCIMProviderRepository provider_repository = (SCIMProviderRepository) SCIMRepositoryManager.getInstance().getSystemRepository();
				
				SCIMSystem target_system = provider_repository.getSystemById(systemId);
				
				String call_url = target_system.getSystemUrl() + "/system/repository";
				
				
				return provider_repository.getSystemColumnsBySystemId(systemId);
			}
		};
	}

	public static Route setSystemRepository() {
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
