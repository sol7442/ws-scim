package com.wession.scim.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.wession.common.RESTClient;
import com.wession.common.ScimUtils;
import com.wession.scim.intf.schemas_name;
import com.wession.scim.resource.Group;
import com.wession.scim.resource.User;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

public class DemoGWSCIM implements schemas_name   {
	RESTClient client = new RESTClient();
	String URL = "http://localhost:5000/scim/v2.0/";
	String Resource = "Users";
	String params = "attributes=externalId";
	
	JSONObject json_app_gw = new JSONObject();
	JSONObject json_app_bo = new JSONObject();
	
	{
		// 이부분은 SCIM 서버에서 받아와서 처리될 수 있도록 
		json_app_gw.put("display", "Groupware");
		json_app_gw.put("value", "ce9c1b4e-6993-4558-a5a8-0f79b26cb7b3");
		json_app_gw.put("$ref", "http://localhost:5000/scim/v2.0/Groups/ce9c1b4e-6993-4558-a5a8-0f79b26cb7b3");
		
		json_app_bo.put("display", "BuddyOne");
		json_app_bo.put("value", "8752fc15-5519-4ff3-af07-1198b0516f90");
		json_app_bo.put("$ref", "http://localhost:5000/scim/v2.0/Groups/8752fc15-5519-4ff3-af07-1198b0516f90");
	}
	
	public static void main(String[] args) {

	}

	public void Sync() {
		ArrayList <String> scim_member_list = new ArrayList <String> ();
		
		System.out.println("START Groupware Provisioning");
		
		JSONObject group_gw_json = client.get(URL + "/Groups/" + json_app_gw.getAsString("value"));

		Group Groupware = new Group(group_gw_json);
		JSONArray group_members = (JSONArray) Groupware.get("members");
		if (group_members == null) group_members = new JSONArray();
		
		Iterator itor_members = group_members.iterator();

		ArrayList<HashMap<String, String>> gw_user = new ArrayList<HashMap<String, String>>();
		int cnt = 0;
		
		while (itor_members.hasNext()) {
			if (cnt % 50 == 0) System.out.print(".");
			JSONObject member = (JSONObject) itor_members.next();
			String link = member.getAsString("$ref");
			link = link.replaceAll("yoursite.com:8080", "localhost:5000");
			User scim_user = new User(client.get(link));
			String gw_id = ScimUtils.findArray((JSONArray) scim_user.get("emails"), "value");
//			System.out.println("Groupware USER : " + scim_user.getAsString("userName") + " - " + gw_id);

			scim_member_list.add(gw_id);

			DemoGWBean gw_user_bean = new DemoGWBean(
										gw_id, 
										scim_user.getAsString(_user_name_display), 
										scim_user.getAsString(_user_title),
										scim_user.getPhoneNumbers() == null || scim_user.getPhoneNumbers().size() == 0 ? null : scim_user.getPhoneNumbers().get(0),
										scim_user.getAsString("deptname"), 
										scim_user.getAsString(_user_external_id)
									  );

			// 기존에 있는지 확인
			DemoGWBean gw_user_db = new DemoGW().getAccount(gw_id);
			
			if (gw_user_db != null) {
				// 기존 계정은 비교해서 변경처리
				new DemoGW().updateAccount(gw_user_bean);
			} else {
				// 신규 계정은 insert 처리
				new DemoGW().insertAccount(gw_user_bean);
			}
			cnt++;
		}
		// 없어진 계정을 별도로 정리함
		ArrayList<DemoGWBean> ghost_account = new DemoGW().findGhostAccount(scim_member_list);
//		System.out.println();
//		System.out.println("Ghost Account =================================");
//		System.out.println();
		
		for (int i=0; i<ghost_account.size(); i++) {
			DemoGWBean g_user = ghost_account.get(i);
			System.out.println(i + ") " + g_user.getCompanyID() + " / " + g_user.getName() + " / " + g_user.getEmail());
		}
	}
}
