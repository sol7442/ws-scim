package com.wowsanta.scim.service.auth;

import static spark.Spark.halt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wowsanta.scim.exception.SCIMException;
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
				logger.info("contentType : " + request.uri());
				logger.info("contentType : " + request.contentType());
				
				if(request.uri().equals("/login")){
					return;
				}
				
				if(request.uri().startsWith("/agent/library/")){
					String auth_token = request.headers("Authorization");
					logger.debug("library path ... skip... Authorization > {} " ,auth_token);
					System.out.println("library path ... skip... Authorization >> " + auth_token);
					return;
				}
				
				if(request.uri().startsWith("/agent/config/")){
					String auth_token = request.headers("Authorization");
					if(auth_token == null) {
						logger.debug("config path ... skip... Authorization > {} " ,auth_token);
						System.out.println("config path ... skip... Authorization >> " + auth_token);
						return;
					}
				}
				
				if(request.uri().startsWith("/library")){
					String auth_token = request.headers("Authorization");
					logger.debug("library path ... skip... Authorization > {} " ,auth_token);
					System.out.println("library path ... skip... Authorization >> " + auth_token);
					return;
				}
				
				if(request.uri().startsWith("/config")){
					String auth_token = request.headers("Authorization");
					if(auth_token == null) {
						logger.debug("config path ... skip... Authorization > {} " ,auth_token);
						System.out.println("config path ... skip... Authorization >> " + auth_token);
						return;
					}
				}
				
				response.header("Content-Type", "application/scim+json");
				
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
						response.status(401);
						halt(401);
					}
				}
			}
		};
	}
}
