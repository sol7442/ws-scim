package com.wowsanta.scim.service.schudler;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.internal.LinkedTreeMap;
import com.wowsanta.scim.exception.SCIMError;
import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.protocol.FrontReqeust;
import com.wowsanta.scim.protocol.FrontResponse;
import com.wowsanta.scim.protocol.ResponseState;
import com.wowsanta.scim.repository.SCIMRepositoryManager;
import com.wowsanta.scim.repository.system.SCIMSystemRepository;
import com.wowsanta.scim.resource.user.LoginUser;
import com.wowsanta.scim.resource.worker.Worker;
import com.wowsanta.scim.scheduler.SCIMJob;
import com.wowsanta.scim.scheduler.SCIMScheduler;

import spark.Request;
import spark.Response;
import spark.Route;

public class SchedulerService {
	Logger logger = LoggerFactory.getLogger(SchedulerService.class);
	
	
	public Route getSystemScheduler() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				try {
					String systemId = request.params(":systemId");
					SCIMSystemRepository system_repository = SCIMRepositoryManager.getInstance().getSystemRepository();
					
					
					List<SCIMScheduler> scheduler_list = system_repository.getSchdulerBySystemId(systemId);
					logger.debug("scheduler_list : ", scheduler_list.size());
					
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
				FrontResponse front_response = new FrontResponse();
				try {
					String schedulerId = request.params("schedulerId");
					SCIMSystemRepository system_repository = SCIMRepositoryManager.getInstance().getSystemRepository();
					
					SCIMScheduler scheduler 	= system_repository.getSchdulerById(schedulerId);
					LoginUser login_user 		= request.session().attribute("loginUser");
					
				
						logger.info("scheduler call : {} : {}",login_user, scheduler);
					
						Worker worker = new Worker();
						worker.setWorkerId(login_user.getUserId());
						worker.setWorkerType(login_user.getType().toString());
						
						logger.info("scheduler call : {} : {}",worker, scheduler);
						
						SCIMJob job = (SCIMJob)Class.forName(scheduler.getJobClass()).newInstance();
						Object result = job.run(scheduler, worker);
						
						logger.info("scheduler call result : {} ",result);
						
						front_response.setData(result);
						front_response.setState(ResponseState.Success);

				}catch (Exception e) {
					front_response.setState(ResponseState.Fail);
					front_response.setMessage(e.getMessage());
					
					logger.error("{}",e.getMessage(),e);
					
				}
				
				return front_response;
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
				
				logger.info("getSchedulerHistoryById : ", schedulerId);
				
				SCIMSystemRepository provider_repository = SCIMRepositoryManager.getInstance().getSystemRepository();
				return provider_repository.getSchedulerHistoryById(schedulerId);
			}
		};
	}

	public Route getSchedulerDetailHistoryByWorkId() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				String workId = request.params(":workId");
				
				logger.info("getSchedulerDetailHistoryByWorkId : {}", workId);
				
				SCIMSystemRepository provider_repository =  SCIMRepositoryManager.getInstance().getSystemRepository();
				return provider_repository.findAuditByWorkId(workId);
			}
		};
	}

	public Route createScheduler() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				FrontResponse front_response = new FrontResponse();
				try {
					FrontReqeust front_request = FrontReqeust.parse(request.body());					
					
					SCIMScheduler scheduler = new SCIMScheduler();
					scheduler.setSchedulerId(front_request.getParams().get("schedulerId"));
					scheduler.setSchedulerName(front_request.getParams().get("schedulerName"));
					scheduler.setSchedulerType(front_request.getParams().get("schedulerType"));
					scheduler.setSchedulerDesc(front_request.getParams().get("schedulerDesc"));
					scheduler.setJobClass(front_request.getParams().get("jobClass"));
					
					scheduler.setEncode(front_request.getParams().get("encode"));
					
					scheduler.setTriggerType(front_request.getParams().get("triggerType"));
					scheduler.setDayOfMonth(Integer.parseInt(front_request.getParams().get("dayOfMonth")));
					scheduler.setDayOfWeek(Integer.parseInt(front_request.getParams().get("dayOfWeek")));
					scheduler.setHourOfDay(Integer.parseInt(front_request.getParams().get("hourOfDay")));
					scheduler.setMinuteOfHour(Integer.parseInt(front_request.getParams().get("minuteOfHour")));
				      
					scheduler.setSourceSystemId(front_request.getParams().get("sourceSystemId"));
					scheduler.setTargetSystemId(front_request.getParams().get("targetSystemId"));
					scheduler.setExecuteSystemId(front_request.getParams().get("executeSystemId"));
					
					Date exe_date = new Date();
					scheduler.setLastExecuteDate(exe_date);					
				      
					logger.info("create scheduler : {} ", scheduler.toString(true));
//					
					SCIMSystemRepository system_repository = SCIMRepositoryManager.getInstance().getSystemRepository();
					if(system_repository.getSchdulerById(scheduler.getSchedulerId()) == null) {
						system_repository.createScheduler(scheduler);
					}
					front_response.setState(ResponseState.Success);
				}catch (Exception e) {
					logger.error(e.getMessage(),e);
					front_response.setMessage(e.getMessage());;
					front_response.setState(ResponseState.Fail);
				}
				return front_response;
			}
		};
	}

	public Route updateScheduler() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				FrontResponse front_response = new FrontResponse();
				try {
					FrontReqeust front_request = FrontReqeust.parse(request.body());
					
					SCIMScheduler scheduler = new SCIMScheduler();
					scheduler.setSchedulerId(front_request.getParams().get("schedulerId"));
					scheduler.setSchedulerName(front_request.getParams().get("schedulerName"));
					scheduler.setSchedulerType(front_request.getParams().get("schedulerType"));
					scheduler.setSchedulerDesc(front_request.getParams().get("schedulerDesc"));
					scheduler.setJobClass(front_request.getParams().get("jobClass"));
					
					scheduler.setEncode(front_request.getParams().get("encode"));
					
					scheduler.setTriggerType(front_request.getParams().get("triggerType"));
					scheduler.setDayOfMonth(Integer.parseInt(front_request.getParams().get("dayOfMonth")));
					scheduler.setDayOfWeek(Integer.parseInt(front_request.getParams().get("dayOfWeek")));
					scheduler.setHourOfDay(Integer.parseInt(front_request.getParams().get("hourOfDay")));
					scheduler.setMinuteOfHour(Integer.parseInt(front_request.getParams().get("minuteOfHour")));
				      
					scheduler.setSourceSystemId(front_request.getParams().get("sourceSystemId"));
					scheduler.setTargetSystemId(front_request.getParams().get("targetSystemId"));
					scheduler.setExecuteSystemId(front_request.getParams().get("executeSystemId"));
					
					Date exe_date = null;
					try {
						exe_date = new Date(Date.parse(front_request.getParams().get("lastExecuteDate")));
					}catch(Exception e) {
						logger.error("{}",e.getMessage(),e);
					}					
					scheduler.setLastExecuteDate(exe_date);
					
					logger.info("update scheduler : {} ", scheduler.toString(true));
//					
					SCIMSystemRepository system_repository = SCIMRepositoryManager.getInstance().getSystemRepository();
					if(system_repository.getSchdulerById(scheduler.getSchedulerId()) != null) {
						system_repository.updateScheduler(scheduler);
					}
					front_response.setState(ResponseState.Success);
				}catch (Exception e) {
					logger.error(e.getMessage(),e);
					front_response.setMessage(e.getMessage());;
					front_response.setState(ResponseState.Fail);
				}
				return front_response;
			}
		};
	}

	public Route deleteScheduler() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				FrontResponse front_response = new FrontResponse();
				try {
					String schedulerId = request.params("schedulerId");
					
					logger.info("delete scheduler : {} ", schedulerId);
					
					SCIMSystemRepository system_repository = SCIMRepositoryManager.getInstance().getSystemRepository();
					if(system_repository.getSchdulerById(schedulerId) != null) {
						system_repository.deleteScheduler(schedulerId);
					}
					front_response.setState(ResponseState.Success);
				}catch (Exception e) {
					logger.error(e.getMessage(),e);
					front_response.setMessage(e.getMessage());;
					front_response.setState(ResponseState.Fail);
				}
				return front_response;
			}
		};
	}
}
