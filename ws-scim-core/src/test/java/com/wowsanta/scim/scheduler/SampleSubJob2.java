package com.wowsanta.scim.scheduler;

import java.util.Date;

import org.quartz.JobExecutionContext;

import com.wowsanta.scim.log.SCIMLogger;

public class SampleSubJob2 extends SCIMJob {

	/**
	 * 
	 */
	private static final long serialVersionUID = 220822385078720533L;

	@Override
	public void doExecute(JobExecutionContext context) {
		SCIMLogger.proc("--do SampleSubJob2 execute : {} : {}.{}.{}--", new Date(), 1,2,3);
		
	}

	@Override
	public void beforeExecute(JobExecutionContext context) {
		SCIMLogger.proc("--do SampleSubJob2 before : {} : {}.{}.{}--", new Date(), 1,2,3);
		
	}

	@Override
	public void afterExecute(JobExecutionContext context) {
		SCIMLogger.proc("--do SampleSubJob2 after : {} : {}.{}.{}--", new Date(), 1,2,3);
		
	}

}
