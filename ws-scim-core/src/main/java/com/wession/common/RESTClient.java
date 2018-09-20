package com.wession.common;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.Iterator;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

public class RESTClient {
	
	public final static String SOCKET_TIME_OUT = "http.socket.timeout";
	public final static String CONNECTION_TIME_OUT = "http.connection.timeout";
	public final static String DEFAULT_ENCODING = "UTF-8";
	
	public final static int SOCKET_TIME_OUT_GET_MS = 2 * 60 * 1000;
	public final static int SOCKET_TIME_OUT_POST_MS = 5 * 60 * 1000;
	public final static int CONNECTION_TIME_OUT_MS = 2 * 1000;
	
	public static String TOKEN = "Bearer ".concat("eyJ0eXAiOiJKV1QiLCJpc3N1ZURhdGUiOjE0OTYxOTk5NjEyMzMsImFsZyI6IkhTMjU2In0.eyJzdWIiOiJTQ0lNL1VTRVIiLCJpc3MiOiJTQ0lNIiwiaWF0IjoxNDk2MTk5OTYxLCJleHAiOjE1Mjc3MzU5NjEsInByb3ZpZGVyIjoidHJ1ZSIsInNlcnZlcklwIjoiMTI3LjAuMC4xIiwiY29scGFydCI6IkEwMDEiLCJhcHBDb2RlIjoiRGVtb0hSIn0.utQJYMdIRCJUMM5On3-G6Ay0mKBZ2aWnLKvcQ68kCl0");
	
	public RESTClient() {
		// Token을 service provider에서 불러와서 붙여야 함
		System.out.println("RESTClient:\n");
	}
	
	public void setToken() {
		// token 세팅 후 바로 검증을 해서 문제여부를 확인해야 함
		// 정상적으로 token이 없다면 동작하게 해서는 안됨
	}
	
