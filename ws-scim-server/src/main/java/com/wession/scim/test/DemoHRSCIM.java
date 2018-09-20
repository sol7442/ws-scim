package com.wession.scim.test;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.wession.common.JsonUtils;
import com.wession.common.RESTClient;
import com.wession.common.ScimUtils;
import com.wession.scim.AccountCompare;
import com.wession.scim.CompareData;
import com.wession.scim.Const;
import com.wession.scim.exception.ScimAuthException;
import com.wession.scim.intf.schemas_name;
import com.wession.scim.resource.User;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

public class DemoHRSCIM implements schemas_name {
	RESTClient client = new RESTClient();
	String URL = "http://localhost:5000/scim/v2.0/";
	String Resource = "Users";
	String params = "attributes=externalId";

	JSONObject json_app_gw = new JSONObject();
	JSONObject json_app_bo = new JSONObject();
	
	{
		// 이부분은 SCIM 서버에서 받아와서 처리될 수 있도록 
//		json_app_gw.put("display", "Groupware");
//		json_app_gw.put("value", "ce9c1b4e-6993-4558-a5a8-0f79b26cb7b3");
//		json_app_gw.put("$ref", "http://localhost:5000/scim/v2.0/Groups/ce9c1b4e-6993-4558-a5a8-0f79b26cb7b3");
		
		json_app_gw = setGroupFromSCIM("Groupware");
		
//		json_app_bo.put("display", "BuddyOne");
//		json_app_bo.put("value", "8752fc15-5519-4ff3-af07-1198b0516f90");
//		json_app_bo.put("$ref", "http://localhost:5000/scim/v2.0/Groups/8752fc15-5519-4ff3-af07-1198b0516f90");
		
		json_app_bo = setGroupFromSCIM("BuddyOne");
	}
	
	public static void main(String [] args) {
//		DemoHRSCIM demo = new DemoHRSCIM();
//		demo.Sync();
	}
	
	private JSONObject setGroupFromSCIM(String displayName) {
		String param = "displayName eq \"" + displayName + "\"";
		JSONObject resp = client.get(URL+"Groups?filter=" + URLEncoder.encode(param));
		JSONArray resources = (JSONArray) resp.get("Resources");
		if (resources == null) return null;
		
		Iterator itor = resources.iterator();
		String id = null;
		while (itor.hasNext()) {
			JSONObject group_info = (JSONObject) itor.next();
			id = group_info.getAsString("id");
		}

		if (id == null) return null;
		
		JSONObject ret = new JSONObject();
		ret.put("value", id);
		ret.put("display", displayName);
		ret.put("$ref", URL+"Groups/" + id);
		
		return ret;
		
	}
	
	public void Sync() {
		System.out.println("Start DemoHRSCIM : SCIM <--> HRBean");
		// Provisioning Process
		// 0. 사전준비 사항(상호목록 정보 조회)
		
		// 1. 신규사용자 확인
		// 2. 삭제사용자 확인
		// 3. 변경사용자 확인
		
		// 4. 신규사용자 추가
		// 5. 삭제사용자 삭제
		// 6. 변경사용자 업데이트
		try {
			ArrayList <String> bean_user_list = getAccountListToBean();
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
			
			int cnt = 0;
			if (new_user != null) {
				Iterator <String> itor_new_user = new_user.iterator();
				while (itor_new_user.hasNext()) {
					cnt++;
					if (cnt % 50 == 0) System.out.print(".");
					if (cnt % 2000 == 0) System.out.println();
					
					String bean_id = itor_new_user.next();
					addAccountToSCIM(bean_id);
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
				}
			}
			
			if (update_user != null) {
				Iterator <String> itor_update_user = update_user.iterator();
				while (itor_update_user.hasNext()) {
					cnt++;
					if (cnt % 50 == 0) System.out.print(".");
					if (cnt % 2000 == 0) System.out.println();
					
					String bean_id = itor_update_user.next();
					updateAccountToSCIM(bean_id);
				}
			}
		} catch (ScimAuthException sce) {
			System.out.println(sce.getMessage());	
		}
		
	}
	


	private ArrayList<String> getInsectAccount(ArrayList<String> bean_user_list, ArrayList<String> scim_user_list) {
		ArrayList <String> arry = new ArrayList <String> ();
		Iterator <String> itor = bean_user_list.iterator();
		while (itor.hasNext()) {
			String bean_user_id = itor.next();
			if (scim_user_list.contains(bean_user_id))
				arry.add(bean_user_id);
		}
		return arry.size()>0?arry:null;
	}

