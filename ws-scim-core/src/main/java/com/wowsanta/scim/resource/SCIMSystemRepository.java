package com.wowsanta.scim.resource;

import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.message.SCIMOperation;
import com.wowsanta.scim.obj.SCIMUser;

public interface SCIMSystemRepository {

	public SCIMUser getLoginUser(String id) throws SCIMException;
	public void addOperationResult(SCIMUser user, String source,String direct, SCIMOperation operation, SCIMOperation result)throws SCIMException;
}
