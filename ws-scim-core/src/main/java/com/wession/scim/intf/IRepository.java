/**
 * 
 */
package com.wession.scim.intf;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import com.wession.scim.resource.Group;
import com.wession.scim.resource.User;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

/**
 * @author EJLee
 *
 */
public interface IRepository {
	
	public void load(); // Repository를 로딩하는 메소드
	public void save(); // Repository를 저장하는 메소드 (DB의 경우는 따로 필요없음)
	public int getSize(String resource); // 지정한 Resource(Users, Groups)의 개수를 반환함
	public void reset(String resource); // 지정한 Resource(Users, Groups)를 초기화 시킴(지운다)
	
	public JSONObject get(String resource, int index); // 지정한 Resource(Users, Groups)에서 index 번째의 JSONObject를 반환함
	public JSONObject getResource(String resource, String sysid); // 지정한 Resource(Users, Groups)에서 id(UUID)가 일치하는 JSONObject를 반환함
	public Iterator iterator(String resource); // 지정한 Resource(Users, Groups)를 Iterator를 변환하여 반환함
	
	public void remove(String resource, int index); // 지정한 Resource(Users, Groups)에서 index 번째의 JSONObject를 반환함
	public void remove(String resource, Object obj);
	
	public boolean existUser(String sysid);
	public boolean existUser(String id, String attrValue);
	public JSONObject getUser(String sysid) ;
	public void addUser(User member);
	public void addUser(JSONObject member);
	public void changeUser(User member) ;
	public void deleteUser(JSONObject member);
	public void deleteUser(String sysid);

	public ArrayList<Map<String, Object>> findUser(String field, String value);
	public JSONArray findUserInGroup(Group group);
	public JSONArray findUserInGroup(String groupName);

	public void addGroup(Group group);
	public void addGroup(JSONObject group);
	
	public boolean existGroup(String sysid);
	public JSONObject getGroup(String sysid);
	public JSONObject findGroup(String displayName);
	
	
	
}
