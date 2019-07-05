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
import com.wowsanta.scim.schema.SCIMConstants;
import com.wowsanta.scim.service.agent.AgentService;
import com.wowsanta.scim.service.auth.AccessService;
import com.wowsanta.scim.service.auth.AuthorizationService;
import com.wowsanta.scim.service.config.ConfigService;
import com.wowsanta.scim.service.library.LibraryService;
import com.wowsanta.scim.service.logger.LoggerService;
import com.wowsanta.scim.service.repository.RepositoryService;
import com.wowsanta.scim.service.schema.SchemaService;
import com.wowsanta.scim.service.scim.v2.service.BlukService;
import com.wowsanta.scim.service.scim.v2.service.GroupService;
import com.wowsanta.scim.service.scim.v2.service.UserService;


public class ScimAgentRouter implements ServiceRouter  {
	Logger logger = LoggerFactory.getLogger(ScimAgentRouter.class);
	
	private AuthorizationService authService = new AuthorizationService();
	private AccessService accessService = new AccessService();
	
	private UserService userService = new UserService();
	private GroupService groupService = new GroupService();
	private BlukService blukService = new BlukService();

	
	private ConfigService configService = new ConfigService();
	private LoggerService loggerService = new LoggerService();
	private LibraryService libraryService = new LibraryService();
	private RepositoryService repositoryService = new RepositoryService();

	private SchemaService schemaService = new SchemaService();

	
	@Override
	public void regist() throws ServiceException {
		logger.info("ScimAgentRouter regist START ----------------------");
		
		before("/*",authService.verify());
		after("/*", accessService.access());
		
		scim_v2();
		config();
		log();
		library();
		repository();
		schema();
		logger.info("ScimAgentRouter regist START ----------------------");

	}
	
	private void repository() {
		path("/repository", () -> {
			get("/table/list"					,repositoryService.getTableList(), new JsonTransformer());
			get("/table/column/list/:tableName"	,repositoryService.getTableColumnList(), new JsonTransformer());
			post("/query"						,repositoryService.getQueryResult(), new JsonTransformer());
		});//()repository/table/list		
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
			post("/"				,configService.updateSystemConfigFile());
		});
	}

	private void log() {
		path("/log", () -> {
			get("/list"				,loggerService.getSystemLogList(), new JsonTransformer());
			get("/file/:fileName"	,loggerService.getSystemLogFile());
		});
	}

	private void schema() {
		path("/schema", () -> {			
			get("/:fileName"   		,schemaService.getSchemaFile(), new JsonTransformer());
			get("/mapper/output"   		,schemaService.getSchemaOutputMapper(), new JsonTransformer());
			post("/mapper/output"   	,schemaService.updateSchemaOutputMapper(), new JsonTransformer());
			get("/mapper/input"   		,schemaService.getSchemaInputMapper(), new JsonTransformer());
		});
	}
	private void scim_v2() {		
		path("/scim/" + SCIMConstants.VERSION, () -> {
			get    ("/Users/:userId"		,userService.getUser(), new JsonTransformer());
			put    ("/Users"				,userService.create(), new JsonTransformer());
			post   ("/Users"				,userService.updateUser(), new JsonTransformer());
			post   ("/Users/search"			,userService.search(), new JsonTransformer());
			post   ("/Users/find"			,userService.find(), new JsonTransformer());
			patch  ("/Users"				,userService.patch(), new JsonTransformer());
			
			get    ("/Groups/:userId"		,groupService.getGroup(), 	new JsonTransformer());
			put    ("/Groups"				,groupService.createGroup(), new JsonTransformer());
			post   ("/Groups"				,groupService.updateGroup(), new JsonTransformer());
			post   ("/Groups/search"		,groupService.searchGroup(), new JsonTransformer());
			post   ("/Groups/find"			,groupService.findGroup(), 	new JsonTransformer());
			patch  ("/Groups"				,groupService.patchGroup(),	new JsonTransformer());
			
			post   ("/Bulk",blukService.post(), new JsonTransformer());
		});
	}
}
