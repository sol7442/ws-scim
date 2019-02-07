package com.wowsanta.scim.scheduler;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.json.AbstractJsonObject;

public class SCIMScheduler extends AbstractJsonObject {
	private String name;
	private String description;

	private SCIMJob job ;

	private List<String>  subJobs = new ArrayList<String>();
	private SCIMTrigger trigger;
	
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
	public void setJob(SCIMJob job){
		this.job = job;
	}
	public SCIMJob getJob() {
		return this.job;
	}
	
	public List<String> getSubJobs() {
		return subJobs;
	}
	public void addSubJob(String subJob) {
		this.subJobs.add(subJob);
	}
	public void setSubJobs(List<String> subJobs) {
		this.subJobs = subJobs;
	}
	public SCIMTrigger getTrigger() {
		return trigger;
	}
	public void setTrigger(SCIMTrigger trigger) {
		this.trigger = trigger;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Scheduler build() throws SCIMException{
		
		try {
		
			Class job_cls = Class.forName(this.job.getClassName());
			JobDetail job_detail = newJob(job_cls).build();
			
			Queue<JobDetail> jobDetailQueue = new ConcurrentLinkedQueue<JobDetail>();
			for (String job_clss : subJobs) {
				Class cls = Class.forName(job_clss);
				JobDetail job_deail = newJob(cls).build();
				jobDetailQueue.add(job_deail);
			}
			job_detail.getJobDataMap().put("JobDetailQueue", jobDetailQueue);
			
			this.scheduler = StdSchedulerFactory.getDefaultScheduler();
			this.scheduler.start();
			this.scheduler.scheduleJob(job_detail,this.trigger.build());
			
		}catch (Exception e) {
			throw new SCIMException("Scheduler build Failed []",e);
		}
		return this.scheduler;
	}
	public static SCIMScheduler load(String json_string) throws SCIMException {
		
		SCIMScheduler scheduler = new SCIMScheduler();
		try {
			//JsonReader reader = new JsonReader(new FileReader(file_name));
			JsonObject jsonObject = new JsonParser().parse(json_string).getAsJsonObject();
			
			scheduler.setName(jsonObject.get("name").getAsString());
			scheduler.setDescription(jsonObject.get("description").getAsString());
			JsonArray sub_jobs = jsonObject.get("subJobs").getAsJsonArray();
			for (JsonElement jsonElement : sub_jobs) {
				scheduler.addSubJob(jsonElement.getAsString());
			}
			
			scheduler.setJob((SCIMJob) SCIMJob.load(jsonObject.get("job").getAsJsonObject()));
			scheduler.setTrigger((SCIMTrigger) SCIMTrigger.load(jsonObject.get("trigger").getAsJsonObject()));
			
		} catch (Exception e) {
			throw new SCIMException("SCIMScheduler load Failed ["+json_string+"]",e);
		}
		
		return scheduler;
	}
	
	public void shutdown() throws SCIMException {
		try {
			this.scheduler.shutdown();
		} catch (SchedulerException e) {
			throw new SCIMException("Scheduler Shutdown Failed",e);
		}
	}
	
	
}
