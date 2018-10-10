package com.wowsanta.scim.service.auth;

import spark.RouteGroup;
import static spark.Spark.*;

public class AuthenticationRoute implements RouteGroup{

	@Override
	public void addRoutes() {
		post("/login", new LoginService());
		
	}

}
