package com.wowsanta.scim.resource;

import com.wowsanta.scim.exception.SCIMException;

public interface SCIMSystemRepository extends SCIMRepository {

	SCIMAdmin getAdmin(String id) throws SCIMException;

}
