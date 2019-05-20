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
import com.wowsanta.scim.message.SCIMSearchRequest;
import com.wowsanta.scim.obj.SCIMAudit;
import com.wowsanta.scim.obj.SCIMResource2;
import com.wowsanta.scim.obj.SCIMSchedulerHistory;
import com.wowsanta.scim.obj.SCIMSystem;
import com.wowsanta.scim.obj.SCIMUser;
import com.wowsanta.scim.obj.SCIMUserMeta;
import com.wowsanta.scim.object.Resource_Object;
import com.wowsanta.scim.object.SCIM_Resource;
import com.wowsanta.scim.object.SCIM_User;
import com.wowsanta.scim.protocol.ClientReponse;
import com.wowsanta.scim.protocol.ResponseState;
import com.wowsanta.scim.repository.RepositoryException;
import com.wowsanta.scim.repository.SCIMRepositoryManager;
import com.wowsanta.scim.repository.SCIMResourceGetterRepository;
import com.wowsanta.scim.repository.resource.SCIMResourceRepository;
import com.wowsanta.scim.repository.system.SCIMSystemRepository;
import com.wowsanta.scim.resource.SCIMResourceSetterRepository;
import com.wowsanta.scim.resource.worker.Worker;
import com.wowsanta.scim.scheduler.SCIMJob;
import com.wowsanta.scim.scheduler.SCIMScheduler;
import com.wowsanta.scim.schema.SCIMConstants;
import com.wowsanta.scim.schema.SCIMDefinitions.ResoureType;

public class ResourceSyncJob_Groupware extends SCIMJob {
	Logger logger = LoggerFactory.getLogger(ResourceSyncJob_Groupware.class);
	Logger audit_logger  = LoggerFactory.getLogger("audit");


	@Override
	public Object run(SCIMScheduler scheduler, Worker worker) throws SCIMException {
		
		try {

			SCIMSystemRepository system_repository = SCIMRepositoryManager.getInstance().getSystemRepository();
			
			SCIMAudit audit = makeAuditObject(scheduler, worker);
			SCIMSchedulerHistory history = makeHistoryObject(scheduler, audit);

			
			syn_group(worker, scheduler,audit, history );
			syn_user(worker, scheduler, audit, history);
			
			logger.info("Sync Groupware Result : {} ", history);
			
			system_repository.addSchedulerHistory(history);
			
			if(history.getFailCount() == 0) {
				system_repository.updateSchdulerLastExcuteDate(scheduler.getSchedulerId(),new Date());
			}
			
			return history;
			
		}catch (Exception e) {
			logger.info("Syn Failed : ",e);
			throw e;
		}
	}

	private void syn_user(Worker worker,  SCIMScheduler scheduler, SCIMAudit audit,SCIMSchedulerHistory history) {
		try {

			SCIMSystemRepository system_repository = SCIMRepositoryManager.getInstance().getSystemRepository();
			SCIMResourceRepository resource_repository = SCIMRepositoryManager.getInstance().getResourceRepository();
			
			SCIMSystem source_system = system_repository.getSystemById(scheduler.getSourceSystemId());
		
			String find_request_url = source_system.getSystemUrl() + "/scim/v2.0/Users/find";
//			String find_request_url = "https://localhost:5000" + "/scim/v2.0/Users/find";
			
			SCIMFindRequest req_msg = new SCIMFindRequest();
			String where = getWhereStatement(scheduler);
			req_msg.setWhere(where);
			req_msg.setCount(0);
			req_msg.setStartIndex(0);
			
			logger.info("Syn FindRequest  : where : {} ", where);
			logger.debug("request body : \n{}", req_msg.toString());
			
			//SCIMListResponse find_res = findRequestPost(worker, find_request_url, req_msg);
			RESTClient client = new RESTClient(worker);
			SCIMListResponse find_res = client.post_find(find_request_url, req_msg,scheduler.getEncode());
			logger.info("Syn ListResponse : count : {} ", find_res.getTotalResults());	
			
			List<Resource_Object> user_list =  find_res.getResources();
			for (Resource_Object resource : user_list) {
				try {
					String userId = (String) resource.get("id");
					
					Resource_Object old_user = resource_repository.getUser(userId);
					logger.info("check user duplication {} - {}", userId, old_user);	

					audit.setResourceType(ResoureType.USER);
					audit.setResourceId(userId);
					
					SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
					String now_date_str = transFormat.format(new Date());
					
					if(old_user == null) {
						audit.setMethod("CREATE");
						
						resource.put("modifyDate",now_date_str);
						resource.put("createDate",now_date_str);
						
						resource_repository.createUser(resource);						
					}else {
						audit.setMethod("UPDATE");
						
						resource.put("modifyDate",now_date_str);
						resource.put("createDate",null);
						
						resource_repository.updateUser(resource);
					}
					audit.setResult("SUCCESS");					
				}catch (Exception e) {
					audit.setDetail(e.getMessage());
					audit.setResult("FAILED");
					
					logger.error(e.getMessage(),e);
				}
				
				logger.info("Syn {},{},{},{}\n{} ",audit.getResourceType(),audit.getMethod(),audit.getResult(),audit.getDetail(),resource);
				system_repository.addAudit(audit);
				history.addAudit(audit);	
			}
			
		}catch (Exception e) {
			logger.error(e.getMessage()  + " : {}", worker, e);
		}
	}

