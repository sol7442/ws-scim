package com.wowsanta.scim.service.scim.v2;


import com.wowsanta.scim.exception.SCIMError;

import spark.Request;
import spark.Response;
import spark.Route;

public class UserControl {

	public static Route getUser() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return SCIMError.NotImplemented;
			}
		} ;
	}

	public static Route create() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return SCIMError.NotImplemented;
			}
		} ;
	}

	public static Route updateUser() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return SCIMError.NotImplemented;
			}
		} ;
	}

	public static Route patch() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return SCIMError.NotImplemented;
			}
		} ;
	}

}
