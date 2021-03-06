package com.wowsanta.scim.service;

import static spark.Spark.after;
import static spark.Spark.before;
import static spark.Spark.path;
import static spark.Spark.post;
import static spark.Spark.put;
import static spark.Spark.patch;
import static spark.Spark.get;

import com.wowsanta.scim.log.SCIMLogger;
import com.wowsanta.scim.schema.SCIMConstants;
import com.wowsanta.scim.service.auth.AuthorizationService;
import com.wowsanta.scim.service.auth.LoginService;
import com.wowsanta.scim.service.schudler.SchedulerService;
import com.wowsanta.scim.service.scim.v2.BlukControl;
import com.wowsanta.scim.service.scim.v2.UserControl;
import com.wowsanta.scim.service.scim.v2.service.BlukService;
import com.wowsanta.scim.service.system.SystemApiService;


public abstract class SparkController {
	public abstract void control();
	
//	private LoginService auth = new LoginService();
//	
//	public void control() {
//		before("/*",AuthorizationService.verify());
//		
//		after("/*", (req, res) -> {
//			res.header("Content-Type", "application/scim+json");
//			SCIMLogger.access("{} -> {} {} : {} ",req.ip(), req.requestMethod(), req.uri(),  res.status());
//			}
//		);
//		
//		login();
//		scim_v2();
//		system();
//		hrsystem();
//		scheduler();
//		api();
//	}
//
//	private void scheduler() {
//		path("/scheduler", () -> {			
//			get("/system/:systemId"   		,SystemApiService.getSchedulerBySystemId(), json());
//			get("/:schedulerId"   			,SystemApiService.getSchedulerById(), json());
//			get("/history/:schedulerId" 	,SystemApiService.getSchedulerHistory(), json());
//			post("/run/remote" 				,SystemApiService.runRemoteScheduler(), json());
//			post("/run/system" 				,SystemApiService.runSystemScheduler(), json());
//		});
//		
//	}
//
//	private void hrsystem() {
//		path("/hrsystem", () -> {
//			get("/"   ,SystemApiService.getProviderSystems(), json());
//			get("/:id",SystemApiService.getSystem(), json());
//		});
//	}
//	private void system() {
//		path("/system", () -> {
//			get("/columns/:systemId"   ,SystemApiService.getSystemColumnBySystemId(), json());
//			get("/"   ,SystemApiService.getConsumerSystems(), json());
//			get("/:id",SystemApiService.getSystem(), json());
//		});
//	}
//	private void api() {
//		path("/api", () -> {
//			//scim_v2();
//			//scheduler();
//		});
//	}
//
//
//
//	private void scim_v2() {		
//		path("/scim/" + SCIMConstants.VERSION, () -> {
//			get    ("/Users/:userId",UserControl.getUser(), json());
//			put    ("/Users",UserControl.create(), json());
//			post   ("/Users",UserControl.updateUser(), json());
//			patch  ("/Users",UserControl.patch(), json());
//			post   ("/Bulk",BlukControl.post(), json());
//			//post   ("/Bulk",BlukService.execute(), json());
//		});
//	}
//
//	private void login() {
//		post("/login", new LoginController(new LoginService()), json());
//	}
}
