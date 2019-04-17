package com.ehyundai.im;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ehyundai.object.Resource;
import com.ehyundai.object.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wowsanta.scim.client.RESTClient;
import com.wowsanta.scim.exception.SCIMError;
import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.log.SCIMLogger;
import com.wowsanta.scim.message.SCIMFindRequest;
import com.wowsanta.scim.message.SCIMListResponse;
import com.wowsanta.scim.obj.SCIMAudit;
import com.wowsanta.scim.obj.SCIMResource2;
import com.wowsanta.scim.obj.SCIMSchedulerHistory;
import com.wowsanta.scim.obj.SCIMSystem;
import com.wowsanta.scim.obj.SCIMUser;
import com.wowsanta.scim.obj.SCIMUserMeta;
import com.wowsanta.scim.object.SCIM_Resource;
import com.wowsanta.scim.object.SCIM_User;
import com.wowsanta.scim.protocol.ClientReponse;
import com.wowsanta.scim.protocol.ResponseState;
import com.wowsanta.scim.repository.SCIMRepositoryManager;
import com.wowsanta.scim.repository.SCIMResourceGetterRepository;
import com.wowsanta.scim.resource.SCIMResourceSetterRepository;
import com.wowsanta.scim.resource.SCIMSystemRepository;
import com.wowsanta.scim.resource.worker.Worker;
import com.wowsanta.scim.scheduler.SCIMJob;
import com.wowsanta.scim.scheduler.SCIMScheduler;
import com.wowsanta.scim.schema.SCIMConstants;

public class ResourceSyncJob_Groupware extends SCIMJob {
	Logger logger = LoggerFactory.getLogger(ResourceSyncJob_Groupware.class);
	Logger audit_logger  = LoggerFactory.getLogger("audit");


	@Override
	public Object run(SCIMScheduler scheduler, Worker worker) throws SCIMException {
		
		try {

			SCIMSystemRepository system_repository = SCIMRepositoryManager.getInstance().getSystemRepository();
			SCIMSystem source_system = system_repository.getSystemById(scheduler.getSourceSystemId());
			

			SCIMAudit audit = makeAuditObject(scheduler, worker);
			SCIMSchedulerHistory history = makeHistoryObject(scheduler, audit);

			
			String find_request_url = source_system.getSystemUrl() + "/scim/v2.0/Users/find";
			SCIMFindRequest req_msg = new SCIMFindRequest();
			String where = getWhereStatement(scheduler);
			req_msg.setWhere(where);
			req_msg.setCount(100);
			req_msg.setStartIndex(0);
			
			logger.info("Syn FindRequest  : where : {} ", where);
			
			logger.debug("request body : \n{}", req_msg.toString());
			
			
			SCIMListResponse find_res = findRequestPost(worker, find_request_url, req_msg);
			logger.info("Syn ListResponse : count : {} ", find_res.getTotalResults());			
			
			sync(find_res, audit, history);

			logger.info("Sync Groupware Result : {} ", history);

			system_repository.addSchedulerHistory(history);
			//system_repository.updateSchdulerLastExcuteDate(scheduler.getSchedulerId(),new Date());
			
			return history;
			
		}catch (SCIMException e) {
			logger.info("Syn Failed : ",e);
			throw e;
		}catch (Exception e) {
			e.printStackTrace();
			throw new SCIMException(e.getMessage(), SCIMError.InternalServerError, e);
		}
	}

