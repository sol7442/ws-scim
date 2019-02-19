package com.wowsanta.scim.service;

import static com.wowsanta.scim.server.JsonUtil.json_parse;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonObject;
import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.log.SCIMLogger;
import com.wowsanta.scim.obj.SCIMUser;
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
			
			SCIMUser user = this.service.login(id,pw);
			
			SCIMJWTToken token = new SCIMJWTToken();
			token.setUserId(user.getId());
			token.setUserName(user.getUserName());
			user.setPassword("");
			
			String str_token = token.issue();
	
			Map<String,Object> response_map = new HashMap<String, Object>();
			response_map.put("token", str_token);
			response_map.put("user", user);
			
//			SCIMJWTToken validate_token = new SCIMJWTToken();
//			SCIMUser validate_user = validate_token.verify(str_token);
			
			result.setCode(SCIMCode.SUCCESS);
			result.setMessage("sucess");
			result.setData(response_map);
			
			request.session(true).attribute("user",user);
			
		}catch(SCIMException e) {
			result.setCode(SCIMCode.FAILED);
			result.setMessage("failed : " + e.getMessage());
			result.setData(e);
			
			request.session().removeAttribute("user");
		}finally {
			SCIMLogger.proc("Login Result : {}", result);
		}
		
		return result;
	}

}
