package com.wowsanta.scim.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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
import com.wowsanta.scim.obj.SCIMUser;
import com.wowsanta.scim.schema.SCIMConstants;

public class RESTClient {
	private final SCIMUser user;
	
	public RESTClient(SCIMUser user) {
		this.user = user;
	}

	public SCIMBulkResponse bulk(String url,SCIMBulkRequest request) throws SCIMException {
		SCIMBulkResponse response = new SCIMBulkResponse();
		
		HttpResponse http_response = post(url, request);
		int http_res_code = http_response.getStatusLine().getStatusCode();
		if( http_res_code >= SCIMConstants.HtppConstants.OK && http_res_code <= SCIMConstants.HtppConstants.IM_Used) {
			try {
				response.parse(EntityUtils.toString(http_response.getEntity()));
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
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

		return response;
	}
	public HttpResponse post(String url, SCIMJsonObject request) throws SCIMException {
		try {
			HttpPost post = new HttpPost(url);
			post.addHeader("Content-Type", "application/json;UTF-8");
			post.addHeader("Authorization", RESTClientPool.getInstance().generateAuthorizationToken(user, 1000*60));


			post.setEntity(new StringEntity(request.toString(),"UTF-8"));
			return execute(post);
			
		} catch (Exception e) {
			throw new SCIMException("POST FAILED : " + url, e);
		}
	}
	
	public HttpResponse post(String url) throws SCIMException {
		try {
			HttpPost post = new HttpPost(url);
			post.addHeader("Content-Type", "application/json;UTF-8");
			post.addHeader("Authorization", RESTClientPool.getInstance().generateAuthorizationToken(user, 1000*60));
			//post.setEntity(new StringEntity(request.toString(),"UTF-8"));
			return execute(post);
			
		} catch (Exception e) {
			throw new SCIMException("POST FAILED : " + url, e);
		}
	}

	public String call(String url) throws SCIMException {
		HttpResponse http_response = get(url);
		int http_res_code = http_response.getStatusLine().getStatusCode();
		
		StringBuilder str = new StringBuilder();
		
		if( http_res_code >= SCIMConstants.HtppConstants.OK && http_res_code <= SCIMConstants.HtppConstants.IM_Used) {
			try {
				HttpEntity entity = http_response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(content));
				String line;
				while ((line = reader.readLine()) != null) {
					str.append(line);
				}
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			throw new SCIMException("call error : ", http_res_code);
		}
		
		return str.toString();
	}
	public String run(String url) throws SCIMException {
		HttpResponse http_response = post(url);
		int http_res_code = http_response.getStatusLine().getStatusCode();
		
		StringBuilder str = new StringBuilder();
		
		if( http_res_code >= SCIMConstants.HtppConstants.OK && http_res_code <= SCIMConstants.HtppConstants.IM_Used) {
			try {
				HttpEntity entity = http_response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(content));
				String line;
				while ((line = reader.readLine()) != null) {
					str.append(line);
				}
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			throw new SCIMException("call error : ", http_res_code);
		}
		
		return str.toString();
	}
	public HttpResponse get(String url) throws SCIMException {
		try {
			
			HttpGet get = new HttpGet(url);
			get.addHeader("Content-Type", "application/json;UTF-8");
			get.addHeader("Accept-Charset", "UTF-8");
			get.addHeader("Authorization", RESTClientPool.getInstance().generateAuthorizationToken(user, 1000*60));
			
			return execute(get);
			
		} catch (Exception e) {
			throw new SCIMException("CALL FAILED : " + url, e);
		}
		
		
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
