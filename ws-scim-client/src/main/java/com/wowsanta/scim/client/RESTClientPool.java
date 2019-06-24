package com.wowsanta.scim.client;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.client.HttpClient;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;

import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.obj.SCIMUser;
import com.wowsanta.scim.resource.worker.Worker;
import com.wowsanta.scim.sec.SCIMJWTToken;

public class RESTClientPool {
	private static class Singleton {
		private static final RESTClientPool instance = new RESTClientPool();
	}

	public static synchronized RESTClientPool getInstance() {
		return Singleton.instance;
	}
	
	
//	private PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
	private CloseableHttpClient client = null;
	
	private SCIMUser user;
	private SCIMJWTToken scim_wt_token = new SCIMJWTToken();
	
	
	public RESTClientPool() {

	}
	
	public HttpClient get() {
		if(this.client == null) {
			build();
		}
		return this.client;
	}
	
	private void build() {
	    try {
	    	
	    	SSLContextBuilder builder = new SSLContextBuilder();
	    	builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
	    	SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(builder.build(),SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
	    	Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
	    	        .register("http", new PlainConnectionSocketFactory())
	    	        .register("https", sslsf)
	    	        .build();
	    	
	    	PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(registry);
	    	cm.setMaxTotal(2000);

	    	this.client = HttpClients.custom()
	    	    .setSSLSocketFactory(sslsf)
	    	    .setConnectionManager(cm)
	    	    .build();
	        
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		}
	}
	public String generateAuthorizationToken(SCIMUser user, int time) throws SCIMException {
		SCIMJWTToken scim_wt_token = new SCIMJWTToken();
		
		scim_wt_token.setUserId(user.getId());
		scim_wt_token.setUserName(user.getUserName());
		scim_wt_token.setExpireTime(time);
		
		
		return scim_wt_token.issue();
	}
	
	public String generateAuthorizationToken(Worker worker, int time) throws SCIMException {
		SCIMJWTToken scim_wt_token = new SCIMJWTToken();
		
		scim_wt_token.setUserId(worker.getWorkerId());
		scim_wt_token.setUserType(worker.getWorkerType());
		//scim_wt_token.setUserName(worker.get.getUserName());
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

	public void close() {
//		try {
//			this.client.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}finally {
//			this.client = null;
//		}
	}
}

