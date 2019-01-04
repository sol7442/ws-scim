package com.wession.scim.test;

import com.wession.common.CommUtils;
import com.wession.common.JsonUtils;
import com.wession.scim.controller.Password;
import com.wession.scim.schema.UserSchemaValidation;

import net.minidev.json.JSONObject;

public class ValidTest {

	public static void main(String[] args) {
		ValidTest test = new ValidTest();
//		test.passwordValid();
		test.schemaValid();
	}
	
	public void passwordValid() {
		String value[] = { "1234abcd@", "abcdABCD#", "1234ABCD@", "123abC@", "1234abCD@" };

		for (String s : value) {
			System.out.println(s + " : " + Password.checkPassword("kmsTest", s));
		}
	}

	public void schemaValid() {
		String file_path = "./sample/userSchemaValue.json";
		JSONObject json = JsonUtils.getJSonFile(file_path);
		JSONObject result = UserSchemaValidation.userSchemaCheck(json.toString());
		CommUtils.saveFile(result.toString(), "./sample/validResult.json");
		
		System.out.println(result.toJSONString());
	}
}
