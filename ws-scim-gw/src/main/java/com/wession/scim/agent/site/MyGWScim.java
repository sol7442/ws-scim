package com.wession.scim.agent.site;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import com.wession.common.ScimUtils;
import com.wession.scim.agent.DataMap;
import com.wession.scim.agent.site.intf.AbstractScimAdaptor;
import com.wession.scim.resource.User;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

public class MyGWScim extends AbstractScimAdaptor  {
	JSONObject json_app = new JSONObject();
	
	public MyGWScim () {
		super("v2.0");
		json_app = getGroup(appName);
	}
	
	private String getAppId(User scim_user) {
		String app_id = ScimUtils.findArray((JSONArray) scim_user.get("emails"), "value");
		return app_id;
	}
	
	private JSONObject mapToJSON(DataMap app_user_dbmap) {
		JSONObject db = new MyGWBean(app_user_dbmap).toJSONObject();
		return db;
	}

	public JSONObject Sync() {
		// 수정해야할 부분 (1) ========================
		MyGWAdpator adaptor = new MyGWAdpator();
		// ============================================

		writeLogger("provision", "info", "[시작] START " + appName + " Provisioning : " + new Date());
		System.out.println("[시작] START " + appName + " Provisioning : " + new Date());

		JSONObject ret = new JSONObject();
		
		int count_add = 0;
		int count_update = 0;
		int count_delete = 0;
		long startTime = System.currentTimeMillis();
		
		ret.put("target", appName);
		ret.put("startTime", new Date());
		
		ArrayList <String> scim_member_list = new ArrayList <String> ();
		Iterator <JSONObject> itor_members = getMembers(json_app);
		
		int member_cnt = 0;
		while (itor_members.hasNext()) {
			member_cnt++;
			
			if (member_cnt % 50 == 0) System.out.print(".");
			if (member_cnt % 1000 == 0) System.out.println();
			
			JSONObject member = (JSONObject) itor_members.next();
			String link = member.getAsString("$ref");
			
			link = link.replaceAll("yoursite.com:8080", "localhost:5000");
			//System.out.println(link);
			
			User scim_user = new User(client.get(link));

			String app_id = getAppId(scim_user);
			if (app_id != null && !"".equals(app_id)) {
				scim_member_list.add(app_id);
	
				// 수정해야할 부분 (2) ==============================
				MyGWBean app_user_bean = new MyGWBean(
						app_id, 
						scim_user.getAsString(_user_name_display), 
						scim_user.getAsString(_user_title),
						scim_user.getPhoneNumbers() == null || scim_user.getPhoneNumbers().size() == 0 ? null : scim_user.getPhoneNumbers().get(0),
						scim_user.getAsString("deptname"), 
						scim_user.getAsString(_user_external_id)
				);
				// ===================================================
				
				DataMap app_user_dbmap = adaptor.getAccount(app_id);
	
				if (app_user_dbmap != null) {
					JSONObject scim = app_user_bean.toJSONObject();
					JSONObject db = mapToJSON(app_user_dbmap);
	
					if (!scim.equals(db)) {
						if (adaptor.updateAccount(app_user_bean.toDataMap(), app_user_dbmap)) {
							// 변경시 update count 증가
							count_update++;
							writeLogger("provision", "info", "UPATE USER INFO : " + app_user_bean.toString());
						}
					}
				} else {
					adaptor.insertAccount(app_user_bean.toDataMap());
					writeLogger("provision", "info", "NEW USER INFO : " + app_user_bean.toString());
					count_add++;
				}
			} else {
				writeLogger("provision", "debug", "No APP ID : " + scim_user.getAsString(_user_external_id));
//				System.out.println("No APP ID : " + scim_user.getAsString(_user_external_id));
			}
		}
		writeLogger("provision", "info", "[종료] End " + appName + " Provisioning : " + new Date());
		
		ArrayList<DataMap> ghost_account = adaptor.findGhostAccount(scim_member_list);
		JSONArray ghost = new JSONArray();
		
//		processLog.debug("Ghost Account Find ... ");
		for (int i=0; i<ghost_account.size(); i++) {
			DataMap g_user_map = ghost_account.get(i);
			MyGWBean g_user = new MyGWBean(g_user_map);
			ghost.add(g_user.toJSONObject());
//			processLog.debug(i + ") " + g_user.getCompanyID() + " / " + g_user.getName() + " / " + g_user.getEmail());
//			System.out.println(i + ") " + g_user.getCompanyID() + " / " + g_user.getName() + " / " + g_user.getEmail());
		}
		
		ret.put("members", member_cnt);
		ret.put("count_add", count_add);
		ret.put("count_update", count_update);
		ret.put("ghost", ghost);
		ret.put("endTime", new Date());
		ret.put("elapsed", System.currentTimeMillis() - startTime);
		
//		processLog.debug("RETURN MESSAGE\n\t" + ret.toJSONString());
		
		return ret;
	}
}
