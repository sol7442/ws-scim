package com.wession.scim.server;

import static spark.Spark.after;
import static spark.Spark.before;
import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.patch;
import static spark.Spark.path;
import static spark.Spark.post;
import static spark.Spark.put;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.http.Part;

import org.slf4j.Logger;

import com.wession.common.WessionLog;
import com.wession.jwt.JWTDecode;
import com.wession.scim.controller.Bulk;
import com.wession.scim.controller.Patch;
import com.wession.scim.controller.ProvisioningConfig;
import com.wession.scim.controller.ServiceProviderConfig;
import com.wession.scim.simple.MemRepository;

import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import spark.Request;
import spark.Response;
import spark.Spark;


public class SparkMain {
	private static Logger systemLog;
	private static WessionLog wlog = new WessionLog();
	private static ServiceProviderConfig conf = ServiceProviderConfig.getInstance();
	private static ProvisioningConfig provision = ProvisioningConfig.getInstance();
	
	public SparkMain() {
		systemLog = wlog.getSystemLog();
	}
	
	
	public static void main(String [] args) {
		JSONObject server_config = conf.getServerConfig();
		JSONObject provision_config = (JSONObject) JSONValue.parse(provision.getProvisioningConfig());
		SparkMain sm = new SparkMain();
		
		System.out.println(provision_config.getAsString("status"));
		
		systemLog.info("Start Spark Web Framework (WessionIM REST 웹서비스를 기동합니다.) ********************\n\n");
		
		long startTime = System.currentTimeMillis();
		
		int port = (int) server_config.getAsNumber("servicePort");
		int maxThreads = (int) server_config.getAsNumber("maxThreads");
		int minThreads = (int) server_config.getAsNumber("minThreads");
		int idleTimeoutMills = (int) server_config.getAsNumber("idleTimeoutMills");
		
		Spark.port(port);
		Spark.threadPool(maxThreads, minThreads, idleTimeoutMills);
		//Spark.staticFileLocation("/html");
		Spark.staticFiles.registerMimeType("ico", "ico");
		Spark.staticFiles.externalLocation(server_config.getAsString("staticFiles"));
		//Spark.staticFiles.externalLocation("/tmp/upload"); //외부경로사용? 쓰는방법확인!
		
		//Spark.secure("deploy/jks_keystore", "wessionim", null, null);
		
		StringBuilder sb_log = new StringBuilder();
		
		sb_log.append("    port : ").append(port).append("\n");
		sb_log.append("    maxThread : ").append(maxThreads).append("\n");
		sb_log.append("    minThreads : ").append(minThreads).append("\n");
		sb_log.append("    idleTimeoutMills : ").append(idleTimeoutMills).append("\n");
		
		systemLog.debug("* Spark Configuration : \n" + sb_log.toString());

		MemRepository repo = MemRepository.getInstance();
		repo.load();

		systemLog.debug("* SCIM Resources Load Time : {} (ms)", (System.currentTimeMillis() - startTime));
		
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
		
		before("/*", (req, res) -> {
			req.attribute("RequestTime", System.currentTimeMillis()+"");
/*
			//System.out.println(req.pathInfo());
			// localhost에서 올라온 것들은 검증하지 않음
			String ip = req.ip();
			if ("0:0:0:0:0:0:0:1".equals(ip) || "127.0.0.1".equals(ip)) {
				System.out.println("Local Working. No Authentication accepted.");
			} else {
				JSONObject resJson = sm.checkToken(req, res);
				if(!"0".equals(resJson.getAsString("status"))) {
					halt(401, resJson.toJSONString());
				}
			}
*/
			System.out.println("req contextPath : " + req.contextPath());
			System.out.println("req uri : " + req.uri());
			System.out.println("req url : " + req.url());
			System.out.println("req servletPath : " + req.servletPath());
			
			Vector <String> exceptUri = new Vector();
			exceptUri.add("/index.html");
			exceptUri.add("/favicon.ico");
			exceptUri.add("/authentication");
			String uri = req.uri();
			if (exceptUri.contains(uri)) {
				
			} else {
				JSONObject resJson = sm.checkToken(req, res);
				if(!"0".equals(resJson.getAsString("status"))) {
					//halt(401, resJson.toJSONString());
				}
			}
		});
		
		post("/authentication", (req, res) -> sm.auth(req, res));
		post("/admin/update", (req, res) -> sm.scimUpdate(req, res));

		path("/scim/" + version, () -> {
			// Account
			get   ("/Users", (req, res) -> sm.getUserParam(req, res));
			get   ("/Users/:id", (req, res) -> sm.getUser(req, res));
			post  ("/Users", (req, res) -> sm.createUser(req, res));
			put   ("/Users/:id", (req, res) -> sm.updateUser(req, res));
			patch ("/Users/:id", (req, res) -> sm.patchUser(req, res));
			delete("/Users/:id", (req, res) -> sm.deleteUser(req, res));
			
			// Group
			get   ("/Groups", (req, res) -> sm.getGroupParam(req, res));
			get   ("/Groups/:id", (req, res) -> sm.getGroup(req, res));
			post  ("/Groups", (req, res) -> sm.createGroup(req, res));
			put   ("/Groups/:id", (req, res) -> sm.updateGroup(req, res));
			patch ("/Groups/:id", (req, res) -> sm.patchGroup(req, res));
			delete("/Groups/:id", (req, res) -> sm.deleteGroup(req, res));
			
			// SCIM
			get   ("/ServiceProviderConfig", (req, res) -> sm.getServiceProviderConfig(req, res));
			get   ("/ServiceProviderConfig/:path", (req, res) -> sm.getServiceProviderConfig(req, res));
			get   ("/ResourceTypes", (req, res) -> sm.getResourceTypes(req, res));
			get   ("/Schemas", (req, res) -> sm.getSchemas(req, res));
			post  ("/Bulk", (req, res) -> sm.setBulk(req, res));
			
		});
		
		// Search for Users and Groups
		post("/scim/.search", (req, res) -> sm.search(req, res));
		
		// Search = GET 
		// https://example.com/{v}/{resource}?ﬁlter={attribute}{op}{value}&sortBy={attributeName}&sortOrder={ascending|descending}

		get("/command/:version/:command", (req, res) -> sm.command(req, res));
		get("/command/:version/ProvisioningConfig", (req, res) -> sm.provisioning(req, res));
		

		path("/test", () -> {
			post("/upload", (req, res) -> sm.sendAgentLog(req, res));
		});
		
		after("/*", (req, res) -> {res.header("Content-Type", "application/scim+json"); wlog.accessLog(req, res);});
		
		long endTime = System.currentTimeMillis();
		systemLog.info("Ready to Service  ********************\n\tService Loading Time : " + (endTime - startTime)  + " (ms)\n");
	}
	
