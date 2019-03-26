package com.wowsanta.scim.scheduler;

import static org.quartz.TriggerBuilder.newTrigger;

import org.quartz.CronScheduleBuilder;
import org.quartz.Trigger;

public class SCIMWeekTrigger extends SCIMTrigger{

	private int dayOfWeek;
	private int minute;
	private int hour;
	
	public SCIMWeekTrigger() {
		super("week");
		this.jsonClass = SCIMWeekTrigger.class.getCanonicalName();
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
	public void setDayOfWeek(int dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}
	public int getDayOfWeek() {
		return this.dayOfWeek;
	}
	@Override
	public Trigger build() {
		return newTrigger().withSchedule(CronScheduleBuilder.weeklyOnDayAndHourAndMinute(dayOfWeek, hour, minute)).build();
	}

}
