package com.wowsanta.scim.resource;

import java.util.HashMap;
import java.util.Map;

public class Target {

	private String code;
	private String name;
	private Map<String,Target> subTargets ;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Map<String,Target> getSubTargets() {
		return subTargets;
	}
	public void setSubTargets(Map<String,Target> sub) {
		this.subTargets = sub;
	}
	public boolean hasSubTargets() {
		if(this.subTargets == null) {
			return false;
		}
		
		return this.subTargets.size() == 0 ? false : true;
	}
	public Target getSubTarget(String name) {
		if(this.subTargets == null) {
			return null;
		}
		return this.subTargets.get("name");
	}
	public void putSubTarget(Target target) {
		if(this.subTargets == null) {
			this.subTargets = new HashMap<String, Target>();
		}
		this.subTargets.put(target.name,target);
	}
}
