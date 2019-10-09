package com.wowsanta.scim.scheduler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import com.mchange.net.SmtpException;
import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.json.AbstractJsonObject;
import com.wowsanta.scim.obj.SCIMSystem;
import com.wowsanta.scim.obj.SCIMUser;
import com.wowsanta.scim.obj.SCIMUserMeta;
import com.wowsanta.scim.resource.user.LoginUser;
import com.wowsanta.scim.resource.user.LoginUserType;
import com.wowsanta.scim.resource.worker.Worker;
import com.wowsata.util.json.WowsantaJson;

public class SCIMScheduler  {
	
	static transient Logger logger = LoggerFactory.getLogger(SCIMScheduler.class);
	
	private String schedulerId;
	private String schedulerName;
	private String schedulerType;
	private String schedulerDesc;
	private String jobClass;
	private String triggerType;
	private int dayOfMonth;
	private int dayOfWeek;
	private int hourOfDay;
	private int minuteOfHour;
	private String executeSystemId;
	private String sourceSystemId;
	private String targetSystemId;
	private Date lastExecuteDate;
	private String encode;
	
	private transient Scheduler scheduler;
	
	public static SCIMScheduler parse(LinkedTreeMap scheduler_object) {
		SCIMScheduler scheduler = new SCIMScheduler();
		scheduler.schedulerId   	= (String) scheduler_object.get("schedulerId");
		scheduler.schedulerName   	= (String) scheduler_object.get("schedulerName");
		scheduler.schedulerType   	= (String) scheduler_object.get("schedulerType");
		scheduler.schedulerDesc   	= (String) scheduler_object.get("schedulerDesc");
		scheduler.jobClass   		= (String) scheduler_object.get("jobClass");
		scheduler.triggerType   	= (String) scheduler_object.get("triggerType");
		
		if(scheduler_object.get("dayOfMonth") != null) {
			Double _dayOfMonth 		= (Double) scheduler_object.get("dayOfMonth");
			scheduler.dayOfMonth   		= _dayOfMonth.intValue();
		}
		
		if(scheduler_object.get("dayOfWeek") != null) {
			Double _dayOfWeek 		= (Double) scheduler_object.get("dayOfWeek");
			scheduler.dayOfWeek  		= _dayOfWeek.intValue();
		}
		
		if(scheduler_object.get("hourOfDay") != null) {
			Double _hourOfDay 		= (Double) scheduler_object.get("hourOfDay");
			scheduler.hourOfDay   		= _hourOfDay.intValue();
		}
		
		if(scheduler_object.get("minuteOfHour") != null) {
			Double _minuteOfHour 	= (Double) scheduler_object.get("minuteOfHour");
			scheduler.minuteOfHour   	= _minuteOfHour.intValue();
		}

		scheduler.executeSystemId   = (String) scheduler_object.get("executeSystemId");
		scheduler.sourceSystemId   	= (String) scheduler_object.get("sourceSystemId");
		scheduler.targetSystemId   	= (String) scheduler_object.get("targetSystemId");
		scheduler.encode            = (String) scheduler_object.get("encode");
		
		if(scheduler_object.get("lastExecuteDate") != null) {
			Object date_object = scheduler_object.get("lastExecuteDate");
			SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a",Locale.US);
			try {
				scheduler.lastExecuteDate = sdf.parse((String) date_object);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		
		return scheduler;
	}
	
	public String toString() {
		return toString(false);
	}
	public String toString(boolean pretty) {
		try {
			GsonBuilder builder = new GsonBuilder().disableHtmlEscaping();
			if (pretty) {
				builder.setPrettyPrinting();
			}
			Gson gson = builder.create();
			return gson.toJson(this);
		} catch (Exception e) {
			logger.error(e.getMessage() + " : ",  e);
		}
		return null;
	}
	
	public String getSchedulerId() {
		return schedulerId;
	}
	public void setSchedulerId(String schedulerId) {
		this.schedulerId = schedulerId;
	}
	public String getSchedulerName() {
		return schedulerName;
	}
	public void setSchedulerName(String schedulerName) {
		this.schedulerName = schedulerName;
	}
	public String getSchedulerType() {
		return schedulerType;
	}
	public void setSchedulerType(String schedulerType) {
		this.schedulerType = schedulerType;
	}
	public String getSchedulerDesc() {
		return schedulerDesc;
	}
	public void setSchedulerDesc(String schedulerDesc) {
		this.schedulerDesc = schedulerDesc;
	}
	public String getJobClass() {
		return jobClass;
	}
	public void setJobClass(String jobClass) {
		this.jobClass = jobClass;
	}
	public String getTriggerType() {
		return triggerType;
	}
	public void setTriggerType(String triggerType) {
		this.triggerType = triggerType;
	}
	public int getDayOfMonth() {
		return dayOfMonth;
	}
	public void setDayOfMonth(int dayOfMonth) {
		this.dayOfMonth = dayOfMonth;
	}
	public int getDayOfWeek() {
		return dayOfWeek;
	}
	public void setDayOfWeek(int dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}
	public int getHourOfDay() {
		return this.hourOfDay;
	}
	public void setHourOfDay(int hourOfDay) {
		this.hourOfDay = hourOfDay;
	}
	public int getMinuteOfHour() {
		return minuteOfHour;
	}
	public void setMinuteOfHour(int minuteOfHour) {
		this.minuteOfHour = minuteOfHour;
	}
	public String getSourceSystemId() {
		return sourceSystemId;
	}
	public void setSourceSystemId(String sourceSystemId) {
		this.sourceSystemId = sourceSystemId;
	}
	public String getTargetSystemId() {
		return targetSystemId;
	}
	public void setTargetSystemId(String targetSystemId) {
		this.targetSystemId = targetSystemId;
	}
	public Date getLastExecuteDate() {
		return lastExecuteDate;
	}
	public void setLastExecuteDate(Date lastExecuteDate) {
		this.lastExecuteDate = lastExecuteDate;
	}
	
	private Trigger buildTrigger() {
		Trigger trigger = null;
		if("DAY".equals(this.triggerType)) {
			trigger = newTrigger().withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(
					this.hourOfDay,
					this.minuteOfHour)).build();
		}else if ("WEEK".equals(this.triggerType)) {
			trigger = newTrigger().withSchedule(CronScheduleBuilder.weeklyOnDayAndHourAndMinute(
					this.dayOfWeek,
					this.hourOfDay,
					this.minuteOfHour)).build();
		}else if ("MONTH".equals(this.triggerType)) {
			trigger = newTrigger().withSchedule(CronScheduleBuilder.monthlyOnDayAndHourAndMinute(
					this.dayOfMonth,
					this.hourOfDay,
					this.minuteOfHour)).build();
		}else {
			trigger = newTrigger().withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(
					this.hourOfDay,
					this.minuteOfHour)).build();
		}
		return trigger;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Scheduler build() throws SCIMException{
		try {
			Class job_cls = Class.forName(this.jobClass);
			JobDetail job_detail = newJob(job_cls).build();
			
			Worker worker = new Worker();
			worker.setWorkerId(this.schedulerId);
			worker.setWorkerType(LoginUserType.SCHEDULER.toString());
			
			job_detail.getJobDataMap().put("schedulerInfo", this);
			job_detail.getJobDataMap().put("workerInfo", worker);
			
			this.scheduler = StdSchedulerFactory.getDefaultScheduler();
			this.scheduler.start();
			this.scheduler.scheduleJob(job_detail,buildTrigger());
			
		}catch (Exception e) {
			throw new SCIMException("Scheduler build Failed ",e);
		}
		return this.scheduler;
	}
	
	public void shutdown() throws SCIMException {
		try {
			this.scheduler.shutdown();
		} catch (SchedulerException e) {
			throw new SCIMException("Scheduler Shutdown Failed",e);
		}
	}
	public String getExecuteSystemId() {
		return executeSystemId;
	}
	public void setExecuteSystemId(String executeSystemId) {
		this.executeSystemId = executeSystemId;
	}
	public void startNow(LoginUser login_user) throws SCIMException {
		
		try {
			Class job_cls = Class.forName(this.jobClass);
			JobDetail job_detail = newJob(job_cls).build();
			
			
			
			Worker worker = new Worker();
			worker.setWorkerId(login_user.getUserId());
			worker.setWorkerType(login_user.getType().toString());
			
			job_detail.getJobDataMap().put("schedulerInfo", this);
			job_detail.getJobDataMap().put("workerInfo", worker);
			
			this.scheduler = StdSchedulerFactory.getDefaultScheduler();
			this.scheduler.start();
			
			Trigger trigger = newTrigger().startNow().build();
			this.scheduler.scheduleJob(job_detail,trigger);
			
		}catch (Exception e) {
			throw new SCIMException("Scheduler build Failed ",e);
		}
	}

	public String getEncode() {
		return this.encode;
	}

	public void setEncode(String encode) {
		this.encode = encode;
	}

}