	private String notImplements(String method) {
		JSONObject error = new JSONObject();
		
		JSONObject json = new JSONObject();
		json.put("code", 501);
		json.put("method", method);
		json.put("message", "This API is not Implemented.");
		
		error.put("error", json);
		
		return error.toJSONString();
		
	}


	private String provisioning(Request req, Response res) {
		return new ProvisioningConfig().getProvisioningConfig();
	}


	private String getUser(Request request, Response response) {
		return new Controller().getUser(request, response);
	}
	
	private String getUserParam(Request request, Response response) {
		return new Controller().getUserParam(request, response);
	}
	
	private String createUser(Request request, Response response) {
		return new Controller().createUser(request, response);
	}
	
	private String updateUser(Request request, Response response) {
		return new Controller().updateUser(request, response);
	}
	
	private String patchUser(Request request, Response response) {
		return new Patch().patchUser(request, response);
	}
	
	private String deleteUser(Request request, Response response) {
		return new Controller().deleteUser(request, response);
	}
	
	
	
	private String getGroup(Request request, Response response) {
		return new Controller().getGroup(request, response);
	}
	
	private String getGroupParam(Request request, Response response) {
		return new Controller().getGroupParam(request, response);
	}
	
	private String createGroup(Request request, Response response) {
		return new Controller().createGroup(request, response);
	}
	
	private String updateGroup(Request request, Response response) {
		return new Controller().updateGroup(request, response);
	}
	
	private String patchGroup(Request request, Response response) {
		return new Patch().patchGroup(request, response);
	}
	
	private String deleteGroup(Request request, Response response) {
		return new Controller().deleteGroup(request, response);
	}

	private String getServiceProviderConfig(Request request, Response response) {
		return conf.getServiceConfig(request, response);
	}
	
	private String getResourceTypes(Request request, Response response) {
		return notImplements("GET");
	}
	
	private String getSchemas(Request request, Response response) {
		return notImplements("GET");
	}
	
