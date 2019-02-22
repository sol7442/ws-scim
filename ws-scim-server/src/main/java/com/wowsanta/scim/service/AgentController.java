package com.wowsanta.scim.service;

import static com.wowsanta.scim.server.JsonUtil.json;
import static spark.Spark.after;
import static spark.Spark.before;
import static spark.Spark.path;
import static spark.Spark.post;
import static spark.Spark.put;
import static spark.Spark.patch;
import static spark.Spark.get;

import com.wowsanta.scim.log.SCIMLogger;
import com.wowsanta.scim.schema.SCIMConstants;
import com.wowsanta.scim.service.agent.AgentService;
import com.wowsanta.scim.service.auth.AuthorizationService;
import com.wowsanta.scim.service.auth.LoginService;
import com.wowsanta.scim.service.schudler.SchdulerService;
import com.wowsanta.scim.service.scim.v2.BlukControl;
import com.wowsanta.scim.service.scim.v2.UserControl;
import com.wowsanta.scim.service.scim.v2.service.BlukService;
import com.wowsanta.scim.service.system.SystemApiService;


public class AgentController extends SparkController {
	
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
		scheduler();
	}

	private void scheduler() {
		path("/scheduler", () -> {
			put ("/"   				,SystemApiService.getSchedulerById(), json());
			post("/"   				,SystemApiService.getSchedulerById(), json());
			get ("/"   				,SystemApiService.getSchedulerById(), json());
			get ("/:schedulerId"   	,SystemApiService.getSchedulerById(), json());
			post("/run/remote" 		,AgentService.runRemoteScheduler(), json());
		});
		
	}

	private void scim_v2() {		
		path("/scim/" + SCIMConstants.VERSION, () -> {
			post   ("/Bulk",BlukControl.post(), json());
		});
	}

	private void login() {
		post("/login", new LoginController(new LoginService()), json());
	}
}
