package com.wowsanta.scim.repo;

import java.util.Date;
import java.util.List;

import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.message.SCIMOperation;
import com.wowsanta.scim.obj.SCIMAdmin;
import com.wowsanta.scim.obj.SCIMSystem;
import com.wowsanta.scim.obj.SCIMUser;
import com.wowsanta.scim.repo.rdb.AbstractRDBRepository;
import com.wowsanta.scim.resource.SCIMSystemRepository;
import com.wowsanta.scim.scheduler.SCIMScheduler;
import com.wowsanta.scim.scheduler.SCIMSchedulerHistory;

public class SqlLiteSystemRepository extends AbstractRDBRepository implements SCIMSystemRepository{

	@Override
	public SCIMUser getLoginUser(String id) throws SCIMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SCIMSystem> getSystemAll() throws SCIMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SCIMSystem> getSystemAll(String type) throws SCIMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateSchdulerLastExcuteDate(String schdulerId, Date date) throws SCIMException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<SCIMScheduler> getSchdulerAll() throws SCIMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SCIMScheduler> getSchdulerBySystemId(String systemId) throws SCIMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SCIMSchedulerHistory> getSchedulerHistory(String schedulerId) throws SCIMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SCIMScheduler getSchdulerById(String schedulerId) throws SCIMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SCIMSystem getSystemById(String sourceSystemId) throws SCIMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addOperationResult(String workId, SCIMUser user, String source, String direct, SCIMOperation operation,
			SCIMOperation result) throws SCIMException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addSchedulerHistory(String schedulerId, String workId, int req_put_count, int req_post_count,
			int req_patch_count, int req_delate_count, int res_put_count, int res_post_count, int res_patch_count,
			int res_delate_count) throws SCIMException {
		// TODO Auto-generated method stub
		
	}

}
