package com.wowsanta.scim.scheduler;

import java.util.Date;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.json.AbstractJsonObject;

public class SCIMScheduler extends AbstractJsonObject {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2975295174595106979L;
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
	private String sourceSystemId;
	private String targetSystemId;
	private Date lastExecuteDate;
	
	private transient Scheduler scheduler;
	
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
			
			job_detail.getJobDataMap().put("schedulerInfo", this);
			
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
}
