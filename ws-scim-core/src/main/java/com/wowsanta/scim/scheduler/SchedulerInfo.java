package com.wowsanta.scim.scheduler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

public class SchedulerInfo {
	
	private String name;
	private String description;
	private String type;
	
	private JobInfo jobInfo;
	private TriggerInfo triggerInfo;
	
	private Scheduler scheduler;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public JobInfo getJobInfo() {
		return jobInfo;
	}
	public void setJobInfo(JobInfo job_info) {
		this.jobInfo = job_info;
	}
	public TriggerInfo getTriggerInfo() {
		return triggerInfo;
	}
	public void setTriggerInfo(TriggerInfo trigger_info) {
		this.triggerInfo = trigger_info;
	}
	public Scheduler getScheduler() {
		return scheduler;
	}
	public void setScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
	}
	
	public Scheduler build() throws SchedulerException, InterruptedException, ClassNotFoundException{
		this.scheduler = StdSchedulerFactory.getDefaultScheduler();
		this.scheduler.start();
		this.scheduler.scheduleJob(jobInfo.build(),triggerInfo.build());
		Thread.sleep(90L * 1000L);
		
		return this.scheduler;
	}
	
	public static SchedulerInfo load(String file_name) throws FileNotFoundException {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonReader reader = new JsonReader(new FileReader(file_name));
		return gson.fromJson(reader,SchedulerInfo.class); 
	}
	
	public void save(String file_name) throws IOException {
	
		OutputStreamWriter writer = new OutputStreamWriter(
				new FileOutputStream(
						new File(file_name)),StandardCharsets.UTF_8);
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		gson.toJson(this,writer);
		writer.flush();
		writer.close();
	}

	public String toJson() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(this);
	}
}