	private void syn_group(Worker worker, SCIMScheduler scheduler, SCIMAudit audit,SCIMSchedulerHistory history ) {
		try {
			
			SCIMSystemRepository   system_repository = SCIMRepositoryManager.getInstance().getSystemRepository();
			SCIMResourceRepository resource_repository = SCIMRepositoryManager.getInstance().getResourceRepository();
			SCIMSystem source_system = system_repository.getSystemById(scheduler.getSourceSystemId());

			
			String find_request_url = source_system.getSystemUrl() + "/scim/v2.0/Groups/find";
//			String find_request_url = "https://localhost:5000" + "/scim/v2.0/Groups/find";
			
			SCIMFindRequest req_msg = new SCIMFindRequest();
			String where = getWhereStatement(scheduler);
			req_msg.setWhere(where);
			req_msg.setCount(0);
			req_msg.setStartIndex(0);
			
			logger.info("Syn FindRequest  : where : {} ", where);
			logger.debug("request body : \n{}", req_msg.toString());
			
			RESTClient client = new RESTClient(worker);
			SCIMListResponse find_res = client.post_find(find_request_url, req_msg,"UTF-8");// "UTF-8");
			
			logger.info("Syn ListResponse : count : {} ", find_res.getTotalResults());	
			
			List<Resource_Object> group_list =  find_res.getResources();
			for (Resource_Object resource : group_list) {
				try {
					Resource_Object old_group = resource_repository.getGroup((String) resource.get("id"));
					logger.info("check group duplication {} - {}", resource.get("id"), old_group);	

					audit.setResourceType(ResoureType.GROUP);
					audit.setResourceId((String) resource.get("id"));
					if(old_group == null) {
						audit.setMethod("CREATE");
						
						create_tree_group(resource);
						
						//resource_repository.createGroup(resource);						
					}else {
						audit.setMethod("UPDATE");
						
						SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
						String now_date_str = transFormat.format(new Date());
						
						resource.put("modifyDate",now_date_str);
						resource.put("createDate",null);
						
						resource_repository.updateGroup(resource);
					}
					audit.setResult("SUCCESS");					
				}catch (Exception e) {
					audit.setDetail(e.getMessage());
					audit.setResult("FAILED");
					logger.error(e.getMessage(),e);
					System.out.println(e.getMessage());
				}

				logger.info("Syn {},{},{},{}\n{} ",audit.getResourceType(),audit.getMethod(),audit.getResult(),audit.getDetail(),resource);
				system_repository.addAudit(audit);
				history.addAudit(audit);	
			}
			
		}catch (Exception e) {
			logger.error(e.getMessage()  + " : {}", worker, e);
		}
	}


	private void create_tree_group(Resource_Object resource) {
		SCIMResourceRepository resource_repository = SCIMRepositoryManager.getInstance().getResourceRepository();
		try {
			String parent_id = (String) resource.get("organizationParent");
			Resource_Object parent_group = resource_repository.getGroup(parent_id);
			if(parent_group == null && !parent_id.equals("-1")) {
				create_tree_group(getParentGroup(resource));
			}

			SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
			String now_date_str = transFormat.format(new Date());
			
			resource.put("modifyDate",now_date_str);
			resource.put("createDate",now_date_str);
			resource.put("active",1);
			resource.put("organizationPath", "ORGROOT;" + resource.get("organizationPath"));
			resource_repository.createGroup(resource);

		} catch (RepositoryException e) {
			e.printStackTrace();
		}

	}

	private Resource_Object getParentGroup(Resource_Object resource) {
		Resource_Object parent_group = new Resource_Object();
		
		String org_desc = (String) resource.get("organizationDescription");
		String prg_path = (String) resource.get("organizationPath");
		
		
		String[] org_desc_ = org_desc.split(";");
		String[] org_path_ = prg_path.split(";");
		
		if(org_path_.length >=2) {
			
			StringBuffer desc_buffer = new StringBuffer();
			StringBuffer path_buffer = new StringBuffer();
			
			for (int i=0; i<org_desc_.length -1; i++ ) {
				desc_buffer.append(org_desc_[i]).append(";");
			}
			for (int i=0; i<org_path_.length -1; i++ ) {
				path_buffer.append(org_path_[i]).append(";");
			}

			String parent_name 	 = org_desc_[org_desc_.length - 2];
			String parent_code   = org_path_[org_path_.length - 2];
			String parent_desc = desc_buffer.toString();
			String parent_path = path_buffer.toString();
			
			String parent_parent = null;
			
			if(org_path_.length >= 3) {
				parent_parent = org_path_[org_path_.length - 3];
			}else {
				parent_parent = "ORGROOT";
			}
			
			parent_group.setId(parent_code);
			parent_group.put("id"	,parent_code);
			parent_group.put("organizationDescription"	,parent_desc);
			parent_group.put("organizationPath"   		,parent_path);
			parent_group.put("organizationName"  		,parent_name);
			parent_group.put("organizationCode"  		,parent_code);
			parent_group.put("organizationParent"		,parent_parent);
			
		}else if(org_path_.length == 1) {// 사업장(계열사)
			parent_group.setId("ORGROOT");
			parent_group.put("id"	,"ORGROOT");
			parent_group.put("organizationName"  		,"현대백화점그룹");
			parent_group.put("organizationPath"   		,"");
			parent_group.put("organizationDescription"	,"현대백화점그룹;");
			parent_group.put("organizationCode"  		,"ORGROOT");
			parent_group.put("organizationParent"		,"-1");
		}else {
			parent_group = null;
		}
		
		return parent_group;
	}

	private String getWhereStatement(SCIMScheduler scheduler) {
		
		String where = "";
		
		Date last_exec_date = scheduler.getLastExecuteDate();
		try {
			if(last_exec_date == null) {
				where = "active eq 'Y'";
			}else {
				SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				where = "modifyDate ge '"+ transFormat.format(last_exec_date) +"'";
			}
		}catch (Exception e) {
			logger.error(e.getMessage() + " : {} ", last_exec_date, e);
			where = "IsUse eq 'Y'";
		}
		
		return where;
	}


}
