package com.wowsanta.scim.repository.system;

import java.util.Date;
import java.util.List;

import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.obj.SCIMAdmin;
import com.wowsanta.scim.obj.SCIMAudit;
import com.wowsanta.scim.obj.SCIMResource2;
import com.wowsanta.scim.obj.SCIMSchedulerHistory;
import com.wowsanta.scim.obj.SCIMSystem;
import com.wowsanta.scim.object.Resource_Object;
import com.wowsanta.scim.resource.SCIMAuditData;
import com.wowsanta.scim.resource.SCIMSystemColumn;
import com.wowsanta.scim.resource.user.LoginUser;
import com.wowsanta.scim.scheduler.SCIMScheduler;

public interface SCIMSystemRepository {

	public LoginUser getLoginUser(String id) throws SCIMException;
	public void updateLoginTime(String userId)throws SCIMException;
	
	public List<SCIMSystem> getSystemAll()throws SCIMException;
	public List<SCIMSystem> getSystemAll(String type)throws SCIMException;
	
		
	public void updateSchdulerLastExcuteDate(String schdulerId, Date date) throws SCIMException; 

	public List<SCIMSchedulerHistory> getSchedulerHistory(String schedulerId)throws SCIMException;
	
	public SCIMSystem getSystemById(String sourceSystemId) throws SCIMException;
	
	public void addAudit(SCIMAudit audit)throws SCIMException;;
	public void addSchedulerHistory(SCIMSchedulerHistory history)throws SCIMException;;
	
	public SCIMAdmin getAdmin(String id)throws SCIMException;
	public SCIMAdmin createAdmin(SCIMAdmin admin) throws SCIMException;
	public SCIMAdmin updateAdmin(SCIMAdmin admin)throws SCIMException;	
	public void deleteAdmin(String id)throws SCIMException;
	public List<SCIMAdmin> getAdminList()throws SCIMException;
	
	public void addSystemColumn(SCIMSystemColumn scimSystemColumn)throws SCIMException;
	public List<SCIMSystemColumn> getSystemColumnsBySystemId(String string)throws SCIMException;
	public List<SCIMSchedulerHistory> getSchedulerHistoryById(String schedulerId)throws SCIMException;
	public List<SCIMAudit> findAuditByWorkId(String workId)throws SCIMException;
	public List<SCIMAudit> findAuditByUserId(String userId)throws SCIMException;
	public List<SCIMAudit> getAccountHistory(String userId)throws SCIMException;
	
	
//	public Resource_Object getSystemUser(String systemId, String id) throws SCIMException;
//	public void createSystemUser(String systemId, Resource_Object resource)throws SCIMException;
//	public void updateSystemUser(String systemId, Resource_Object resource)throws SCIMException;
	public void createSystem(SCIMSystem system)throws SCIMException;
	public void updateSystem(SCIMSystem system)throws SCIMException;
	public void deleteSystem(String systemId)throws SCIMException;
	
	public void createScheduler(SCIMScheduler scheduler)throws SCIMException;;
	public void updateScheduler(SCIMScheduler scheduler)throws SCIMException;
	public void deleteScheduler(String schedulerId)throws SCIMException;
	
	public SCIMScheduler getSchdulerById(String schedulerId) throws SCIMException;
	public List<SCIMScheduler> getSchdulerAll()throws SCIMException;
	public List<SCIMScheduler> getSchdulerBySystemId(String systemId)throws SCIMException;
	
	public int getAccountCount()throws SCIMException;
	public List<Resource_Object> getAccountList()throws SCIMException;
	public int getSystemAccountCount(String systemId)throws SCIMException;;
	public List<Resource_Object> getSystemAccountList(String systemId)throws SCIMException;
	public List<Resource_Object> getSystemAccountListByUserId(String userId) throws SCIMException;
	public List<SCIMAudit> getSystemAccountHistory(String systemId, String userId)throws SCIMException;
	
	public int getSystemGhostAccountCount(String systemId)throws SCIMException;
	public int getSystemActiveAccountCount(String systemId)throws SCIMException;
	public int getIsolatatedAccountCount()throws SCIMException;
	
//	public int getTotoalAccountCount()throws SCIMException;
//	public int getInactiveAccountCount()throws SCIMException;

//
//	public int getTotoalSystemAccountCount(String systemId) throws SCIMException;
//	public int getInactiveSystemAccountCount(String systemId)throws SCIMException;
//	public int getGhostSysAccountCount(String systemId)throws SCIMException;
//
//	public List<SCIMResource2> getUsersByWhere(String where, String order, int start_index, int end_index)throws SCIMException;	
//	public List<SCIMResource2> getSystemUsersByWhere(String systemId, String where, String order, int start_index, int end_index) throws SCIMException;;
//	
//	public List<SCIMResource2> getUserSystemList(String userId) throws SCIMException;
	
}
