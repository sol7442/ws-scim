package com.wowsanta.scim.repository.impl;

import java.text.SimpleDateFormat;

import ch.qos.logback.classic.Logger;

public class DefaultDataMapper {
	public Object booleanToYn(Object yn) {
		if (yn instanceof Boolean) {
			Boolean yn_boolean = (Boolean) yn;
			return yn_boolean ? "Y":"N";
		}else {
			return "N";
		}
	}
	
	public Object ynToInteger(Object yn) {
		if (yn instanceof String) {
			String yn_string = (String) yn;
			return yn_string.toUpperCase().equals("Y") ? 1:0;
		}else {
			return 0;
		}
	}
	public Object ynToBoolean(Object yn) {
		if (yn instanceof String) {
			String yn_string = (String) yn;
			return yn_string.toUpperCase().equals("Y") ? true:false;
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
	
	public Object javaDateToSql(Object data) {
		java.util.Date java_date = (java.util.Date) data;
		java.sql.Date sql_date = (java.sql.Date) java_date;
		return sql_date;
	}
	public Object javaDateToLong(Object data) {
		if (data instanceof java.util.Date) {
			java.util.Date java_date = (java.util.Date) data;
			return java_date.getTime();////new java.sql.Date(sql_date.getTime());
		}else {
			return System.currentTimeMillis();//new java.sql.Date(System.currentTimeMillis());
		}
	}
	
	public Object javaDateToSqlTimestamp(Object data) {
		if (data instanceof java.util.Date) {
			java.util.Date date = (java.util.Date)data;
			return new java.sql.Timestamp(date.getTime());//.getTime());  
		}else {
			return null;
		}
	}
	public Object defaultTrue(Object data) {
		return true;
	}
	public Object defaultFalse(Object data) {
		return false;
	}
	public Object defaultSystemCurrentTime(Object data) {
		return System.currentTimeMillis();
	}
	public Object defaultMaxLog(Object data) {
		return Long.MAX_VALUE;//System.currentTimeMillis();
	}
	
}
