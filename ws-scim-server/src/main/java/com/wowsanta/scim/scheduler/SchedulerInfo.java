package com.wowsanta.scim.scheduler;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

public class SchedulerInfo {
	private JobInfo job;
	private TriggerInfo trigger;
	
	private Scheduler scheduler;
	public Scheduler build() throws SchedulerException, InterruptedException{
		this.scheduler = StdSchedulerFactory.getDefaultScheduler();
		this.scheduler.start();
		this.scheduler.scheduleJob(job.build(),trigger.build());
		Thread.sleep(90L * 1000L);
		
		return this.scheduler;
	}
}
