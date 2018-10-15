package com.wowsanta.scim.scheduler;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class ConcilationJob extends AbstractJob {
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

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return null;
	}
}