	private String setBulk(Request request, Response response) {
		return new Bulk().setBulk(request, response);
	}
	
	private String search(Request request, Response response) {
		return notImplements("GET");
	}
	
	
	private String command(Request req, Response res) {
		String ver = req.params(":version");
		String cmd = req.params(":command");
		
		System.out.println(ver + ":" + cmd);
		
		if ("v1.0".equals(ver) && "reset".equals(cmd)) {
			MemRepository repo = MemRepository.getInstance();
			repo.reset("Users");
			repo.reset("Groups");
			repo.save();
		}
	
		return "Clear";
	}


	private String auth(Request req, Response res) {
		// 정상로그인 시 manger로 이동
		// 오류시 index.html로 다시 이동
		String id = req.raw().getParameter("puserid");
		String pwd = req.raw().getParameter("ppasswd");
		
		if (id.equals("111")) {
			res.cookie("token", "eyJ0eXAiOiJKV1QiLCJpc3N1ZURhdGUiOjE0OTYxOTk5NjEyMzMsImFsZyI6IkhTMjU2In0.eyJzdWIiOiJTQ0lNL1VTRVIiLCJpc3MiOiJTQ0lNIiwiaWF0IjoxNDk2MTk5OTYxLCJleHAiOjE1Mjc3MzU5NjEsInByb3ZpZGVyIjoidHJ1ZSIsInNlcnZlcklwIjoiMTI3LjAuMC4xIiwiY29scGFydCI6IkEwMDEiLCJhcHBDb2RlIjoiRGVtb0hSIn0.utQJYMdIRCJUMM5On3-G6Ay0mKBZ2aWnLKvcQ68kCl0");
			res.redirect("/manager/");
		} else {
			res.redirect("/index.html");
		}
		return "";
	}


	private JSONObject checkToken(Request request, Response response) {
		return new JWTDecode().decodeToken(request, response);
	}


	private Object scimUpdate(Request req, Response res) {
		return new Controller().update(req, res);
	}


	private void test(Request req, Path tempFile) {
		Collection<Part> parts = null;
		try {
			parts = req.raw().getParts();
			Iterator<Part> iters = parts.iterator();
			
			while(iters.hasNext()) {
				Part part = iters.next();
				InputStream input = part.getInputStream();
				
				Files.copy(input, tempFile, StandardCopyOption.REPLACE_EXISTING);
				logInfo(part, tempFile);
			}
			
		} catch (IOException | ServletException e1) {
			e1.printStackTrace();
		}
	}


	private String sendAgentLog(Request req, Response res) {
			
			File uploadDir = new File("tmp/logUpload");
			uploadDir.mkdirs();
			String tmp_file_prefix = "agent_";
			String tmp_file_sufix=".log";
			
			 try {
				Path tempFile = Files.createTempFile(uploadDir.toPath(), tmp_file_prefix, tmp_file_sufix);
				req.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement(""));
				
	//			test(req, tempFile);
				String key = req.raw().getParameter("key");
				System.out.println(req.raw().getParameter("test1"));
				System.out.println(URLDecoder.decode(req.raw().getParameter("test2")));
				InputStream input = req.raw().getPart(key).getInputStream();
	            Files.copy(input, tempFile, StandardCopyOption.REPLACE_EXISTING);
	            logInfo(req, tempFile, key);
				
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ServletException e) {
				e.printStackTrace();
			}
			return "{result:\"ok\"}";
		}


	private String getFileName(Part part) {
	    	
	//    	System.out.println(part.getContentType());
	//    	System.out.println(part.getName());
	//    	System.out.println(part.getSubmittedFileName());
	//    	System.out.println(part.getHeaderNames());
	//    	System.out.println(part.getHeader("content-disposition"));
	//        for (String cd : part.getHeader("content-disposition").split(";")) {
	//            if (cd.trim().startsWith("filename")) {
	//                return cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
	//            }
	//        }
	        return part.getSubmittedFileName();
	    }

	// methods used for logging
	private void logInfo(Part part, Path tempFile) throws IOException, ServletException {
	    System.out.println("Uploaded file '" + getFileName(part) + "' saved as '" + tempFile.toAbsolutePath() + "'");
	}

	private void logInfo(Request req, Path tempFile, String key) throws IOException, ServletException {
		System.out.println("Uploaded file '" + getFileName(req.raw().getPart(key)) + "' saved as '" + tempFile.toAbsolutePath() + "'");
	}

}
