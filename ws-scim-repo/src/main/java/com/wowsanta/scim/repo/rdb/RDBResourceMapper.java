package com.wowsanta.scim.repo.rdb;

import java.util.HashMap;
import java.util.Map;

public class RDBResourceMapper {
	private static RDBResourceMapper instance;
	
	private Map<String,String> map = new HashMap<String,String>();
	
	public Object get(String key) {
		Object obj = null;		
		try {
			obj = Class.forName(this.map.get(key)).newInstance();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		return obj;
	}
}
