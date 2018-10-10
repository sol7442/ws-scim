package com.wowsanta.scim.server.env.copy;

import static spark.Spark.path;

import com.wowsanta.scim.service.agent.AgentService;

import spark.RouteGroup;

public class EnvironmentRoute implements RouteGroup{
	@Override
	public void addRoutes() {
		path("/agent", new AgentService());
		
	}
}
