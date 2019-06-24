package com.wowsanta.scim.client;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.wowsanta.scim.exception.SCIMError;
import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.json.AbstractJsonObject;
import com.wowsanta.scim.json.SCIMJsonObject;
import com.wowsanta.scim.message.SCIMBulkRequest;
import com.wowsanta.scim.message.SCIMBulkResponse;
import com.wowsanta.scim.message.SCIMFindRequest;
import com.wowsanta.scim.message.SCIMListResponse;
import com.wowsanta.scim.obj.SCIMUser;
import com.wowsanta.scim.protocol.ClientReponse;
import com.wowsanta.scim.protocol.ClientRequest;
import com.wowsanta.scim.protocol.JsonRequest;
import com.wowsanta.scim.resource.user.LoginUser;
import com.wowsanta.scim.resource.worker.Worker;
import com.wowsanta.scim.schema.SCIMConstants;
import com.wowsanta.scim.sec.SCIMJWTToken;


public class RESTClient {
	
	Logger logger = LoggerFactory.getLogger(RESTClient.class);
	private Worker worker;
	
	public RESTClient(Worker worker) {
		this.worker = worker;
	}
	
	
	public ClientReponse get(String url) throws ClientException{
		ClientReponse response = null;
		try {
			HttpGet get = new HttpGet(url);
			get.addHeader("Content-Type", "application/json;UTF-8");
			get.addHeader("Accept-Charset", "UTF-8");
			get.addHeader("Authorization", RESTClientPool.getInstance().generateAuthorizationToken(worker, 1000*60));
			
			HttpResponse http_response = execute(get);
			int http_res_code = http_response.getStatusLine().getStatusCode();
			StringBuilder buffer = new StringBuilder();
			if( http_res_code >= SCIMConstants.HtppConstants.OK && http_res_code <= SCIMConstants.HtppConstants.IM_Used) {
				HttpEntity entity = http_response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(content));
				String line;
				while ((line = reader.readLine()) != null) {
					buffer.append(line);
				}
				response = ClientReponse.parse(buffer.toString());
			}else {
				throw new ClientException("HTTP ERROR : " + http_res_code + url );
			}
		}catch (Exception e) {
			throw new ClientException("HTTP ERROR : " + url, e );
		} 
		return response;
	}
	
	
	public SCIMListResponse post_find(String url, SCIMFindRequest request, String encoding) throws ClientException{
		SCIMListResponse response = null;
		try {
			HttpPost post = new HttpPost(url);
			post.addHeader("Content-Type", "application/json;");
			if(worker != null) {
				post.addHeader("Authorization", RESTClientPool.getInstance().generateAuthorizationToken(worker, 1000*60));
			}
			
			post.setEntity(new StringEntity(request.toString(false)));
			HttpResponse http_response = execute(post);
			int http_res_code = http_response.getStatusLine().getStatusCode();
			StringBuilder buffer = new StringBuilder();
			if( http_res_code >= SCIMConstants.HtppConstants.OK && http_res_code <= SCIMConstants.HtppConstants.IM_Used) {
				try {
					HttpEntity entity = http_response.getEntity();
					InputStream content = entity.getContent();
					BufferedReader reader = new BufferedReader(new InputStreamReader(content,encoding));
					String line;
					while ((line = reader.readLine()) != null) {
						buffer.append(line);
					}
				} catch (Exception e) {
					logger.error("response read error : {}, ", e.getMessage(), e);
					throw new SCIMException("RESULT READ ERROR", e);
				}
				response = SCIMListResponse.parse(buffer.toString());
			}else {
				logger.error("response string : {}, ", EntityUtils.toString(http_response.getEntity()));
				throw new SCIMException("HTTP ERROR : " + url, SCIMError.InternalServerError);
			}
			
		} catch (Exception e) {
			logger.error("{}",e.getMessage(),e);
			throw new ClientException("POST FAILED : " + url, e);
		}
		
		return response;
	}
	public SCIMBulkResponse post_bulk(String url,SCIMBulkRequest request, String encoding ) throws SCIMException {
		SCIMBulkResponse response = null;
		
		try {
			HttpPost post = new HttpPost(url);
			post.addHeader("Content-Type", "application/json;UTF-8");
			if(worker != null) {
				post.addHeader("Authorization", RESTClientPool.getInstance().generateAuthorizationToken(worker, 1000*60));
			}
			
			post.setEntity(new StringEntity(request.toString(false),encoding));
			HttpResponse http_response = execute(post);
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
				} catch (Exception e) {
					logger.error("response read error : {}, ", e.getMessage(), e);
					throw new SCIMException("RESULT READ ERROR", e);
				}
				response = SCIMBulkResponse.parse(buffer.toString());
			}else {
				logger.error("response string : {}, ", EntityUtils.toString(http_response.getEntity()));
				throw new SCIMException("HTTP ERROR : " + url, SCIMError.InternalServerError);
			}
			
		} catch (Exception e) {
			throw new SCIMException("POST FAILED : " + url,SCIMError.noTarget, e);
		}
		
		return response;
	}
	

	public ClientReponse post(String url, ClientRequest client_request) throws SCIMException {
		try {
			HttpPost post = new HttpPost(url);
			post.addHeader("Content-Type", "application/json");
			if(worker != null) {
				post.addHeader("Authorization", RESTClientPool.getInstance().generateAuthorizationToken(worker, 1000*60));
			}
			post.setEntity(new StringEntity(client_request.toString()));
			
			HttpResponse http_response = execute(post);
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
				} catch (Exception e) {
					logger.error("response read error : {}, ", e.getMessage(), e);
					throw new SCIMException("RESULT READ ERROR", e);
				}
				
				Gson gson = new GsonBuilder().disableHtmlEscaping().create();
				return gson.fromJson(buffer.toString(), ClientReponse.class); 
			}else {
				logger.error("response string : {}, ", EntityUtils.toString(http_response.getEntity()));
				throw new SCIMException("HTTP ERROR : " + url, SCIMError.InternalServerError);
			}
		} catch (Exception e) {
			logger.error("{}, ", e.getMessage(),e);
			throw new SCIMException("HTTP ERROR : " + url, SCIMError.InternalServerError);
		}
	}
	
	public HttpResponse post(String url, JsonRequest request) throws SCIMException {
		try {
			HttpPost post = new HttpPost(url);
			post.addHeader("Content-Type", "application/json;UTF-8");
			if(worker != null) {
				post.addHeader("Authorization", RESTClientPool.getInstance().generateAuthorizationToken(worker, 1000*60));
			}

			post.setEntity(new StringEntity(request.toString(false),"UTF-8"));
			return execute(post);
			
		} catch (Exception e) {
			throw new SCIMException("POST FAILED : " + url,SCIMError.noTarget, e);
		}
	}
	
	public <T> T post(String url, JsonRequest request, Class<T> classOfT, String encode) throws SCIMException {
		try {
			HttpPost post = new HttpPost(url);
			post.addHeader("Content-Type", "application/json");
			if(worker != null) {
				post.addHeader("Authorization", RESTClientPool.getInstance().generateAuthorizationToken(worker, 1000*60));
			}
			post.setEntity(new StringEntity(request.toString(),encode));
			
			HttpResponse http_response = execute(post);
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
				} catch (Exception e) {
					logger.error("response read error : {}, ", e.getMessage(), e);
					throw new SCIMException("RESULT READ ERROR", e);
				}
				
				
				Gson gson = new GsonBuilder().disableHtmlEscaping().create();
				return gson.fromJson(buffer.toString(), classOfT); 
			}else {
				logger.error("response string : {}, ", EntityUtils.toString(http_response.getEntity()));
				throw new SCIMException("HTTP ERROR : " + url, SCIMError.InternalServerError);
			}
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
			
			post.setEntity(new StringEntity(request.toString(),"UTF-8"));
			return execute(post);
			
		} catch (Exception e) {
			throw new SCIMException("POST FAILED : " + url,SCIMError.noTarget, e);
		}
	}
