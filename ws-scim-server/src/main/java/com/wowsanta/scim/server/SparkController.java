package com.wowsanta.scim.server;

import static com.wowsanta.scim.server.JsonUtil.json;
import static spark.Spark.after;
import static spark.Spark.before;
import static spark.Spark.path;
import static spark.Spark.post;


import com.wowsanta.scim.log.SCIMLogger;
import com.wowsanta.scim.schema.SCIMConstants;
import com.wowsanta.scim.service.AuthorizationController;
import com.wowsanta.scim.service.LoginController;
import com.wowsanta.scim.service.auth.LoginService;
import com.wowsanta.scim.service.scim.v2.BulkController;
import com.wowsanta.scim.service.scim.v2.bulk.BlukService;


public class SparkController {
	
	private LoginService auth = new LoginService();
	
	public void control() {
		before("/*",new AuthorizationController());
		
		after("/*", (req, res) -> {
			res.header("Content-Type", "application/scim+json");
			SCIMLogger.access("{} -> {} {} : {} ",req.ip(), req.requestMethod(), req.uri(),  res.status());
			}
		);
		
		login();
		scim_v2();
	}

	private void scim_v2() {
		path("/scim/" + SCIMConstants.VERSION, () -> {
			post  ("/Bulk",new BulkController(new BlukService()), json());
		});
	}

	private void login() {
		post("/login", new LoginController(new LoginService()), json());
	}
}
