package com.wession.scim.test;

import java.util.Iterator;
import java.util.LinkedList;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

public class SignletoneTest {
	private LinkedList arry = new LinkedList();

	private SignletoneTest() {
	}

	private static class Singleton {
		private static final SignletoneTest instance = new SignletoneTest();
	}

	public static SignletoneTest getInstance() {
		return Singleton.instance;
	}

	public Iterator getIterator() {
		synchronized (arry) {
			return arry.iterator();
		}
	}
	
	public int getSize() {
//		synchronized (arry) {
			return arry.size();
//		}
	}
	
	public JSONObject get(int idx) {
//		synchronized (arry) {
			return (JSONObject) arry.get(idx);
//		}
	}

	public void addArray(JSONObject json) {
		//synchronized (arry) {
			arry.add(json);
		//}
	}

	public void delArray(String id) {
		synchronized (arry) {
			Iterator itor = arry.iterator();
			if (itor.hasNext()) {
				JSONObject obj = (JSONObject) itor.next();
				String find_id = obj.getAsString("id");
				if (find_id.equals(id)) {
					itor.remove();
				}
			}
		}
	}
}
