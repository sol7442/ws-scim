package com.wowsanta.scim.service.schudler;

import static com.wowsanta.scim.server.JsonUtil.json_parse;

import java.util.List;

import com.google.gson.JsonObject;
import com.wowsanta.scim.SCIMSystemInfo;
import com.wowsanta.scim.SCIMSystemManager;
import com.wowsanta.scim.exception.SCIMError;
import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.log.SCIMLogger;
import com.wowsanta.scim.obj.SCIMUser;
import com.wowsanta.scim.resource.SCIMProviderRepository;
import com.wowsanta.scim.resource.SCIMRepositoryManager;
import com.wowsanta.scim.resource.SCIMSystemRepository;
import com.wowsanta.scim.resource.user.LoginUser;
import com.wowsanta.scim.resource.worker.Worker;
import com.wowsanta.scim.scheduler.SCIMJob;
import com.wowsanta.scim.scheduler.SCIMScheduler;
import com.wowsanta.scim.schema.SCIMErrorCode;
import com.wowsanta.scim.schema.SCIMScuessCode;

import spark.Request;
import spark.Response;
import spark.Route;

public class SchedulerService {

	public Route getSystemScheduler() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				try {
					String systemId = request.params(":systemId");
					SCIMSystemRepository system_repository = SCIMRepositoryManager.getInstance().getSystemRepository();
					
					
					List<SCIMScheduler> scheduler_list = system_repository.getSchdulerBySystemId(systemId);
					System.out.println("scheduler_list : " + scheduler_list.size());
					
					return scheduler_list;
				}catch(SCIMException e) {
					return e ;//new SCIMError(SCIMErrorCode.e500,e.getLocalizedMessage());
				}
			}
		};
	}

	public Route runSystemScheduler() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				JsonObject request_json = json_parse(request.body());
				String schedulerId = request_json.get("schedulerId").getAsString();

				SCIMSystemRepository system_repository = SCIMRepositoryManager.getInstance().getSystemRepository();
				
				SCIMScheduler scheduler 	= system_repository.getSchdulerById(schedulerId);
				LoginUser login_user 		= request.session().attribute("loginUser");
				
				System.out.println("login_user : " + login_user);
				
				try {
					SCIMLogger.proc("scheduler call : {} : {}",login_user, scheduler);
				
					Worker worker = new Worker();
					worker.setWorkerId(login_user.getUserId());
					worker.setWorkerType(login_user.getType().toString());
					
					SCIMLogger.proc("scheduler call : {} : {}",worker, scheduler);
					
					SCIMJob job = (SCIMJob)Class.forName(scheduler.getJobClass()).newInstance();
					Object result = job.run(scheduler, worker);
					
					SCIMLogger.proc("scheduler call result : {} ",result);
					
					return result;
				}catch (Exception e) {
					SCIMLogger.error("Scheduler Run Failed : ",e);
					return SCIMError.InternalServerError;
				}
			}
		};
	}

	public Route getSchedulerBySystemId() {
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

	public Route getSchedulerById() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return SCIMError.NotImplemented;
			}
		};
	}

	public Route getSchedulerHistoryById() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				String schedulerId = request.params(":schedulerId");
				
				SCIMLogger.proc("getSchedulerHistoryById : ", schedulerId);
				
				SCIMProviderRepository provider_repository = (SCIMProviderRepository) SCIMRepositoryManager.getInstance().getSystemRepository();
				return provider_repository.getSchedulerHistoryById(schedulerId);
			}
		};
	}

	public Route getSchedulerDetailHistoryByWorkId() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				String workId = request.params(":workId");
				
				SCIMLogger.proc("getSchedulerDetailHistoryByWorkId : {}", workId);
				
				SCIMProviderRepository provider_repository = (SCIMProviderRepository) SCIMRepositoryManager.getInstance().getSystemRepository();
				return provider_repository.findAuditByWorkId(workId);
			}
		};
	}

}
