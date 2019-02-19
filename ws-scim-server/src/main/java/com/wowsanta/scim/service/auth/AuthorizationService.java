package com.wowsanta.scim.service.auth;

import static spark.Spark.halt;

import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.obj.SCIMUser;
import com.wowsanta.scim.resource.SCIMRepositoryManager;
import com.wowsanta.scim.resource.SCIMSystemRepository;
import com.wowsanta.scim.sec.SCIMJWTToken;

import spark.Filter;
import spark.Request;
import spark.Response;
import spark.Route;

public class AuthorizationService {
	public static Filter verify() {
		return new Filter() {
			
			@Override
			public void handle(Request request, Response response) throws Exception {
				if(request.uri().equals("/login")){
					return;
				}
				
				String auth_token = request.headers("Authorization");
				System.out.println("Authorization >> : " + auth_token);
				
				try {
					SCIMJWTToken verify_token = new SCIMJWTToken();
					SCIMUser login_user = verify_token.verify(auth_token);
					
					System.out.println("login-user : " + login_user);
					if(login_user != null) {
						// check user ..
//						SCIMSystemRepository systemRepository = SCIMRepositoryManager.getInstance().getSystemRepository();
//						SCIMUser user = systemRepository.getLoginUser(verify_token.getUserId());
						request.session(true).attribute("loginUser",login_user);
					}
					
				} catch (SCIMException e) {
					System.out.println("e>>" + e.getMessage());
					response.status(401);
					halt(401);
				}
			}
		};
	}
}
