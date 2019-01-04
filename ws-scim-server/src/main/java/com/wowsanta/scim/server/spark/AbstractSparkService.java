package com.wowsanta.scim.server.spark;

import spark.Spark;

public abstract class AbstractSparkService {
	protected void initialize(SparkServer service) {
		Spark.port(service.getServicePort());
		Spark.threadPool(service.getMaxThreads(),service.getMinThreads(),service.getIdleTimeoutMills());
		Spark.staticFiles.registerMimeType("ico", "ico");
		Spark.staticFiles.externalLocation(service.getStaticFiles());
	}
	
	public abstract void setRouters();
	
	
}
