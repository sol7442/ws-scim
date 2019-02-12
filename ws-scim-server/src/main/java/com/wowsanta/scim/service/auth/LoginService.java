package com.wowsanta.scim.service.auth;

import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.obj.SCIMAdmin;
import com.wowsanta.scim.resource.SCIMCode;
import com.wowsanta.scim.resource.SCIMRepositoryManager;
import com.wowsanta.scim.resource.SCIMSystemRepository;
import com.wowsanta.scim.resource.ServiceAdmin;
import com.wowsanta.scim.schema.SCIMConstants;

public class LoginService  {

	public LoginService() {
		//this.repository SystemManager.getInstance().getSystemRepository();
	}
	public SCIMAdmin login(String id, String pw) throws SCIMException{
		SCIMSystemRepository systemRepository = SCIMRepositoryManager.getInstance().getSystemRepository();
		SCIMAdmin admin = systemRepository.getAdmin(id);
		
		if(!admin.getPassword().equals(pw)) {
			System.out.println(admin.getPassword());
			System.out.println(pw);
			throw new SCIMException("ADMIN AUTHENTICATION FAILED ", SCIMCode.FAILED);
		}
		
		return admin;
	}

}
