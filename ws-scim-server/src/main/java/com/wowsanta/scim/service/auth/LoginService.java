package com.wowsanta.scim.service.auth;

import static com.wowsanta.scim.server.JsonUtil.json_parse;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;
import com.wowsanta.scim.exception.SCIMError;
import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.log.SCIMLogger;
import com.wowsanta.scim.obj.SCIMAdmin;
import com.wowsanta.scim.obj.SCIMUser;
import com.wowsanta.scim.policy.impl.DefaultPasswordPoilcy;
import com.wowsanta.scim.repository.SCIMRepositoryManager;
import com.wowsanta.scim.resource.SCIMCode;
import com.wowsanta.scim.resource.SCIMSystemRepository;
import com.wowsanta.scim.resource.ServiceResult;
import com.wowsanta.scim.resource.user.LoginUser;
import com.wowsanta.scim.schema.SCIMConstants;
import com.wowsanta.scim.sec.SCIMJWTToken;
import com.wowsanta.scim.service.audit.AuditService;

import spark.Request;
import spark.Response;
import spark.Route;

public class LoginService  {
	Logger logger = LoggerFactory.getLogger(LoginService.class);


	public Route login() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				//ServiceResult result = null;
				
				LoginUser user = null;
				try {
					String[] id_pw = parseLoginData(request);
					
					//1. 사용자 가져오기
					user = getLoginUserFromRepository(id_pw[0]);
					
					//2. 사용자 인증하기
					authenticate(user, id_pw[1]);
					
					//3. 토큰 생성
					Map<String,Object> result_data= issueJWTToken(user);
					
					//4. update login time
					updateLoginTime(user);
					
					//5. 세션 저장 하기
					request.session(true).attribute("user",user);
					
					//6. http 응답 
					response.status(200);
					return result_data;
				}catch(SCIMException e) {
					response.status(e.getError().getStatus());
					return e.getError();
					
				}catch (Exception e) {
					logger.info("LOGIN Exception : -- : {} ", e.getMessage());
					response.status(SCIMError.InternalServerError.getStatus());
					return SCIMError.InternalServerError;
				}
				finally {
					logger.info("LOGIN  : {} ", user);
				}
			}

			private void updateLoginTime(LoginUser user) {
				try {
					SCIMSystemRepository systemRepository = SCIMRepositoryManager.getInstance().getSystemRepository();
					systemRepository.updateLoginTime(user.getUserId());
				}
				catch (Exception e) {
					SCIMException scim_ex = new SCIMException("Resource Repository Exception", SCIMError.InternalServerError, e);
					logger.error(scim_ex.getMessage(), scim_ex);
				}
			}

		};
	}
	private String[] parseLoginData(Request request) throws SCIMException {
		String[] id_pw = new String[2];
		try {
			
			JsonObject request_json = json_parse(request.body());
			id_pw[0] = request_json.get("id").getAsString();
			id_pw[1] = request_json.get("pw").getAsString();
			
		}catch (Exception e) {
			throw new SCIMException("Lonin Data Parse Error", SCIMError.BadRequest, e);
		}
		
		return id_pw;
	}
	
	private  Map<String,Object> issueJWTToken(LoginUser user) throws SCIMException {
		SCIMJWTToken token = new SCIMJWTToken();
		token.setUserId(user.getUserId());
		token.setUserName(user.getUserName());
		token.setUserType(user.getType().toString());
		
		String str_token = token.issue();
		
		Map<String,Object> response_map = new HashMap<String, Object>();
		response_map.put("token", str_token);
		response_map.put("user", user);
		
		return response_map;
	}
	
	private  void authenticate(LoginUser user, String pw) throws SCIMException {
		
		DefaultPasswordPoilcy policy = new DefaultPasswordPoilcy();
		String hashed_pw = policy.encrypt(pw);
		
		if(user.getPassword() == null) {
			logger.info("Authentication Pass {} ",user);
			return;
		}
		if(!user.getPassword().equals(hashed_pw)) {
			throw new SCIMException("User Password Not Matched", SCIMError.Unauthorized);
		}
		
		
//		try {
			
//		}catch (Exception e) {
//			
//			SCIMException scim_ex = new SCIMException("User Password Encrypt Failed", SCIMError.InternalServerError, e);
//			logger.error(scim_ex.getMessage(), scim_ex);
//			
//			throw scim_ex;
//		}
	}

	private  LoginUser getLoginUserFromRepository(String id) throws SCIMException {
		LoginUser user = null;
		try {
			SCIMSystemRepository systemRepository = SCIMRepositoryManager.getInstance().getSystemRepository();
			user = systemRepository.getLoginUser(id);
			
			if(user == null) {
				SCIMException scim_ex = new SCIMException("User Not Found : " + id, SCIMError.Unauthorized);
				throw scim_ex;
			}
			
		}
		catch (SCIMException e) {
			throw e;
		}
		catch (Exception e) {
			SCIMException scim_ex = new SCIMException("Resource Repository Exception", SCIMError.InternalServerError, e);
			logger.error(scim_ex.getMessage(), scim_ex);
			
			throw scim_ex;
		}
		return user;
	}

}
