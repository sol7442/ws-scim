package com.wession.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
import java.util.Vector;

import com.wession.scim.Const;
import com.wession.scim.schema.ListResponse;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import spark.QueryParamsMap;

public class ScimUtils {
	
//	private static String site_url = System.getProperty("scim_service_domain");	//"yoursite.com";
//	private static String service_port = System.getProperty("scim_service_port"); //"8080"
//	private static String service_uri = System.getProperty("scim_service_uri"); //"/scim/v2.0";
//	private static String ref = "http://" + site_url + ":" + service_port + service_uri;
	
	public static String makeId() {
		return UUID.randomUUID().toString();
	}

	public static String makeRef(String ref, String id, String resourceType) {


		if (resourceType.equals("User")) {
			ref = ref + "/Users/" + id;
		} else if (resourceType.equals("Group")) {
			ref = ref + "/Groups/" + id;
		} else {
			ref = ref + resourceType + "/" + id;
		}

		return ref;
	}

	public static String findValue(JSONObject jObj, String findKey) {

		Set<String> keys = jObj.keySet();

		for (String key : keys) {
			if (key.equalsIgnoreCase(findKey)) {
				return jObj.getAsString(key);
			}
			if (jObj.get(key) instanceof JSONObject) {
				return findValue((JSONObject) jObj.get(key), findKey);
			}
		}
		
		 return null;
	}
	
	public static String findArray(JSONArray jArry, String findKey) {

		if (jArry == null) return null;
		for (Object obj: jArry) {
			JSONObject jObj = (JSONObject) obj;
			
			Set<String> keys = jObj.keySet();

			for (String key : keys) {
				if (key.equalsIgnoreCase(findKey)) {
					return jObj.getAsString(key);
				}
				if (jObj.get(key) instanceof JSONObject) {
					return findValue((JSONObject) jObj.get(key), findKey);
				}
			}
		}
		
		 return null;
	}
	
	public static boolean checkSchemas(JSONArray arry, String schema) {
		boolean ret = false;
		Iterator itor = arry.iterator();
		while (itor.hasNext()) {
			String json_schema = (String) itor.next();
			if (json_schema.equals(schema)) {
				ret = true;
				break;
			}
		}
		return ret;
	}
	
	public static  String getClassName(Object obj) {
		return obj.getClass().getSimpleName();
	}

	public static  boolean checkClassName(Object obj, String simpleName) {
		if (simpleName.equals(getClassName(obj)))
			return true;
		else
			return false;
	}
	
	public static String getJSONType(Object data) {
		String data_type = "";
		
		if (data == null) return "new_one";
		
		if ("JSONArray".equals(ScimUtils.getClassName(data))) {
//			System.out.println("JSONArray");
			data_type = "array";
			
		} else if ("JSONObject".equals(ScimUtils.getClassName(data))) {
//			System.out.println("JSONObject");
			data_type = "json";
			
		} else if ("String".equals(ScimUtils.getClassName(data))) {
//			System.out.println("String");
			data_type = "string";
			
		} else if ("Integer".equals(ScimUtils.getClassName(data))) {
//			System.out.println("Integer");
			data_type = "number";
			
		} else if ("Double".equals(ScimUtils.getClassName(data))) {
//			System.out.println("Double");
			data_type = "number";
			
		} else if ("Boolean".equals(ScimUtils.getClassName(data))) {
//			System.out.println("Boolean");
			data_type = "boolean";
			
		} else {
			System.out.println(ScimUtils.getClassName(data));
			data_type = "string";
			
		}
		
		return data_type;
	}
	
	public static Object deepCopy(Object oldObj) throws Exception {
		ObjectOutputStream oos = null;
		ObjectInputStream ois = null;
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream(); // A
			oos = new ObjectOutputStream(bos); // B
			// serialize and pass the object
			oos.writeObject(oldObj); // C
			oos.flush(); // D
			ByteArrayInputStream bin = new ByteArrayInputStream(bos.toByteArray()); // E
			ois = new ObjectInputStream(bin); // F
			// return the new object
			return ois.readObject(); // G
		} catch (Exception e) {
			System.out.println("Exception in ObjectCloner = " + e);
			throw (e);
		} finally {
			oos.close();
			ois.close();
		}
	}
	
	public static Integer getInteger(QueryParamsMap qm, String key) {
		if (qm == null)
			return null;
		if (!qm.hasKey(key))
			return null;
		try {
			return Integer.parseInt(qm.value(key));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Integer getInteger(QueryParamsMap qm, String key, int _defalut) {
		Integer ret = getInteger(qm, key);
		return (ret == null) ? _defalut : ret;
	}
	
	public static  Vector<String> toVector(String strs, String seperator) {
		Vector<String> keys = new Vector<String>();
		String[] keycomma = strs.split(seperator);
		for (int i = 0; i < keycomma.length; i++) {
			keys.add(keycomma[i].trim());
		}
		return keys;
	}

	public static  ListResponse getAttributes(Iterator itor, Vector<String> keys) {
		ListResponse lst = new ListResponse();
		if (itor == null || keys == null) {
			return null;
		}
		while (itor.hasNext()) {
			JSONObject obj = (JSONObject) itor.next();
			String id = obj.getAsString("id");
			JSONObject result = new JSONObject();
			for (String key : keys) {
				Object attr = obj.get(key);
				result.put("id", id);
				if (attr != null)
					result.put(key, attr);
			}
			if (result.size() > 1)
				lst.addResource(result);
		}
		return lst;
	}
}
