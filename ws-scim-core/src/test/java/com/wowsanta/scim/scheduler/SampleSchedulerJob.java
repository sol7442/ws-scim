package com.wowsanta.scim.scheduler;

import java.util.Date;

import org.quartz.JobExecutionContext;

import com.wowsanta.scim.log.SCIMLogger;

public class SampleSchedulerJob extends SCIMJob {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6159424656716460448L;

	public SampleSchedulerJob() {
		setClassName(SampleSchedulerJob.class.getCanonicalName());
	}
	
	@Override
	public void doExecute(JobExecutionContext context) {
		SCIMLogger.proc("--do execute : {} : {}.{}.{}--", new Date(), 1,2,3);
		
	}

	@Override
	public void beforeExecute(JobExecutionContext context) {
		SCIMLogger.proc("--do before : {} : {}.{}.{}--", new Date(), 1,2,3);
		
	}

	@Override
	public void afterExecute(JobExecutionContext context) {
		SCIMLogger.proc("--do after : {} : {}.{}.{}--", new Date(), 1,2,3);
		
	}

}
