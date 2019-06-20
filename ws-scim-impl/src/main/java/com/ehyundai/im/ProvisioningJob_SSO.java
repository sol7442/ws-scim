package com.ehyundai.im;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wowsanta.scim.client.RESTClient;
import com.wowsanta.scim.exception.SCIMError;
import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.log.SCIMLogger;
import com.wowsanta.scim.message.SCIMBulkOperation;
import com.wowsanta.scim.message.SCIMBulkRequest;
import com.wowsanta.scim.message.SCIMBulkResponse;
import com.wowsanta.scim.obj.SCIMAudit;
import com.wowsanta.scim.obj.SCIMSchedulerHistory;
import com.wowsanta.scim.obj.SCIMSystem;
import com.wowsanta.scim.obj.SCIMUser;
import com.wowsanta.scim.object.Resource_Object;
import com.wowsanta.scim.repository.AttributeValue;
import com.wowsanta.scim.repository.RepositoryInputMapper;
import com.wowsanta.scim.repository.RepositoryOutputMapper;
import com.wowsanta.scim.repository.SCIMRepositoryManager;
import com.wowsanta.scim.repository.SCIMResourceGetterRepository;
import com.wowsanta.scim.repository.SCIMServerResourceRepository;
import com.wowsanta.scim.repository.resource.SCIMResourceRepository;
import com.wowsanta.scim.repository.system.SCIMSystemRepository;
import com.wowsanta.scim.resource.SCIMLocationFactory;
import com.wowsanta.scim.resource.worker.Worker;
import com.wowsanta.scim.scheduler.SCIMJob;
import com.wowsanta.scim.scheduler.SCIMScheduler;
import com.wowsanta.scim.schema.SCIMConstants;
import com.wowsanta.scim.schema.SCIMDefinitions;
import com.wowsanta.scim.schema.SCIMDefinitions.ResoureType;
import com.wowsanta.scim.util.Random;

import oracle.net.aso.g;

public class ProvisioningJob_SSO extends SCIMJob {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2799856505493920258L;

	Logger logger = LoggerFactory.getLogger(ProvisioningJob_SSO.class);
	Logger audit_logger  = LoggerFactory.getLogger("audit");
	
