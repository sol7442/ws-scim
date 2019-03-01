package com.wowsanta.scim.policy;

import com.wowsanta.scim.exception.SCIMException;

public interface SCIMPasswordPolicy {
	public boolean vaildate(String password)throws SCIMException;
	public String encrypt(String password)throws SCIMException;
}