	private User setSCIMUser(DemoHRBean bean_user) {
		if (bean_user == null) return null;
		String externalId = bean_user.getHr_id();
		String username = bean_user.getUserName(); 
		User scim_user = new User(externalId, username);
		
		scim_user.put(_user_username, username + "(" + externalId + ")");
		scim_user.put(_user_title, bean_user.getTitle());
		scim_user.put(_user_name_display, username);
		scim_user.put("deptname", bean_user.getDeptName());
		scim_user.put("active", true);
		
		// 전화번호, IMS, Email 는 JSONArray로 관리됨
		boolean hasPhoneNumber = false;
		String phone = bean_user.getPhone();
		if (phone != null && !"".equals(phone)) {
			scim_user.setPhoneNumber(phone, "work", false);
			hasPhoneNumber = true;
		}
		
		boolean hasEmail = false;
		String email = bean_user.getEmail();
		if (email != null && !"".equals(email)) {
			scim_user.setEmail(email, "work", false);
			hasEmail = true;
		}
		
		boolean hasIMS = false;
		String ims_id = bean_user.getIms_id();
		if (ims_id != null && !"".equals(ims_id)) {
			scim_user.setIMS(ims_id, "work", false);
			hasIMS = true;
		}
		
		// Groups 내용 추가 - Provisioning Policy와 연계처리
		if (hasEmail) {
			scim_user.addGroup(json_app_gw);
		}
		
		if (hasIMS) {
			scim_user.addGroup(json_app_bo);
		}
		scim_user.updateModifyTime();
		
		return scim_user;
	}
	
	private ArrayList<String> getNewAccount(ArrayList <String> bean_user_list, ArrayList <String> scim_user_list) {
		return notInSecond(bean_user_list, scim_user_list);
	}
	
	private ArrayList<String> getRemoveAccount(ArrayList <String> bean_user_list, ArrayList <String> scim_user_list) {
		return notInSecond(scim_user_list, bean_user_list);
	}
	
