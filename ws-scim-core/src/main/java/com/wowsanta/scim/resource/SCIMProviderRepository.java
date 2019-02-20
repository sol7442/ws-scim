package com.wowsanta.scim.resource;

import java.util.List;

import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.message.SCIMOperation;
import com.wowsanta.scim.obj.SCIMUser;

public interface SCIMProviderRepository extends SCIMSystemRepository {
	
	public void addSystemColumn(SCIMSystemColumn scimSystemColumn)throws SCIMException;
	public List<SCIMSystemColumn> getSystemColumnsBySystemId(String string)throws SCIMException;
}
