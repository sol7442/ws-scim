package com.wowsanta.scim.service.auth;

import static spark.Spark.halt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.log.SCIMLogger;
import com.wowsanta.scim.resource.user.LoginUser;
import com.wowsanta.scim.sec.SCIMJWTToken;

import spark.Filter;
import spark.Request;
import spark.Response;

public class AuthorizationService {
	
	Logger logger = LoggerFactory.getLogger(AuthorizationService.class);
	
	public Filter verify() {
		return new Filter() {
			
			@Override
			public void handle(Request request, Response response) throws Exception {
				if(request.uri().equals("/login")){
					return;
				}
				
				LoginUser login_user = request.session().attribute("loginUser");
				if(login_user == null) {
					String auth_token = request.headers("Authorization");
					logger.debug("Authorization > {} " ,auth_token);
					try {
						SCIMJWTToken verify_token = new SCIMJWTToken();
						login_user = verify_token.verify(auth_token);
						
						logger.debug("Authorization User > {} " , login_user);
						if(login_user != null) {
							request.session(true).attribute("loginUser",login_user);
						}
						
					} catch (SCIMException e) {
						SCIMLogger.sys("TOEKN VAILDATE FAILED ", e.getMessage());
						response.status(401);
						halt(401);
					}
				}
			}
		};
	}
}
