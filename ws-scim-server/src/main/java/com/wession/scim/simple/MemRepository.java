package com.wession.scim.simple;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.jayway.jsonpath.JsonPath;
import com.wession.common.KryoUtil;
import com.wession.scim.resource.Group;
import com.wession.scim.resource.Meta;
import com.wession.scim.resource.User;
import com.wession.scim.schema.Error;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

public class MemRepository {
	private static MemRepository repo;
	
	private JSONArray users;
	private JSONArray groups;
	public JSONObject serviceProvider;
	public JSONObject policy;
	
	private static class Singleton {
		private static final MemRepository instance = new MemRepository();
	}
	
	public MemRepository () {
		users = new JSONArray();
		groups = new JSONArray();
	}
	
	public static synchronized  MemRepository getInstance() {
		return Singleton.instance;
	}

	public int getSize(String resource) {
		if ("Users".equals(resource)) {
				return users.size();
		} else if ("Groups".equals(resource)) {
				return groups.size();
		} else {
			return 0;
		}
	}
	
	public JSONObject get(String resource, int index) {
		if ("Users".equals(resource)) {
				try {
					return (JSONObject) users.get(index);
				} catch (IndexOutOfBoundsException iobe) {
					return (JSONObject) users.get(users.size()-1);
				}
		} else if ("Groups".equals(resource)) {
				try {
					return (JSONObject) groups.get(index);
				} catch (IndexOutOfBoundsException iobe) {
					return (JSONObject) groups.get(groups.size()-1);
				}
		} else {
			return null;
		}
	}
	
	public void remove(String resource, int index) {
		if ("Users".equals(resource)) {
				try {
					users.remove(index);
				} catch (IndexOutOfBoundsException iobe) {
					iobe.printStackTrace();
				}
		} else if ("Groups".equals(resource)) {
				try {
					groups.remove(index);
				} catch (IndexOutOfBoundsException iobe) {
					iobe.printStackTrace();
				}
		}
	}
	
	public void remove(String resource, Object obj) {
		if ("Users".equals(resource)) {
				try {
					users.remove(obj);
				} catch (IndexOutOfBoundsException iobe) {
					iobe.printStackTrace();
				}
		} else if ("Groups".equals(resource)) {
				try {
					groups.remove(obj);
				} catch (IndexOutOfBoundsException iobe) {
					iobe.printStackTrace();
				}
		}
	}
	
	public void reset(String resource) {
		if ("Users".equals(resource)) {
			try {
				users.clear();
			} catch (IndexOutOfBoundsException iobe) {
				iobe.printStackTrace();
			}
		} else if ("Groups".equals(resource)) {
			try {
				Iterator itor = groups.iterator();
				while (itor.hasNext()) {
					JSONObject group = (JSONObject) itor.next();
					group.remove("members");
				}
			} catch (IndexOutOfBoundsException iobe) {
				iobe.printStackTrace();
			}
		}
	}
	
	public Iterator iterator(String resource) {
		if ("Users".equals(resource)) {
			//System.out.println("MemRepository : Users - " + users.size());
			return users.iterator();
		} else if ("Groups".equals(resource)) {
			//System.out.println("MemRepository : Groups " + groups.size());
			//System.out.println("MemRepository : Groups " + groups.iterator());
			return groups.iterator();
		} else {
			return null;
		}
	}
	
	public JSONObject getResource(String resource, String sysid) {
		Iterator itor = iterator(resource);
		while (itor.hasNext()) {
			JSONObject o = (JSONObject) itor.next();
			String id = o.getAsString("id");
			if (id.equals(sysid)) return o;
		}
		Error err = new Error(404, "Not find " + resource + " - " + sysid);
		return err;
	}
	
	public void addUser(User member) {
		users.add(member.toJSONObject());
	}
	
	public void addUser(JSONObject member) {
		users.add(member);
	}
	
	public void deleteUser(JSONObject member) {
		users.remove(member);
	}
	
	public void deleteUser(String sysid) {
//		Iterator itor = users.iterator();
		for (int i = 0; i< getSize("Users"); i++) {
			JSONObject obj = (JSONObject) get("Users", i);
			String find_id = obj.getAsString("id");
			if (find_id.equals(sysid)) {
				remove("Users", obj);
			}
		}
	}
	
