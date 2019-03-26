package com.wowsanta.scim.scheduler;

import static org.quartz.TriggerBuilder.newTrigger;

import org.quartz.CronScheduleBuilder;
import org.quartz.Trigger;

public class SCIMDayTrigger extends SCIMTrigger {
	private int minute;
	private int hour;
	
	public SCIMDayTrigger() {
		super("day");
		this.jsonClass = SCIMDayTrigger.class.getCanonicalName();
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
	@Override
	public Trigger build() {
		return newTrigger().withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(this.hour ,this.minute)).build();
	}
}
