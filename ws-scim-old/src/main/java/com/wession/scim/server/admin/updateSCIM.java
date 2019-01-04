package com.wession.scim.server.admin;

import java.util.Iterator;
import java.util.Set;

import com.wession.common.RESTClient;
import com.wession.scim.controller.ServiceProviderConfig;
import com.wession.scim.intf.schemas_name;
import com.wession.scim.simple.MemRepository;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import spark.Request;
import spark.Response;

public class updateSCIM implements schemas_name {
	private static ServiceProviderConfig conf = ServiceProviderConfig.getInstance();
	private MemRepository repo = new MemRepository().getInstance();
	
	private JSONArray getDietGroups(){
		JSONArray dietGroups = new JSONArray();
		Iterator itor_groups = repo.iterator("Groups");
		if (itor_groups == null) return dietGroups;
		while (itor_groups.hasNext()) {
			JSONObject json = new JSONObject();
			JSONObject group = (JSONObject) itor_groups.next();
			json.put(_user_groups_id, group.getAsString(_group_id));
			json.put(_user_groups_display, group.getAsString(_group_name_display));
			String group_ref = conf.getRefer("v2.0", "Groups") + "/" + group.getAsString(_group_id);
			json.put(_user_groups_ref, group_ref);
			dietGroups.add(json);
//			System.out.println("** getDietGroups : " + json.toJSONString());
		}
		return dietGroups;
	}
	
	private JSONObject getDietGroup(String groupname) {
		JSONArray arry = getDietGroups();
		Iterator itor = arry.iterator();
		while (itor.hasNext()) {
			JSONObject json = (JSONObject) itor.next();
			String json_name = json.getAsString("display");
//			System.out.println("** groupname -> json_name : " + json_name);
			if (groupname.equals(json_name)) 
				return json;
		}
		return new JSONObject();
	}
	
	public String patch(Request request, Response response) {
		String id = request.queryParams("pscimid");
		String displayName = request.queryParams("pdisplayname");
		String param_groups = request.queryParams("pgroups");
		
		/*
		System.out.println("==> QueryParams");
		Set <String> keys = request.queryParams();
		for (String key: keys) {
			String [] values = request.queryParamsValues(key);
			if (values.length == 1) {
				System.out.println("\t" + key + ":" + values[0]);
			} else if (values.length > 1) {
				for (int i = 0; i<values.length; i++)
					System.out.println("\t" + key + ":[" + i + "]" + values[i]);
			}
		}
		*/
		
		RESTClient client = new RESTClient();
		String URL_Users = conf.getRefer("v2.0", "Users") + "/" + id;
//		System.out.println("URL_Users :" + URL_Users);
		JSONObject user = client.get(URL_Users);
		
		String user_title = user.getAsString("title");
		String user_displayName = user.getAsString("displayName");
		String user_deptName = user.getAsString("deptname");
		boolean user_active = (boolean) user.get("active");
		

		// 기존 데이터와 파라미터를 비교처리함
		setDataAsString(request, "ptitle", "title", user);
		setDataAsString(request, "pdisplayname", "displayName", user);
		
		setDataAsBoolean(request, "active", "active", user);
		
		setDataAsTypeObject(request, "pphonenumbers", "phoneNumbers", user);
		setDataAsTypeObject(request, "pims", "ims", user);
		setDataAsTypeObject(request, "pemails", "emails", user);

		JSONArray user_phoneNumbers = (JSONArray) user.get("phoneNumbers");
		JSONArray user_ims = (JSONArray) user.get("ims");
		JSONArray user_emails = (JSONArray) user.get("emails");
		
		// 그룹을 정리함
		// 기본 ims가 있으면 BuddyOne 등록, email이 있으면 Groupware 등록
		
		JSONArray groups = new JSONArray();
		user.put("groups", groups);
		if (user_emails != null && user_emails.size() > 0) {
//			System.out.println("  ******* Add Groups : Groupware - " + user_emails.toJSONString());
			groups.add(getDietGroup("Groupware"));
		}
		if (user_ims != null && user_ims.size() > 0) {
//			System.out.println("  ******* Add Groups : BuddyOne - " + user_ims.toJSONString());
			groups.add(getDietGroup("BuddyOne"));
		}

		/*
		if (param_groups != null) {
			param_groups = param_groups.trim();
			if (!"".equals(param_groups)) {
				JSONArray groups = new JSONArray();
				if (param_groups.contains(",")) {
					String param_group[] = param_groups.split(",");
					for (int i=0; i<param_group.length; i++) {
						if ("Groupware".equals(param_group[i].trim())) {
							System.out.println("DietGroup : " + getDietGroup("Groupware").toJSONString());
							groups.add(getDietGroup("Groupware"));
						} else if ("BuddyOne".equals(param_group[i].trim())) {
							groups.add(getDietGroup("BuddyOne"));
						}
					}
				} else {
					if ("Groupware".equals(param_groups)) {
						groups.add(getDietGroup("Groupware"));
					} else if ("BuddyOne".equals(param_groups)) {
						groups.add(getDietGroup("BuddyOne"));
					}
				}
				user.put("groups", groups);
			}
		}
		*/

//		System.out.println("send : " + user.toJSONString());
		JSONObject put = client.put(URL_Users, user.toJSONString());
//		System.out.println(put.toJSONString());
		put.put("message", "/admin/update says.. ok.");
		return put.toJSONString();
	}
	


	private void setDataAsString(Request request, String param, String attr, JSONObject user) {
		String pname = request.raw().getParameter(param) == null?"":request.raw().getParameter(param);
		if (pname.equals("")) {
			user.put(attr, null);
		} else if (!pname.equals(user.getAsString(attr))) {
			user.put(attr, pname);
		}
	}
	
	private void setDataAsBoolean(Request request, String param, String attr, JSONObject user) {
		String pname = request.raw().getParameter(param) == null?"":request.raw().getParameter(param);
		if (pname.equals("")) {
			user.put(attr, false);
		} else {
			Boolean pboolean = pname.equals("true")?true:false;
			Boolean aboolean = (Boolean) user.get(attr);
			if (pboolean != aboolean)
				user.put(attr, pboolean);
		}
	}
	
	private void setDataAsTypeObject(Request request, String param, String attr, JSONObject user) {
		String pnames = request.raw().getParameter(param) == null?",":request.raw().getParameter(param);
		String[] pname = pnames.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
		
		if (pname.length < 2) {
			user.put(attr, new JSONArray());
			return;
		}
		
		int i = 0;
		String type = pname[i];
		String value = pname[i+1];
		String json_str = "{\"type\":" + type + ",\"value\":" + value + "}";
		JSONObject json = (JSONObject) JSONValue.parse(json_str);
		
//		System.out.println("  ******* >> type value object attr name : " + attr);
//		System.out.println("  ******* >> type value object : " + json.toJSONString());
		
		JSONArray user_arry = (JSONArray) user.get(attr);
		if (user_arry == null || user_arry.size() < 1) {
			if (json != null) {
				user_arry = new JSONArray();
				user.put(attr, user_arry);
				user_arry.add(json);
//				System.out.println(" empty array added");
			}
		} else {
			Iterator itor = user_arry.iterator();
			while (itor.hasNext()) {
				JSONObject user_json = (JSONObject) itor.next();
				if (!json.equals(user_json)) {
					user_arry.remove(user_json);
					user_arry.add(json);
//					System.out.println(" alreay json removed and new json add");
//					System.out.println("   >>>>> remove : " + user_json.toJSONString());
//					System.out.println("   >>>>> add    : " + json.toJSONString());
				}
			}
		}
		
	}
}
