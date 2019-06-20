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
import com.wowsanta.scim.schema.SCIMConstants;
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
import com.wowsanta.scim.service.schema.SchemaService;
import com.wowsanta.scim.service.schudler.SchedulerService;
import com.wowsanta.scim.service.scim.v2.service.BlukService;
import com.wowsanta.scim.service.scim.v2.service.GroupService;
import com.wowsanta.scim.service.scim.v2.service.UserService;
import com.wowsanta.scim.service.system.SystemApiService;
import com.wowsanta.scim.service.system.SystemService;


public class ScimServerRouter implements ServiceRouter  {
	Logger logger = LoggerFactory.getLogger(ScimServerRouter.class);
	


	private EnvironmentService envService = new EnvironmentService();
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
	
	private AccountService accountService = new AccountService();
	
	private UserService userService = new UserService();
	private GroupService groupService = new GroupService();
	private BlukService blukService = new BlukService();
	
	@Override
	public void regist() throws ServiceException {
		logger.info("ScimServerRouter regist START ----------------------");
		before("/*",authService.verify());
		after("/*", accessService.access());

		account();
		login();
		scim_v2();
		hrsystem();
		system();
		scheduler();
		env();
		audit();
		agent();
		config();
		log();
		library();
		logger.info("ScimServerRouter regist FINISH ----------------------");
	}

	private void account() {
		
		logger.info("-account");
		path("/account", () -> {			
			post("/im/find"   		 			,accountService.findUserList(), new JsonTransformer());
			post("/sys/find/:systemId"   		,accountService.findSysUserList(), new JsonTransformer());
			
			
			get("/im/state"   		     		,accountService.getUserState(), new JsonTransformer());
			get("/sys/state/:systemId"   		,accountService.getSysUserState(), new JsonTransformer());
			get("/im/status"  		     		,accountService.getUserStatus(), new JsonTransformer());
			get("/sys/status/:systemId"  		,accountService.getSysUserStatus(), new JsonTransformer());
			get("/im/usersys/:userId"  			,accountService.getUserSystemList(), new JsonTransformer());
			get("/sys/userhis/:systemId/:userId",accountService.getSystemAccountHistory(), new JsonTransformer());
			get("/history/:userId"   			,accountService.getAccountHistory(), new JsonTransformer());

			//get("/system/:systemId"  			,accountService.getSystemAccount(), new JsonTransformer());
			//get("/state/system/:systemId"	,accountService.findUserList(), new JsonTransformer());
			//get("/status/system/:systemId"	,accountService.findUserList(), new JsonTransformer());
			
			
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
			
			get    ("/Groups/:userId",	groupService.getGroup(), 	new JsonTransformer());
			put    ("/Groups",			groupService.createGroup(), new JsonTransformer());
			post   ("/Groups",			groupService.updateGroup(), new JsonTransformer());
			post   ("/Groups/search",	groupService.searchGroup(), new JsonTransformer());
			post   ("/Groups/find",		groupService.findGroup(), 	new JsonTransformer());
			patch  ("/Groups",			groupService.patchGroup(),	new JsonTransformer());
			
			post   ("/Bulk",blukService.post(), new JsonTransformer());
		});
		
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
			get("/schemas"			,configService.getSystemSchemaList(), new JsonTransformer());
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
			put("/"   				,systemService.createSystem(), new JsonTransformer());
			post("/"   				,systemService.updateSystem(), new JsonTransformer());
			delete("/:systemId"   	,systemService.deleteSystem(), new JsonTransformer());
			
			get("/columns/:systemId"   ,SystemApiService.getSystemColumnBySystemId(), new JsonTransformer());
			get("/"   					,SystemApiService.getConsumerSystems(), new JsonTransformer());
			get("/:id"					,SystemApiService.getSystem(), new JsonTransformer());
			get("/all/"					,systemService.getSystems(), new JsonTransformer());
		});
	}
	private void scheduler() {
		logger.info("-scheduler");
		path("/scheduler", () -> {
			put("/"   				,schedulerService.createScheduler(), new JsonTransformer());
			post("/"   				,schedulerService.updateScheduler(), new JsonTransformer());
			delete("/:schedulerId"  ,schedulerService.deleteScheduler(), new JsonTransformer());
			
			get("/:schedulerId"   				,schedulerService.getSchedulerById(), new JsonTransformer());
			get("/system/:systemId"   			,schedulerService.getSchedulerBySystemId(), new JsonTransformer());
			get("/history/:schedulerId"   		,schedulerService.getSchedulerHistoryById(), new JsonTransformer());
			get("/history/work/:workId"   		,schedulerService.getSchedulerDetailHistoryByWorkId(), new JsonTransformer());
			post("/run/:schedulerId" 			,schedulerService.runSystemScheduler(), new JsonTransformer());
		});
	}
	
	private void env() {
		logger.info("-env");
		path("/env", () -> {	
			path("/admins", () -> {
				get(	"/"   			, envService.getAllAdmin()		, new JsonTransformer());
				put(	"/"   			, envService.createAdmin()		, new JsonTransformer());				
				post(	"/"   			,envService.updateAdmin()		, new JsonTransformer());
				get(	"/:adminId"   	,envService.getAdmin()			, new JsonTransformer());
				delete(	"/:adminId"   	,envService.deleteAdmin()		, new JsonTransformer());
			});
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
			
			get("/repository/table/list/:systemId" 		,agentService.getTableList(), new JsonTransformer());
			post("/repository/table/column/list" 		,agentService.getColumnList(), new JsonTransformer());
			
			get("/log/list/:systemId"   				 ,agentService.getLogFileList(), new JsonTransformer());
			get("/log/file/:systemId/name/:fileName"   	 ,agentService.getLogFile() );
			
			get("/schema/output/:systemId"   	 		 ,agentService.getSchemOutputMapper(), new JsonTransformer());
			post("/schema/output/:systemId"   	 		 ,agentService.updateSchemOutputMapper(), new JsonTransformer());
			get("/schema/input/:systemId"   	 		 ,agentService.getSchemInputMapper(), new JsonTransformer());
		});	
	}
}
