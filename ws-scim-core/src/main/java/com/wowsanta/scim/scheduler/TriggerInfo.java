package com.wowsanta.scim.scheduler;

import org.quartz.Trigger;
import static org.quartz.TriggerBuilder.newTrigger;

import org.quartz.CronScheduleBuilder;
public class TriggerInfo {

	public static final String CRON 	= "cron";
	public static final String DAY 		= "day";
	public static final String WEEK 	= "week";
	public static final String MONTH	= "month";
	
	
	private String base;
	private int minute;
	private int hour;
	private int dayOfWeek;
	private int dayOfMonth;
	private String cronExp;
	
	public String getBase() {
		return base;
	}

	public void setBase(String base) {
		this.base = base;
	}

	public int getMinute() {
		return minute;
	}

	public void setMinute(int minute) {
		this.minute = minute;
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public int getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(int dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	public int getDayOfMonth() {
		return dayOfMonth;
	}

	public void setDayOfMonth(int dayOfMonth) {
		this.dayOfMonth = dayOfMonth;
	}

	public String getCronExp() {
		return cronExp;
	}

	public void setCronExp(String cronExp) {
		this.cronExp = cronExp;
	}
	public Trigger build() {
		Trigger trigger = null;
		if(CRON.equals(this.base)) {
			trigger = newTrigger().withSchedule(CronScheduleBuilder.cronSchedule(cronExp)).build();
		}else if(DAY.equals(this.base)) {
			trigger = newTrigger().withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(1 , 1)).build();
		}else if(WEEK.equals(this.base)) {
			trigger = newTrigger().withSchedule(CronScheduleBuilder.weeklyOnDayAndHourAndMinute(dayOfWeek, hour, minute)).build();
		}else if(MONTH.equals(this.base)) {
			trigger = newTrigger().withSchedule(CronScheduleBuilder.monthlyOnDayAndHourAndMinute(dayOfMonth, hour, minute)).build();
		}
		
		return trigger;
	}

}
