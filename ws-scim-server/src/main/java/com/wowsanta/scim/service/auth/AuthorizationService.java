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
				
				System.out.println("Authorization : " + request.headers("Authorization"));
				
//				try {
//					SCIMJWTToken verify_token = new SCIMJWTToken();
//					
//					verify_token.verify(request.headers("Authorization"));
//					
//					SCIMSystemRepository systemRepository = SCIMRepositoryManager.getInstance().getSystemRepository();
//					SCIMUser user = systemRepository.getLoginUser(verify_token.getUserId());
//	
//					request.session(true).attribute("user",user);
//					
//				} catch (SCIMException e) {
//					response.status(401);
//					halt(401);
//				}
			}
		};
	}
}
