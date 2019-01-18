package com.wowsanta.scim.service;

import static com.wowsanta.scim.server.JsonUtil.json_parse;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonObject;
import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.log.SCIMLogger;
import com.wowsanta.scim.resource.SCIMAdmin;
import com.wowsanta.scim.resource.SCIMCode;
import com.wowsanta.scim.resource.ServiceResult;
import com.wowsanta.scim.sec.SCIMJWTToken;
import com.wowsanta.scim.service.auth.LoginService;

import spark.Request;
import spark.Response;
import spark.Route;

public class LoginController implements Route {
	private LoginService service;
	public LoginController(LoginService service) {
		this.service = service;
	}
	@Override
	public Object handle(Request request, Response response) throws Exception {
		ServiceResult result = new ServiceResult();
		try {
			
			JsonObject request_json = json_parse(request.body());
			String id = request_json.get("id").getAsString();
			String pw = request_json.get("pw").getAsString();
			
			SCIMAdmin admin = this.service.login(id,pw);
			
			SCIMJWTToken token = new SCIMJWTToken();
			token.setUserId(admin.getId());
			token.setUserName(admin.getName());
			token.setUserType(admin.getType());
			
			String str_token = token.issue("ServiceName","SCIM_KEY_@1234");
	
			Map<String,Object> response_map = new HashMap<String, Object>();
			response_map.put("token", str_token);
			response_map.put("admin", admin);
			
			result.setCode(SCIMCode.SUCCESS);
			result.setMessage("sucess");
			result.setData(response_map);
			
			request.session(true).attribute("admin",admin);
			
		}catch(SCIMException e) {
			result.setCode(SCIMCode.FAILED);
			result.setMessage("failed : " + e.getMessage());
			result.setData(e);
			
			request.session().removeAttribute("admin");
		}finally {
			SCIMLogger.proc("Login Result : {}", result);
		}
		
		return result;
	}

}