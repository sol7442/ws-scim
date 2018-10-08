package com.wowsanta.scim.server.env.anget;

import com.wowsanta.scim.SystemManager;
import com.wowsanta.scim.server.ServiceConsumer;

import spark.Route;

public abstract class AgentService implements Route {

	protected ServiceConsumer serviceConsumer = SystemManager.getInstance().getServiceConsumer();
	
	public abstract String getServicePath();

}
