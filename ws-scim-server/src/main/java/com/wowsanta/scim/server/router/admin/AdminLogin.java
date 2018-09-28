package com.wowsanta.scim.server.router.admin;

import spark.Request;
import spark.Response;
import spark.Route;

public class AdminLogin implements Route{

	@Override
	public Object handle(Request request, Response response) throws Exception {
		System.out.println("admin-login");
		return "login result";
	}

}
