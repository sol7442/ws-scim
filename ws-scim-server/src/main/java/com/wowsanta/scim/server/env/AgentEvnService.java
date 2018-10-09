package com.wowsanta.scim.server.env;

import static spark.Spark.get;

import com.wowsanta.scim.SystemManager;
import com.wowsanta.scim.server.ServiceConsumer;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.RouteGroup;

public class AgentEvnService implements RouteGroup {
	protected ServiceConsumer serviceConsumer = SystemManager.getInstance().getServiceConsumer();

	@Override
	public void addRoutes() {
		get(get_list.PATH, new get_list());
	}
	
	private class get_list implements Route{
		public static final String PATH = "/all";
		@Override
		public Object handle(Request request, Response response) throws Exception {
			System.out.println("---------");
			System.out.println(serviceConsumer.toJson());
			return serviceConsumer.toJson();
		}
	}

}
