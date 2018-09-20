package com.wession.scim.agent.site;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import com.wession.common.JsonUtils;
import com.wession.common.RESTClient;
import com.wession.common.ScimUtils;
import com.wession.scim.AccountCompare;
import com.wession.scim.CompareData;
import com.wession.scim.Const;
import com.wession.scim.agent.DataMap;
import com.wession.scim.agent.site.intf.AbstractScimAdaptor;
import com.wession.scim.exception.ScimAuthException;
import com.wession.scim.resource.User;
import com.wession.scim.schema.Error;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

public class MyHRScim extends AbstractScimAdaptor  {
	JSONObject json_app = new JSONObject();
	
	JSONObject json_app_gw = setGroupFromSCIM("Groupware");
	JSONObject json_app_bo = setGroupFromSCIM("BuddyOne");
	
	public MyHRScim () {
		super("v2.0");
		json_app = getGroup(appName);
	}

	private String getAppId(User scim_user) {
		String app_id = scim_user.getAsString(_user_external_id);
		return app_id;
	}
	
	private JSONObject mapToJSON(DataMap app_user_dbmap) {
		JSONObject db = new MyHRBean(app_user_dbmap).toJSONObject();
		return db;
	}

	
	public JSONObject conciliation() {
		MyHRAdpator adaptor = new MyHRAdpator();
		
		System.out.println("Start MyHRScim Conciliation : SCIM <--> HRBean");
		JSONObject ret = new JSONObject();
		
		int cnt = 0;
		int count_add = 0;
		int count_update = 0;
		int count_delete = 0;
		long startTime = System.currentTimeMillis();
		
		ret.put("target", appName);
		ret.put("startTime", new Date());
		
		// Provisioning Process
		// 0. 사전준비 사항(상호목록 정보 조회)
		
		// 1. 신규사용자 확인
		// 2. 삭제사용자 확인
		// 3. 변경사용자 확인
		
		// 4. 신규사용자 추가
		// 5. 삭제사용자 삭제
		// 6. 변경사용자 업데이트
		
		try {
			ArrayList <String> bean_user_list = adaptor.getAccountIDList();
			ArrayList <String> scim_user_list = getAccountListToSCIM();
	
			ArrayList <String> new_user = getNewAccount(bean_user_list, scim_user_list); // DB에만 있는 Account
			ArrayList <String> delete_user = getRemoveAccount(bean_user_list, scim_user_list); // SCIM에만 있는 Account
			ArrayList <String> update_user = getInsectAccount(bean_user_list, scim_user_list); // DB에도 있고, SCIM에도 있는 Account
	
			System.out.println("bean_user_list : " + bean_user_list);
			System.out.println("scim_user_list : " + scim_user_list);
			
			System.out.println("new_user : " + new_user);
			System.out.println("delete_user : " + delete_user);
			System.out.println("update_user : " + update_user);
			
			System.out.println(json_app_bo.toJSONString());
			System.out.println(json_app_gw.toJSONString());
			
			
			if (new_user != null) {
				Iterator <String> itor_new_user = new_user.iterator();
				while (itor_new_user.hasNext()) {
					cnt++;
					if (cnt % 50 == 0) System.out.print(".");
					if (cnt % 2000 == 0) System.out.println();
					
					String bean_id = itor_new_user.next();
					addAccountToSCIM(bean_id);
					count_add++;
				}
			}
	
			if (delete_user != null) {
				Iterator <String> itor_delete_user = delete_user.iterator();
				while (itor_delete_user.hasNext()) {
					cnt++;
					if (cnt % 50 == 0) System.out.print(".");
					if (cnt % 2000 == 0) System.out.println();
					
					String bean_id = itor_delete_user.next();
					removeAccountToSCIM(bean_id);
					count_delete++;
				}
			}
			
			if (update_user != null) {
				Iterator <String> itor_update_user = update_user.iterator();
				while (itor_update_user.hasNext()) {
					cnt++;
					if (cnt % 50 == 0) System.out.print(".");
					if (cnt % 2000 == 0) System.out.println();
					
					String bean_id = itor_update_user.next();
					boolean updated = updateAccountToSCIM(bean_id);
					if (updated) count_update++;
				}
			}
			
		} catch (ScimAuthException sce) {
			System.out.println(sce.getMessage());
			Error err = new Error(200, sce.getMessage());
			return err;
		}
		
		ret.put("members", cnt);
		ret.put("count_add", count_add);
		ret.put("count_delete", count_delete);
		ret.put("count_update", count_update);
		//ret.put("ghost", ghost);
		ret.put("endTime", new Date());
		ret.put("elapsed", System.currentTimeMillis() - startTime);
		return ret;
	}

