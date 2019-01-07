package com.wowsanta.scim.service;

import static spark.Spark.after;
import static spark.Spark.before;
import static spark.Spark.path;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;
import static spark.Spark.delete;
import static spark.Spark.patch;
import static com.wowsanta.scim.service.JsonUtil.*;

import com.wowsanta.scim.control.SCIMServiceController;
import com.wowsanta.scim.log.SCIMLogger;
import com.wowsanta.scim.service.admin.LoginService;

public class ServiceProviderController implements SCIMServiceController{
	private String version;
	@Override
	public void setVersion(String version) {
		this.version = version;
	}
	@Override
	public void regist() {
		regist_before();
		regist_admin();
		regist_scim();
		regist_after();
	}
	private void regist_before() {
		path("/admin", () -> {
			before("", (req, res) -> {
				
			});		
			before("/*", (req, res) -> {
				String authentication = req.headers("Authorization");
				System.out.println("authentication token : " + authentication);
			});
		});
	}
	private void regist_admin() {
		path("/admin", () -> {
			post  ("/login", new LoginService(), json());
		});
	}
	private void regist_scim() {
		path("/scim/" + version, () -> {
//			// Account
//			get   ("/Users", (req, res) -> sm.getUserParam(req, res));
//			get   ("/Users/:id", (req, res) -> sm.getUser(req, res));
//			post  ("/Users", (req, res) -> sm.createUser(req, res));
//			put   ("/Users/:id", (req, res) -> sm.updateUser(req, res));
//			patch ("/Users/:id", (req, res) -> sm.patchUser(req, res));
//			delete("/Users/:id", (req, res) -> sm.deleteUser(req, res));
//			
//			// Group
//			get   ("/Groups", (req, res) -> sm.getGroupParam(req, res));
//			get   ("/Groups/:id", (req, res) -> sm.getGroup(req, res));
//			post  ("/Groups", (req, res) -> sm.createGroup(req, res));
//			put   ("/Groups/:id", (req, res) -> sm.updateGroup(req, res));
//			patch ("/Groups/:id", (req, res) -> sm.patchGroup(req, res));
//			delete("/Groups/:id", (req, res) -> sm.deleteGroup(req, res));
//			
//			// SCIM
//			get   ("/ServiceProviderConfig", (req, res) -> sm.getServiceProviderConfig(req, res));
//			get   ("/ServiceProviderConfig/:path", (req, res) -> sm.getServiceProviderConfig(req, res));
//			get   ("/ResourceTypes", (req, res) -> sm.getResourceTypes(req, res));
//			get   ("/Schemas", (req, res) -> sm.getSchemas(req, res));
//			post  ("/Bulk", (req, res) -> sm.setBulk(req, res));
			
		});
	}
	public void regist_after() {
		after("/*", (req, res) -> {
			res.header("Content-Type", "application/scim+json");
			SCIMLogger.access("{} -> {} {} : {} ",req.ip(), req.requestMethod(), req.uri(),  res.status());
			}
		);
	}

}

