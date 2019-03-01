package com.wowsanta.scim.resource;

import java.util.List;

import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.message.SCIMOperation;
import com.wowsanta.scim.obj.SCIMAdmin;
import com.wowsanta.scim.obj.SCIMUser;
import com.wowsanta.scim.scheduler.SCIMSchedulerHistory;

public interface SCIMProviderRepository extends SCIMSystemRepository {
	
	public SCIMAdmin getAdmin(String id)throws SCIMException;
	public SCIMAdmin createAdmin(SCIMAdmin admin) throws SCIMException;
	public SCIMAdmin updateAdmin(SCIMAdmin admin)throws SCIMException;	
	public void deleteAdmin(String id)throws SCIMException;
	public List<SCIMAdmin> getAdminList()throws SCIMException;
	
	public void addSystemColumn(SCIMSystemColumn scimSystemColumn)throws SCIMException;
	public List<SCIMSystemColumn> getSystemColumnsBySystemId(String string)throws SCIMException;
	public List<SCIMSchedulerHistory> getSystemSchedulerHistory(String systemId)throws SCIMException;
}
