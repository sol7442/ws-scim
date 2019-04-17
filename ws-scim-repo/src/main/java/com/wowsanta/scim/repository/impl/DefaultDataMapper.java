package com.wowsanta.scim.repository.impl;

import org.quartz.impl.calendar.DailyCalendar;

public class DefaultDataMapper {
	public Object booleanToYn(Object yn) {
		if (yn instanceof Boolean) {
			Boolean yn_boolean = (Boolean) yn;
			return yn_boolean ? "Y":"N";
		}else {
			return "N";
		}
	}
	
	public Object ynToBoolean(Object yn) {
		if (yn instanceof String) {
			String yn_string = (String) yn;
			return yn_string.equals("Y") ? true:false;
		}else {
			return false;
		}
	}
	
	public Object sqlDateToJava(Object date) {
		if (date instanceof java.sql.Date) {
			java.sql.Date sql_date = (java.sql.Date) date;
			return new java.util.Date(sql_date.getTime());
		}else {
			return new java.util.Date();
		}
	}
	
	public Object javaDateToSql(Object date) {
		if (date instanceof java.util.Date) {
			java.util.Date sql_date = (java.util.Date) date;
			return new java.sql.Date(sql_date.getTime());
		}else {
			return new java.sql.Date(System.currentTimeMillis());
		}
	}
}
