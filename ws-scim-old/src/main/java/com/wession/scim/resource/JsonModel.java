package com.wession.scim.resource;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.wession.scim.controller.ServiceProviderConfig;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

public abstract class JsonModel {
	JSONObject obj;
	
	public void put(String key, Object o) {
		obj.put(key, o);
	}
	
	public void remove(String key) {
		obj.remove(key);
	}
	
	public Object get(String key) {
		return  obj.get(key);
	}
	
	public String getAsString(String key) {
		return obj.getAsString(key);
	}
	
	public Number getAsNumber(String key) {
		return obj.getAsNumber(key);
	}
	
	public Boolean getAsBoolean(String key) {
		return (Boolean) obj.get(key);
	}

	public JSONObject getAsJSONObject(String key) {
		return (JSONObject) obj.get(key);
	}
	
	public void merge(JSONObject obj2) {
		obj.merge(obj2);
	}
	
	public JSONObject toJSONObject() {
		return obj;
	}
	
	public String toJSONString() {
		return obj.toJSONString();
	}
	
	// meta 갱신처리
	public void updateModifyTime() {
		Meta meta = new Meta((JSONObject) obj.get("meta"));
		meta.setLastModify(obj);
		obj.put("meta", meta.toJSONObject());
	}
	
	protected String getRefer() {
		return ServiceProviderConfig.getInstance().getRefer();
	}
	
	protected List<String> getArrayValue(JSONArray arry) {
		List <String> lst = new ArrayList<String>();
		if (arry == null) return lst;
		Iterator itor = arry.iterator();
		while (itor.hasNext()) {
			JSONObject o = (JSONObject) itor.next();
			String ov = o.getAsString("value");
			lst.add(ov);
		}
		return lst;
	}
	
	protected boolean removeArrayValue(JSONArray arry, String value) {
		boolean result = false;
		if (arry == null) {
			result = true;
		} else {
			int remove = -1;
			int idx = 0;
			Iterator itor = arry.iterator();
			while (itor.hasNext()) {
				JSONObject o = (JSONObject) itor.next();
				String ov = o.getAsString("value");
				if (ov == null || ov.equals(value)) {
					remove = idx;
				}
				idx++;
			}
			if (remove>-1) arry.remove(remove);
		}
		return result;
	}
	
	protected boolean setArrayValue(JSONArray arry, String value, String type, String display, boolean primary) {
		if (arry == null) return false;
		
		JSONObject add_o = new JSONObject();
		add_o.put("value", value);
		add_o.put("type", type);
		if (display != null) add_o.put("display", display);
		if (primary) add_o.put("primary", primary);
		
		// 동일한 이메일이 있으면 업데이트 처리
		// primary는 하나만 있어야 하므로, 전체를 변경처리

		int remove = -1;
		int idx = 0;
		Iterator itor = arry.iterator();
		while (itor.hasNext()) {
			JSONObject in_O = (JSONObject) itor.next();
			String in_Value = in_O.getAsString("value");
			
			if (in_Value == null || in_Value.equals(value)) {
				remove = idx;
			}
			
			if (primary) {
				in_O.remove("primary");
			}
			
			idx++;
		}
		if (remove>-1) arry.remove(remove);
		arry.add(add_o);
		
		return true;
	}
	protected boolean resetArrayValue(JSONArray arry, String value, String type, String display, boolean primary) {
		if (arry == null) return false;
		
		arry.clear();
		JSONObject add_o = new JSONObject();
		add_o.put("value", value);
		add_o.put("type", type);
		if (display != null) add_o.put("display", display);
		if (primary) add_o.put("primary", primary);
		arry.add(add_o);
		System.out.println("resetArrayValue : "  + arry.size());
		
		return true;
	}
}
