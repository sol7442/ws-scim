package com.wowsanta.scim.server.env.anget;

import spark.RouteGroup;
import static spark.Spark.*;

public class AgentRoute implements RouteGroup{

	@Override
	public void addRoutes() {
		get(AgentListGet.PATH, new AgentListGet());
		
	}

}
