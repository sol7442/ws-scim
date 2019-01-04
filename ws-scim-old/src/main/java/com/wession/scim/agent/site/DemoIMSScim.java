package com.wession.scim.agent.site;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import com.wession.common.RESTClient;
import com.wession.common.ScimUtils;
import com.wession.scim.agent.DataMap;
import com.wession.scim.agent.site.intf.AbstractBean;
import com.wession.scim.agent.site.intf.AbstractScimAdaptor;
import com.wession.scim.agent.site.intf.ScimAdaptor;
import com.wession.scim.intf.schemas_name;
import com.wession.scim.resource.Group;
import com.wession.scim.resource.User;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

public class DemoIMSScim extends AbstractScimAdaptor   {
	JSONObject json_app = new JSONObject();
	
	public DemoIMSScim () {
		super("v2.0");
		json_app = getGroup(appName);
	}

	public JSONObject Sync() {
		System.out.println("START " + appName + " Provisioning : " + new Date());
		
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
			User scim_user = new User(client.get(link));

			String ims_id = ScimUtils.findArray((JSONArray) scim_user.get("ims"), "value");
			scim_member_list.add(ims_id);

			DemoIMSBean ims_user_bean = new DemoIMSBean(
										ims_id, 
										scim_user.getAsString(_user_name_display), 
										scim_user.getAsString(_user_title),
										scim_user.getPhoneNumbers() == null || scim_user.getPhoneNumbers().size() == 0 ? null : scim_user.getPhoneNumbers().get(0),
										scim_user.getAsString(_user_external_id),
										scim_user.getAsString("id")
									  );

			// 기존에 있는지 확인
			DataMap ims_user_dbmap = new DemoIMSAdpator().getAccount(ims_id);

			if (ims_user_dbmap != null) {
				// 기존 계정은 비교해서 변경처리
				JSONObject scim = ims_user_bean.toJSONObject();
				JSONObject db = new DemoIMSBean(ims_user_dbmap).toJSONObject();

				if (!scim.equals(db)) {
					new DemoIMSAdpator().updateAccount(ims_user_bean.toDataMap(), ims_user_dbmap);
					count_update++;
				}
			} else {
				// 신규 계정은 insert 처리
				new DemoIMSAdpator().insertAccount(ims_user_bean.toDataMap());
				count_add++;
			}
		}

		
		// 없어진 계정을 별도로 정리함
		ArrayList<DataMap> ghost_account = new DemoIMSAdpator().findGhostAccount(scim_member_list);
		JSONArray ghost = new JSONArray();
		
		for (int i=0; i<ghost_account.size(); i++) {
			DataMap g_user_map = ghost_account.get(i);
			DemoIMSBean g_user = new DemoIMSBean(g_user_map);
			ghost.add(g_user.toJSONObject());
			System.out.println(i + ") " + g_user.getCompanyID() + " / " + g_user.getName() + " / " + g_user.getId());
		}
		
		ret.put("members", member_cnt);
		ret.put("count_add", count_add);
		ret.put("count_update", count_update);
		ret.put("ghost", ghost);
		ret.put("endTime", new Date());
		ret.put("elapsed", System.currentTimeMillis() - startTime);
		return ret;
	}
}