	private User setSCIMUser(DataMap datamap_user) {
		if (datamap_user == null) return null;
		String externalId = datamap_user.getAsString("hr_id");
		String username = datamap_user.getAsString("userName");
		User scim_user = new User(externalId, username);
		
		scim_user.put(_user_username, username + "(" + externalId + ")");
		scim_user.put(_user_title, datamap_user.getAsString("title"));
		scim_user.put(_user_name_display, username);
		scim_user.put("deptname", datamap_user.getAsString("deptName"));
		scim_user.put("active", true);
		
		// 전화번호, IMS, Email 는 JSONArray로 관리됨
		boolean hasPhoneNumber = false;
		String phone = datamap_user.getAsString("phone");
		if (phone != null && !"".equals(phone)) {
			scim_user.setPhoneNumber(phone, "work", false);
			hasPhoneNumber = true;
		} else {
			scim_user.put(_user_phonenumbers, new JSONArray());
		}
		
		boolean hasEmail = false;
		String email = datamap_user.getAsString("email");
		if (email != null && !"".equals(email)) {
			scim_user.setEmail(email, "work", false);
			hasEmail = true;
		} else {
			scim_user.put(_user_emails, new JSONArray());
		}
		
		boolean hasIMS = false;
		String ims_id = datamap_user.getAsString("ims_id");
		if (ims_id != null && !"".equals(ims_id)) {
			scim_user.setIMS(ims_id, "work", false);
			hasIMS = true;
		} else {
			scim_user.put(_user_ims, new JSONArray());
		}
		
		// Groups 내용 추가 - Provisioning Policy와 연계처리
		scim_user.put(_user_groups, new JSONArray());
		if (hasEmail) {
			scim_user.addGroup(json_app_gw);
		}
		
		if (hasIMS) {
			scim_user.addGroup(json_app_bo);
		}
		scim_user.updateModifyTime();
		
		return scim_user;
	}
	
