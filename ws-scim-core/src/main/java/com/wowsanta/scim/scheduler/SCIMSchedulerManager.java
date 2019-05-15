package com.wowsanta.scim.scheduler;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wowsanta.scim.exception.SCIMException;

public class SCIMSchedulerManager{
	
	transient Logger logger = LoggerFactory.getLogger(SCIMSchedulerManager.class);
	
	private static transient SCIMSchedulerManager instance;
	private List<SCIMScheduler> schedulerList = new ArrayList<SCIMScheduler>(); 
	
	public static SCIMSchedulerManager getInstance() {
		if(instance == null) {
			instance = new SCIMSchedulerManager();
		}
		return instance;
	}
	public void initialize() throws SCIMException {
		for (SCIMScheduler scimScheduler : this.schedulerList) {
			scimScheduler.build();
		}
	}
	public void addScheduler(SCIMScheduler scheduler) {
		this.schedulerList.add(scheduler);
	}
	public List<SCIMScheduler> getSchedulerList() {
		return schedulerList;
	}
	public void setSchedulerList(List<SCIMScheduler> schedulerList) {
		this.schedulerList = schedulerList;
	}
	public void close() throws SCIMException {
		for (SCIMScheduler scimScheduler : schedulerList) {
			scimScheduler.shutdown();
		}
	}
	public SCIMScheduler getScheduler(String schedulerId) {
		for (SCIMScheduler scimScheduler : schedulerList) {
			if(scimScheduler.getSchedulerId().equals(schedulerId)) {
				return scimScheduler;
			}
		}
		return null;
	}
}
