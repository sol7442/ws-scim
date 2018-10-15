package com.wowsanta.scim.scheduler;

import org.quartz.JobDetail;
import static org.quartz.JobBuilder.newJob;

@SuppressWarnings("rawtypes")
public class JobInfo {
	
	private String jobClass;
	
	public String getJobClass() {
		return jobClass;
	}

	public void setJobClass(String jobClass) {
		this.jobClass = jobClass;
	}
	
	@SuppressWarnings("unchecked")
	public JobDetail build() throws ClassNotFoundException {
		Class cls = Class.forName(this.jobClass);
		return newJob(cls).build();
	}

}

