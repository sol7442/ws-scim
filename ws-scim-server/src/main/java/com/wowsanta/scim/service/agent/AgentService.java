package com.wowsanta.scim.service.agent;

import static spark.Spark.*;

import com.wowsanta.scim.SystemManager;
import com.wowsanta.scim.service.ServiceConsumer;

import spark.Request;
import spark.Response;
import spark.RouteGroup;

public class AgentService implements RouteGroup {
	protected ServiceConsumer serviceConsumer = SystemManager.getInstance().getServiceConsumer();

	@Override
	public void addRoutes() {
		get("all", (req, res) -> this.getAgentList(req,res));
		put("", (req, res) -> this.addAgent(req,res));
		
	}
	
	public String getAgentList(Request req, Response res) {
		return serviceConsumer.toJson();
	}
	public String addAgent(Request req, Response res) {
		
		return "";
	}
	public String updatAgent(Request req, Response res) {
		
		return "";
	}
	public String deleteAgent(Request req, Response res) {
		
		return "";
	}	
}
