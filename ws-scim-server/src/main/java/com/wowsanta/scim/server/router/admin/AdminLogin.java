package com.wowsanta.scim.server.router.admin;


import spark.Request;
import spark.Response;
import spark.Route;

public class AdminLogin implements Route{

	
	@Override
	public Object handle(Request request, Response response) throws Exception {
		String user_id = request.raw().getParameter("puserid");
		String passwod = request.raw().getParameter("ppasswd");
		System.out.println("admin-login");
		System.out.println("id : " + user_id + ",pw :" + passwod);
		
		LoginResponse res = new LoginResponse();
		res.setUrl("home");
		
		return res.toString();
	}

}
