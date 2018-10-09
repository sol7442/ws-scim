package com.wowsanta.scim.server.env;

import static spark.Spark.path;


import spark.RouteGroup;

public class EnvironmentRoute implements RouteGroup{
	@Override
	public void addRoutes() {
		path("/agent", new AgentEvnService());
		
	}
}
