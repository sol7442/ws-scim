package com.wowsanta.scim.server;

import static spark.Spark.after;
import static spark.Spark.before;
import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.patch;
import static spark.Spark.path;
import static spark.Spark.post;
import static spark.Spark.put;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wowsanta.scim.ServiceException;
import com.wowsanta.scim.ServiceRouter;
import com.wowsanta.scim.log.SCIMLogger;
import com.wowsanta.scim.service.EnvironmentService;
import com.wowsanta.scim.service.account.AccountService;
import com.wowsanta.scim.service.agent.AgentService;
import com.wowsanta.scim.service.audit.AuditService;
import com.wowsanta.scim.service.auth.AccessService;
import com.wowsanta.scim.service.auth.AuthorizationService;
import com.wowsanta.scim.service.auth.LoginService;
import com.wowsanta.scim.service.config.ConfigService;
import com.wowsanta.scim.service.library.LibraryService;
import com.wowsanta.scim.service.logger.LoggerService;
import com.wowsanta.scim.service.schudler.SchedulerService;
import com.wowsanta.scim.service.system.SystemApiService;
import com.wowsanta.scim.service.system.SystemService;


public class ScimServerRouter implements ServiceRouter  {
	Logger logger = LoggerFactory.getLogger(ScimServerRouter.class);
	
	private AuthorizationService authService = new AuthorizationService();
	private LoginService loginService = new LoginService();
	private AccessService accessService = new AccessService();

	private SchedulerService schedulerService = new SchedulerService();
	private AuditService auditService = new AuditService();
	private AgentService agentService = new AgentService();

	private SystemService systemService = new SystemService();
	private LoggerService loggerService = new LoggerService();
	private ConfigService configService = new ConfigService();
	private LibraryService libraryService = new LibraryService();
	
	@Override
	public void regist() throws ServiceException {
		logger.info("ScimServerRouter regist START ----------------------");
		before("/*",authService.verify());
		after("/*", accessService.access());

		
		login();
		hrsystem();
		system();
		scheduler();
		env();
		account();
		audit();
		agent();
		config();
		log();
		library();
		logger.info("ScimServerRouter regist FINISH ----------------------");
	}
	
	private void library() {
		path("/library", () -> {
			get("/list"		,libraryService.getLibraryList(), new JsonTransformer());
			//delete("/:list"		,libraryService.getLibraryList(), new JsonTransformer());
			post("/"		,libraryService.updateLibrary());
		});	
	}

	private void config() {
		path("/config", () -> {
			get("/list"				,configService.getSystemConfigList(), new JsonTransformer());
			get("/file/:fileName"	,configService.getSystemConfigFile());
		});
	}

	private void log() {
		path("/log", () -> {
			get("/list"				,loggerService.getSystemLogList(), new JsonTransformer());
			get("/file/:fileName"	,loggerService.getSystemLogFile());
		});
	}

	private void login() {
		logger.info("-login");
		post("/login", loginService.login(), new JsonTransformer());
	}
	private void hrsystem() {
		logger.info("-hrsystem");
		path("/hrsystem", () -> {
			get("/"   ,SystemApiService.getProviderSystems(), new JsonTransformer());
			get("/:id",SystemApiService.getSystem(), new JsonTransformer());
		});
	}
	private void system() {
		logger.info("-system");
		path("/system", () -> {
			get("/columns/:systemId"   ,SystemApiService.getSystemColumnBySystemId(), new JsonTransformer());
			get("/"   					,SystemApiService.getConsumerSystems(), new JsonTransformer());
			get("/:id"					,SystemApiService.getSystem(), new JsonTransformer());
			get("/all/"					,systemService.getSystems(), new JsonTransformer());
		});
	}
	private void scheduler() {
		logger.info("-scheduler");
		path("/scheduler", () -> {
			get("/:schedulerId"   			,schedulerService.getSchedulerById(), new JsonTransformer());
			get("/system/:systemId"   		,schedulerService.getSchedulerBySystemId(), new JsonTransformer());
			get("/history/:schedulerId"   	,schedulerService.getSchedulerHistoryById(), new JsonTransformer());
			get("/history/work/:workId"   	,schedulerService.getSchedulerDetailHistoryByWorkId(), new JsonTransformer());
			post("/run" 					,schedulerService.runSystemScheduler(), new JsonTransformer());
		});
	}
	
	private void env() {
		logger.info("-env");
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
		logger.info("-account");
		path("/account", () -> {			
			get("/system/:systemId"   		,AccountService.getSystemAccount(), new JsonTransformer());
			get("/history/:userId"   		,AccountService.getAccountHistory(), new JsonTransformer());
		});
	}
	
	private void audit() {
		logger.info("-audit");
		path("/audit", () -> {	
			get("/user/:userid"   	,auditService.getUserAuditList(), new JsonTransformer());
		});		
	}
	
	private void agent() {
		logger.info("-agent");
		path("/agent", () -> {	
			post("/library/:systemId" 					 ,agentService.patchLibrary(), new JsonTransformer());
			//post("/library/:systemId" 					,agentService.patchLibrary_old(), new JsonTransformer());
			get("/config/list/:systemId"   				 ,agentService.getConfigFileList(), new JsonTransformer());
			get("/config/file/:systemId/name/:fileName"  ,agentService.getConfigFile());
			post("/config/:systemId" 				 	 ,agentService.patchConfigFile());
			
			get("/log/list/:systemId"   				 ,agentService.getLogFileList(), new JsonTransformer());
			get("/log/file/:systemId/name/:fileName"   	 ,agentService.getLogFile());
		});	
	}
}
