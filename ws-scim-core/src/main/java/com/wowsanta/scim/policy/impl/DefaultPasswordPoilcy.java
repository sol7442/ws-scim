package com.wowsanta.scim.policy.impl;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.policy.SCIMPasswordPolicy;
import com.wowsanta.scim.sec.encryptSHA256;

public class DefaultPasswordPoilcy implements SCIMPasswordPolicy{

	@Override
	public boolean vaildate(String password) throws SCIMException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String encrypt(String password) throws SCIMException {
		try {
			return encryptSHA256.encrypt(password);
		} catch (NoSuchAlgorithmException e) {
			throw new SCIMException("encrypt failed : ",e);
		} catch (UnsupportedEncodingException e) {
			throw new SCIMException("encrypt failed : ",e);
		}
	}
}
