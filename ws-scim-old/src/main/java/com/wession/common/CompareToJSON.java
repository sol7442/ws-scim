package com.wession.common;

import java.io.FileReader;
import java.util.Iterator;


import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

public class CompareToJSON {
	
	//TODO 추후삭제 개발에만 사용
	public static void main(String[] args) {
		String up_file	= "./sample/userUpdate.json";	//업데이트
		String org_file	= "./sample/userOrigin.json";	//원본
		try {
			JSONObject upObj	= (JSONObject) JSONValue.parse(new FileReader(up_file));
			JSONObject orgObj	= (JSONObject) JSONValue.parse(new FileReader(org_file));
			
			compareTo(upObj, orgObj);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static JSONObject compareTo(JSONObject upObj, JSONObject orgObj) {
		
		JSONObject arrKey	= new JSONObject();
		
		String gubun = "U";
		JSONObject resObj	= compareTo(upObj, orgObj, arrKey, gubun);
		
		System.out.println("==update Data =================================\n" + toPrettyFormat(resObj.toJSONString()));
		
		gubun = "D";
		JSONObject delObj = compareTo(orgObj, upObj, arrKey, gubun);
		System.out.println("==delete Data =================================\n" + toPrettyFormat(delObj.toJSONString()));
		
		resObj.merge(delObj);
		System.out.println("==merge  Data =================================\n" + toPrettyFormat(resObj.toJSONString()));
		
		return resObj;
		
	}
	
	//TODO 추후삭제 개발에만 사용
	public static String toPrettyFormat(String jsonString) {
//		JsonParser parser = new JsonParser();
//		JsonObject json = parser.parse(jsonString).getAsJsonObject();
//		Gson gson = new GsonBuilder().setPrettyPrinting().create();
//		String prettyJson = gson.toJson(json);
//		return prettyJson;
		return null;
	}
	
	/**
	 * compareTo 
	 * 기존데이터와신규데이터를 비교하여 수정항목 추출
	 * 업데이트할 객체의 데이터 파싱해서 key,value를 꺼낸다
	 * @param upObj		: 업데이트데이터
	 * @param orgObj	: 원데이터
	 * @param arrKey	: 비교할배열의value항목(primarykey).. 변경될 가능성 많음..
	 */
	public static JSONObject compareTo(JSONObject upObj, Object orgObj, JSONObject arrKey, String flag) {
		JSONObject resJSON = new JSONObject(); //리턴용
		try {
			Iterator<String> keys = upObj.keySet().iterator();
			while (keys.hasNext()) {
				String key		= keys.next();
				Object obj		= upObj.get(key);
				Object compObj	= orgObj;
				
				//
				if (orgObj instanceof JSONObject) {
					compObj = ((JSONObject) orgObj).get(key);
				}
				
				// JSONArray
				if (obj instanceof JSONArray) {
					JSONArray jarr = new JSONArray();
					for (Object object : (JSONArray) obj) {
						arrKey.put("pk", ((JSONObject)object).get("value"));
						JSONObject json = compareTo((JSONObject) object, compObj, arrKey, flag);
						if(!json.isEmpty()) {
							jarr.add(json);
						}
					}
					if(!jarr.isEmpty()) {
						resJSON.put(key, jarr);
					}
					arrKey.clear();
					
					// JSONObject
				} else if (obj instanceof JSONObject) {
					JSONObject json = compareTo((JSONObject) obj, compObj, arrKey, flag);
					if(!json.isEmpty()) {
						resJSON.put(key, json);
					}
					
				} else {
					
					JSONObject json = new JSONObject();
					if("U".equals(flag)) {
						json = compareTo(key, obj, compObj, arrKey);
						
					} else {
						json = compareTo2(key, obj, compObj, arrKey);
						
					}
					if(!json.isEmpty()) {
						jsonParse(json, resJSON);
					}
				}
			}//end while
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resJSON;
	}
	
	/**
	 * compareTo 
	 * 원데이터를 파싱해서 파라미터와 비교하여 결과를 리턴
	 * @param k			: JSON KEY
	 * @param v			: JSON VALUE
	 * @param compObj	: 원데이터객체
	 * @param akey		: 배열의경우대상 pk(key, value로 넘어오기 때문에 어떤 대상의 데이터인지 알수없어 일단 value로 구분..하는데 저 항목이 null허용임.참고)
	 * @return JSONObject : 변경항목이 존재하면 변경할 데이터 담아서 리턴
	 */
	public static JSONObject compareTo(String k, Object v, Object compObj, JSONObject akey) {
		JSONObject resJSON = new JSONObject();
		try {
			if (compObj instanceof JSONArray) {
				//배열비교
				boolean isArrMatch = false;
				for (Object oObj : (JSONArray) compObj) {
					
					/*TODO*/
					//address 개발
					
					if(nullString(akey.get("pk")).equals(nullString(((JSONObject) oObj).get("value")))) {
						isArrMatch = true;
						
						if(!nullString(v).equals(nullString(((JSONObject) oObj).get(k)))) {
							resJSON.put(k, v);
							resJSON.put("pk", akey.get("pk"));
						}
					}
				}
				
				//배열데이터 일치하는 내용없음-신규등록
				if(!isArrMatch) {
					resJSON.put(k, v);
					resJSON.put("pk", akey.get("pk"));
				}
				
				/*TODO - 기존데이터 삭제 항목 체크
				 */
			} else if (compObj instanceof JSONObject) {
				//객체비교
				if(!nullString(v).equals(nullString(((JSONObject) compObj).get(k)))) {
					resJSON.put(k, v);
				}

			} else {
				if(!nullString(v).equals(nullString(compObj))) {
					resJSON.put(k, v);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return resJSON;
	}
	
	
	public static JSONObject compareTo2(String k, Object v, Object compObj, JSONObject akey) {
		JSONObject resJSON = new JSONObject();
		try {
			if (compObj instanceof JSONArray) {
				//배열비교
				boolean isArrMatch = false;
				for (Object oObj : (JSONArray) compObj) {
					
					/*TODO*/
					//address 개발
					
					if(nullString(akey.get("pk")).equals(nullString(((JSONObject) oObj).get("value")))) {
						isArrMatch = true;
						
						if(isNotNull(v) && isNull(((JSONObject) oObj).get(k))) {
							resJSON.put(k, "");
							resJSON.put("pk", akey.get("pk"));
						}
					}
				}
				
				//배열데이터 일치하는 내용없음-삭제
				if(!isArrMatch) {
					resJSON.put(k, "");
					resJSON.put("pk", akey.get("pk"));
				}
				
				/*TODO - 기존데이터 삭제 항목 체크
				 */
			} else if (compObj instanceof JSONObject) {
				//객체비교
				if(isNotNull(v) && isNull(((JSONObject) compObj).get(k))) {
					resJSON.put(k, "");
				}

			} else {
				if(isNotNull(v) && isNull(compObj)) {
					resJSON.put(k, "");
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return resJSON;
	}
	
	
	
	
	public static Object nullString(Object o) {
		return o == null ? "" : o;
	}
	
	public static boolean isNull(Object o) {
		return o == null ? true : false; 
	}
	
	public static boolean isNotNull(Object o) {
		return !isNull(o); 
	}
	
	
	/**
	 * JSONArray 
	 * @param jsonObject, JSONObject 의 key, value 파싱
	 */
	public static void jsonParse(JSONObject jsonObject, JSONObject res) {
		try {
			Iterator<String> keys = jsonObject.keySet().iterator();
			while (keys.hasNext()) {
				String key = keys.next();
				
				if (jsonObject.get(key) instanceof JSONArray) {
					JSONArray jsonArray = (JSONArray) jsonObject.get(key);
					
					for (Object object : jsonArray) {
						jsonParse((JSONObject) object, res);
					}
					
				} else if (jsonObject.get(key) instanceof JSONObject) {
					jsonParse((JSONObject) jsonObject.get(key), res);

				} else {
					res.put(key, jsonObject.get(key));
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
