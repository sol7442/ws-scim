package com.wowsanta.scim.scheduler;

import java.util.Date;

import org.quartz.JobExecutionContext;

import com.wowsanta.scim.log.SCIMLogger;

public class SampleSubJob1 extends SCIMJob{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8372032151720364354L;

	@Override
	public void doExecute(JobExecutionContext context) {
		SCIMLogger.proc("--do SampleSubJob1 execute : {} : {}.{}.{}--", new Date(), 1,2,3);
		
	}

	@Override
	public void beforeExecute(JobExecutionContext context) {
		SCIMLogger.proc("--do SampleSubJob1 before : {} : {}.{}.{}--", new Date(), 1,2,3);
		
	}

	@Override
	public void afterExecute(JobExecutionContext context) {
		SCIMLogger.proc("--do SampleSubJob1 after : {} : {}.{}.{}--", new Date(), 1,2,3);
		
	}

}
