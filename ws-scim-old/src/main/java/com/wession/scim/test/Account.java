package com.wession.scim.test;

import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import com.wession.common.RESTClient;
import com.wession.common.ScimUtils;
import com.wession.scim.intf.schemas_name;
import com.wession.scim.resource.Group;
import com.wession.scim.resource.User;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

public class Account implements schemas_name {
	private static final String FamilyNames = "김구이박정김이전유정홍천방김박이최최채황나남동라김박이장방성서안어주김박이장조표하강장우민강전";
	private static final String Name = "미마문면연안강승중용종희태순성민명준철섭선상리용성기민일은준상한택은주우신장철형수홍창정대희연서훈영호영연호삼미숙강태";
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
		num4 = generator.nextInt(1000);
		num5 = generator.nextInt(1000);
		num6 = generator.nextInt(1000);
		String id = ((num1*num2+num4) % 10) + "" + num1 + "" +  ((num2*num3+num5) % 10) + num2 + "" +  ((num3*num1+num6) % 10) + num3 + String.format("%03d", num4); 
		//String id = ((num1*num2) % 10) + "" + num1 + "" +  ((num2*num3) % 10) + num2 + "" +  ((num3*num1) % 10) + num3 ;
		return "WS" + id;
	}
	
	public static void main(String [] args) {
	
		System.out.println("==== Start ==== Account" );
		setMapper();
		
		// HR DB에서 데이터 추출 처리
		// 기본비교점을 찾는다.

		RESTClient client = new RESTClient();
		String URL = "http://localhost:5000/scim/v2.0/";
		String Resource = "Users";
		String params = "attributes=externalId";
		
		int total = 100;
		int startIndex = 1;
		int itemsPerPage = 0;
		int endIndex = 0;
		Account ac = new Account();
		
		ArrayList arry = ac.getUsers();
		
		ArrayList addUserList = new ArrayList();
		ArrayList delUserList = new ArrayList();
		ArrayList modifyUserList = new ArrayList();
		
		JSONObject json_app_gw = new JSONObject();
		json_app_gw.put("display", "Groupware");
		json_app_gw.put("value", "ce9c1b4e-6993-4558-a5a8-0f79b26cb7b3");
		json_app_gw.put("$ref", "http://localhost:5000/scim/v2.0/Groups/ce9c1b4e-6993-4558-a5a8-0f79b26cb7b3");
		
		JSONObject json_app_bo = new JSONObject();
		json_app_bo.put("display", "BuddyOne");
		json_app_bo.put("value", "8752fc15-5519-4ff3-af07-1198b0516f90");
		json_app_bo.put("$ref", "http://localhost:5000/scim/v2.0/Groups/8752fc15-5519-4ff3-af07-1198b0516f90");

		
		try {
			addUserList = (ArrayList) ScimUtils.deepCopy(arry);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		while (total > endIndex) {
			JSONObject response = client.get(URL + Resource + "?" + params + "&startIndex=" + startIndex);
			System.out.println(response.toJSONString());
			total = (int) response.getAsNumber("totalResults");
			startIndex = (int) response.getAsNumber("startIndex");
			itemsPerPage = (int) response.getAsNumber("itemsPerPage");
			endIndex = startIndex + itemsPerPage;
			JSONArray resources = (JSONArray) response.get("Resources");

			// 개별 사용자 정보를 호출함
			Iterator itor_users =  resources.iterator();
			while (itor_users.hasNext()) {
				JSONObject resource = (JSONObject) itor_users.next();
				String externalId = resource.getAsString("externalId");
				String scimId = resource.getAsString("id");
				//JSONObject json_user = client.get(URL + Resource + "/" + resource.getAsString("id"));
				//User user = new User(json_user);
				//user.increaseVersion();
				//String externalId = user.getAsString("externalId");
				if (addUserList.contains(externalId)) {
					addUserList.remove(externalId);
					modifyUserList.add(externalId);
//					System.out.println("exist user : " + externalId + " / " + resource.getAsString("id"));
				} else {
					delUserList.add(scimId);
				}
				
//				System.out.println(user.get("externalId") + "/" + user.getAsString("userName")  + "/" + user.getLastModified());
			}
			
			startIndex = endIndex;
		}
		
		// 추가대상사용자 생성 등록
		for (int i=0; i<addUserList.size(); i++) {
			String externalId = (String) addUserList.get(i);
			HashMap <String, String> hm = ac.getUser(externalId);
			
			String hr_id = hm.get("hr_id");
			String username = hm.get("name");
			String dept = hm.get("deptname");
			String deptc = hm.get("deptcode");
			String phone_work = hm.get("phone");
			String email_work = hm.get("email");
			String ims_work = hm.get("ims_id");
			String title = hm.get("title");
			
			User user = new User();
			user.setExternalId(hr_id);
			if (phone_work != null) user.setPhoneNumber(phone_work, "work", false);
			if (email_work != null) user.setEmail(email_work, "work", false);
			if (ims_work != null) user.setIMS(ims_work, "work", false);
			
			user.put("userName", username);
			user.put("displayName", dept+ " " + username + " " + title);
			user.put("title", title);
			
			user.put("deptname", dept);
			
			{
				// 그룹정보는 따라 저장함
				JSONArray groups = new JSONArray();
				JSONObject buddy = new JSONObject();
				JSONObject groupware = new JSONObject();
				
				if (email_work != null && !"".equals(email_work)) {
					groupware.put("value", "ce9c1b4e-6993-4558-a5a8-0f79b26cb7b3");
					groups.add(groupware);
				}
				
				if (ims_work != null && !"".equals(ims_work)) {
					buddy.put("value", "8752fc15-5519-4ff3-af07-1198b0516f90");
					groups.add(buddy);
				}
				
				if (groups.size() > 0) user.put("groups", groups);
				
			}
			
			user.updateModifyTime();
			
			JSONObject result = client.post(URL + Resource, user.toJSONString());
			
//			System.out.println(result.toJSONString());
		}
		
		System.out.println("END OF CREATE USER");
		
		// 수정대상사용자 생성 등록
		for (int i=0; i<modifyUserList.size(); i++) {
			String externalId = (String) modifyUserList.get(i);
			HashMap <String, String> hm = ac.getUser(externalId);
			
			String hr_id = hm.get("hr_id");
			String username = hm.get("name");
			String dept = hm.get("deptname");
			String deptc = hm.get("deptcode");
			String phone_work = hm.get("phone");
			String email_work = hm.get("email");
			String ims_work = hm.get("ims_id");
			String title = hm.get("title");
			
			try {
				User DBUser = ac.setUser(hm);
//				System.out.println(DBUser.toJSONString());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			String param = URLEncoder.encode("externalId eq \"" + hr_id +"\"");
			JSONObject respObj = client.get(URL + Resource + "?filter="+param);
			JSONArray resources = (JSONArray) respObj.get("Resources");
			Iterator itor = resources.iterator();
			JSONObject IMUser = new JSONObject();
			while (itor.hasNext()) {
				IMUser = (JSONObject) itor.next();
			}
			
			User user = new User(IMUser);
			boolean changed = false;
			
			// 변경된 것이 있는지 확인하면 변경
			if (phone_work != null && !"".equals(phone_work)) {
				List <String> phonelist = user.getPhoneNumbers();
				if (phonelist.size()==0 || !phonelist.contains(phone_work)) {
					user.resetPhoneNumber(phone_work, "work", false);
					changed = true;
				}
			} else {
				List<String> phones = user.getPhoneNumbers();
				for (String phoneNumber: phones) {
					user.removePhoneNumber(phoneNumber);
					changed = true;
				}
			}
			
			if (email_work != null && !"".equals(email_work)) {
				List <String> emaillist = user.getEmails();
				if (emaillist.size()==0 || !emaillist.contains(email_work)) {
					user.resetEmail(email_work, "work", false);
					changed = true;
				}
			
			} else {
				List<String> emails = user.getEmails();
				for (String emailaddr: emails){
					user.removeEmail(emailaddr);
					changed = true;
				}
			}
			
			if (ims_work != null && !"".equals(ims_work)) {
				List <String> imslist = user.getIMS();
				if (imslist.size()==0 || !imslist.contains(ims_work)) {
					user.resetIMS(ims_work, "work", false);
					changed = true;
				}
				
			} else {
				List<String> imss = user.getIMS();
				for (String ims: imss) {
					user.removeIMS(ims);
					changed = true;
				}
			}
			
			if (!user.getAsString("userName").equals(username)) {
				user.put("userName", username);
				changed = true;
			}
			
			if (!user.getAsString("displayName").equals(dept+ " " + username + " " + title)) {
				user.put("displayName", dept+ " " + username + " " + title);
				changed = true;
			}
			
			if (!user.getAsString("title").equals(title)) {
				user.put("title", title);
				changed = true;
			}
			
			if (!user.getAsString("deptname").equals(dept)) {
				user.put("deptname", dept);
				changed = true;
			}
			
			// Groups에 대한 관리는 별도로 진행
			{
				// 그룹정보는 따라 저장함
				JSONArray groups = (JSONArray) user.get("groups");
				JSONArray groups_modify = new JSONArray();
				if (email_work != null && !"".equals(email_work)) {
					groups_modify.add(json_app_gw);
				}
				
				if (ims_work != null && !"".equals(ims_work)) {
					groups_modify.add(json_app_bo);
				}
				
				if (groups != null && groups.size()>0) {
					for (int isg = 0; isg < groups.size(); isg++) {
						JSONObject scim_grp = (JSONObject) groups.get(isg);
						boolean isIt = ac.JSONArrayContainsValue(groups_modify, "display", scim_grp.getAsString("display"));
						if (!isIt) changed = true;
					}
					
				} else { // 기존이 없는 경우
					if (groups_modify.size()>0) changed = true;
				}
				
				
				user.put("groups", groups_modify);
			}
			
//			System.out.println(user.toJSONString());

			if (changed) {
				JSONObject result = client.put(URL + Resource + "/" + user.getAsString("id"), user.toJSONString());
				System.out.println("UPDATE User : " + user.getAsString("userName") + "/" + hr_id);
//				System.out.println("\t" + user.toJSONString());
//				System.out.println("\t" + result.toJSONString());
			} else {
				System.out.println("No changed " + user.getAsString("userName") + "/" + hr_id);
			}
		}
		
		System.out.println(delUserList.size());
		System.out.println("START DELETE USER");
		
		for (int idx=0; idx<delUserList.size(); idx++) {
			String id = (String) delUserList.get(idx);
			JSONObject result = client.delete(URL + Resource + "/" + id);
		}
		
		
		System.out.println("START IMS Provisioning");
		
		// 그룹에 등록된 데이터를 불러오자
		ArrayList <String> scim_member_list = new ArrayList <String> ();
		
		JSONObject group_buddy_json = client.get(URL+"/Groups/" + json_app_bo.getAsString("value"));
		Group BuddyOne = new Group(group_buddy_json);
		JSONArray group_members = (JSONArray) BuddyOne.get("members");
		Iterator itor_members = group_members.iterator();

		while (itor_members.hasNext()) {
			JSONObject member = (JSONObject) itor_members.next();
			String link = member.getAsString("$ref");
			link = link.replaceAll("yoursite.com:8080", "localhost:5000");
			User scim_user = new User(client.get(link));
			String ims_id = ScimUtils.findArray((JSONArray) scim_user.get("ims"), "value");
			System.out.println("IMS USER : " + scim_user.getAsString("userName") + " - " + ims_id);
			scim_member_list.add(ims_id);
			
			DemoIMSBean ims_user_bean = new DemoIMSBean(ims_id, 
														scim_user.getAsString(_user_username), 
														scim_user.getAsString(_user_title), 
														scim_user.getPhoneNumbers()==null || scim_user.getPhoneNumbers().size() == 0?null:scim_user.getPhoneNumbers().get(0), 
														scim_user.getAsString(_user_external_id));
			
			// 기존에 있는지 확인
			DemoIMSBean ims_user_db = new DemoIMS().getAccount(ims_id);
			
			if (ims_user_db != null) {
				// 기존 계정은 비교해서 변경처리
				new DemoIMS().updateAccount(ims_user_bean);
			} else {
				// 신규 계정은 insert 처리
				new DemoIMS().insertAccount(ims_user_bean);
			}
		}
		// 없어진 계정을 별도로 정리함
		ArrayList <DemoIMSBean> ghost_account = new DemoIMS().findGhostAccount(scim_member_list);
		System.out.println(ghost_account.size());	
		
		// =====================================================================================
		
		System.out.println();
		System.out.println("START Groupware Provisioning");
		JSONObject group_gw_json = client.get(URL+"/Groups/" + json_app_gw.getAsString("value"));
		Group Groupware = new Group(group_gw_json);
		JSONArray group_members1 = (JSONArray) Groupware.get("members");
		Iterator itor_members1 = group_members1.iterator();
		
		scim_member_list.clear();
		ArrayList <HashMap<String, String>> gw_user = new ArrayList <HashMap<String, String>> ();
		
		while (itor_members1.hasNext()) {
			JSONObject member = (JSONObject) itor_members1.next();
			String link = member.getAsString("$ref");
			link = link.replaceAll("yoursite.com:8080", "localhost:5000");
			User scim_user = new User(client.get(link));
			String gw_id = ScimUtils.findArray((JSONArray) scim_user.get("emails"), "value");
			System.out.println("Groupware USER : " + scim_user.getAsString("userName") + " - " + gw_id);
			
			scim_member_list.add(gw_id);
			
			DemoGWBean gw_user_bean = new DemoGWBean(gw_id, 
														scim_user.getAsString(_user_username), 
														scim_user.getAsString(_user_title), 
														scim_user.getPhoneNumbers()==null || scim_user.getPhoneNumbers().size() == 0?null:scim_user.getPhoneNumbers().get(0),
														scim_user.getAsString("deptname"), 		
														scim_user.getAsString(_user_external_id));
			
			// 기존에 있는지 확인
			DemoGWBean gw_user_db = new DemoGW().getAccount(gw_id);
			
			if (gw_user_db != null) {
				// 기존 계정은 비교해서 변경처리
				new DemoGW().updateAccount(gw_user_bean);
			} else {
				// 신규 계정은 insert 처리
				new DemoGW().insertAccount(gw_user_bean);
			}
		}
		// 없어진 계정을 별도로 정리함
		ArrayList <DemoGWBean> ghost_account2 = new DemoGW().findGhostAccount(scim_member_list);
		System.out.println(ghost_account2.size());	
	
		
		/*
		 * 기초데이터 생성용
		 * 
		Group demacia = new Group("데마시아", new Meta("Group"));
		Group noxus = new Group("녹서스", new Meta("Group"));
		Group ionia = new Group("아이오니아", new Meta("Group"));
		Group fleyod = new Group("플레요드", new Meta("Group"));
		
		MemRepository repo = MemRepository.getInstance();
		repo.addGroup(demacia);
		repo.addGroup(noxus);
		repo.addGroup(ionia);
		repo.addGroup(fleyod);
		
		String findUserName = "";
		for (int i=0; i<1000; i++) {
			
			String userid = getRandomUserID();
			String name = getRandomName();
			findUserName = name;
			User champ = new User(userid, name, new Meta("User"));
			champ.put("displayName", name);
			champ.put("active", true);
			champ.put("userType", "VIP");
			champ.put("locale", "ko-KR");
			if (i % 7 == 2) {
				champ.addGroup(demacia);
			}
			if (i % 7 == 5) {
				champ.addGroup(noxus);
			}
			if (i % 13 == 4) {
				champ.addGroup(ionia);
			}
			if (i % 11 == 6) {
				champ.addGroup(fleyod);
			}
			champ.updateModifyTime();
			//System.out.println(champ.toJSONString());
			repo.addUser(champ);
		}
		
		System.out.println(repo.findUser("userName", findUserName));
		System.out.println();
		System.out.println(repo.findUserInGroup(fleyod));
		System.out.println();
		System.out.println(repo.findGroup("녹서스").toJSONString());
		System.out.println();
		System.out.println(repo.findUserInGroup("녹서스"));

		//repo.save();

	    */
	}

	private ArrayList getUsers() {
		ArrayList arry = new ArrayList();
		try {
			Connection con = null;
			con = DriverManager.getConnection("jdbc:mysql://wession.com/WessionIM", "imHR", "1234");
			java.sql.Statement st = null;
			ResultSet rs = null;
			
			String sql = "select * from T_HR1";
			st = con.createStatement();
			rs = st.executeQuery(sql);

			while (rs.next()) {
				String str = rs.getNString(1);
				arry.add(str);
			}
			
			System.out.println("getUsers size : " + arry.size());
			
			rs.close();
			st.close();
			con.close();

		} catch (SQLException sqex) {
			System.out.println("SQLException: " + sqex.getMessage());
			System.out.println("SQLState: " + sqex.getSQLState());
		}
		return arry;
	}
	
	private HashMap <String, String> getUser(String id) {
		HashMap<String, String> hm = new HashMap<String, String>();
		try {
			Connection con = null;
			con = DriverManager.getConnection("jdbc:mysql://wession.com/WessionIM", "imHR", "1234");
			java.sql.Statement st = null;
			ResultSet rs = null;
			
			String sql = "select id hr_id, Name name, HR.DeptCode deptcode, DEPT.DeptName deptname, title, ims ims_id, email, phone " +  
						 " from T_HR1 HR, T_DeptInfo DEPT where HR.DeptCode = DEPT.DeptCode and  id = '" + id.trim() + "'";
			st = con.createStatement();
			rs = st.executeQuery(sql);

			while (rs.next()) {
				ResultSetMetaData fields = rs.getMetaData();
				for (int colno = 0; colno < fields.getColumnCount(); colno++) {
					String field = fields.getColumnLabel(colno+1);
					hm.put(field, rs.getString(field));
				}
			}
			
			rs.close();
			st.close();
			con.close();

		} catch (SQLException sqex) {
			System.out.println("SQLException: " + sqex.getMessage());
			System.out.println("SQLState: " + sqex.getSQLState());
		}
		return hm;
	}
	
	private boolean JSONArrayContainsValue(JSONArray target, String attrName, String value) {
		if (target == null) return false;
		Iterator itor = target.iterator();
		while (itor.hasNext()) {
			JSONObject obj = (JSONObject) itor.next();
			String str = obj.getAsString(attrName);
			if (str!= null && value.equals(str)) return true;
		}
		return false;
	}

	private User setUser(HashMap <String, String> qm) throws Exception{
		String userid = qm.get("id");
		// required 데이터가 있는지 확인

		boolean fullfilled_externalId = false;
		boolean fullfilled_userName = false;
		
		for (String key: qm.keySet()) {
			String scim_attr = scim_mapper.getProperty(key);
			if ("externalId".equals(scim_attr)) {  
				fullfilled_externalId = true;
			} else if  ("userName".equals(scim_attr)) {
				fullfilled_userName = true;
			}
		}

		if (!(fullfilled_externalId && fullfilled_userName)) {
			throw new Exception();
		}
		
		User user = new User();
		for (String key : qm.keySet()) {
			String scim_attrName = scim_mapper.getProperty(key);
			if (scim_attrName != null) {
				String scim_attrValue = qm.get(key);
//				System.out.println(scim_attrName + ":" + scim_attrValue);
				if ("externalId".equals(scim_attrValue)) {
					user.setExternalId(scim_attrValue);
				} else if (scim_attrName.contains(".")) {
					// . 이 있으면 소그룹
					String [] arrys = scim_attrName.split("\\.");
					if ("emails".equals(arrys[0])) {
						user.setEmail(scim_attrValue, "work", false);
					} else if ("ims".equals(arrys[0])) {
						user.setIMS(scim_attrValue, "work", false);
					} else if ("phoneNumbers".equals(arrys[0])) {
						user.setPhoneNumber(scim_attrValue, "work", false);
					}
				} else {
					user.put(scim_attrName, scim_attrValue);
				}
			}

		}
//		System.out.println(">>>>>" + user.toJSONString());
		return user;
	}
	
	private static Properties scim_mapper = new Properties();

	// 파일에서 읽어와야 하는 부분 
	private static void setMapper() {
		scim_mapper.setProperty("hr_id", "externalId");
		scim_mapper.setProperty("name", "userName");
		scim_mapper.setProperty("title", "title");
		scim_mapper.setProperty("deptname", "deptname");
		scim_mapper.setProperty("ims_id", "ims.value");
		scim_mapper.setProperty("email", "emails.value");
		scim_mapper.setProperty("phone", "phoneNumbers.value");
	}

}
