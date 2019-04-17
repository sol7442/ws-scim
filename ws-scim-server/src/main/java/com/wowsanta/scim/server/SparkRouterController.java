package com.wowsanta.scim.server;

import static spark.Spark.after;
import static spark.Spark.before;
import static spark.Spark.path;
import static spark.Spark.post;
import static spark.Spark.put;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static spark.Spark.patch;
import static spark.Spark.get;
import static spark.Spark.delete;

import com.wowsanta.scim.log.SCIMLogger;
import com.wowsanta.scim.schema.SCIMConstants;
import com.wowsanta.scim.service.EnvironmentService;
import com.wowsanta.scim.service.SparkController;
import com.wowsanta.scim.service.account.AccountService;
import com.wowsanta.scim.service.agent.AgentService;
import com.wowsanta.scim.service.audit.AuditService;
import com.wowsanta.scim.service.auth.AuthorizationService;
import com.wowsanta.scim.service.auth.LoginService;
import com.wowsanta.scim.service.config.ConfigService;
import com.wowsanta.scim.service.schudler.SchedulerService;
import com.wowsanta.scim.service.scim.v2.service.BlukService;
import com.wowsanta.scim.service.scim.v2.service.UserService;
import com.wowsanta.scim.service.system.SystemApiService;


public class SparkRouterController extends SparkController{
	
	Logger access_logger  = LoggerFactory.getLogger("access");
	  
	private AuthorizationService authService = new AuthorizationService();
	private LoginService loginService = new LoginService();
	private SchedulerService schedulerService = new SchedulerService();
	private UserService userService = new UserService();
	private AgentService agentService = new AgentService();
	private ConfigService configService = new ConfigService();
	private BlukService blukService = new BlukService();
	private AuditService auditService = new AuditService();
	private AccountService accountService = new AccountService();
	
	public void control() {
		before("/*",authService.verify());
		
		after("/*", (req, res) -> {
			res.header("Content-Type", "application/scim+json");
			access_logger.info("{} -> {} {} : {} ",req.ip(), req.requestMethod(), req.uri(),  res.status());
			}
		);
		
		login();
		scim_v2();
		system();
		account();
		hrsystem();
		scheduler();
		api();
		env();
		agent();
		config();
		audit();
	}

	private void audit() {
		path("/audit", () -> {	
			get("/user/:userid"   	,auditService.getUserAuditList(), new JsonTransformer());
		});		
	}

	private void config() {
		path("/config", () -> {	
			get("/service"   	,configService.getSystemInfo(), new JsonTransformer());
			post("/service"   	,configService.setSystemInfo(), new JsonTransformer());
			get("/repository"  	,configService.getRepositoryInfo(), new JsonTransformer());
			post("/repository" 	,configService.setRepositoryInfo(), new JsonTransformer());
			get("/library" 			,configService.setRepositoryInfo(), new JsonTransformer());
			put("/library" 			,configService.setRepositoryInfo(), new JsonTransformer());
			post("/library" 		,configService.setRepositoryInfo(), new JsonTransformer());			
			delete("/library" 		,configService.setRepositoryInfo(), new JsonTransformer());
			patch("/library" 		,configService.patchLibrary(), new JsonTransformer());
		});
	}

	private void agent() {
		path("/agent", () -> {	
			get("/service/:systemId"   		,agentService.getSystemInfo(), new JsonTransformer());
			post("/service/:systemId"   	,agentService.setSystemInfo(), new JsonTransformer());
			get("/repository/:systemId"   	,agentService.getRepositoryInfo(), new JsonTransformer());
			post("/repository/:systemId"   	,agentService.setRepositoryInfo(), new JsonTransformer());
			
			get("/library/:systemId" 		,agentService.getLibrary(), new JsonTransformer());			
			put("/library/:systemId" 		,agentService.setLibrary(), new JsonTransformer());			
			delete("/library/:systemId" 	,agentService.removeLibrary(), new JsonTransformer());
			patch("/library/:systemId" 		,agentService.patchLibrary(), new JsonTransformer());
		});	
	}

	private void env() {
		path("/env", () -> {	
			path("/admins", () -> {
				get(	"/"   			,EnvironmentService.getAllAdmin()		, new JsonTransformer());
				put(	"/"   			,new EnvironmentService().createAdmin()	, new JsonTransformer());				
				post(	"/"   			,EnvironmentService.updateAdmin()		, new JsonTransformer());
				get(	"/:adminId"   	,EnvironmentService.getAdmin()			, new JsonTransformer());
				delete(	"/:adminId"   	,EnvironmentService.deleteAdmin()		, new JsonTransformer());
			});
		});
	}

	private void account() {
		path("/account", () -> {			
			get("/system/:systemId"   		,accountService.getSystemAccount(), new JsonTransformer());
			get("/history/:userId"   		,accountService.getAccountHistory(), new JsonTransformer());
		});
	}

	private void scheduler() {
		path("/scheduler", () -> {
			get("/:schedulerId"   			,schedulerService.getSchedulerById(), new JsonTransformer());
			get("/system/:systemId"   		,schedulerService.getSchedulerBySystemId(), new JsonTransformer());
			get("/history/:schedulerId"   	,schedulerService.getSchedulerHistoryById(), new JsonTransformer());
			get("/history/work/:workId"   	,schedulerService.getSchedulerDetailHistoryByWorkId(), new JsonTransformer());
			post("/run" 					,schedulerService.runSystemScheduler(), new JsonTransformer());
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
			get    ("/Users/:userId",userService.getUser(), new JsonTransformer());
			put    ("/Users",userService.create(), new JsonTransformer());
			post   ("/Users",userService.updateUser(), new JsonTransformer());
			post   ("/Users/search",userService.search(), new JsonTransformer());
			post   ("/Users/find",userService.find(), new JsonTransformer());
			patch  ("/Users",userService.patch(), new JsonTransformer());
			
			
			post   ("/Bulk",blukService.post(), new JsonTransformer());
		});
	}

	private void login() {
		post("/login", loginService.login(), new JsonTransformer());
	}
}
