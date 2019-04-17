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

import com.wowsanta.scim.client.RESTClient;
import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.log.SCIMLogger;
import com.wowsanta.scim.message.SCIMFindRequest;
import com.wowsanta.scim.message.SCIMListResponse;
import com.wowsanta.scim.obj.SCIMAudit;
import com.wowsanta.scim.obj.SCIMResource2;
import com.wowsanta.scim.obj.SCIMSchedulerHistory;
import com.wowsanta.scim.obj.SCIMSystem;
import com.wowsanta.scim.obj.SCIMUser;
import com.wowsanta.scim.repository.SCIMRepositoryManager;
import com.wowsanta.scim.repository.SCIMServerResourceRepository;
import com.wowsanta.scim.resource.SCIMSystemRepository;
import com.wowsanta.scim.resource.worker.Worker;
import com.wowsanta.scim.scheduler.SCIMJob;
import com.wowsanta.scim.scheduler.SCIMScheduler;
import com.wowsanta.scim.schema.SCIMConstants;

public class ResourceSyncJob_SingleSignOn extends SCIMJob {
	Logger logger = LoggerFactory.getLogger(ResourceSyncJob_SingleSignOn.class);
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
			
			logger.info("Syn FindRequest  : where : {} ", where);
			SCIMListResponse find_res = findRequestPost(worker, find_request_url, req_msg);
			logger.info("Syn ListResponse : count : {} ", find_res.getTotalResults());			
			
			sync(source_system.getSystemId(),find_res, audit, history);

			logger.info("Sync SingleSingOn Result : {} ", history);

			system_repository.addSchedulerHistory(history);
			system_repository.updateSchdulerLastExcuteDate(scheduler.getSchedulerId(),new Date());
			
			return history;
			
		}catch (SCIMException e) {
			logger.error("Syn Failed : ",e);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	private String getWhereStatement(SCIMScheduler scheduler) {
		String where = "";
		Date last_exec_date = scheduler.getLastExecuteDate();
		if(last_exec_date == null) {
			where = "";
		}else {
			where = " where MODIFY_TIME BETWEEN "+ last_exec_date.getTime()+" AND "+ new Date().getTime() +"";
		}
		return where;
	}
	private void sync(String systemId, SCIMListResponse find_res, SCIMAudit audit, SCIMSchedulerHistory history) throws SCIMException {
		
		SCIMServerResourceRepository resource_provder_repository = (SCIMServerResourceRepository)SCIMRepositoryManager.getInstance().getResourceRepository();
		
		//SCIMSystemRepository system_repository = SCIMRepositoryManager.getInstance().getSystemRepository();
		
//		List<SCIMResource2> resource_list = find_res.getResources();
//		for (SCIMResource2 resource : resource_list) {
//			audit.setUserId(resource.getId());
//			try {
//			
//				SCIMUser old_user = resource_provder_repository.getSystemUser(systemId, resource.getId());
//				if(old_user == null) {
//					audit.setMethod("CREATE");
//					resource_provder_repository.createSystemDummyUser(systemId, resource);
//					audit.setResult("SUCCESS");
//				}else {
//					audit.setMethod("UPDATE");
//					resource_provder_repository.updateSystemDummyUser(systemId, resource);
//					audit.setResult("SUCCESS");
//				}
//			}catch (Exception e) {
//				audit.setDetail(e.getMessage());
//				audit.setResult("FAILED");
//			}
//			audit_logger.info("ResourceSyncJob_SigleSignOn : {}", audit);
//			///system_repository.addAudit(audit);
//			history.addAudit(audit);
//		}
	}

	private SCIMListResponse findRequestPost(Worker worker, String find_request_url, SCIMFindRequest req_msg) throws SCIMException {
		SCIMListResponse response = new SCIMListResponse();
		
//		RESTClient client = new RESTClient(worker);
//		HttpResponse http_response = client.post(find_request_url, req_msg);
//		int http_res_code = http_response.getStatusLine().getStatusCode();
//		if( http_res_code >= SCIMConstants.HtppConstants.OK && http_res_code <= SCIMConstants.HtppConstants.IM_Used) {
//			try {
//				
//				StringBuilder str = new StringBuilder();
//				HttpEntity entity = http_response.getEntity();
//				InputStream content = entity.getContent();
//				BufferedReader reader = new BufferedReader(new InputStreamReader(content));
//				String line;
//				while ((line = reader.readLine()) != null) {
//					str.append(line);
//				}
//				
//				response.parse(str.toString());
//			} catch (ParseException e) {
//				e.printStackTrace();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
		
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
