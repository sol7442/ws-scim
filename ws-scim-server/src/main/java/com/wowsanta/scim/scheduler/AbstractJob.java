package com.wowsanta.scim.scheduler;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public abstract class AbstractJob implements Job {

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		
	}

	public abstract void beforeExecute(JobExecutionContext context) throws JobExecutionException;
	public abstract void doExecute(JobExecutionContext context) throws JobExecutionException;
	public abstract void afterExecute(JobExecutionContext context) throws JobExecutionException;
}
