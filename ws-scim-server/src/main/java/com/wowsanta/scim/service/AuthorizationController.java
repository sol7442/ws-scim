package com.wowsanta.scim.service;

import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.obj.SCIMAdmin;
import com.wowsanta.scim.obj.SCIMUser;
import com.wowsanta.scim.repository.SCIMRepositoryManager;
import com.wowsanta.scim.resource.SCIMSystemRepository;
import com.wowsanta.scim.resource.user.LoginUser;
import com.wowsanta.scim.sec.SCIMJWTToken;

import spark.Filter;
import spark.Request;
import spark.Response;
import static spark.Spark.halt;
public class AuthorizationController implements Filter {

	@Override
	public void handle(Request request, Response response) throws Exception {
		if(request.uri().equals("/login")){
			return;
		}
		
		try {
			SCIMJWTToken verify_token = new SCIMJWTToken();
			System.out.println("Authorization  >> : " + request.headers("Authorization"));

			LoginUser user = verify_token.verify(request.headers("Authorization"));
			System.out.println("Authorization USER >>: " + user);
			
			request.session(true).attribute("user",user);
			//SCIMSystemRepository systemRepository = SCIMRepositoryManager.getInstance().getSystemRepository();
			//SCIMUser user = systemRepository.getLoginUser(verify_token.getUserId());

			
		} catch (SCIMException e) {
			response.status(401);
			halt(401);
		}
	}

}
