package com.wowsanta.scim.service.admin;

import com.wowsanta.scim.service.SCIMAdminService;
import com.wowsanta.scim.service.SCIMRequest;
import com.wowsanta.scim.service.SCIMResponse;

public class AdminService implements SCIMAdminService {

	@Override
	public SCIMResponse login(SCIMRequest request) {
		LoginRequest login_request = (LoginRequest)request;
		
		System.out.println("id : " + login_request.getId());
		System.out.println("pw : " + login_request.getPw());
		
		LoginResponse result = new LoginResponse();
		result.setCode(100);
		
		return result;
	}
	


	
}
