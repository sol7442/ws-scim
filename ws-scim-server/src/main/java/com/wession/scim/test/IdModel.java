package com.wession.scim.test;

import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import com.wession.common.ScimUtils;
import com.wession.common.DateUtil;
import com.wession.scim.resource.Group;
import com.wession.scim.resource.Member;
import com.wession.scim.resource.Meta;
import com.wession.scim.resource.User;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

public class IdModel {

	private final static String schemas_v20_user = "urn:ietf:params:scim:chemas:core:2.0:User";
	private final static String schemas_v20_group ="urn:ietf:params:scim:schemas:core:2.0:Group";
	
	private static String baseURL = "http://localhost:5000/scim/v2.0";
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String sysid =  ScimUtils.makeId();
		String userid = "sso_user_01";
		String resourceType = "User";
		
		JSONObject user = new JSONObject();
		
			JSONArray schemas = new JSONArray();
			{
				schemas.add(schemas_v20_user);
			}
		
		user.put("schemas", schemas);
		user.put("id", sysid);
		user.put("externalId", userid);
		
			JSONObject meta = new JSONObject();
			meta.put("resourceType", resourceType);
			meta.put("created", "15 Apr 2017 04:25:59 GMT");
			meta.put("lastModified", DateUtil.getDateTimeUTC());
			meta.put("location", ScimUtils.makeRef(baseURL, sysid, resourceType));
			meta.put("version", "W/\"000000000000001\"");
			
		user.put("meta",  meta);
		
			JSONObject name = new JSONObject();
			name.put("formatted", "Mr. Lee, Eunjun");
			name.put("familyName", "Lee");
			name.put("givenName", "Eunjun");
			name.put("middleName", "");
			name.put("honorificPrefix", "Mr.");
			name.put("honorificSuffix", "");
			
		user.put("name", name);
		user.put("userName", "eunjun.lee");
		
			JSONArray phoneNumbers = new JSONArray();
			{
				JSONObject phonenumber = new JSONObject();
				phonenumber.put("value", "010-6223-5635");
				phonenumber.put("type", "010-6223-5635");
				phoneNumbers.add(phonenumber);
			}
			
		user.put("phoneNumbers", phoneNumbers);
		
			JSONArray emails = new JSONArray();
			{
				JSONObject email = new JSONObject();
				email.put("value", "lej0718@gmail.com");
				email.put("type", "work");
				email.put("primary", true);
				emails.add(email);
			}
		
		user.put("emails", emails);
		System.out.println(user.toJSONString());
		User user_1 = new User(user.toJSONString());

		String groupid = ScimUtils.makeId();
		
		JSONObject group = new JSONObject();
		//JSONArray schemas = new JSONArray();
		{
			schemas.clear();
			schemas.add(schemas_v20_group);
		}
		
		group.put("schemas", schemas);
		group.put("id", groupid);
		group.put("displayName", "Administrator");
		
			JSONArray members = new JSONArray();
			{
				JSONObject member = new JSONObject();
				member.put("value", "eb2a83fd-41aa-47d1-b63d-9c2c3c4d340a");
				member.put("$ref", findValue(user, "location"));
				member.put("display", findValue(user, "userName"));
			}
		group.put("members", members);
		
			//JSONObject meta = new JSONObject();
			meta.put("resourceType", "Group");
			meta.put("created", "15 Apr 2017 03:25:59 GMT");
			meta.put("lastModified", DateUtil.getDateTimeUTC());
			meta.put("location", ScimUtils.makeRef(baseURL, groupid, "Group"));
			meta.put("version", "W/\"000000000000001-001\"");
			
		group.put("meta",  meta);
		
		System.out.println(group.toJSONString());
		
		System.out.println("\n\n\n");
		
		Meta user_meta = new Meta("User");
		User member = new User("lej0718", "이은준", user_meta);
		member.setEmail("lej0718@gmail.com", "work", true);
		member.setEmail("lej0718@naver.com", "home", false);
		member.setEmail("lej0718@na-no.co.kr", "work", true);
		member.setEmail("lej0718@gmail.com", "home", false);
		member.setEmail("lej0718@gmail.com", "work", false);
		member.setEmail("lej0718@naver.com", "home", true);
		
		member.removeEmail("lej0718@na-no.co.kr");
		
		member.setPhoneNumber("010-6223-5635", "mobile", true);
		member.setPhoneNumber("070-4254-5635", "work", false);
		user_meta.setLastModify(member.toJSONObject());
		
		System.out.println(member.toJSONString());
		System.out.println(member.getPhoneNumbers());
		
		System.out.println("\n\n\n");
		
		User member2 = new User("misook.kang", "강미숙", user_meta);
		member2.put("display", "강미숙 과장");
		member2.setEmail("misook.kang@wowsanta.com", "work", true);
		member2.setPhoneNumber("010-1230-4567", "mobile", "아이폰", true );
		
		Meta group_meta = new Meta("Group");
		Group pioneer = new Group("파이오니아", group_meta);
		pioneer.addMember(member.toMember());
		pioneer.addMember(member2.toMember());
		pioneer.addMember(member.toMember());
		pioneer.removeMember(member.toMember());
		pioneer.addMember(member.toMember());
		pioneer.addMember(user_1.toMember());
		
		System.out.println(pioneer.toJSONString());
		
		System.out.println("\n\n\n");
		
		System.out.println(group_meta.get("version"));
		group_meta.setLastModify(member2.toJSONObject());
		
		System.out.println("\n\n");
		Group demacia = new Group("데마시아", group_meta);
		Group noxus = new Group("녹서스", group_meta);
		member.addGroup(demacia);
		member.addGroup(noxus);
		member.updateModifyTime();
		member2.addGroup(demacia);
		System.out.println(member.toJSONString());
		System.out.println(demacia.toJSONString());
	}


    public static String findValue(JSONObject jObj, String findKey)  {

    Set <String> keys = jObj.keySet();

    for (String key: keys) {
        if (key.equalsIgnoreCase(findKey)) {
            return jObj.getAsString(key);
        }
        if (jObj.get(key) instanceof JSONObject) {
            return findValue((JSONObject)jObj.get(key), findKey);
        }
    }

    return "";
}
}