//	public String post2(String url, SCIMJsonObject request) throws SCIMException {
//		try {
//			HttpResponse http_response = post(url, request );
//			int http_res_code = http_response.getStatusLine().getStatusCode();
//			StringBuilder buffer = new StringBuilder();
//			if( http_res_code >= SCIMConstants.HtppConstants.OK && http_res_code <= SCIMConstants.HtppConstants.IM_Used) {
//				try {
//					HttpEntity entity = http_response.getEntity();
//					InputStream content = entity.getContent();
//					BufferedReader reader = new BufferedReader(new InputStreamReader(content));
//					String line;
//					while ((line = reader.readLine()) != null) {
//						buffer.append(line);
//					}
//					return buffer.toString();
//				} catch (Exception e) {
//					e.printStackTrace();
//					throw new SCIMException("RESULT READ ERROR", e);
//				} 
//			}else {
//				throw new SCIMException("HTTP ERROR : " + url, SCIMError.InternalServerError);
//			}
//			
//		} catch (Exception e) {
//			throw new SCIMException("POST FAILED : " + url, SCIMError.InternalServerError, e);
//		}
//	}
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
			
			String token = RESTClientPool.getInstance().generateAuthorizationToken(worker, 1000*60);
			
			SCIMJWTToken scim_wt_token = new SCIMJWTToken();
			LoginUser login_user = scim_wt_token.verify(token);
			
			logger.info("patch token user : {}", login_user);
			
			post.addHeader("Authorization", token );
			
			HttpResponse response = execute(post);
			
			
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
