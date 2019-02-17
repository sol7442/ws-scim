package com.wowsanta.scim.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import com.google.gson.Gson;

public class ProviderServiceTest {
//
//	//@Test
//	public void testLoginRequestByUrlConnection() {
//		
//		try {
//		
//			String url="http://localhost:5000/admin/login";
//			URL object=new URL(url);
//			
//			HttpURLConnection con = (HttpURLConnection) object.openConnection();
//			con.setDoOutput(true);
//			con.setDoInput(true);
//			con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
//			con.setRequestProperty("Accept", "application/json");
//			con.setRequestMethod("POST");
//			
//			LoginRequest login_request = new LoginRequest();
//            login_request.setId("admin");
//            login_request.setPw("password");
//            
//			OutputStreamWriter wr= new OutputStreamWriter(con.getOutputStream());
//			wr.write(new Gson().toJson(login_request));
//			wr.close();
//			
//			
//			StringBuilder sb = new StringBuilder();  
//			int HttpResult = con.getResponseCode(); 
//			if (HttpResult == HttpURLConnection.HTTP_OK) {
//			    BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
//			    String line = null;  
//			    while ((line = br.readLine()) != null) {  
//			        sb.append(line + "\n");  
//			    }
//			    br.close();
//			    System.out.println("" + sb.toString());  
//			} else {
//			    System.out.println(con.getResponseMessage());  
//			} 
//			
//			con.disconnect();
//			
//		}catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	@Test
//	public void testLoginRequestResponse() {
//		String url = "http://localhost:5000/admin/login";
//
//		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
//        try {
//            HttpPost request = new HttpPost(url);
//            
//            LoginRequest login_request = new LoginRequest();
//            login_request.setId("admin");
//            login_request.setPw("password");
//            
////            JsonObject obj = new JsonObject();
////            obj.addProperty("id","admin");
////            obj.addProperty("pw","password");
//            
//            StringEntity entity_string = new StringEntity(new Gson().toJson(login_request),"UTF-8");
//			request.setEntity(entity_string);
//
//		    request.addHeader("content-type", "application/json");
//		    CloseableHttpResponse result = httpClient.execute(request);
//		    
//		    System.out.println(result.getStatusLine().getStatusCode());
//	        String json = EntityUtils.toString(result.getEntity(), "UTF-8");
//	        
//	        //result.close();
//	        
//	        
//	        System.out.println("============================");
//	        System.out.println(json);
//	        System.out.println("============================");
//	    
//	        
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		} catch (ClientProtocolException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}finally {
//			System.out.println("closed----");
//			try {
//				//httpClient.close();
//				Thread.sleep(10000);
//				httpClient.close();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			System.out.println("closed----");
//		}
//        
//	}
}
