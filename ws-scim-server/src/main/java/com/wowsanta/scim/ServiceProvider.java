package com.wowsanta.scim;

import com.wowsanta.scim.obj.ServiceProviderConfigMeta;
import com.wowsanta.scim.server.SparkServer;
import com.wowsanta.scim.service.SCIMServiceProvider;

public class ServiceProvider extends SCIMServiceProvider {

	private static final long serialVersionUID = -2587976151200655170L;
	
	private ServiceProviderConfigMeta meta;
	private SparkServer server;;
	
//	@Override
//	public SCIMServiceServer getServer() {
//		return this.server;
//	}
//	
//	public void setServer(SCIMServiceServer server) {
//		this.server = (SparkServer) server;
//	}
//	
//	@Override
//	public SCIMMeta getMeta() {
//		return this.meta;
//	}
//	
//	@Override
//	public void setMeta(SCIMMeta meta) {
//		this.meta = (ServiceProviderConfigMeta)meta;
//	}
}
