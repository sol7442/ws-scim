package com.wowsanta.scim.service.auth;

import com.wowsanta.scim.SystemManager;
import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.resource.SCIMAdmin;
import com.wowsanta.scim.resource.SCIMRepository;
import com.wowsanta.scim.resource.SCIMSystemRepository;
import com.wowsanta.scim.resource.ServiceAdmin;
import com.wowsanta.scim.sec.SCIMJWTToken;
import com.wowsanta.scim.service.SCIMAuthService;
import com.wowsanta.scim.service.SCIMRequest;
import com.wowsanta.scim.service.SCIMResponse;

public class LoginService  {

	SCIMSystemRepository repository;
	public LoginService() {
		this.repository = SystemManager.getInstance().getSystemRepository();
	}
	public SCIMAdmin login(String id, String pw) throws SCIMException{
		ServiceAdmin admin = (ServiceAdmin) this.repository.getAdmin(id);
		
		//this.repository = (SCIMSystemRepository) SystemManager.getInstance().getSystemRepository();
//		
//		System.out.println("id : " + request.getId());
//		System.out.println("pw : " + request.getPw());
//		
//		// 
//		SCIMJWTToken token = new SCIMJWTToken();
//		String str_token = token.issue("ServiceName","SCIM_KEY_@1234");
//		
//		LoginResponse result = new LoginResponse();
//		result.setCode(100);
//		result.setToken(str_token);
		
		return admin;
	}

}