	private AccountCompare compared(JSONObject scim, MyHRBean bean) {
		// Bean가 SCIM User Resource를 비교함
		// 추가, 수정, 삭제에 대한 정보 제공
		// main method : add, patch, deactive, pass(ID가 틀림)
		// data->array : scim_attr_name, new_data(to bean), old_data(to scim)
		AccountCompare acompr = new AccountCompare();
		
		String scim_id = "";
		String bean_id = "";
		
		if (scim == null) {
			acompr.setMethod("add");
			return acompr;
		} else if (bean == null) {
			acompr.setMethod("deactive");
			return acompr;
		} else {
			acompr.setMethod("patch");
		}
		
		scim_id = scim.getAsString(_user_external_id);
		bean_id = bean.getHr_id();
		
		if (!scim_id.equals(bean_id)) {
			acompr.setMethod("pass");
			return acompr; // 문제
		}
		
		String bean_name = bean.getUserName();
		String bean_deptcode = bean.getDeptCode();
		String bean_deptname = bean.getDeptName();
		String bean_title = bean.getTitle();
		String bean_phone = bean.getPhone();
		String bean_email = bean.getEmail();
		String bean_imsId = bean.getIms_id();
		
		String scim_name = scim.getAsString(_user_name_display);
		String scim_deptcode = ""; // 사용하지 않음
		String scim_deptname = scim.getAsString("deptname");
		String scim_title = scim.getAsString(_user_title);
		String scim_phone = ScimUtils.findArray((JSONArray) scim.get(_user_phonenumbers), _user_phonenumber_value);
		String scim_email = ScimUtils.findArray((JSONArray) scim.get(_user_emails), _user_email_value);
		String scim_imsId = ScimUtils.findArray((JSONArray) scim.get(_user_ims), _user_ims_value);
		
		acompr.compare(_user_name_display, bean_name, scim_name);
		acompr.compare("deptname", bean_deptname, scim_deptname);
		acompr.compare(_user_title, bean_title, scim_title);
		acompr.compare(_user_phonenumbers + "." + _user_phonenumber_value, bean_phone, scim_phone);
		acompr.compare(_user_emails + "." + _user_email_value, bean_email, scim_email);
		acompr.compare(_user_ims + "." + _user_ims_value, bean_imsId, scim_imsId);
		
		return acompr;
	}
	
	private void addAccountToSCIM(String bean_id) {
		// REST Client를 이용하여 WessionIM에 POST 메소드 전달
		// 신규계정 생성 시, 만들어야 할 데이터맵에 맞는 SQL을 생성하여 입력
		String sql = "select id hr_id, Name userName, HR.DeptCode deptCode, DEPT.DeptName deptName, title, ims ims_id, email, phone " +  
	                 " from T_HR1 HR, T_DeptInfo DEPT where HR.DeptCode = DEPT.DeptCode and id = ?";
		
		DataMap datamap_user = new MyHRAdpator().getAccount(bean_id, sql);
		String postBody = setSCIMUser(datamap_user).toJSONString();

		JSONObject resp = client.post(URL + "/" + Resource, postBody);
		
		// 정상으로 왔는지 확인이 필요함
	}
	
	private boolean updateAccountToSCIM(String hr_id) {
		boolean updated = false;
		String param = "externalId eq \"" + hr_id + "\"";
		JSONObject resp = client.get(URL + "/" + Resource + "?filter=" + URLEncoder.encode(param));
		// 1개만 넘어와야 함
		Number account_cnt = resp.getAsNumber("totalResults");
		if ((int) account_cnt == 1 ) {
			// Update 처리, PUT
			JSONObject user = (JSONObject) ((JSONArray) resp.get("Resources")).get(0);
			updated = updateAccountToSCIM(user);
		} else {
			// Error Log 처리
			System.out.println("ERROR SCIM have duplicate accounts. - " + hr_id);
		}
		return updated;
	}
	