	public boolean existUser(String sysid) {
		Iterator itor = users.iterator();
		while (itor.hasNext()) {
			JSONObject obj = (JSONObject) itor.next();
			String find_id = obj.getAsString("id");
			if (find_id.equals(sysid)) return true;
		}
		return false;
	}
	
	public boolean existUser(String id, String attrValue) {
		Iterator itor = users.iterator();
		while (itor.hasNext()) {
			JSONObject obj = (JSONObject) itor.next();
			String find_id = obj.getAsString(attrValue);
			if (find_id.equals(id)) return true;
		}
		return false;
	}
	
	public void changeUser(User member) {
		String member_id = member.getAsString("id"); 
		Iterator itor = users.iterator();
		int rm_idx = -1;
		int idx = 0;
		while (itor.hasNext()) {
			JSONObject o = (JSONObject) itor.next();
			String id = o.getAsString("id");
			if (id.equals(member_id)) rm_idx = idx;
			idx++;
		}
		if (rm_idx > -1) {
			users.remove(rm_idx);
			users.add(member.toJSONObject());
		}
	}
	
	public JSONObject getUser(String sysid) {
//		System.out.println("getUser - users size : " + users.size());
		Iterator itor = users.iterator();
		while (itor.hasNext()) {
			JSONObject o = (JSONObject) itor.next();
			String id = o.getAsString("id");
			if (id.equals(sysid)) {
				return o;
			}
		}
		Error err = new Error(404, "Not find user - " + sysid);
		return err;
	}
	
	public void addGroup(Group group) {
		groups.add(group.toJSONObject());
	}
	
	public void addGroup(JSONObject group) {
		groups.add(group);
	}
	
	public boolean existGroup(String sysid) {
		Iterator itor = groups.iterator();
		if (itor.hasNext()) {
			JSONObject obj = (JSONObject) itor.next();
			String find_id = obj.getAsString("id");
			if (find_id.equals(sysid)) return true;
		}
		return false;
	}
	
	public JSONObject getGroup(String sysid) {
		Iterator itor = groups.iterator();
		while (itor.hasNext()) {
			JSONObject o = (JSONObject) itor.next();
			String oName = o.getAsString("id");
			if (oName.equals(sysid)) return o;
		}
		Error err = new Error(404, "Not find group - " + sysid);
		return err;
	}
	
	public JSONObject findGroup(String displayName) {
		Group rGroup = null;
		Iterator itor = groups.iterator();
		while (itor.hasNext()) {
			JSONObject o = (JSONObject) itor.next();
			String oName = o.getAsString("displayName");
			if (oName.equals(displayName)) return o;
		}
		Error err = new Error(404, "Not find group - " + displayName);
		return err;
	}
	
	public ArrayList<Map<String, Object>> findUser(String field, String value) {
		return JsonPath.parse(users).read("$[*][?(@." + field + " == '" + value + "')]");
	}
	
	public JSONArray findUserInGroup(Group group) {
		String gid = group.getAsString("id");
		JSONArray rArry = new JSONArray();
		Iterator itor = users.iterator();
		while (itor.hasNext()) {
			JSONObject o = (JSONObject) itor.next();
			JSONArray oa = (JSONArray) o.get("groups");
			if (oa != null) {
				for (Object oao : oa) {
					JSONObject oaoj = (JSONObject) oao;
					if (oaoj.getAsString("value").equals(gid)) {
						rArry.add(o);		
					}
				}
			}
		}
		return rArry;
	}
	
