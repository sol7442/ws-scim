package com.wowsanta.scim.scheduler;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.json.AbstractJsonObject;

public class SCIMSchedulerManager extends AbstractJsonObject{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static transient SCIMSchedulerManager instance;
	
	private List<SCIMScheduler> schedulerList = new ArrayList<SCIMScheduler>(); 
	
	public static SCIMSchedulerManager getInstance() {
		if(instance == null) {
			instance = new SCIMSchedulerManager();
		}
		return instance;
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

//	public void load(String config_file) throws SCIMException{
//		try {
//			JsonReader reader = new JsonReader(new FileReader(config_file));
//			JsonObject jsonObject = new JsonParser().parse(reader).getAsJsonObject();
//			JsonArray scheduler_list_json = jsonObject.get("schedulerList").getAsJsonArray();
//			for (JsonElement jsonElement : scheduler_list_json) {
//				System.out.println(jsonElement);
//				SCIMScheduler scheduler = (SCIMScheduler) SCIMScheduler.load(jsonElement.toString());
//				scheduler.build();
//				this.schedulerList.add(scheduler);
//			}
//			
//		} catch (FileNotFoundException e) {
//			throw new SCIMException("Scheduler Manager Load Failed : "+config_file,e);
//		}
//		
//	}
	public void close() throws SCIMException {
		for (SCIMScheduler scimScheduler : schedulerList) {
			scimScheduler.shutdown();
		}
	}
}