	public static void main(String[] args) {
		
		RESTClient rest = new RESTClient();
		String params = "userName co \"김철호\"";
		
		//file upload
		try {
			rest.fileUploadTest();
			if(true) {
				return;
			}
		} catch (ClientProtocolException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		//SPARK.BEFORE TEST
		JSONObject beforejson = rest.get("http://localhost:5000/scim/v2.0/Users");
		System.out.println("result:\n"+beforejson.toJSONString());
		
		// GET & DELETE TEST (Find & Delete User)
		JSONObject json = rest.get("http://localhost:5000/scim/v2.0/Users?filter=" + URLEncoder.encode(params));
		JSONArray resource = (JSONArray) json.get("Resources");
		
		if(resource != null && !resource.isEmpty()) {
			Iterator itor = resource.iterator();
			while (itor.hasNext()) {
				JSONObject user = (JSONObject) itor.next();
				String id = user.getAsString("id");
				System.out.println("id:" + user.getAsString("id"));
				System.out.println("userName:" + user.getAsString("userName"));
				JSONObject json_delete = rest.delete("http://localhost:5000/scim/v2.0/Users/" + id);
				System.out.println(json_delete.toJSONString());
			}
		}

		// POST TEST (Create User)
		String URL = "http://localhost:5000/scim/v2.0/Users";

		try {
			String file_path = "./sample/postUser.json";
			//String file_path = "./sample/postUserGroup.json";
			JSONObject obj = (JSONObject) JSONValue.parse(new FileReader(file_path));
			JSONObject pjson = rest.post(URL, obj.toJSONString());
			if (pjson != null && !pjson.isEmpty()) {
				System.out.println("POST get JSONObject >");
				System.out.println(pjson.toJSONString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public void fileUploadTest() throws ClientProtocolException, IOException {
		String URL = "http://localhost:5000/test/upload";
		
		File file = new File("./sample/syncResult.log");
		StringBody stringBody = new StringBody("sendSyncLog", ContentType.MULTIPART_FORM_DATA);
		StringBody stringBody2 = new StringBody("한글", ContentType.APPLICATION_JSON);

//		ContentBody contentBody = new FileBody(file, ContentType.DEFAULT_BINARY, URLEncoder.encode(file.getName(), DEFAULT_ENCODING));
		ContentBody contentBody = new FileBody(file, ContentType.MULTIPART_FORM_DATA, URLEncoder.encode(file.getName(), DEFAULT_ENCODING));
		
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		builder.addPart("sendSyncLog", contentBody);
		builder.addPart("key", stringBody);
		builder.addTextBody("test1", "한글");
		builder.addPart("test2", stringBody2);
//		builder.addPart("test3", contentBody);
		
		HttpEntity entity = builder.build();
		
		HttpResponse httpResponse = post(URL, file.toString(), entity);
		
		BufferedReader br = new BufferedReader(new InputStreamReader((httpResponse.getEntity().getContent())));
		
		String output;
		while ((output = br.readLine()) != null) {
			System.out.println(output);
		}
	}
	
	public HttpResponse post(String URL, String postBody, HttpEntity entity) {
		HttpClient httpClient = RESTClientPool.getInstance().get();
		
//		StringBuffer sb = new StringBuffer();
//		JSONObject json = new JSONObject();
		HttpResponse httpResponse = null;
		try {
			HttpPost httpPostRequest = new HttpPost(URL);
			httpPostRequest.addHeader("Authorization", TOKEN);
			httpPostRequest.setEntity(entity);
			httpResponse = httpClient.execute(httpPostRequest);
			
//			BufferedReader br = new BufferedReader(new InputStreamReader((httpResponse.getEntity().getContent())));
//			
//			String output;
//			while ((output = br.readLine()) != null) {
//				sb.append(output);
//			}
//			json = (JSONObject) JSONValue.parse(sb.toString());

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			httpClient.getConnectionManager().closeExpiredConnections();
		}

		return httpResponse;
	}
	
	public JSONObject get(String URL) {
//		System.out.println("GET : URL : " + URL);
//		HttpClient httpClient = new DefaultHttpClient();
		HttpClient httpClient = RESTClientPool.getInstance().get();
		httpClient.getParams().setIntParameter(SOCKET_TIME_OUT, SOCKET_TIME_OUT_POST_MS);
		httpClient.getParams().setIntParameter(CONNECTION_TIME_OUT, CONNECTION_TIME_OUT_MS);

		StringBuffer sb = new StringBuffer();

		try {
			HttpGet httpGetRequest = new HttpGet(URL);
			httpGetRequest.addHeader("Authorization", TOKEN);
			HttpResponse httpResponse = httpClient.execute(httpGetRequest);
			HttpEntity entity = httpResponse.getEntity();

			byte[] buffer = new byte[1024];

			if (entity != null) {
				InputStream inputStream = entity.getContent();
				try {
					int bytesRead = 0;
					BufferedInputStream bis = new BufferedInputStream(inputStream);
					while ((bytesRead = bis.read(buffer)) != -1) {
						String chunk = new String(buffer, 0, bytesRead);
						sb.append(chunk);
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						inputStream.close();
					} catch (Exception ignore) {
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			httpClient.getConnectionManager().closeExpiredConnections();
		}

		try {
			JSONObject json = (JSONObject) JSONValue.parse(sb.toString());
			return json;
		} catch (Exception e) {
			return new JSONObject();
		}
	}

	public JSONObject delete(String URL) {
//		HttpClient httpClient = new DefaultHttpClient();
		HttpClient httpClient = RESTClientPool.getInstance().get();
		httpClient.getParams().setIntParameter(SOCKET_TIME_OUT, SOCKET_TIME_OUT_POST_MS);
		httpClient.getParams().setIntParameter(CONNECTION_TIME_OUT, CONNECTION_TIME_OUT_MS);
		
		StringBuffer sb = new StringBuffer();

		try {
			HttpDelete httpDeleteRequest = new HttpDelete(URL);
			httpDeleteRequest.addHeader("Authorization", TOKEN);
			
			HttpResponse httpResponse = httpClient.execute(httpDeleteRequest);

			// delete를 보냈을 경우 정상이면 204가 반환됨
			if (httpResponse.getStatusLine().getStatusCode() == 204) {
				System.out.println("DELETED : " + URL);
			}

			HttpEntity entity = httpResponse.getEntity();
			byte[] buffer = new byte[1024];

			if (entity != null) {
				InputStream inputStream = entity.getContent();
				try {
					int bytesRead = 0;
					BufferedInputStream bis = new BufferedInputStream(inputStream);
					while ((bytesRead = bis.read(buffer)) != -1) {
						String chunk = new String(buffer, 0, bytesRead);
						sb.append(chunk);
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						inputStream.close();
					} catch (Exception ignore) {
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			httpClient.getConnectionManager().closeExpiredConnections();
		}
		
		try {
			JSONObject json = (JSONObject) JSONValue.parse(sb.toString());
			return json;
		} catch (Exception e) {
			return new JSONObject();
		}

	}

	@SuppressWarnings({ "resource", "deprecation" })
	public JSONObject post(String URL, String postBody) {
//		HttpClient httpClient = new DefaultHttpClient();
		HttpClient httpClient = RESTClientPool.getInstance().get();
		httpClient.getParams().setIntParameter(SOCKET_TIME_OUT, SOCKET_TIME_OUT_POST_MS);
		httpClient.getParams().setIntParameter(CONNECTION_TIME_OUT, CONNECTION_TIME_OUT_MS);
		
		StringBuffer sb = new StringBuffer();
		JSONObject json = new JSONObject();
		try {
			HttpPost httpPostRequest = new HttpPost(URL);
			httpPostRequest.addHeader("Authentication", TOKEN);
			HttpResponse httpResponse = null;

			StringEntity input = new StringEntity(postBody, "UTF-8");
			input.setContentType("application/json");

			httpPostRequest.setEntity(input);
			httpResponse = httpClient.execute(httpPostRequest);

			System.out.println("----------------------------------------");
			System.out.println(httpResponse.getStatusLine());
			System.out.println("----------------------------------------");
			
			BufferedReader br = new BufferedReader(new InputStreamReader((httpResponse.getEntity().getContent())));
			
			String output;
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				sb.append(output);
			}
			System.out.println(sb.toString());
			json = (JSONObject) JSONValue.parse(sb.toString());

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			httpClient.getConnectionManager().closeExpiredConnections();
		}

		return json;
	}
	
	public HttpResponse httpPost(String URL, String postBody) {
//		HttpClient httpClient = new DefaultHttpClient();
		HttpClient httpClient = RESTClientPool.getInstance().get();
		httpClient.getParams().setIntParameter(SOCKET_TIME_OUT, SOCKET_TIME_OUT_POST_MS);
		httpClient.getParams().setIntParameter(CONNECTION_TIME_OUT, CONNECTION_TIME_OUT_MS);
		
		StringBuffer sb = new StringBuffer();
		JSONObject json = new JSONObject();
		HttpResponse httpResponse = null;
		
		try {
			HttpPost httpPostRequest = new HttpPost(URL);
			httpPostRequest.addHeader("Authorization", TOKEN);

			StringEntity input = new StringEntity(postBody, "UTF-8");
			input.setContentType("application/json");

			httpPostRequest.setEntity(input);
			httpResponse = httpClient.execute(httpPostRequest);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			httpClient.getConnectionManager().closeExpiredConnections();
		}

		return httpResponse;
	}
	
	@SuppressWarnings({ "resource", "deprecation" })
	public JSONObject put(String URL, String postBody) {
//		HttpClient httpClient = new DefaultHttpClient();
		HttpClient httpClient = RESTClientPool.getInstance().get();
		httpClient.getParams().setIntParameter(SOCKET_TIME_OUT, SOCKET_TIME_OUT_POST_MS);
		httpClient.getParams().setIntParameter(CONNECTION_TIME_OUT, CONNECTION_TIME_OUT_MS);
		
		StringBuffer sb = new StringBuffer();
		JSONObject json = new JSONObject();

		try {
			HttpPut httpPutRequest = new HttpPut(URL);
			httpPutRequest.addHeader("Authorization", TOKEN);

			StringEntity input = new StringEntity(postBody, "UTF-8");
			input.setContentType("application/json");
			httpPutRequest.setEntity(input);
			
			HttpResponse httpResponse = httpClient.execute(httpPutRequest);

//			System.out.println("----------------------------------------");
//			System.out.println(httpResponse.getStatusLine());
//			System.out.println("----------------------------------------");

			BufferedReader br = new BufferedReader(new InputStreamReader((httpResponse.getEntity().getContent())));

			String output;
//			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				sb.append(output);
			}
			json = (JSONObject) JSONValue.parse(sb.toString());

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			httpClient.getConnectionManager().closeExpiredConnections();
		}

		return json;
	}
	
	@SuppressWarnings({ "resource", "deprecation" })
	public JSONObject patch(String URL, String postBody) {
//		HttpClient httpClient = new DefaultHttpClient();
		HttpClient httpClient = RESTClientPool.getInstance().get();
		httpClient.getParams().setIntParameter(SOCKET_TIME_OUT, SOCKET_TIME_OUT_POST_MS);
		httpClient.getParams().setIntParameter(CONNECTION_TIME_OUT, CONNECTION_TIME_OUT_MS);
		
		StringBuffer sb = new StringBuffer();
		JSONObject json = new JSONObject();

		try {
			HttpPatch httpPatchRequest = new HttpPatch(URL);
			httpPatchRequest.addHeader("Authorization", TOKEN);

			StringEntity input = new StringEntity(postBody, "UTF-8");
			input.setContentType("application/json");
			httpPatchRequest.setEntity(input);
			
			HttpResponse httpResponse = httpClient.execute(httpPatchRequest);

//			System.out.println("----------------------------------------");
//			System.out.println(httpResponse.getStatusLine());
//			System.out.println("----------------------------------------");

			BufferedReader br = new BufferedReader(new InputStreamReader((httpResponse.getEntity().getContent())));

			String output;
//			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				sb.append(output);
			}
			json = (JSONObject) JSONValue.parse(sb.toString());

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			httpClient.getConnectionManager().closeExpiredConnections();
		}

		return json;
	}
}