	private void sync(SCIMListResponse find_res, SCIMAudit audit, SCIMSchedulerHistory history) throws SCIMException {
		
		SCIMResourceGetterRepository resource_getter_repository  = (SCIMResourceGetterRepository)SCIMRepositoryManager.getInstance().getResourceRepository();
		SCIMResourceSetterRepository resource_settter_repository = (SCIMResourceSetterRepository)SCIMRepositoryManager.getInstance().getResourceRepository();
		SCIMSystemRepository system_repository = SCIMRepositoryManager.getInstance().getSystemRepository();
		
		List<SCIM_Resource> resource_list = find_res.getResources();
		logger.info("syn resource count {}", resource_list.size());
		for (SCIM_Resource resource : resource_list) {
			logger.info("syn resource {}", resource);
			
			System.out.println("syn...>>>" + resource);
		}
		
		
//		for (SCIMResource2 resource : resource_list) {
//			audit.setUserId(resource.getId());
//			try {
//			
//				User user = newUserFromResource2(resource);
//				SCIMUser old_user = resource_getter_repository.getUser(resource.getId());
//				if(old_user == null) {
//					audit.setMethod("CREATE");
//					resource_settter_repository.createUser(user);
//					audit.setResult("SUCCESS");
//				}else {
//					audit.setMethod("UPDATE");
//					resource_settter_repository.updateUser(user);
//					audit.setResult("SUCCESS");
//				}
//			}catch (Exception e) {
//				audit.setDetail(e.getMessage());
//				audit.setResult("FAILED");
//			}
//			audit_logger.info("ResourceSyncJob_Groupware : {}", audit);
//			system_repository.addAudit(audit);
//			history.addAudit(audit);
//		}
	}

	private String getWhereStatement(SCIMScheduler scheduler) {
		String where = "";
		Date last_exec_date = scheduler.getLastExecuteDate();
		if(last_exec_date == null) {
			where = "IsUse='Y'";
		}else {
			where = "ModifyDate BETWEEN '"+ toString(last_exec_date)+"' AND '"+ toString(new Date())+"'";
		}
		return where;
	}

	private User newUserFromResource2(SCIMResource2 resource) {
		Resource res = (Resource)resource;
		User user = new User();
		
		user.setActive(res.isActive());
		user.setUserName(res.getUserName());
		user.setCompanyCode(res.getCompanyCode());
		user.setDepartment(res.getDepartment());
		user.setDivision(res.getDivision());
		//user.seteMail(res.ge);
		user.setEmployeeNumber(res.getEmployeeNumber());
		user.setExernalId(res.getExernalId());
		user.setGroupCode(res.getGroupCode());
		user.setGroupName(res.getGroupName());
		user.setId(res.getId());
		user.setJob(res.getJob());
		user.setJobCode(res.getJobCode());
		user.setJoinDate(res.getJoinDate());
		user.setLastAccessDate(res.getLastAccessDate());
		user.setMeta(new SCIMUserMeta());
		user.getMeta().setCreated(res.getCreated());
		user.getMeta().setLastModified(res.getLastModified());
		user.setOrganization(res.getOrganization());
		user.setPassword(res.getPassword());
		user.setPosition(res.getPosition());
		user.setPositionCode(res.getPositionCode());
		user.setRank(res.getRank());
		user.setRankCode(res.getRankCode());
		user.setRetireDate(res.getRetireDate());
		
		return user;
	}

	private SCIMListResponse findRequestPost(Worker worker, String find_request_url, SCIMFindRequest req_msg) throws SCIMException {
		SCIMListResponse response = null;//new SCIMListResponse();
		
		RESTClient client = new RESTClient(worker);
		HttpResponse http_response = client.post(find_request_url, req_msg);
		int http_res_code = http_response.getStatusLine().getStatusCode();
		if( http_res_code >= SCIMConstants.HtppConstants.OK && http_res_code <= SCIMConstants.HtppConstants.IM_Used) {
			try {
				
				StringBuilder str = new StringBuilder();
				HttpEntity entity = http_response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(content));
				String line;
				while ((line = reader.readLine()) != null) {
					str.append(line);
				}
				
				try {
					Gson gson = new GsonBuilder().disableHtmlEscaping().create();
					response = gson.fromJson(str.toString(), SCIMListResponse.class);
					
				}catch (Exception e) {
					e.printStackTrace();
				}
				//response.parse(str.toString());
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return response;
	}
	
	public String toString(Date date) {
		SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(date == null) {
			return "";
		}else {
			return transFormat.format(date);
		}
	}

}
