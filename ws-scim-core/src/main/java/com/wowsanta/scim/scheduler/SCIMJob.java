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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.json.AbstractJsonObject;
import com.wowsanta.scim.log.SCIMLogger;
import com.wowsanta.scim.obj.SCIMAudit;
import com.wowsanta.scim.obj.SCIMSchedulerHistory;
import com.wowsanta.scim.obj.SCIMUser;
import com.wowsanta.scim.resource.worker.Worker;

import static org.quartz.TriggerBuilder.newTrigger;

public abstract class SCIMJob extends AbstractJsonObject implements Job{

	Logger logger = LoggerFactory.getLogger(SCIMJob.class);
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
		logger.info("SCHEDULER START   : {} - {}", scheudler.getSchedulerId(), new Date());
		
	};
	public void doExecute(JobExecutionContext context) throws SCIMException{
		SCIMScheduler scheudler = (SCIMScheduler) context.getJobDetail().getJobDataMap().get("schedulerInfo");
		Worker worker = (Worker) context.getJobDetail().getJobDataMap().get("workerInfo");
		
		Date start_time = new Date();
		Object result = run(scheudler,worker);
		Date end_time = new Date();
			
		logger.info("SCHEDULER RESULT : {} - {}", result, end_time.getTime() - start_time.getTime());
		
	};
	public abstract Object run(SCIMScheduler scheduler, Worker worker)throws SCIMException;
	public void afterExecute(JobExecutionContext context)  throws SCIMException{
		SCIMScheduler scheudler = (SCIMScheduler) context.getJobDetail().getJobDataMap().get("schedulerInfo");
		logger.info("SCHEDULER FINISHED : {} - {}", scheudler.getSchedulerId(), new Date());
	};

	
	public SCIMSchedulerHistory makeHistoryObject(SCIMScheduler scheduler, SCIMAudit audit) {
		SCIMSchedulerHistory history = new SCIMSchedulerHistory(scheduler.getSchedulerId());
		history.setWorkId(audit.getWorkId());
		history.setWorkerId(audit.getWorkerId());
		history.setWorkDate(audit.getWorkDate());
		return history;
	}

	public SCIMAudit makeAuditObject(SCIMScheduler scheduler, Worker worker) {
		SCIMAudit audit = new SCIMAudit(new Date());
		audit.setWorkerId(worker.getWorkerId());
		audit.setWorkerType(worker.getWorkerType());
		audit.setSourceSystemId(scheduler.getSourceSystemId());
		audit.setTargetSystemId(scheduler.getTargetSystemId());
		audit.setAction("SCHEDULER");
		return audit;
	}

}
