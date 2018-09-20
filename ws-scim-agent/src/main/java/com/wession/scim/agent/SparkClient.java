package com.wession.scim.agent;

import static spark.Spark.after;
import static spark.Spark.before;
import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.patch;
import static spark.Spark.path;
import static spark.Spark.post;
import static spark.Spark.put;

import org.slf4j.Logger;

import com.wession.common.WessionLog;
import com.wession.scim.controller.ServiceProviderConfig;

import net.minidev.json.JSONObject;
import spark.Request;
import spark.Response;
import spark.Spark;

public class SparkClient {
	private static Logger systemLog;
	private static WessionLog wlog = new WessionLog();
	private static ServiceProviderConfig conf = ServiceProviderConfig.getInstance();
	
	public SparkClient() {
		systemLog = wlog.getSystemLog();
	}
	
	
	public static void main(String [] args) {
		SparkClient sm = new SparkClient();
		JSONObject agent_config = conf.getAgentConfig();
		systemLog.info("Start Spark Web Framework (WessionIM REST Agent 웹서비스를 기동합니다.) ********************\n\n"); 
		
		long startTime = System.currentTimeMillis();
		
		int port = (int) agent_config.getAsNumber("servicePort");
		int maxThreads = (int) agent_config.getAsNumber("maxThreads");
		int minThreads = (int) agent_config.getAsNumber("minThreads");
		int idleTimeoutMills = (int) agent_config.getAsNumber("idleTimeoutMills");
		
		Spark.port(port);
		Spark.threadPool(maxThreads, minThreads, idleTimeoutMills);
		
		StringBuilder sb_log = new StringBuilder();
		
		sb_log.append("    port : ").append(port).append("\n");
		sb_log.append("    maxThread : ").append(maxThreads).append("\n");
		sb_log.append("    minThreads : ").append(minThreads).append("\n");
		sb_log.append("    idleTimeoutMills : ").append(idleTimeoutMills).append("\n");
		
		systemLog.debug("* Spark Configuration : \n" + sb_log.toString());

		//repo.load();

		//systemLog.debug("* SCIM Resources Load Time : " + (System.currentTimeMillis() - startTime) + " (ms)");
		
		// URI : /scim/v2.0/Me -> 501 not implements
		// URI : /scim/v2.0/User
		// URI : /scim/v2.0/Group
		// URI : /scim/v2.0/ServiceProviderConfig
		// URI : /scim/v2.0/ResourceType
		// URI : /scim/v2.0/Schema
		// post -> create
		// put -> replace
		// patch -> update or multiple-serialized process
		// delete -> delete or disable
		
		String version = "v2.0";
		
		before("/*", (req, res) -> {req.attribute("RequestTime", System.currentTimeMillis()+""); });
		
		path("/scim/" + version, () -> {
			// Account
			get   ("/Users", (req, res) -> sm.getUserParam(req, res));
			get   ("/Users/:id", (req, res) -> sm.getUser(req, res));
			post  ("/Users", (req, res) -> sm.createUser(req, res));
			put   ("/Users/:id", (req, res) -> sm.updateUser(req, res));
			patch ("/Users/:id", (req, res) -> sm.patchUser(req, res));
			delete("/Users/:id", (req, res) -> sm.deleteUser(req, res));
			
			// SCIM
			post  ("/Bulk", (req, res) -> sm.setBulk(req, res));
			
		});
		
		// Search = GET 
		// https://example.com/{v}/{resource}?ﬁlter={attribute}{op}{value}&sortBy={attributeName}&sortOrder={ascending|descending}

		get("/command/:version/:command", (req, res) -> sm.command(req, res));
		get("/command/:version/:command/:userid", (req, res) -> sm.command(req, res));
		
		after("/*", (req, res) -> {res.header("Content-Type", "application/scim+json"); wlog.accessLog(req, res);});
		
		long endTime = System.currentTimeMillis();
		systemLog.info("Ready to Service  ********************\n\tService Loading Time : " + (endTime - startTime)  + " (ms)\n");
	}
	
	private Object setBulk(Request request, Response response) {
		return new Controller().setBulk(request, response);
	}


	private Object deleteUser(Request request, Response response) {
		return new Controller().deleteUser(request, response);
	}


	private Object patchUser(Request request, Response response) {
		return new Controller().patchUser(request, response);
	}


	private Object updateUser(Request request, Response response) {
		return new Controller().updateUser(request, response);
	}


	private Object createUser(Request request, Response response) {
		return new Controller().createUser(request, response);
	}


	private Object getUser(Request request, Response response) {
		return new Controller().getUser(request, response);
	}


	private Object getUserParam(Request request, Response response) {
		return new Controller().getUserParam(request, response);
	}


	private String command(Request request, Response response) {
		return new Commander().process(request, response);
	}
	
	
}
