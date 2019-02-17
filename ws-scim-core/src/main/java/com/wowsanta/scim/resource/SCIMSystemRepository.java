package com.wowsanta.scim.resource;

import java.util.Date;
import java.util.List;

import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.message.SCIMOperation;
import com.wowsanta.scim.obj.SCIMSystem;
import com.wowsanta.scim.obj.SCIMUser;
import com.wowsanta.scim.scheduler.SCIMScheduler;

public interface SCIMSystemRepository {

	public SCIMUser getLoginUser(String id) throws SCIMException;

	public List<SCIMSystem> getSystemAll()throws SCIMException;
	public SCIMSystem getSystem(String targetSystemId)throws SCIMException;
		
	public void updateSchdulerLastExcuteDate(String schdulerId, Date date) throws SCIMException; 
	public List<SCIMScheduler> getSchdulerAll()throws SCIMException;
	
	public void addOperationResult(SCIMUser user, String source,String direct, SCIMOperation operation, SCIMOperation result)throws SCIMException;
	
	
}
