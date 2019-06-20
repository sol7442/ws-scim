package com.wowsanta.scim.repository.convert;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CnvtTimestamp {
	static Logger logger = LoggerFactory.getLogger(CnvtTimestamp.class);
	
	final SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
	
	
	public Object J2D(Object obj) {
		Timestamp value = null;
		try {
			if(obj != null) {
				if (obj instanceof String) {
					Date date_value = fmt.parse((String) obj);
					value = new Timestamp(date_value.getTime());
				}else {
					logger.error("instance type {} : {}",obj, obj.getClass().getName());
				}	
			}
		}catch (Exception e) {
			logger.error("{}",obj, e );
		}
		logger.debug("J2D : {} > {}",obj,value);
		return value;
	}
	public Object fromString(Object obj) {
		Timestamp value = null;
		try {
			if(obj != null) {
				if (obj instanceof String) {
					Date date_value = fmt.parse((String) obj);
					value = new Timestamp(date_value.getTime());
				}else {
					logger.error("instance type {} : {}",obj, obj.getClass().getName());
				}	
			}
		}catch (Exception e) {
			logger.error("{}",obj, e );
		}
		logger.debug("J2D : {} > {}",obj,value);
		return value;
	}
	public Object fromJavaDate(Object obj) {
		Timestamp value = null;
		try {
			if(obj != null) {
				if (obj instanceof java.util.Date) {
					Date date_value = (Date) obj;
					value = new Timestamp(date_value.getTime());
				}else {
					logger.error("instance type {} : {}",obj, obj.getClass().getName());
				}	
			}
		}catch (Exception e) {
			logger.error("{}",obj, e );
		}
		logger.debug("J2D : {} > {}",obj,value);
		return value;
	}
	
	public Object toLong (Object obj) {
		long value = 0;
		try {
			if (obj instanceof String) {
				Date date = fmt.parse((String) obj);
				value = date.getTime();
			}
		}catch (Exception e) {
			logger.error("{}",obj, e );
		}
		return value;
	}
	
	public Object toBigDecimal (Object obj) {
		BigDecimal value = null;
		try {
			if (obj instanceof String) {
				Date date = fmt.parse((String) obj);
				value = BigDecimal.valueOf(date.getTime());
			}
		}catch (Exception e) {
			logger.error("{}",obj, e );
		}
		return value;
	}
	
	public Timestamp toTimestamp(Object obj) {
		Timestamp timestamp = null;
		if (obj instanceof String) {
			timestamp = new Timestamp((long) toLong(obj));
		}else if(obj instanceof Long) {
			timestamp = new Timestamp((long) obj);
		}else if(obj instanceof BigDecimal) {
			long value1 = ((BigDecimal)obj).longValue();
			timestamp = new Timestamp(value1);
		}else if(obj instanceof Timestamp) {
			timestamp = (Timestamp) obj;
		}else {
			//
		}
		return timestamp;
	}
	public String toString(Object obj) {
		Timestamp timestamp = toTimestamp(obj);
		if(timestamp != null) {
			return timestamp.toString();
		}else {
			return null;
		}
	}
	
	public String toJson(Object obj) {
		Timestamp timestamp = toTimestamp(obj);
		if(timestamp != null) {
			return fmt.format(timestamp);
		}else {
			return null;
		}
	}
}
