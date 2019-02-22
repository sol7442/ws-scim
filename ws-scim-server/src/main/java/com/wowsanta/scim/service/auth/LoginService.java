package com.wowsanta.scim.service.auth;

import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.obj.SCIMAdmin;
import com.wowsanta.scim.obj.SCIMUser;
import com.wowsanta.scim.resource.SCIMCode;
import com.wowsanta.scim.resource.SCIMRepositoryManager;
import com.wowsanta.scim.resource.SCIMSystemRepository;
import com.wowsanta.scim.schema.SCIMConstants;

public class LoginService  {

	public LoginService() {
		//this.repository SystemManager.getInstance().getSystemRepository();
	}
	public SCIMUser login(String id, String pw) throws SCIMException{
		SCIMSystemRepository systemRepository = SCIMRepositoryManager.getInstance().getSystemRepository();
		SCIMUser user = systemRepository.getLoginUser(id);
		
		if(!user.getPassword().equals(pw)) {
			System.out.println(user.getPassword());
			System.out.println(pw);
			throw new SCIMException("USER AUTHENTICATION FAILED ", SCIMCode.FAILED);
		}
		
		return user;
	}

}
