package com.wowsanta.scim.client;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.obj.SCIMUser;
import com.wowsanta.scim.sec.SCIMJWTToken;

public class RESTClientPool {
	private static class Singleton {
		private static final RESTClientPool instance = new RESTClientPool();
	}

	public static synchronized RESTClientPool getInstance() {
		return Singleton.instance;
	}
	
	
	private PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
	private CloseableHttpClient client;
	
	private SCIMUser user;
	private SCIMJWTToken scim_wt_token = new SCIMJWTToken();
	
	
	public RESTClientPool() {
		connectionManager.setMaxTotal(100);		
		connectionManager.setDefaultMaxPerRoute(10);
		this.client = HttpClients.custom().setConnectionManager(connectionManager).build();
	}
	
	public HttpClient get() {
		return this.client;
	}
	
	public String generateAuthorizationToken(SCIMUser user, int time) throws SCIMException {
		SCIMJWTToken scim_wt_token = new SCIMJWTToken();
		
		scim_wt_token.setUserId(user.getId());
		scim_wt_token.setUserName(user.getUserName());
		scim_wt_token.setExpireTime(time);
		
		
		return scim_wt_token.issue();
	}
	
	public String generateAuthorizationToken() throws SCIMException {
		return this.scim_wt_token.issue();
	}
	public  void setUserInfo(String userId, String userName, int time) {
		scim_wt_token.setUserId(userId);
		scim_wt_token.setUserName(userName);
		scim_wt_token.setExpireTime(time);
	}
	
	public void setUser(SCIMUser user) {
		this.user = user;
	}
	public SCIMUser getUser() {
		return this.user;
	}
}

