package com.wowsanta.scim.resource;

import java.util.Date;
import java.util.List;

import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.obj.SCIMAudit;
import com.wowsanta.scim.obj.SCIMSchedulerHistory;
import com.wowsanta.scim.obj.SCIMSystem;
import com.wowsanta.scim.resource.user.LoginUser;
import com.wowsanta.scim.scheduler.SCIMScheduler;

public interface SCIMSystemRepository {

	public LoginUser getLoginUser(String id) throws SCIMException;
	public void updateLoginTime(String userId)throws SCIMException;
	
	public List<SCIMSystem> getSystemAll()throws SCIMException;
	public List<SCIMSystem> getSystemAll(String type)throws SCIMException;
	
		
	public void updateSchdulerLastExcuteDate(String schdulerId, Date date) throws SCIMException; 
	public List<SCIMScheduler> getSchdulerAll()throws SCIMException;

	public List<SCIMScheduler> getSchdulerBySystemId(String systemId)throws SCIMException;

	public List<SCIMSchedulerHistory> getSchedulerHistory(String schedulerId)throws SCIMException;

	public SCIMScheduler getSchdulerById(String schedulerId) throws SCIMException;

	public SCIMSystem getSystemById(String sourceSystemId) throws SCIMException;
	//public SCIMSystem getSystem(String targetSystemId)throws SCIMException;
	
//	public void addOperationResult(String workId, SCIMUser user, String source,String direct, SCIMOperation operation, SCIMOperation result)throws SCIMException;
//	public void addSchedulerHistory(String schedulerId, String workId, int req_put_count, int req_post_count,
//			int req_patch_count, int req_delate_count, int res_put_count, int res_post_count, int res_patch_count,
//			int res_delate_count) throws SCIMException;
	
	public void addAudit(SCIMAudit audit)throws SCIMException;;
	public void addSchedulerHistory(SCIMSchedulerHistory history)throws SCIMException;;

	
	
}
