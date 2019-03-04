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
import org.apache.http.util.EntityUtils;

import com.ehyundai.object.Resource;
import com.ehyundai.object.User;
import com.wowsanta.scim.client.RESTClient;
import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.log.SCIMLogger;
import com.wowsanta.scim.message.SCIMFindRequest;
import com.wowsanta.scim.message.SCIMListResponse;
import com.wowsanta.scim.message.SCIMSearchRequest;
import com.wowsanta.scim.obj.SCIMAudit;
import com.wowsanta.scim.obj.SCIMResource2;
import com.wowsanta.scim.obj.SCIMSchedulerHistory;
import com.wowsanta.scim.obj.SCIMSystem;
import com.wowsanta.scim.obj.SCIMUser;
import com.wowsanta.scim.obj.SCIMUserMeta;
import com.wowsanta.scim.resource.SCIMRepositoryManager;
import com.wowsanta.scim.resource.SCIMResourceGetterRepository;
import com.wowsanta.scim.resource.SCIMResourceRepository;
import com.wowsanta.scim.resource.SCIMResourceSetterRepository;
import com.wowsanta.scim.resource.SCIMSystemRepository;
import com.wowsanta.scim.resource.worker.Worker;
import com.wowsanta.scim.scheduler.SCIMJob;
import com.wowsanta.scim.scheduler.SCIMScheduler;
import com.wowsanta.scim.schema.SCIMConstants;

public class ResourceSyncJob_Groupware extends SCIMJob {

	@Override
	public boolean run(SCIMScheduler scheduler, Worker worker) throws SCIMException {
		SCIMSystem source_system = SCIMRepositoryManager.getInstance().getSystemRepository().getSystemById(scheduler.getSourceSystemId());
		
		SCIMResourceGetterRepository resource_getter_repository  = (SCIMResourceGetterRepository)SCIMRepositoryManager.getInstance().getResourceRepository();
		SCIMResourceSetterRepository resource_settter_repository = (SCIMResourceSetterRepository)SCIMRepositoryManager.getInstance().getResourceRepository();
		SCIMSystemRepository system_repository = SCIMRepositoryManager.getInstance().getSystemRepository();
		String find_request_url = source_system.getSystemUrl() + "/scim/v2.0/Users/find";
		
		SCIMFindRequest req_msg = new SCIMFindRequest();
		
		Date last_exec_date = scheduler.getLastExecuteDate();
		if(last_exec_date == null) {
			req_msg.setWhere("IsUse='Y'");
		}else {
			req_msg.setWhere("ModifyDate BETWEEN '"+ toString(last_exec_date)+"' AND '"+ toString(new Date())+"'");
		}
		
		SCIMLogger.proc("Syn FindRequest  : {} ", req_msg);
		SCIMListResponse find_res = findRequestPost(worker, find_request_url, req_msg);
		SCIMLogger.proc("Syn ListResponse : ({}) {} ", find_res.getTotalResults(), find_res);
		
		SCIMAudit audit = new SCIMAudit(new Date());
		audit.setWorkerId(worker.getWorkerId());
		audit.setWorkerType(worker.getWorkerType());
		audit.setSourceSystemId(scheduler.getSourceSystemId());
		audit.setTargetSystemId(scheduler.getTargetSystemId());
		audit.setAction("SCHEDULER");
		
		SCIMSchedulerHistory history = new SCIMSchedulerHistory(scheduler.getSchedulerId());
		history.setWorkId(audit.getWorkId());
		history.setWorkerId(audit.getWorkerId());
		history.setWorkDate(audit.getWorkDate());
		
		List<SCIMResource2> resource_list = find_res.getResources();
		for (SCIMResource2 resource : resource_list) {
			audit.setUserId(resource.getId());
			try {
			
				User user = newUserFromResource2(resource);
				SCIMUser old_user = resource_getter_repository.getUser(resource.getId());
				if(old_user == null) {
					audit.setMethod("CREATE");
					resource_settter_repository.createUser(user);
					audit.setResult("SUCCESS");
				}else {
					audit.setMethod("UPDATE");
					resource_settter_repository.updateUser(user);
					audit.setResult("SUCCESS");
				}
			}catch (Exception e) {
				audit.setDetail(e.getMessage());
				audit.setResult("FAILED");
			}
			SCIMLogger.audit("ResourceSyncJob_Groupware : {}", audit);
			system_repository.addAudit(audit);
			history.addAudit(audit);
		}

		SCIMLogger.proc("Sync Groupware Result : {} ", history);
		
		system_repository.addSchedulerHistory(history);
		system_repository.updateSchdulerLastExcuteDate(scheduler.getSchedulerId(),new Date());
		
		return true;
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
		SCIMListResponse response = new SCIMListResponse();
		
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
				
				response.parse(str.toString());
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
