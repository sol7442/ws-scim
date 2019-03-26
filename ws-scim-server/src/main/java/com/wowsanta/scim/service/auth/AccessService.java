package com.wowsanta.scim.service.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spark.Filter;
import spark.Request;
import spark.Response;

public class AccessService {

	static Logger logger = LoggerFactory.getLogger("access");
	public Filter access() {
		return new Filter() {
			@Override
			public void handle(Request request, Response response) throws Exception {
				logger.info("{} -> {} {} : {} ",request.ip(), request.requestMethod(), request.uri(),  response.status());
			}
		};
	}

}
