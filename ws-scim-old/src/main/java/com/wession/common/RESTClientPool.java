package com.wession.common;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.HttpParams;

public class RESTClientPool {
	// Singleton 설정 
	private static class Singleton {
		private static final RESTClientPool instance = new RESTClientPool();
	}

	public static synchronized RESTClientPool getInstance() {
		return Singleton.instance;
	}
	
	DefaultHttpClient httpClient; 
	
	public RESTClientPool() {
		httpClient = new DefaultHttpClient();
		PoolingClientConnectionManager pool = new   PoolingClientConnectionManager(httpClient.getConnectionManager().getSchemeRegistry());
		pool.setMaxTotal(5000);
		pool.setDefaultMaxPerRoute(500);
		HttpParams params = httpClient.getParams();
		httpClient = new DefaultHttpClient(pool, params);
	}
	
	public DefaultHttpClient get() {
		return httpClient;
	}
}
