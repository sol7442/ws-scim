package com.wession.scim.agent;

import java.util.LinkedHashMap;
import java.util.Set;

public class DataMap extends LinkedHashMap<String, Object> { 
	public String getAsString(String key) {
		Object v = this.get(key);
		if (v == null) return null;
		return (String) v;
	}
	public String toJSONString() {
		return "";
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		Set <String> keys = this.keySet();
		for (String key: keys) {
			sb.append(key).append(":").append(this.get(key)).append(",");
		}
		if (sb.length()>2) sb.substring(0, sb.length()-2);
		return sb.toString();
	}
}
