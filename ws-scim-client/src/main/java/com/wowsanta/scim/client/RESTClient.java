package com.wowsanta.scim.client;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.json.SCIMJsonObject;
import com.wowsanta.scim.message.SCIMBulkRequest;
import com.wowsanta.scim.message.SCIMBulkResponse;
import com.wowsanta.scim.schema.SCIMConstants;

public class RESTClient {
	public RESTClient() {
	}

	public SCIMBulkResponse bulk(String url,SCIMBulkRequest request) throws SCIMException {
		SCIMBulkResponse response = new SCIMBulkResponse();
		
		HttpResponse http_response = post(url, request);
		int http_res_code = http_response.getStatusLine().getStatusCode();
		if( http_res_code >= SCIMConstants.HtppConstants.OK && http_res_code <= SCIMConstants.HtppConstants.IM_Used) {
			System.out.println("==================");
			try {
				response.parse(EntityUtils.toString(http_response.getEntity()));
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("==================");
		}else {
			try {
				System.out.println("================== : " + http_res_code);
				System.out.println(EntityUtils.toString(http_response.getEntity()));
				System.out.println("================== : " + http_res_code);
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		///response.parse(result_entity.toString());
		
		return response;
	}
	public HttpResponse post(String url, SCIMJsonObject request) throws SCIMException {
		try {
			HttpPost post = new HttpPost(url);
			post.addHeader("Content-Type", "application/json");
			post.addHeader("Authorization", RESTClientPool.getInstance().generateAuthorizationToken());
			
			post.setEntity(new StringEntity(request.toString()));
			return execute(post);
			
		} catch (Exception e) {
			throw new SCIMException("POST FAILED : " + url, e);
		}
	}

	public void get(String url) throws SCIMException {
		HttpGet get = new HttpGet(url);
		get.addHeader("Content-Type", "application/json");

		execute(get);
//		try {
//			EntityUtils.toString(result_entity);
//		} catch (ParseException | IOException e) {
//			e.printStackTrace();
//		}

	}

	public void put(String url, SCIMJsonObject request) throws SCIMException {

		HttpPut get = new HttpPut(url);
		get.addHeader("Content-Type", "application/json");

		execute(get);
//		try {
//			EntityUtils.toString(result_entity);
//		} catch (ParseException | IOException e) {
//			e.printStackTrace();
//		}

	}

	public void delete(String url, SCIMJsonObject request) throws SCIMException {
		HttpDelete del = new HttpDelete(url);
		del.addHeader("Content-Type", "application/json");

		execute(del);
//		try {
//			EntityUtils.toString(result_entity);
//		} catch (ParseException | IOException e) {
//			e.printStackTrace();
//		}
	}

	public void patch(String url, SCIMJsonObject request) throws SCIMException {
//		HttpPatch pat = new HttpPatch(url);
//		pat.addHeader("Content-Type", "application/json");
//		
//		HttpResponse response = execute(pat);
//		
//		HttpEntity result_entity = 
//		try {
//			EntityUtils.toString(result_entity);
//		} catch (ParseException | IOException e) {
//			e.printStackTrace();
//		}
	}

	private HttpResponse execute(HttpRequestBase base) throws SCIMException {
		try {
			return RESTClientPool.getInstance().get().execute(base);
		} catch (Exception e) {
			throw new SCIMException("HTTP Execute  FAILED : " + base.getURI(), e);
		}
	}
}
