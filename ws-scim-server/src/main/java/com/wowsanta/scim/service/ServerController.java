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
import com.wowsanta.scim.server.JsonTransformer;
import com.wowsanta.scim.service.account.AccountService;
import com.wowsanta.scim.service.auth.AuthorizationService;
import com.wowsanta.scim.service.auth.LoginService;
import com.wowsanta.scim.service.schudler.SchdulerService;
import com.wowsanta.scim.service.scim.v2.BlukControl;
import com.wowsanta.scim.service.scim.v2.UserControl;
import com.wowsanta.scim.service.scim.v2.service.BlukService;
import com.wowsanta.scim.service.system.SystemApiService;


public class ServerController extends SparkController{
	
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
		account();
		hrsystem();
		scheduler();
		api();
	}

	private void account() {
		path("/account", () -> {			
			get("/system/:systemId"   		,AccountService.getSystemAccount(), new JsonTransformer());
			get("/history/:userId"   		,AccountService.getAccountHistory(), new JsonTransformer());
		});
	}

	private void scheduler() {
		path("/scheduler", () -> {			
			get("/system/:systemId"   		,SystemApiService.getSchedulerBySystemId(), new JsonTransformer());
			get("/:schedulerId"   			,SystemApiService.getSchedulerById(), new JsonTransformer());
			get("/history/system/:systemId" ,SchdulerService.getSystemSchedulerHistory(), new JsonTransformer());
			get("/history/:schedulerId" 	,SystemApiService.getSchedulerHistory(), new JsonTransformer());
			post("/run/remote" 				,SystemApiService.runRemoteScheduler(), new JsonTransformer());
			post("/run/system" 				,SchdulerService.runSystemScheduler(), new JsonTransformer());
		});
		
	}

	private void hrsystem() {
		path("/hrsystem", () -> {
			get("/"   ,SystemApiService.getProviderSystems(), new JsonTransformer());
			get("/:id",SystemApiService.getSystem(), new JsonTransformer());
		});
	}
	private void system() {
		path("/system", () -> {
			get("/columns/:systemId"   ,SystemApiService.getSystemColumnBySystemId(), new JsonTransformer());
			get("/"   ,SystemApiService.getConsumerSystems(), new JsonTransformer());
			get("/:id",SystemApiService.getSystem(), new JsonTransformer());
		});
	}
	private void api() {
		path("/api", () -> {
			//scim_v2();
			//scheduler();
		});
	}



	private void scim_v2() {		
		path("/scim/" + SCIMConstants.VERSION, () -> {
			get    ("/Users/:userId",UserControl.getUser(), new JsonTransformer());
			put    ("/Users",UserControl.create(), new JsonTransformer());
			post   ("/Users",UserControl.updateUser(), new JsonTransformer());
			patch  ("/Users",UserControl.patch(), new JsonTransformer());
			post   ("/Bulk",BlukControl.post(), new JsonTransformer());
			get    ("/Bulk",BlukControl.getAll(), new JsonTransformer());
			get    ("/Bulk/:lasteDate",BlukControl.get(), new JsonTransformer());
		});
	}

	private void login() {
		post("/login", new LoginController(new LoginService()), new JsonTransformer());
	}
}