	@Override
	public Object run(SCIMScheduler scheduler, Worker worker) throws SCIMException {
		SCIMSchedulerHistory history = null;
		try {
			
			SCIMSystemRepository system_repository = SCIMRepositoryManager.getInstance().getSystemRepository();
			String target_system_id = scheduler.getTargetSystemId();
			
			SCIMAudit audit = makeAuditObject(scheduler, worker);
			 history = makeHistoryObject(scheduler, audit);
			
			SCIMBulkRequest scim_bluk_request = new SCIMBulkRequest();
			scim_bluk_request.setRequestId(audit.getWorkId());

			SCIMResourceRepository resource_repository = SCIMRepositoryManager.getInstance().getResourceRepository();

			String filter = null;
			Date last_exec_date = scheduler.getLastExecuteDate();
			if(last_exec_date !=null) {
				SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				filter = "modifyDate ge '" + transFormat.format(last_exec_date) + "'"; // or id eq \"803148\" ";
			}
			logger.info("Bulk Filter -Grop : {} ", filter);
			
			int totoal_group_count = resource_repository.getGroupCount(filter);
			List<Resource_Object> group_list = resource_repository.searchGroup(filter,0,0,totoal_group_count);
			for (Resource_Object group : group_list) {
				SCIMBulkOperation operation = new SCIMBulkOperation();
				operation.setPath(SCIMConstants.GROUP_ENDPOINT);
				operation.setData(group);
				operation.setBulkId(Random.number(0, 1000000));
				operation.setMethod(SCIMDefinitions.MethodType.PUT.toString());
				
				scim_bluk_request.addOperation(operation);
				
				logger.info("operation : {}",operation);
			}
			logger.info("Bulk Size -Grop : {} ", group_list.size());
			
			int totoal_user_count = resource_repository.getUserCount(filter);
			List<Resource_Object> user_list = resource_repository.searchUser(filter,0,0,totoal_user_count);
			for (Resource_Object user : user_list) {
				SCIMBulkOperation operation = new SCIMBulkOperation();
				operation.setPath(SCIMConstants.USER_ENDPOINT);
				operation.setData(user);
				operation.setBulkId(Random.number(0, 1000000));
				operation.setMethod(SCIMDefinitions.MethodType.PUT.toString());
				scim_bluk_request.addOperation(operation);
				
				logger.info("operation : {}",operation);
			}
			logger.info("Bulk Size -User : {} ", user_list.size());
			
			SCIMSystem target_system = system_repository.getSystemById(target_system_id);

			String bluk_request_url = target_system.getSystemUrl() + "/scim/v2.0/Bulk";
//			String bluk_request_url = "https://127.0.0.1:5000" + "/scim/v2.0/Bulk";
			
			RESTClient client = new RESTClient(worker);
			SCIMBulkResponse scim_bluk_response = client.post_bulk(bluk_request_url, scim_bluk_request, scheduler.getEncode());

			logger.info("SCIMBulkResponse : {} " , scim_bluk_response.toString(false));
			
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
			
			for (SCIMBulkOperation request_operation : scim_bluk_request.getOperations()) {

				SCIMBulkOperation response_operation = findResponseOperation(request_operation,scim_bluk_response.getOperations());
				audit.setResourceId(request_operation.getData().getId());

				if(SCIMConstants.GROUP_ENDPOINT.equals(request_operation.getPath())) {
					audit.setResourceType(ResoureType.GROUP);
				}else {
					audit.setResourceType(ResoureType.USER);
				}
				
				
				if(response_operation.getStatus().equals("200")) {		
					if(request_operation.getPath().equals(SCIMConstants.USER_ENDPOINT)) {
						AttributeValue system_id_value = new AttributeValue("systemId",target_system_id);
						AttributeValue user_id_value = new AttributeValue("id",request_operation.getData().getId());
						
						List<AttributeValue> attribute_list = new ArrayList<AttributeValue>();
						attribute_list.add(system_id_value);
						attribute_list.add(user_id_value);
						
						Resource_Object old_user  = resource_repository.getSystemUser(system_user_resource_output_mapper, attribute_list);
						Resource_Object new_user  =	request_operation.getData();
						
						
						final SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
						
						new_user.put("systemId",target_system_id);
						new_user.put("provisionDate", fmt.format(new Date()));
												
						new_user.put("externalId", new_user.getId());
						if(old_user == null) {
							audit.setMethod("CREATE");
							resource_repository.createSystemUser(system_user_resource_input_mapper, new_user );
						}else {
							audit.setMethod("UPDATE");
							resource_repository.updateSystemUser(system_user_resource_input_mapper, new_user);
							
							String detail = compareResource(old_user,new_user);
							audit.setDetail(detail);
						}
					}
					audit.setResult("SUCCESS");
				}else {
					audit.setResult("FAILED");
					audit.setDetail(response_operation.getResponse().getDetail());
				}

				audit_logger.info("Provisioning SSO : {}", audit);
				system_repository.addAudit(audit);
				history.addAudit(audit);
				logger.info("Provision Response : {} < {} : {} ",audit.getWorkId(),response_operation );
			}
			
			system_repository.addSchedulerHistory(history);
			if(history.getFailCount() == 0) {
				system_repository.updateSchdulerLastExcuteDate(scheduler.getSchedulerId(),new Date());
			}

			return history;
			
		}catch (Exception e) {
			logger.error("Syn Failed : {} ",e.getMessage(), e);
		}
		
		return history;
	}

	private String compareResource(Resource_Object old_user, Resource_Object new_user) {
		logger.debug("old sys user : {}", old_user);
		logger.debug("new sys user : {}", new_user);
		
		Map<String,Object> old_data = old_user.getAttributes();
		Map<String,Object> new_data = new_user.getAttributes();
		Set<String> data_keys = old_data.keySet();
		
		StringBuffer buffer = new StringBuffer();
		for (String key : data_keys) {
			if(
				key.equals("provisionDate") ||
				key.equals("lastAccessDate") ||
				key.equals("modifyDate") ||
				key.equals("createDate") ||
				key.equals("externalId")
				) {
				
				continue;
			}
			
			if(old_data.get(key) != null) {
				if(!old_data.get(key).equals(new_data.get(key))) {
					buffer.append(key).append(":");
					buffer.append(old_data.get(key));
					buffer.append("->");
					buffer.append(new_data.get(key));
					buffer.append(",");
				}
			}		
		}
		return buffer.toString();
	}

	private SCIMBulkOperation findResponseOperation(SCIMBulkOperation request_operation,
			List<SCIMBulkOperation> operations) {
		for (SCIMBulkOperation response_operation : operations) {
			if(response_operation.getBulkId().equals(request_operation.getBulkId())) {
				return 	response_operation;
			}
		}
		return null;
	}

}
