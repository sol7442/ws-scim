package com.wowsanta.scim.server;

import static com.wowsanta.scim.server.JsonUtil.json;
import static com.wowsanta.scim.server.JsonUtil.json_parse;
import static spark.Spark.after;
import static spark.Spark.before;
import static spark.Spark.path;
import static spark.Spark.post;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonObject;
import com.wowsanta.scim.SystemManager;
import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.log.SCIMLogger;
import com.wowsanta.scim.resource.SCIMAdmin;
import com.wowsanta.scim.resource.SCIMCode;
import com.wowsanta.scim.resource.SCIMSystemRepository;
import com.wowsanta.scim.resource.ServiceResult;
import com.wowsanta.scim.sec.SCIMJWTToken;
import com.wowsanta.scim.service.LoginController;
import com.wowsanta.scim.service.auth.LoginService;

import spark.Request;
import spark.Response;
import spark.Route;

public class SparkController {
	
	private LoginService auth = new LoginService();
	
	public void control() {
		before("/*", (req, res) -> {
			System.out.println("requst url : " + req.uri());
			if("/auth/login".equals(req.uri())){
				String authentication = req.headers("Authorization");
				//auth.verify
			}
			
			String authentication = req.headers("Authorization");
			System.out.println("authentication token : " + authentication);
		});
		
		after("/*", (req, res) -> {
			res.header("Content-Type", "application/scim+json");
			SCIMLogger.access("{} -> {} {} : {} ",req.ip(), req.requestMethod(), req.uri(),  res.status());
			}
		);
		
		login();
	}

	private void login() {
		post("/auth/login", new LoginController(new LoginService()), json());
	}
}