	@SuppressWarnings("unchecked")
	private ArrayList<String> notInSecond(ArrayList <String> first, ArrayList <String> second) {
		if (first == null) return null;
		if (second == null) {
			try {
				return (ArrayList<String>) ScimUtils.deepCopy(first);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		ArrayList<String> arry = new ArrayList<String> ();
		Iterator<String> itor = first.iterator();
		while (itor.hasNext()) {
			String first_id = itor.next();
			if (!second.contains(first_id)) arry.add(first_id);
		}
		return arry;
	}
	
	@SuppressWarnings("deprecation")
	private User getAccountToSCIM(String userId) {
		// REST Client를 이용하여 WessionIM 서버에서 User 객체를 호출함
		String param = URLEncoder.encode("externalId eq \"" + userId +"\"");
		JSONObject respObj = client.get(URL + Resource + "?filter="+param);
		JSONArray resources = (JSONArray) respObj.get("Resources");
		Iterator<Object> itor = resources.iterator();
		JSONObject json = new JSONObject();
		while (itor.hasNext()) {
			json = (JSONObject) itor.next(); // 1개만 오는 것이 정상
		}
		return new User(json);
	}
	
	private ArrayList<String> getAccountListToSCIM() throws ScimAuthException {
		// REST Client를 이용하여 WessionIM 서버에서 전체 User 목록을 요청함
		ArrayList <String> userList = new ArrayList <String> ();
		
		int total = 100;
		int startIndex = 1;
		int itemsPerPage = 0;
		int endIndex = 0;
		String params = "attributes=externalId";
		
		while (total > endIndex) {
			// 기본적으로 정상인증이 되어있는지에 대한 확인이 필요함
			JSONObject response = client.get(URL + Resource + "?" + params + "&startIndex=" + startIndex);

			if (ScimUtils.checkSchemas((JSONArray) response.get("schemas"), Const.schemas_v20_error)) {
				throw new ScimAuthException(401, response.getAsString("detail"));
			}
			
			total = (int) response.getAsNumber("totalResults");
			startIndex = (int) response.getAsNumber("startIndex");
			itemsPerPage = (int) response.getAsNumber("itemsPerPage");
			endIndex = startIndex + itemsPerPage;
			JSONArray resources = (JSONArray) response.get("Resources");

			Iterator<Object> itor_users =  resources.iterator();
			while (itor_users.hasNext()) {
				JSONObject resource = (JSONObject) itor_users.next();
				String externalId = resource.getAsString("externalId");
				userList.add(externalId);
			}
			
			startIndex = endIndex;
		}
		return userList;
	}
	
	private ArrayList<String> getAccountListToBean() {
		// DB Connection 등을 이용하여 인사 서버에서 User Bean을 생성함
		return new DemoHR().getAccountIDList();
	}
	
	private ArrayList<DemoHRBean> getAllAccountToBean() {
		// DB Connection 등을 이용하여 인사 서버에서 User Bean을 생성함
		return new DemoHR().getAccountList();
	}
	
	private DemoHRBean getAccountToBean(String userId) {
		// DB Connection 등을 이용하여 인사 서버에서 User Bean을 생성함
		return new DemoHR().getAccount(userId);
	}
	
	private AccountCompare compared(JSONObject scim, DemoHRBean bean) {
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
		// 신규계정 생성 시
		DemoHRBean user_bean = new DemoHR().getAccount(bean_id);
		String postBody = setSCIMUser(user_bean).toJSONString();

		JSONObject resp = client.post(URL + Resource, postBody);
	}
	
	private void updateAccountToSCIM(String bean_id) {
		String param = "externalId eq \"" + bean_id + "\"";
		JSONObject resp = client.get(URL + Resource + "?attributes=externalId&filter=" + URLEncoder.encode(param));
		// 1개만 넘어와야 함
		Number account_cnt = resp.getAsNumber("totalResults");
		if ((int) account_cnt == 1 ) {
			// Update 처리, PUT
			JSONObject user = (JSONObject) ((JSONArray) resp.get("Resources")).get(0);
			updateAccountToSCIM(user);
		} else {
			// Error Log 처리
			System.out.println("ERROR SCIM have duplicate accounts. - " + bean_id);
		}
		
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
			JSONObject result = client.put(URL + Resource + "/" + user.getAsString("id"), user.toJSONString());
//			System.out.println("UPDATE User : " + user.getAsString("userName") + "/" + hr_id);
			// System.out.println("\t" + user.toJSONString());
			// System.out.println("\t" + result.toJSONString());
		} else {
//			System.out.println("No changed " + user.getAsString("userName") + "/" + hr_id);
		}
			
	}
	
	private void updateAccountToSCIM(JSONObject user_scim_id) {
		// REST Client를 이용하여 WessionIM에 PUT 메소드 전달
		// 기존계정 교체시
		String scim_id = user_scim_id.getAsString("id");
		DemoHRBean user_bean = new DemoHR().getAccount(user_scim_id.getAsString(_user_external_id));
		JSONObject user_scim = client.get(URL + Resource + "/" + scim_id);
		AccountCompare accp = compared(user_scim, user_bean);
		String method = accp.getMethod();
		
		JSONObject put_json = new JSONObject();
		put_json.put("id", scim_id);
		put_json.put(_user_schemas, user_scim.get(_user_schemas));
		
		if (method.equals("patch")) {
			ArrayList <CompareData> arry = accp.getData();
			for (int i=0; i<arry.size(); i++) {
				CompareData cpd = arry.get(i);
				if (cpd.getAttrName().contains(".")) {
					String [] cpds = cpd.getAttrName().split("\\.");
					String att_name = cpds[0];
					String att_value = cpds[1];
					
					if (att_value != null && !"".equals(att_value)) {
						JSONArray inner_arry = new JSONArray();
						JSONObject inner_obj = new JSONObject();
						inner_obj.put("type", "work");
						inner_obj.put(att_value, cpd.getNewValue());
						inner_arry.add(inner_obj);
						put_json.put(att_name, inner_arry);
						
					} else {
						// 지우는 것임
						put_json.remove(att_name);
					}
					
					//System.out.println(cpd.getAttrName());
				} else {
					put_json.put(cpd.getAttrName(), cpd.getNewValue());
				}
			}
			
			String postBody = put_json.toJSONString();
			//System.out.println("[" + put_json.size() + "]  " + postBody);
			if (put_json.size()>2) {
				JSONObject puts = client.put(URL + Resource + "/" + scim_id, postBody);
//				System.out.println(puts.toJSONString());
			}
		}
		
		// Groups 정리는 별도로 진행
		//TODO 실제 이부분은 서버에서 처리해야 하지 않을까?
		addGroups(user_scim);
		
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
		JSONObject resp = client.get(URL + Resource + "?attributes=externalId&filter=" + URLEncoder.encode(param));
		scim_id = ScimUtils.findArray((JSONArray) resp.get("Resources"), "id");
		JSONObject dels = client.delete(URL + Resource + "/" + scim_id);
//		System.out.println(dels.toJSONString());
	}
}
