package com.wowsanta.scim.scheduler;

import java.util.Date;
import java.util.Queue;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import com.wowsanta.scim.json.AbstractJsonObject;
import com.wowsanta.scim.log.SCIMLogger;
import com.wowsanta.scim.obj.SCIMUser;

import static org.quartz.TriggerBuilder.newTrigger;

public abstract class SCIMJob extends AbstractJsonObject implements Job{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3798841074146691128L;
	
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		SCIMScheduler scheudler = (SCIMScheduler) context.getJobDetail().getJobDataMap().get("scheduler");
		SCIMUser worker = (SCIMUser) context.getJobDetail().getJobDataMap().get("worker");
		
        beforeExecute(scheudler,worker );
        doExecute(scheudler ,worker);
        afterExecute(scheudler ,worker);
        
        //scheduleNextJob(context);
	}

	public abstract void doExecute(SCIMScheduler scheudler , SCIMUser worker) ;
	public abstract void beforeExecute(SCIMScheduler context , SCIMUser worker) ;
	public abstract void afterExecute(SCIMScheduler context, SCIMUser worker) ;
	
	@SuppressWarnings("unchecked")
	public void scheduleNextJob(JobExecutionContext context) {
		SCIMLogger.proc("--do scheduleNextJob execute : {} --", new Date());
		if(context != null) {
			Object object = context.getJobDetail().getJobDataMap().get("JobDetailQueue");
			Queue<JobDetail> jobDetailQueue = (Queue<JobDetail>) object;
	        JobDetail job_detail = jobDetailQueue.poll();
	        if(job_detail != null) {
	        	
	        	job_detail.getJobDataMap().put("JobDetailQueue", jobDetailQueue);
	        	
	        	Trigger nowTrigger = newTrigger().startNow().build();
				try {
					Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
					scheduler.start();
					scheduler.scheduleJob(job_detail, nowTrigger);
				} catch (SchedulerException e) {
					throw new RuntimeException(e);
				}
	        }else {
	        	SCIMLogger.proc("--end scheduleNextJob : {} --", new Date());
	        }
		}
       
	}

}
