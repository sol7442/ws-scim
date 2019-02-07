package com.wowsanta.scim.scheduler;

import static org.quartz.TriggerBuilder.newTrigger;

import org.quartz.CronScheduleBuilder;
import org.quartz.Trigger;

public class SCIMMonthTrigger extends SCIMTrigger {

	private int minute;
	private int hour;
	private int dayOfMonth;
	
	public SCIMMonthTrigger() {
		super("month");
		setClassName(SCIMMonthTrigger.class.getCanonicalName());
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
	public void setDayOfMonth(int dayOfMonth) {
		this.dayOfMonth = dayOfMonth;
	}
	public int getDayOfMonth() {
		return this.dayOfMonth;
	}
	
	@Override
	public Trigger build() {
		return newTrigger().withSchedule(CronScheduleBuilder.monthlyOnDayAndHourAndMinute(dayOfMonth, hour, minute)).build();
	}

}
