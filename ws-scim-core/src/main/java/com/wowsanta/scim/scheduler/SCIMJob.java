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

import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.json.AbstractJsonObject;
import com.wowsanta.scim.log.SCIMLogger;
import com.wowsanta.scim.obj.SCIMUser;
import com.wowsanta.scim.resource.worker.Worker;

import static org.quartz.TriggerBuilder.newTrigger;

public abstract class SCIMJob extends AbstractJsonObject implements Job{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3798841074146691128L;
	
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try{
			beforeExecute(context);
	        doExecute(context);
	        afterExecute(context);
	        
		}catch (SCIMException e) {
			e.printStackTrace();
			throw new JobExecutionException(e);
		}
	}

	public void beforeExecute(JobExecutionContext context)  throws SCIMException{
		
		SCIMScheduler scheudler = (SCIMScheduler) context.getJobDetail().getJobDataMap().get("schedulerInfo");
		
//		Worker worker = (Worker) context.getJobDetail().getJobDataMap().get("workerInfo");
//		context.getJobDetail().getJobDataMap().put("workerInfo", worker);
		
		SCIMLogger.proc("SCHEDULER START   : {} - {}", scheudler.getSchedulerId(), new Date());
		
	};
	public void doExecute(JobExecutionContext context) throws SCIMException{
		SCIMScheduler scheudler = (SCIMScheduler) context.getJobDetail().getJobDataMap().get("schedulerInfo");
		Worker worker = (Worker) context.getJobDetail().getJobDataMap().get("workerInfo");
		
		Date start_time = new Date();
		boolean result = run(scheudler,worker);
		Date end_time = new Date();
			
		SCIMLogger.proc("SCHEDULER RESULT : {} - {}", result, end_time.getTime() - start_time.getTime());
		
	};
	public abstract boolean run(SCIMScheduler scheduler, Worker worker)throws SCIMException;
	public void afterExecute(JobExecutionContext context)  throws SCIMException{
		SCIMScheduler scheudler = (SCIMScheduler) context.getJobDetail().getJobDataMap().get("schedulerInfo");
		SCIMLogger.proc("SCHEDULER FINISHED : {} - {}", scheudler.getSchedulerId(), new Date());
	};

	
//	@SuppressWarnings("unchecked")
//	public void scheduleNextJob(JobExecutionContext context) {
//		SCIMLogger.proc("--do scheduleNextJob execute : {} --", new Date());
//		if(context != null) {
//			Object object = context.getJobDetail().getJobDataMap().get("JobDetailQueue");
//			Queue<JobDetail> jobDetailQueue = (Queue<JobDetail>) object;
//	        JobDetail job_detail = jobDetailQueue.poll();
//	        if(job_detail != null) {
//	        	
//	        	job_detail.getJobDataMap().put("JobDetailQueue", jobDetailQueue);
//	        	
//	        	Trigger nowTrigger = newTrigger().startNow().build();
//				try {
//					Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
//					scheduler.start();
//					scheduler.scheduleJob(job_detail, nowTrigger);
//				} catch (SchedulerException e) {
//					throw new RuntimeException(e);
//				}
//	        }else {
//	        	SCIMLogger.proc("--end scheduleNextJob : {} --", new Date());
//	        }
//		}
//       
//	}

}
