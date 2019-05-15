package com.wowsanta.scim.repository.system;

import java.util.List;

import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.obj.SCIMAdmin;
import com.wowsanta.scim.obj.SCIMAudit;
import com.wowsanta.scim.obj.SCIMResource2;
import com.wowsanta.scim.obj.SCIMSchedulerHistory;
import com.wowsanta.scim.resource.SCIMSystemColumn;

public interface SCIMProviderRepository extends SCIMSystemRepository {
	
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
	
	
	
	public int getTotoalAccountCount()throws SCIMException;
	public int getInactiveAccountCount()throws SCIMException;
	public int getIsolatatedAccountCount()throws SCIMException;

	public int getTotoalSystemAccountCount(String systemId) throws SCIMException;
	public int getInactiveSystemAccountCount(String systemId)throws SCIMException;
	public int getGhostSysAccountCount(String systemId)throws SCIMException;

	public List<SCIMResource2> getUsersByWhere(String where, String order, int start_index, int end_index)throws SCIMException;	
	public List<SCIMResource2> getSystemUsersByWhere(String systemId, String where, String order, int start_index, int end_index) throws SCIMException;;
	
	public List<SCIMResource2> getUserSystemList(String userId) throws SCIMException;

	
	
}
