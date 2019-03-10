package com.wowsanta.scim.resource;

import java.util.List;

import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.obj.SCIMAdmin;
import com.wowsanta.scim.obj.SCIMAudit;
import com.wowsanta.scim.obj.SCIMSchedulerHistory;

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
}
