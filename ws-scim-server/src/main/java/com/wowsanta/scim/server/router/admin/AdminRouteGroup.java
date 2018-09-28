package com.wowsanta.scim.server.router.admin;

import spark.RouteGroup;
import static spark.Spark.*;

public class AdminRouteGroup implements RouteGroup{

	@Override
	public void addRoutes() {
		post("/login", new AdminLogin());
		
	}

}
