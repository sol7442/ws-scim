package com.wowsanta.scim.client;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.util.Collection;
import java.util.Map;
import java.util.Set;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;
import com.wowsanta.scim.exception.SCIMError;
import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.json.AbstractJsonObject;
import com.wowsanta.scim.json.SCIMJsonObject;
import com.wowsanta.scim.message.SCIMBulkRequest;
import com.wowsanta.scim.message.SCIMBulkResponse;
import com.wowsanta.scim.obj.SCIMUser;
import com.wowsanta.scim.resource.worker.Worker;
import com.wowsanta.scim.schema.SCIMConstants;


public class RESTClient {
	
	Logger logger = LoggerFactory.getLogger(RESTClient.class);
	
//	private SCIMUser user;
	private Worker worker;
	
//	public RESTClient(SCIMUser user) {
//		this.user = user;
//	}
	
	public RESTClient(Worker worker) {
		this.worker = worker;
	}

	public SCIMBulkResponse post_bulk(String url,SCIMBulkRequest request) throws SCIMException {
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
			if(worker != null) {
				post.addHeader("Authorization", RESTClientPool.getInstance().generateAuthorizationToken(worker, 1000*60));
			}

			post.setEntity(new StringEntity(request.toString(),"UTF-8"));
			return execute(post);
			
		} catch (Exception e) {
			throw new SCIMException("POST FAILED : " + url,SCIMError.noTarget, e);
		}
	}
	public HttpResponse post(String url, AbstractJsonObject request) throws SCIMException {
		try {
			HttpPost post = new HttpPost(url);
			post.addHeader("Content-Type", "application/json;UTF-8");
			if(worker != null) {
				post.addHeader("Authorization", RESTClientPool.getInstance().generateAuthorizationToken(worker, 1000*60));
			}

			System.out.println("request data : " + request.toString()); 
			
			post.setEntity(new StringEntity(request.toString(),"UTF-8"));
			return execute(post);
			
		} catch (Exception e) {
			throw new SCIMException("POST FAILED : " + url,SCIMError.noTarget, e);
		}
	}
	public String post2(String url, SCIMJsonObject request) throws SCIMException {
		try {
			HttpResponse http_response = post(url,request );
			int http_res_code = http_response.getStatusLine().getStatusCode();
			StringBuilder buffer = new StringBuilder();
			if( http_res_code >= SCIMConstants.HtppConstants.OK && http_res_code <= SCIMConstants.HtppConstants.IM_Used) {
				try {
					HttpEntity entity = http_response.getEntity();
					InputStream content = entity.getContent();
					BufferedReader reader = new BufferedReader(new InputStreamReader(content));
					String line;
					while ((line = reader.readLine()) != null) {
						buffer.append(line);
					}
					return buffer.toString();
				} catch (Exception e) {
					e.printStackTrace();
					throw new SCIMException("RESULT READ ERROR", e);
				} 
			}else {
				throw new SCIMException("HTTP ERROR : " + url, SCIMError.InternalServerError);
			}
			
		} catch (Exception e) {
			throw new SCIMException("POST FAILED : " + url, SCIMError.InternalServerError, e);
		}
	}
	public String post2(String url, AbstractJsonObject request) throws SCIMException {
		try {
			HttpResponse http_response = post(url,request );
			int http_res_code = http_response.getStatusLine().getStatusCode();
			StringBuilder buffer = new StringBuilder();
			if( http_res_code >= SCIMConstants.HtppConstants.OK && http_res_code <= SCIMConstants.HtppConstants.IM_Used) {
				try {
					HttpEntity entity = http_response.getEntity();
					InputStream content = entity.getContent();
					BufferedReader reader = new BufferedReader(new InputStreamReader(content));
					String line;
					while ((line = reader.readLine()) != null) {
						buffer.append(line);
					}
					return buffer.toString();
				} catch (Exception e) {
					e.printStackTrace();
					throw new SCIMException("RESULT READ ERROR", e);
				} 
			}else {
				throw new SCIMException("HTTP ERROR : " + url, SCIMError.InternalServerError);
			}
			
		} catch (Exception e) {
			throw new SCIMException("POST FAILED : " + url, SCIMError.InternalServerError, e);
		}
	}
	public String post2(String url, JsonObject request) throws SCIMException {
		try {
			HttpResponse http_response = post(url,request );
			int http_res_code = http_response.getStatusLine().getStatusCode();
			StringBuilder buffer = new StringBuilder();
			if( http_res_code >= SCIMConstants.HtppConstants.OK && http_res_code <= SCIMConstants.HtppConstants.IM_Used) {
				try {
					HttpEntity entity = http_response.getEntity();
					InputStream content = entity.getContent();
					BufferedReader reader = new BufferedReader(new InputStreamReader(content));
					String line;
					while ((line = reader.readLine()) != null) {
						buffer.append(line);
					}
					return buffer.toString();
				} catch (Exception e) {
					e.printStackTrace();
					throw new SCIMException("RESULT READ ERROR", e);
				} 
			}else {
				throw new SCIMException("HTTP ERROR : " + url, SCIMError.InternalServerError);
			}
			
		} catch (Exception e) {
			throw new SCIMException("POST FAILED : " + url, SCIMError.InternalServerError, e);
		}
	}
	
	public HttpResponse post(String url,JsonObject object) throws SCIMException {
		try {
			HttpPost post = new HttpPost(url);
			StringEntity postingString = new StringEntity(object.toString(),"UTF-8");
			
			post.addHeader("Content-Type", "application/json;UTF-8");
			post.addHeader("Authorization", RESTClientPool.getInstance().generateAuthorizationToken(worker, 1000*60));
			post.setEntity(postingString);
			
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
			System.out.println(http_response);
			throw new SCIMException("call error : ");
		}
		
		return str.toString();
	}
	public String run(String url,JsonObject params) throws SCIMException {
		
		HttpResponse http_response = post(url,params);
		
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
			throw new SCIMException("call error : ");
		}
		
		return str.toString();
	}
	public HttpResponse get(String url) throws SCIMException {
		try {
			
			HttpGet get = new HttpGet(url);
			get.addHeader("Content-Type", "application/json;UTF-8");
			get.addHeader("Accept-Charset", "UTF-8");
			get.addHeader("Authorization", RESTClientPool.getInstance().generateAuthorizationToken(worker, 1000*60));
			
			return execute(get);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new SCIMException("CALL FAILED : " + url, SCIMError.InternalServerError,e);
		}
	}
	
	public String get2(String url) throws SCIMException{
		
		HttpResponse http_response = get(url);
		int http_res_code = http_response.getStatusLine().getStatusCode();
		StringBuilder buffer = new StringBuilder();
		if( http_res_code >= SCIMConstants.HtppConstants.OK && http_res_code <= SCIMConstants.HtppConstants.IM_Used) {
			try {
				HttpEntity entity = http_response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(content));
				String line;
				while ((line = reader.readLine()) != null) {
					buffer.append(line);
				}
				return buffer.toString();
			} catch (Exception e) {
				e.printStackTrace();
				throw new SCIMException("RESULT READ ERROR", e);
			} 
		}else {
			throw new SCIMException("HTTP ERROR : " + url, SCIMError.InternalServerError);
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

//	public void patch(String url, SCIMJsonObject request) throws SCIMException {
////		HttpPatch pat = new HttpPatch(url);
////		pat.addHeader("Content-Type", "application/json");
////		
////		HttpResponse response = execute(pat);
////		
////		HttpEntity result_entity = 
////		try {
////			EntityUtils.toString(result_entity);
////		} catch (ParseException | IOException e) {
////			e.printStackTrace();
////		}
//	}

	private HttpResponse execute(HttpRequestBase base) throws SCIMException {
		try {
			return RESTClientPool.getInstance().get().execute(base);
		} catch (Exception e) {
			close();
			throw new SCIMException("HTTP Execute  FAILED : " + base.getURI(), e);
		}
	}

	public String post(String url, File[] libray_file) {
		
		try {
		
			HttpPost post = new HttpPost(url);
			MultipartEntityBuilder meb = MultipartEntityBuilder.create();
			for (File file : libray_file) {
				meb.addBinaryBody(file.getName(), file);
			}
			meb.setContentType(ContentType.MULTIPART_FORM_DATA);
			post.setEntity(meb.build());
			post.addHeader("Authorization", RESTClientPool.getInstance().generateAuthorizationToken(worker, 1000*60));
			
			HttpResponse response = execute(post);
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return "";
	}

	
	public void patch(String patch_url, File[] libray_file) throws SCIMException{
		try {
			
			HttpPatch post = new HttpPatch(patch_url);
			MultipartEntityBuilder meb = MultipartEntityBuilder.create();
			for (File file : libray_file) {
				meb.addBinaryBody(file.getName(), file);
			}
			meb.setContentType(ContentType.MULTIPART_FORM_DATA);
			post.setEntity(meb.build());
			post.addHeader("Authorization", RESTClientPool.getInstance().generateAuthorizationToken(worker, 1000*60));
			
			HttpResponse response = execute(post);
			
			System.out.println(response.toString());
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void sendFile(String post_url, Map<String, byte[]> buffer_map) throws ClientException{
		try {

			MultipartEntityBuilder meb = MultipartEntityBuilder.create();
			Set<String> buffer_keys = buffer_map.keySet();
			for (String fileName : buffer_keys) {
				meb.addBinaryBody(fileName, buffer_map.get(fileName));
			}
			meb.setContentType(ContentType.MULTIPART_FORM_DATA);
			
			HttpPost post = new HttpPost(post_url);
			post.setEntity(meb.build());
			post.addHeader("Authorization", RESTClientPool.getInstance().generateAuthorizationToken(worker, 1000*60));
			
			HttpResponse response = execute(post);
			
			logger.info("send file result : {} ",response.toString());
			
		}catch (Exception e) {
			e.printStackTrace();
			throw new ClientException("send file failed ",e);
		}
		
	}
	

	public HttpResponse getFile(String url) throws ClientException{
		try {
			
			//URI urlObject = new URI(url);
			HttpGet get = new HttpGet(url);
			get.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
			get.addHeader("Accept-Charset", "UTF-8");
			get.addHeader("Authorization", RESTClientPool.getInstance().generateAuthorizationToken(worker, 1000*60));
			
			return execute(get);

		} catch (Exception e) {
			e.printStackTrace();
			throw new ClientException("FAILED : " + url, e);
		}
	}

	public void close() {
		RESTClientPool.getInstance().close();
	}





}
