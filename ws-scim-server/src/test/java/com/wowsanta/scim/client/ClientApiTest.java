package com.wowsanta.scim.client;

import org.junit.Test;

import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.message.SCIMBulkRequest;
import com.wowsanta.scim.message.SCIMBulkResponse;
import com.wowsanta.scim.schema.SCIMConstants;



public class ClientApiTest {

	private static final String url = "http://localhost:5000/scim";
	
	@Test
	public void put_bulk_test() {
		System.out.println("put bulk request >>>");

		RESTClientPool.getInstance().setUserInfo("sys-scim-admin","scim-admin",60*1000);
		
		String bulk_url = url + SCIMConstants.VERSION_ENDPINT + SCIMConstants.BULK_ENDPOINT;
		SCIMBulkRequest request = create_bulk_request();
		RESTClient client = new RESTClient();
		
		try {
			System.out.println(">>>>");
			System.out.println(request.toString(true));
			System.out.println(">>>>");
			
			SCIMBulkResponse response = client.bulk(bulk_url, request);
			System.out.println(response.toString(true));
		} catch (SCIMException e) {
			e.printStackTrace();
		}
	}
}
