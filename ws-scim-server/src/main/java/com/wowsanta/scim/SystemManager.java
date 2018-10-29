package com.wowsanta.scim;

import com.wowsanta.scim.service.ServiceConsumer;
import com.wowsanta.scim.service.ServiceProvider;

public class SystemManager {

	private static SystemManager instance;
	
	private Configuration config;
	private ServiceProvider serviceProvider;
	private ServiceConsumer serviceConsumer;
	
	public static SystemManager getInstance() {
		if(instance == null) {
			instance = new SystemManager();
		}
		return instance;
	}

	public Configuration getConfig() {
		return config;
	}

	public void setConfig(Configuration config) {
		this.config = config;
	}

	public void setServiceProvider(ServiceProvider sp) {
		this.serviceProvider = sp;
	}
	public ServiceProvider getServiceProvider() {
		return this.serviceProvider;
	}

	public ServiceConsumer getServiceConsumer() {
		return serviceConsumer;
	}

	public void setServiceConsumer(ServiceConsumer serviceConsumer) {
		this.serviceConsumer = serviceConsumer;
	}
	
	
}
