package com.wowsanta.scim.server;

import static com.wowsanta.scim.server.JsonUtil.json;
import static spark.Spark.after;
import static spark.Spark.before;
import static spark.Spark.path;
import static spark.Spark.post;
import static spark.Spark.get;

import com.wowsanta.scim.log.SCIMLogger;
import com.wowsanta.scim.schema.SCIMConstants;
import com.wowsanta.scim.service.AuthorizationController;
import com.wowsanta.scim.service.LoginController;
import com.wowsanta.scim.service.auth.AuthorizationService;
import com.wowsanta.scim.service.auth.LoginService;
import com.wowsanta.scim.service.scim.v2.BulkController;
import com.wowsanta.scim.service.scim.v2.bulk.BlukService;
import com.wowsanta.scim.service.system.SystemApiService;


public class SparkController {
	
	private LoginService auth = new LoginService();
	
	public void control() {
		before("/*",AuthorizationService.verify());
		
		after("/*", (req, res) -> {
			res.header("Content-Type", "application/scim+json");
			SCIMLogger.access("{} -> {} {} : {} ",req.ip(), req.requestMethod(), req.uri(),  res.status());
			}
		);
		
		login();
		scim_v2();
		system();
		hrsystem();
		scheduler();
		api();
	}

	private void scheduler() {
		path("/scheduler", () -> {
			get("/:systemId"   ,SystemApiService.getSystemScheduler(), json());
			get("/history/:schedulerId"   ,SystemApiService.getSchedulerHistory(), json());
			post("/run/:schedulerId"   ,SystemApiService.runSystemScheduler(), json());
		});
		
	}

	private void hrsystem() {
		path("/hrsystem", () -> {
			get("/"   ,SystemApiService.getProviderSystems(), json());
			get("/:id",SystemApiService.getSystem(), json());
		});
	}

	private void api() {
		path("/api/:systemId", () -> {
			scim_v2();
			path("/scheduler", () -> {
				post("/run/:schedulerId"   ,SystemApiService.runSystemScheduler(), json());
			});
		});
	}

	private void system() {
		path("/system", () -> {
			get("/"   ,SystemApiService.getConsumerSystems(), json());
			get("/:id",SystemApiService.getSystem(), json());
		});
	}

	private void scim_v2() {		
		path("/scim/" + SCIMConstants.VERSION, () -> {
			post  ("/Bulk",BlukService.execute(), json());
		});
	}

	private void login() {
		post("/login", new LoginController(new LoginService()), json());
	}
}