	public JSONArray findUserInGroup(String groupName) {
		JSONArray rArry = new JSONArray();
		Iterator itor = users.iterator();
		while (itor.hasNext()) {
			JSONObject o = (JSONObject) itor.next();
			JSONArray oa = (JSONArray) o.get("groups");
			if (oa != null) {
				for (Object oao : oa) {
					JSONObject oaoj = (JSONObject) oao;
					if (oaoj.getAsString("display").equals(groupName)) {
						rArry.add(o);		
					}
				}
			}
		}
		return rArry;
	}

	
	public static void main(String[] args) throws InterruptedException, IOException {
		// Kryo를 통한 저장
		// JSON 자체를 저장 
		repo = MemRepository.getInstance();
		
		File file = new File("./groups.dat");
		if (file.exists()) {
			if (repo.groups.size() == 0) {
				repo.groups = KryoUtil.readFiles(file);
			} else {
				Group pioneer = new Group("파이오니어", new Meta("Group"));
				Group warrior = new Group("전사", new Meta("Group"));
				repo.addGroup(pioneer);
				repo.addGroup(warrior);
				KryoUtil.writeFiles(file, repo.groups);
			}
		} else {
			Group pioneer = new Group("파이오니어", new Meta("Group"));
			Group warrior = new Group("전사", new Meta("Group"));
			repo.addGroup(pioneer);
			repo.addGroup(warrior);
			KryoUtil.writeFiles(file, repo.groups);
		}
		
		
		Group pioneer = new Group(repo.findGroup("파이오니어"));
		Group warrior = new Group(repo.findGroup("전사"));
		
		file = new File("./users.dat");
		if (file.exists()) {
			if (repo.users.size() == 0) {
				repo.users = KryoUtil.readFiles(file);
			} else {
				for (int i=0; i<1000; i++) {
					User user = new User("lej0718@gmail.com", "Eunjun.Lee");
					repo.addUser(user);
					if (i % 31 == 6) 
						user.addGroup(pioneer);
					if (i % 29 == 14) 
						user.addGroup(warrior);
					user.updateModifyTime();
				}
				KryoUtil.writeFiles(file, repo.users);
			}
		} else {
			for (int i=0; i<1000; i++) {
				User user = new User("lej0718@gmail.com", "Eunjun.Lee");
				repo.addUser(user);
				if (i % 31 == 6) 
					user.addGroup(pioneer);
				if (i % 29 == 14) 
					user.addGroup(warrior);
				user.updateModifyTime();
			}
			KryoUtil.writeFiles(file, repo.users);
		}

		System.out.println("0) " + System.currentTimeMillis());

		System.out.println("repo.users.size() : " + repo.users.size());
		long lap1 = System.currentTimeMillis();
		System.out.println("1) " + lap1);
		
		ArrayList<Map<String, String>> men = JsonPath.read(repo.users, "$[*][?(@.groups[0].display == '전사')]");
		long lap2 = System.currentTimeMillis();
		System.out.println("2) " + lap2 + "(" + (lap2-lap1) + "ms)");
		System.out.println("jsonpath size : " + men.size());
		
		JSONArray rArry = new JSONArray();
		Iterator itor = repo.users.iterator();
		while (itor.hasNext()) {
			JSONObject o = (JSONObject) itor.next();
			JSONArray oa = (JSONArray) o.get("groups");
			if (oa != null) {
				for (Object oao : oa) {
					JSONObject oaoj = (JSONObject) oao;
					if (oaoj.getAsString("display").equals("전사")) {
						rArry.add(o);		
					}
				}
			}
				
		}
		long lap3 = System.currentTimeMillis();
		System.out.println("3) " + lap3 + "(" + (lap3-lap2) + "ms)");
		System.out.println("itorator size : " + rArry.size());
		System.out.println(rArry.toJSONString());

		for (int i=0; i<men.size(); i++) {
			
			Map o = men.get(i);
			Iterator keys = o.keySet().iterator();
			while(keys.hasNext()) {
				String key = (String) keys.next();
				//System.out.println("\t" + key + " : " + o.get(key));
				//System.out.println("\t" + "instanceof " + o.get(key).getClass());
			}
			//System.out.println("instanceof " + o.getClass());
			//System.out.println("JSON \t " + i + ":" + o);
		}
		
	}
	
	Lock reentrantLock = new ReentrantLock();
	public void load() {
		// TODO Auto-generated method stub
		reentrantLock.lock();
		try {
			File users_file = new File("./users.dat");
			if (users_file.exists()) {
				users = KryoUtil.readFiles(users_file);
			} else {
				KryoUtil.writeFiles(users_file, users);
			}
			
			
			File groups_file = new File("./groups.dat");
			if (groups_file.exists()) {
				groups = KryoUtil.readFiles(groups_file);
			} else {
				KryoUtil.writeFiles(groups_file, users);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			reentrantLock.unlock();
		}
	}
	
	public void save() {
		// TODO Auto-generated method stub
		reentrantLock.lock();
		try {
			KryoUtil.writeFiles(new File("./users.dat"), users);
			KryoUtil.writeFiles(new File("./groups.dat"), groups);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			reentrantLock.unlock();
		}
	}

}
