package com.wession.scim.server;

import com.wession.scim.controller.Groups;
import com.wession.scim.controller.Users;
import com.wession.scim.server.admin.updateSCIM;
import com.wession.scim.simple.MemRepository;

import net.minidev.json.JSONObject;
import spark.Request;
import spark.Response;

public class Controller {
	private MemRepository repo = MemRepository.getInstance();

	private String notImplements(String method) {
		JSONObject error = new JSONObject();
		
		JSONObject json = new JSONObject();
		json.put("code", 501);
		json.put("method", method);
		json.put("message", "This API is not Implemented.");
		
		error.put("error", json);
		
		return error.toJSONString();
		
	}
	
	/*
	 * ====================== Resource Groups ========================
	 */
	
	protected String getGroup(Request request, Response response) {
		return new Groups().getGroup(request, response);
	}

	protected String getGroupParam(Request request, Response response) {
		return new Groups().getGroupParam(request, response);
	}

	protected String createGroup(Request request, Response response) {
		return new Groups().createGroup(request, response);
	}

	protected String updateGroup(Request request, Response response) {
		return new Groups().updateGroup(request, response);
	}

	protected String patchGroup(Request request, Response response) {
		return notImplements(request.requestMethod());
	}

	protected String deleteGroup(Request request, Response response) {
		return new Groups().deleteGroup(request, response);
	}

	/*
	 * ====================== Resource Users ========================
	 */

	protected String getUser(Request request, Response response) {
		return new Users().getUser(request, response);
	}

	protected String getUserParam(Request request, Response response) {
		return new Users().getUserParam(request, response);
	}

	protected String createUser(Request request, Response response) {
		return new Users().createUser(request, response);
	}

	protected String updateUser(Request request, Response response) {
		return new Users().updateUser(request, response);
	}

	protected String patchUser(Request request, Response response) {
		return notImplements(request.requestMethod());
	}

	protected String deleteUser(Request request, Response response) {
		return new Users().deleteUser(request, response);
	}
	
	/*
	 * 
	 */

	protected String update(Request request, Response response) {
		return new updateSCIM().patch(request, response);
	}
}
