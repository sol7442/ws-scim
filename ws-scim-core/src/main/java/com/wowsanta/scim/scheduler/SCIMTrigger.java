package com.wowsanta.scim.scheduler;

import org.quartz.Trigger;

import com.wowsanta.scim.json.AbstractJsonObject;
import com.wowsata.util.json.WowsantaJson;

public abstract class SCIMTrigger extends WowsantaJson {
	private final String base;
	
	public SCIMTrigger(String base) {
		this.base = base;
	}

	public String getBase() {
		return base;
	}
	
	public abstract Trigger build();


}
