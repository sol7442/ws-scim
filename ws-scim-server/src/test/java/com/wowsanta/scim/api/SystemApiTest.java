package com.wowsanta.scim.api;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import com.wowsanta.scim.client.RESTClient;
import com.wowsanta.scim.client.RESTClientPool;
import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.schema.SCIMConstants;


public class SystemApiTest {

	static {
		RESTClientPool.getInstance().setUserInfo("sys-scim-admin","scim-admin",60*1000);
	}
	
	@Test
	public void get_all_test() {
		get_all();
	}
	
	public void get_all() {
		RESTClientPool.getInstance().setUserInfo("sys-scim-admin","scim-admin",60*1000);
		RESTClient client = new RESTClient();
		final String url = "http://localhost:5000/system/";  
		try {
			HttpResponse response = client.get(url);
			if( response.getStatusLine().getStatusCode() >= SCIMConstants.HtppConstants.OK && response.getStatusLine().getStatusCode() <= SCIMConstants.HtppConstants.IM_Used) {
				try {
					System.out.println(EntityUtils.toString(response.getEntity()));
				} catch (ParseException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (SCIMException e) {
			e.printStackTrace();
		}
	}
}
