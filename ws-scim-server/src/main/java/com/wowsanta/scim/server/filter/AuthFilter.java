package com.wowsanta.scim.server.filter;

import spark.Filter;
import spark.Request;
import spark.Response;

public class AuthFilter implements Filter {

	@Override
	public void handle(Request request, Response response) throws Exception {
		String authorization = request.headers("Authorization");
	}

}
