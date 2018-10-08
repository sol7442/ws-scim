package com.wowsanta.scim.server.env.anget;

import spark.Request;
import spark.Response;

public class AgentListGet extends AgentService {
	public static String PATH = "/all";
	
	@Override
	public Object handle(Request request, Response response) throws Exception {
		return this.serviceConsumer.toJson();
	}

	@Override
	public String getServicePath() {
		return "/evn/agent/all";
	}

}