	private boolean updateAccountToSCIM(JSONObject simple_user_scim) {
		boolean updated = false;
		// REST Client를 이용하여 WessionIM에 PUT 메소드 전달
		// 기존계정 교체시
		String sql = "select id hr_id, Name userName, HR.DeptCode deptCode, DEPT.DeptName deptName, title, ims ims_id, email, phone " +  
		             " from T_HR1 HR, T_DeptInfo DEPT where HR.DeptCode = DEPT.DeptCode and id = ?";
		String scim_id = simple_user_scim.getAsString("id");
//		JSONObject user_scim = client.get(URL + "/Users/" + scim_id);
		JSONObject user_scim = simple_user_scim;
		
		DataMap user_bean = new MyHRAdpator().getAccount(user_scim.getAsString(_user_external_id), sql);
//			System.out.println("scim : " + user_scim.toJSONString());
//			System.out.println("data : " + user_bean.toString());
		AccountCompare accp = compared(user_scim, new MyHRBean(user_bean));
		String method = accp.getMethod();
		
		JSONObject put_json = new JSONObject();
		put_json.put("id", scim_id);
		put_json.put(_user_schemas, user_scim.get(_user_schemas));
		
		if (method.equals("patch")) {
			ArrayList <CompareData> arry = accp.getData();
			for (int i=0; i<arry.size(); i++) {
				CompareData cpd = arry.get(i);
				if (cpd.getAttrName().contains(".")) { // groups, phoneNumbers, emails, ims
					String [] cpds = cpd.getAttrName().split("\\.");
					String att_name = cpds[0];
					String att_value = cpds[1];
					String real_value = cpd.getNewValue();
					
					System.out.println("Compare Data - Attribute Name : " + cpd.getAttrName() + " / " + real_value);
					
					if (real_value != null && !"".equals(real_value)) {
						JSONArray inner_arry = new JSONArray();
						JSONObject inner_obj = new JSONObject();
						inner_obj.put("type", "work");
						inner_obj.put(att_value, cpd.getNewValue());
						inner_arry.add(inner_obj);
						put_json.put(att_name, inner_arry);
						
					} else {
						// 지우는 것임
						// 지우기전에 기존 Array에 있는 것이 있는지 확인이 필요함
						System.out.println(user_scim.getAsString(_user_name_display) + ">> att_name : " + att_name + "." + att_value + "    att_value : " + real_value);
						put_json.put(att_name, new JSONArray());
					}
					
					//System.out.println(cpd.getAttrName());
				} else {
					put_json.put(cpd.getAttrName(), cpd.getNewValue());
				}
			}
			
//				System.out.println("[put_json(" + put_json.size() + ")]  " + put_json.toJSONString());
//				System.out.println("[scim_user]  " + user_scim.toJSONString());
			if (put_json.size()>2) {
				// 여기서 Groups에 대한 정리를 한번 더 해야함
				checkGroups(put_json, user_scim);
				String postBody = put_json.toJSONString();
				
				System.out.println("[scim_user]  " + user_scim.toJSONString());
				System.out.println("[put_json(" + put_json.size() + ") after checkGroup]  " + postBody);
				
				JSONObject puts = client.put(URL + "/" + Resource + "/" + scim_id, postBody);
				updated = true;
//				System.out.println(puts.toJSONString());
			}
		}
		
		return updated;
	}
	
	private void checkGroups(JSONObject put_json, JSONObject user_scim) {
		// 기존 user_scim의 그룹과 비교하여 변경사항을 입력하여 PUT으로 발송함
		if (user_scim == null) return ;
		
		String email_work = null;
		String ims_work = null;
		
		JSONArray scim_groups = (JSONArray) user_scim.get("groups");
		JSONArray put_emails = (JSONArray) put_json.get("emails");
		JSONArray put_ims = (JSONArray) put_json.get("ims");
		
//		System.out.println("  scim_groups : " + (scim_groups==null?"NULL":scim_groups));
//		System.out.println("  put_emails : "  + (put_emails==null?"NULL":put_emails));
//		System.out.println("  put_ims : " 	  + (put_ims==null?"NULL":put_ims));
		
		if (put_emails != null) {
			if (scim_groups == null) scim_groups = new JSONArray();
			//기존에 있는지 확인 필요
			boolean exist = false;
			for (int i=0; i<scim_groups.size(); i++) {
				JSONObject group = (JSONObject) scim_groups.get(i);
				if (group.equals(json_app_gw)) exist = true;
			}
			
			String scim_email = ScimUtils.findArray((JSONArray) put_emails, _user_email_value);
			if (scim_email == null || "".equals(scim_email)) {
				scim_groups.remove(json_app_gw);
//				System.out.println("remove groups : " + scim_groups.toJSONString());
			} else if (!exist) {
				scim_groups.add(json_app_gw);
//				System.out.println("add groups : " + scim_groups.toJSONString());
			}
		}
		
		if (put_ims != null) {
			if (scim_groups == null) scim_groups = new JSONArray();
			//기존에 있는지 확인 필요
			boolean exist = false;
			for (int i=0; i<scim_groups.size(); i++) {
				JSONObject group = (JSONObject) scim_groups.get(i);
				if (group.equals(json_app_bo)) exist = true;
			}
			String scim_imsId = ScimUtils.findArray((JSONArray) put_ims, _user_ims_value);
			if (scim_imsId == null || "".equals(scim_imsId)) {
				scim_groups.remove(json_app_bo);
//				System.out.println("remove groups : " + scim_groups.toJSONString());

			} else if (!exist) {
				scim_groups.add(json_app_bo);
//				System.out.println("add groups : " + scim_groups.toJSONString());

			}
		}
		
		if (scim_groups != null && scim_groups.size()>0) put_json.put("groups", scim_groups);
		
	}
	

