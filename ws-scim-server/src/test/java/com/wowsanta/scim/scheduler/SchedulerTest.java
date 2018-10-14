package com.wowsanta.scim.scheduler;

import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class SchedulerTest {

	private final String config_file_name = "../config/scheduler.json";

	
	@Test
	public void make() {
		SchedulerInfo sc_info = new SchedulerInfo();
		sc_info.setName("GW-Conciliation-Scheduler");
		sc_info.setType("Consiliation");
		
		JobInfo job_info = new JobInfo();
		TriggerInfo trigger_info = new TriggerInfo();
		
		sc_info.setJobInfo(job_info);
		sc_info.setTriggerInfo(trigger_info);
		
		
		
	}	
	
	@Test
	public void load() {
		
	}
	
	private class GW_Concilation_Job extends AbstractJob{

		@Override
		public void beforeExecute(JobExecutionContext context) throws JobExecutionException {
			System.out.println("before : --");
		}

		@Override
		public void doExecute(JobExecutionContext context) throws JobExecutionException {
			System.out.println("do exe : --");
		}

		@Override
		public void afterExecute(JobExecutionContext context) throws JobExecutionException {
			System.out.println("after : --");
		}
		
	}
}
