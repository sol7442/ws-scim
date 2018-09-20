package com.wession.jwt;

import org.slf4j.Logger;

import com.wession.common.CommUtils;
import com.wession.common.WessionLog;
import com.wession.scim.schema.Error;

import net.minidev.json.JSONObject;
import spark.Request;
import spark.Response;

public class JWTDecode {
	
	private Logger processLog = new WessionLog().getProcessLog();
	

	public JSONObject decodeToken(Request request, Response response) {
		
		String authentication = request.headers("Authorization");
		
		if (CommUtils.isEmpty(authentication)) {
			Error err = new Error(400, "Authorization is empty.");
			response.status(400);
			processLog.error(err.toJSONString());
			return err;
		}
		String [] encTokens = authentication.split(" ");
		
		if (encTokens.length < 2) {
			Error err = new Error(400, "The access token type does not match.");
			response.status(400);
			processLog.error(err.toJSONString());
			return err;
		}
		
		JSONObject resJson = JWTToken.jwtDecode(encTokens[1]);
		if(!(""+resJson.get("status")).equals("0")) {
			response.status(400);
			processLog.error(resJson.toJSONString());
		} else {
			response.status(200);
		}
		return resJson;
	}

}
