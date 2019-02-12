package com.wowsanta.scim.repo;

import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.obj.SCIMAdmin;
import com.wowsanta.scim.repo.rdb.AbstractRDBRepository;
import com.wowsanta.scim.resource.SCIMSystemRepository;

public class SqlLiteSystemRepository extends AbstractRDBRepository implements SCIMSystemRepository{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2711930299742307893L;

	@Override
	public SCIMAdmin getAdmin(String id) throws SCIMException {
		// TODO Auto-generated method stub
		return null;
	}

	

}