	private void addGroups(JSONObject user) {
		// Groups에 대한 관리는 별도로 진행
		if (user == null) return;
		
		// 그룹정보는 따라 저장함
		boolean changed = false;
		
		String email_work = null;
		String ims_work = null;
		String hr_id = user.getAsString(_user_external_id);
		
		JSONArray emails = (JSONArray) user.get("emails");
		if (emails != null) {
			for (int i=0; i<emails.size(); i++) {
				JSONObject email = (JSONObject) emails.get(i);
				if ("work".equals(email.getAsString("type"))) {
					email_work = email.getAsString("value");
				}
			}
		}
		
		JSONArray ims = (JSONArray) user.get("ims");
		if (ims != null) {
			for (int i=0; i<ims.size(); i++) {
				JSONObject im = (JSONObject) ims.get(i);
				if ("work".equals(im.getAsString("type"))) {
					ims_work = im.getAsString("value");
				}
			}
		}
		
		JSONArray groups = (JSONArray) user.get("groups");
		JSONArray groups_modify = new JSONArray();
		if (email_work != null && !"".equals(email_work)) {
			groups_modify.add(json_app_gw);
		}

		if (ims_work != null && !"".equals(ims_work)) {
			groups_modify.add(json_app_bo);
		}

		if (groups != null && groups.size() > 0) {
			for (int isg = 0; isg < groups.size(); isg++) {
				JSONObject scim_grp = (JSONObject) groups.get(isg);
				boolean isIt = JsonUtils.JSONArrayContainsValue(groups_modify, "display", scim_grp.getAsString("display"));
				if (!isIt)
					changed = true;
			}

		} else { // 기존이 없는 경우
			if (groups_modify.size() > 0)
				changed = true;
		}

		user.put("groups", groups_modify);

		if (changed) {
			JSONObject result = client.put(URL + "/" + Resource + "/" + user.getAsString("id"), user.toJSONString());
//			System.out.println("UPDATE User : " + user.getAsString("userName") + "/" + hr_id);
			// System.out.println("\t" + user.toJSONString());
			// System.out.println("\t" + result.toJSONString());
		} else {
//			System.out.println("No changed " + user.getAsString("userName") + "/" + hr_id);
		}
			
	}
	
	private void patchAccountToSCIM() {
		// REST Client를 이용하여 WessionIM에 PATCH 메소드 전달
		// 기존계정 정보 중 일부 수정시
	}
	
	private void removeAccountToSCIM(String bean_id) {
		// REST Client를 이용하여 WessionIM에 DELETE 메소드 전달
		// 기존계정 삭제요청시 
		String scim_id = "";
		String param = "externalId eq \"" + bean_id + "\"";
		JSONObject resp = client.get(URL + "/" + Resource + "?attributes=externalId&filter=" + URLEncoder.encode(param));
		scim_id = ScimUtils.findArray((JSONArray) resp.get("Resources"), "id");
		JSONObject dels = client.delete(URL + "/" + Resource + "/" + scim_id);
//		System.out.println(dels.toJSONString());
	}
}
