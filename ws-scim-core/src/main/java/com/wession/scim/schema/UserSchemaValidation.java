package com.wession.scim.schema;

import java.io.IOException;
import java.util.Iterator;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingMessage;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.wession.common.JsonUtils;
import com.wession.scim.intf.JsonFilePath;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

public class UserSchemaValidation implements JsonFilePath {

	public static JSONObject userSchemaCheck(String jsonText) {
		JSONObject res = new JSONObject();

		try {
			JsonNode userSchema = JsonUtils.getJsonNodeFile(_user_schema_pattern);
			JsonNode userData = JsonUtils.getJsonNodeString(jsonText);

			JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
			JsonSchema schema = factory.getJsonSchema(userSchema);
			ProcessingReport report = schema.validate(userData);
			
			res.put("isValid", report.isSuccess());
			
			JSONArray jarr = new JSONArray();
			Iterator<ProcessingMessage> iter = report.iterator();
			while (iter.hasNext()) {
				JsonNode jn = iter.next().asJson();
				jarr.add((JSONObject) JSONValue.parse(jn.toString()));
				
//				테스트
//				Iterator<Entry<String, JsonNode>> it = jn.fields();
//				while (it.hasNext()) {
//					Entry<String, JsonNode> entry = it.next();
//					System.out.println(entry.getKey() + ":" + entry.getValue());
//				}
			}
			if(!jarr.isEmpty()) {
				res.put("report", jarr);
			}
			
		} catch (ProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return res;
	}
}
