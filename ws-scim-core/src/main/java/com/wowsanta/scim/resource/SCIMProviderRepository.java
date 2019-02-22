package com.wowsanta.scim.resource;

import java.util.List;

import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.message.SCIMOperation;
import com.wowsanta.scim.obj.SCIMUser;
import com.wowsanta.scim.scheduler.SCIMSchedulerHistory;

public interface SCIMProviderRepository extends SCIMSystemRepository {
	
	public void addSystemColumn(SCIMSystemColumn scimSystemColumn)throws SCIMException;
	public List<SCIMSystemColumn> getSystemColumnsBySystemId(String string)throws SCIMException;
	public List<SCIMSchedulerHistory> getSystemSchedulerHistory(String systemId)throws SCIMException;
}
