package com.ehyundai.im;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wowsanta.scim.client.RESTClient;
import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.message.SCIMFindRequest;
import com.wowsanta.scim.message.SCIMListResponse;
import com.wowsanta.scim.obj.SCIMAudit;
import com.wowsanta.scim.obj.SCIMSchedulerHistory;
import com.wowsanta.scim.obj.SCIMSystem;
import com.wowsanta.scim.object.Resource_Object;
import com.wowsanta.scim.repository.AttributeValue;
import com.wowsanta.scim.repository.RepositoryInputMapper;
import com.wowsanta.scim.repository.RepositoryOutputMapper;
import com.wowsanta.scim.repository.SCIMRepositoryManager;
import com.wowsanta.scim.repository.SCIMServerResourceRepository;
import com.wowsanta.scim.repository.resource.SCIMResourceRepository;
import com.wowsanta.scim.repository.system.SCIMSystemRepository;
import com.wowsanta.scim.resource.worker.Worker;
import com.wowsanta.scim.scheduler.SCIMJob;
import com.wowsanta.scim.scheduler.SCIMScheduler;
import com.wowsanta.scim.schema.SCIMDefinitions.ResoureType;

public class AnalysisJob_SingleSignOn extends SCIMJob {
	Logger logger = LoggerFactory.getLogger(AnalysisJob_SingleSignOn.class);
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
			RESTClient client = new RESTClient(worker);
			SCIMListResponse find_res = client.post_find(find_request_url, req_msg,scheduler.getEncode());
			logger.info("Syn ListResponse : count : {} ", find_res.getTotalResults());			
			
			sync(source_system.getSystemId(),find_res, audit, history);

			logger.info("Sync SingleSingOn Result : {} ", history);

			system_repository.addSchedulerHistory(history);
			
			if(history.getFailCount() == 0) {
				system_repository.updateSchdulerLastExcuteDate(scheduler.getSchedulerId(),new Date());
			}
			
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
		try {
			if(last_exec_date == null) {
				where = "IsUse eq 'Y'";
			}else {
				//SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				final SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
				where = "modifyDate ge " + fmt.format(last_exec_date);//.getTime(); //'"+ //transFormat.format(last_exec_date) +"'";
			}
		}catch (Exception e) {
			logger.error(e.getMessage() + " : {} ", last_exec_date, e);
			where = "IsUse eq 'Y'";
		}
		return where;
	}
	private void sync(String systemId, SCIMListResponse find_res, SCIMAudit audit, SCIMSchedulerHistory history) throws SCIMException {
		
		SCIMResourceRepository resource_repository = SCIMRepositoryManager.getInstance().getResourceRepository();
		SCIMSystemRepository system_repository = SCIMRepositoryManager.getInstance().getSystemRepository();
		
		
		String system_user_input_mapper_file = SCIMRepositoryManager.getInstance().getResourceRepositoryConfig().getSystemUserInputMapper();
		String system_user_output_mapper_file = SCIMRepositoryManager.getInstance().getResourceRepositoryConfig().getSystemUseroutputMapper();
		logger.info("system_user_input_mapper : {} ", system_user_input_mapper_file);
		logger.info("system_user_output_mapper_file : {} ", system_user_output_mapper_file);
		RepositoryOutputMapper system_user_resource_output_mapper = null;
		RepositoryInputMapper  system_user_resource_input_mapper = null;
		try {
			system_user_resource_input_mapper  = RepositoryInputMapper.load(system_user_input_mapper_file);
			system_user_resource_output_mapper = RepositoryOutputMapper.load(system_user_output_mapper_file);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		
		
		List<Resource_Object> resource_list = find_res.getResources();
		for (Resource_Object resource : resource_list) {
			resource.put("systemId",systemId);

			try {
				
				AttributeValue system_id_value = new AttributeValue("systemId",systemId);
				AttributeValue user_id_value = new AttributeValue("id",resource.getId());
				
				List<AttributeValue> attribute_list = new ArrayList<AttributeValue>();
				attribute_list.add(system_id_value);
				attribute_list.add(user_id_value);
				
				Resource_Object im_user  = resource_repository.getUser(resource.getId());
				if(im_user == null) {
					resource.put("state","Ghost");
				}
				
				Resource_Object old_user  = resource_repository.getSystemUser(system_user_resource_output_mapper, attribute_list);
				logger.debug("old sys user : {}", old_user);
				if(old_user == null) {
					audit.setMethod("CREATE");
					resource_repository.createSystemUser(system_user_resource_input_mapper, resource);
					audit.setResult("SUCCESS");
				}else {
					audit.setMethod("UPDATE");
					resource_repository.updateSystemUser(system_user_resource_input_mapper, resource);
					audit.setResult("SUCCESS");
				}
			}catch (Exception e) {
				logger.error(e.getMessage(),e);
				audit.setDetail(e.getMessage());
				audit.setResult("FAILED");
			}
			logger.info("Analysis : {}", resource);
//			system_repository.addAudit(audit);
			history.addAudit(audit);
		}
	}

//	private SCIMListResponse findRequestPost(Worker worker, String find_request_url, SCIMFindRequest req_msg) throws SCIMException {
//		SCIMListResponse response = new SCIMListResponse();
//		
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
//		
//		return response;
//	}
	
	public String toString(Date date) {
		SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(date == null) {
			return "";
		}else {
			return transFormat.format(date);
		}
	}

}
