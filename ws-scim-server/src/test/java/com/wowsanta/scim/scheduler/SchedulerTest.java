package com.wowsanta.scim.scheduler;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;

public class SchedulerTest {

	private final String config_file_name = "../config/scheduler.json";

	
	//@Test
	public void make() {
		SchedulerInfo sc_info = new SchedulerInfo();
		sc_info.setName("GW-Conciliation-Scheduler");
		sc_info.setType("Consiliation");
		
		JobInfo job_info = new JobInfo();
		TriggerInfo trigger_info = new TriggerInfo();
		
		job_info.setJobClass(ConcilationJob.class.getCanonicalName());
		
		trigger_info.setBase(TriggerInfo.CRON);
		trigger_info.setCronExp("0/1 * * * * ?");
		
		sc_info.setJobInfo(job_info);
		sc_info.setTriggerInfo(trigger_info);
		
		try {
			sc_info.save(this.config_file_name);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	
	
	@Test
	public void load() {
		try {
			SchedulerInfo sc_info = SchedulerInfo.load(config_file_name);
			sc_info.build();
		
			Thread.sleep(1000*60);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SchedulerException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
//	
//	private class GW_Concilation_Job extends AbstractJob{
//
//		@Override
//		public void beforeExecute(JobExecutionContext context) throws JobExecutionException {
//			System.out.println("before : --");
//		}
//
//		@Override
//		public void doExecute(JobExecutionContext context) throws JobExecutionException {
//			System.out.println("do exe : --");
//		}
//
//		@Override
//		public void afterExecute(JobExecutionContext context) throws JobExecutionException {
//			System.out.println("after : --");
//		}
//
//		@Override
//		public String getName() {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public String getType() {
//			// TODO Auto-generated method stub
//			return null;
//		}
//		
//	}
}
