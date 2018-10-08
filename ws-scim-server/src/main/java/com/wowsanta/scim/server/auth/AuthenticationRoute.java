package com.wowsanta.scim.server.auth;

import spark.RouteGroup;
import static spark.Spark.*;

import com.wowsanta.scim.server.auth.login.LoginService;

public class AuthenticationRoute implements RouteGroup{

	@Override
	public void addRoutes() {
		post("/login", new LoginService());
		
	}

}
