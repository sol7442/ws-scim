package com.wowsanta.scim.service.admin;

import com.wowsanta.scim.service.JsonResult;

import spark.Request;
import spark.Response;
import spark.Route;

public class LoginService implements Route {

	
	@Override
	public Object handle(Request request, Response response) throws Exception {
		LoginResult result = new LoginResult();
		try {
			String user_id = request.raw().getParameter("id");
			String passwod = request.raw().getParameter("pw");

			System.out.println("============================");
			
			System.out.println("id ->" + request.queryParams("id"));
			System.out.println("pw ->" + request.queryParams("pw"));
			
			System.out.println(request.body());
			System.out.println(request.params());
			System.out.println(request.raw().getParameterMap());
			
			System.out.println("admin-login");
			System.out.println("id : " + user_id + ",pw :" + passwod);
			System.out.println("id : " + request.params("id") + ",pw :" + request.params("pw"));
			
			result.setCode(100);
			return result;	
			
		}catch(Exception e) {
			throw e;
		}
	}
	
	private class LoginResult extends JsonResult{
		
	}

}
