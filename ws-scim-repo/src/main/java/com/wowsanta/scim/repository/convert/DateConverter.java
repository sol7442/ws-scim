package com.wowsanta.scim.repository.convert;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import oracle.net.aso.d;

public class DateConverter {
	Logger logger = LoggerFactory.getLogger(DateConverter.class);
	
	private final SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
	
	public Object sqlTimestampToString(Object object) {
		String date_string = null;
		if(object != null) {
			if (object instanceof java.sql.Timestamp){
				java.sql.Timestamp time_stamp = (java.sql.Timestamp) object;
				date_string = fmt.format(time_stamp);
			}	
		}
		logger.debug("date convert {} > {}",object, date_string);
		return date_string;
	}
	public Object sqlTimestampToStringDefaultNull(Object object) {
		String date_string = null;
		if(object != null) {
			if (object instanceof java.sql.Timestamp){
				java.sql.Timestamp time_stamp = (java.sql.Timestamp) object;
				date_string = fmt.format(time_stamp);
			}	
		}
		logger.debug("date convert {} > {}",object, date_string);
		return date_string;
	}
	
	public Object sqlTimestampToStringDefaultCurrenttime(Object object) {
		String date_string = null;
		if(object != null) {
			if (object instanceof java.sql.Timestamp){
				java.sql.Timestamp time_stamp = (java.sql.Timestamp) object;
				date_string = fmt.format(time_stamp);
			}	
		}else {
			java.sql.Timestamp time_stamp = new java.sql.Timestamp(new Date().getTime());
			date_string = fmt.format(time_stamp);
		}
		logger.debug("date convert {} > {}",object, date_string);
		return date_string;
	}
	
	public Object BigDecimalToDateString(Object object) {
		if(object == null) {
			return null;
		}
		
		String date_string = null;
		if (object instanceof BigDecimal) {
			BigDecimal value = (BigDecimal) object;
			Date date = new Date(value.longValue());
			date_string = fmt.format(date);
		}
		
		return date_string;
	}
	
	public Object longToTimeString(Object object) {
		String date_string = null;
		if(object != null) {
			Date date = new Date((long) object);
			date_string = fmt.format(date);
		}
		
		return date_string;
	}
	
	public Object stringToLongDefaultCurrenttime(Object object) {
		long date_value = System.currentTimeMillis();
		try {
			if(object != null) {
				Date date = fmt.parse((String) object);
				date_value = date.getTime();
			}
		} catch (ParseException e) {
			logger.error("convert fail {}", e.getMessage(), e);
		}
		logger.debug("date convert {} > {}",object, date_value);
		return date_value;
	}
	
	public Object stringToSqlDateDefaultNull(Object object) {
		if(object == null) {
			return null;
		}

		java.sql.Date sql_date = null;
		try {
			Date java_date = fmt.parse((String) object);
			sql_date = new java.sql.Date(java_date.getTime());
			
		} catch (ParseException e) {
			logger.error("convert fail {}", e.getMessage(), e);
		}
		logger.debug("date convert {} > {}",object, sql_date);
		return sql_date;
	}
	
	public Object sqlDateToStringDefaultNull(Object object) {
		String date_string = null;
		if(object != null) {
			if (object instanceof java.sql.Date){
				java.sql.Date sql_date = (java.sql.Date) object;
				date_string = fmt.format(sql_date);
			}	
		}
		logger.debug("date convert {} > {}",object, date_string);
		return date_string;
	}
	public Object sqlDateToString(Object object) {
		String date_string = null;
		if(object != null) {
			if (object instanceof java.sql.Date){
				java.sql.Date sql_date = (java.sql.Date) object;
				date_string = fmt.format(sql_date);
			}	
		}
		logger.debug("date convert {} > {}",object, date_string);
		return date_string;
	}
	public Object currentTimeToLong(Object object) {
		return System.currentTimeMillis();
	}
	public Object stringToSqlDate(Object object) {
		if(object == null) {
			return null;
		}

		java.sql.Date sql_date = null;
		try {
			Date java_date = fmt.parse((String) object);
			sql_date = new java.sql.Date(java_date.getTime());
			
		} catch (ParseException e) {
			logger.error("convert fail {}", e.getMessage(), e);
		}
		logger.debug("date convert {} > {}",object, sql_date);
		return sql_date;
	}
	
	public Object stringToSqlTimestamp(Object object) {
		java.sql.Timestamp time_stamp = null;
		try {
			Date java_date = fmt.parse((String) object);
			time_stamp = new java.sql.Timestamp(java_date.getTime());
			
		} catch (ParseException e) {
			logger.error("convert fail {}", e.getMessage(), e);
		}
		logger.debug("date convert {} > {}",object, time_stamp);
		return time_stamp;
	}
	
	public Object stringToSqlTimestampDefaultNull(Object object) {
		java.sql.Timestamp time_stamp = null;
		try {
			if(object != null) {
				Date java_date = fmt.parse((String) object);
				time_stamp = new java.sql.Timestamp(java_date.getTime());	
			}
		} catch (ParseException e) {
			logger.error("convert fail {}", e.getMessage(), e);
		}
		logger.debug("date convert {} > {}",object, time_stamp);
		return time_stamp;
	}
	
	public Object stringToSqlTimestampDefaultCurrentTime(Object object) {
		java.sql.Timestamp time_stamp = null;
		try {
			if(object != null) {
				Date java_date = fmt.parse((String) object);
				time_stamp = new java.sql.Timestamp(java_date.getTime());	
			}else {
				time_stamp = new java.sql.Timestamp(new Date().getTime());
			}
		} catch (ParseException e) {
			logger.error("convert fail {}", e.getMessage(), e);
		}
		logger.debug("date convert {} > {}",object, time_stamp);
		return time_stamp;
	}
	
	
	public Object javaDateToSqlDate(Object object) {
		java.sql.Date sql_date = null;
		if(object != null) {
			System.out.println(object.getClass().getCanonicalName());
			if (object instanceof java.util.Date){
				java.util.Date date = (java.util.Date) object;
				sql_date = new java.sql.Date(date.getTime());
			}	
		}
		logger.debug("date convert {} > {}",object, sql_date);
		return sql_date;
	}
}
