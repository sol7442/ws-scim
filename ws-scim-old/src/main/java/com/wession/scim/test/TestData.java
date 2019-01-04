package com.wession.scim.test;

import java.util.Random;

import com.wession.scim.controller.Patch;
import com.wession.scim.exception.ScimSchemaException;
import com.wession.scim.simple.MemRepository;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

public class TestData {
	// Test Data 생성용
	private static final String FamilyNames = "김구이박정김이전유정홍천방김박이최최채황나남동라김박이장방성서안어주김박이장조표하강장우민강전";
	private static final String Name = "미마문면연안강승중용종희태준남순성민명준준철주섭선상리용성성기민일은준상한택은미주우신장철형수홍은창철수정대희연동준서훈영호영연호삼미숙강태";
	private static final String ABC = "ABCDEFGHIJKLMNOPQR"; 
	
	public static String getRandomName() {
		Random generator = new Random();
		int num1, num2, num3;
		num1 = generator.nextInt(FamilyNames.length());
		num2 = generator.nextInt(Name.length());
		num3 = generator.nextInt(Name.length());
		return (FamilyNames.charAt(num1) + "" + Name.charAt(num2) + Name.charAt(num3));
	}
	
	public static String getRandomUserID() {
		Random generator = new Random();
		int num1, num2, num3, num4, num5, num6;
		num1 = generator.nextInt(10);
		num2 = generator.nextInt(10);
		num3 = generator.nextInt(10);
		num4 = generator.nextInt(10);
		num5 = generator.nextInt(1000);
		num6 = generator.nextInt(1000);
		String id = ((num1*num2+num4) % 10) + "" + num1 + "" +  ((num2*num3+num5) % 10) + num2 + "" +  ((num3*num1+num6) % 10) + num3 + String.format("%03d", num4); 
		//String id = ((num1*num2) % 10) + "" + num1 + "" +  ((num2*num3) % 10) + num2 + "" +  ((num3*num1) % 10) + num3 ;
		return "WS" + id;
	}
	
	public static void main(String [] args) {
		System.out.println(getRandomUserID() + " " + getRandomName());
		String json_string = "{\"id\":\"aaa\",\"id\"}";
		try {
			JSONObject json = (JSONObject) JSONValue.parse(json_string);
			if (json == null) {
				System.out.println("Parse Error");
			} else {
				System.out.println(json.toJSONString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		MemRepository repo = MemRepository.getInstance(); 
		repo.load();
		System.out.println(repo.getSize("Users"));
		
		Patch patch = new Patch();
		JSONObject json_patch = new JSONObject();
		JSONArray schemas = new JSONArray();
		schemas.add("urn:ietf:params:scim:api:messages:2.0:PatchOp");
		json_patch.put("schemas", schemas);
		JSONArray Operations = new JSONArray();
			JSONObject operation = new JSONObject();
			operation.put("op", "replace");
			operation.put("path", "title");
				JSONArray values = new JSONArray();
				/*
					JSONObject value1 = new JSONObject();
					value1.put("title", "테스트");
					JSONObject value2 = new JSONObject();
					value2.put("title", "테스트2");
				values.add(value1);
				values.add(value2);
				*/
				values.add("NewTitle");
				//values.add("OldTitle");
			operation.put("value", values);
		Operations.add(operation);
		json_patch.put("Operations", Operations);
		
		/*
		 * { "schemas":
		       ["urn:ietf:params:scim:api:messages:2.0:PatchOp"],
		     "Operations":[
		       {
		        "op":"add",
		        "path":"members",
		        "value":[
		         {
		           "display": "Babs Jensen",
		           "$ref":
		   "https://example.com/v2/Users/2819c223...413861904646",
		           "value": "2819c223-7f76-453a-919d-413861904646"
		         }
		        ]
		       },
		       ... + additional operations if needed ...
		     ]
		   }
		 */
		String scim_id = "6b17994f-7b21-4cc9-902a-3c8c30ed44d0";
		
		try {
			JSONObject result = patch.process2("Users", scim_id, json_patch);
			System.out.println(">>> Result \n\t" + result.toJSONString());
		} catch (ScimSchemaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
