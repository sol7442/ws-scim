package com.wowsanta.scim.controller;

import static com.wowsanta.scim.service.JsonUtil.json;
import static spark.Spark.*;

import com.wowsanta.scim.service.SCIMAdminService;
import com.wowsanta.scim.service.admin.LoginRequest;

import spark.Request;
import spark.Response;
import spark.Route;


public class AdminController {
	public AdminController(final SCIMAdminService service){
		System.out.println("regist admin service ======================");
		path("/admin", () -> {
			post("/login", new Route() {
				@Override
				public Object handle(Request request, Response response) throws Exception {
					return service.login(json(request.body(),LoginRequest.class));
				}
			} ,json());
		});
		System.out.println("regist admin service ======================");
	}
}
