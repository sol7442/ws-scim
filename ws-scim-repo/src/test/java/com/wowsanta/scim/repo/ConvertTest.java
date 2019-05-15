package com.wowsanta.scim.repo;

import java.math.BigDecimal;
import java.sql.Timestamp;

import org.junit.Test;

import com.wowsanta.scim.repository.convert.CnvtTimestamp;

public class ConvertTest {


	@Test
	public void convert_test() {
		CnvtTimestamp cnvt = new CnvtTimestamp();
		String json_value = "2019-05-03T22:25:54+0900";		
		long   long_value = 1556889954000L;
		
		System.out.println(json_value);
		System.out.println("===========================");
		System.out.println(cnvt.toTimestamp(json_value));
		System.out.println(cnvt.toLong(json_value));
		System.out.println(cnvt.toBigDecimal(json_value));
		
		System.out.println("===========================");
		
		Timestamp timestamp = cnvt.toTimestamp(long_value);
		System.out.println(cnvt.toJson(timestamp));
		System.out.println(cnvt.toJson(long_value));
		
//		System.out.println(cnvt.toString(json_value));
//		System.out.println(cnvt.toString(long_value));
//		System.out.println(cnvt.toJson(long_value));
		
		
		
	}
	
	//@Test
	public void integer_convert_test() {
		System.out.println("value : " + toInteger("1").toString());
		System.out.println("value string : " + toTimestamp("2019-05-03 22:25:54").toString());
		System.out.println("value long   : " + toTimestampToLong("2019-05-03 22:25:54").toString());
		System.out.println("value Decimal: " + toTimestampToLonToDecimal("2019-05-03 22:25:54").toString());
		
		System.out.println("value : " + ynToBoolean("Y").toString());
		System.out.println("value : " + ynToBooleanToIntiger("N").toString());
		
		
		System.out.println("value : " + DecimaltoLongTimestampTo(new BigDecimal(Long.valueOf("1556894762476"))));
		System.out.println("value : " + LongTimestampTo(System.nanoTime()));
		System.out.println("value : " + System.currentTimeMillis());
		System.out.println("value : " + System.nanoTime());
		
//		
//		System.out.println("value : " + LongTimestampTo(new Long(1556889954)));
//		int value = 1556778194922;
//		long value2 = 1556778194922;
		
	}
	
	Object ynToBoolean(Object obj) {
		boolean value = false;
		if (obj instanceof String) {
			 value = obj.equals("Y") ? true : false;
		}
		return value;
	}
	Object ynToBooleanToIntiger (Object obj) {
		int value = 0;
		if (obj instanceof String) {
			 boolean value1 = obj.equals("Y") ? true : false;
			 value = value1?1:0;
		}
		return value;
	}
	
	Object toInteger (Object obj) {
		int value = 0;
		if (obj instanceof String) {
			value = Integer.parseInt((String) obj);
		}
		
		return value;
	}
	Object toTimestamp (Object obj) {
		java.sql.Timestamp value = null;
		if (obj instanceof String) {
			value = java.sql.Timestamp.valueOf((String) obj); 
		}
		return value;
	}
	
	Object toTimestampToLong (Object obj) {
		long value = 0;
		if (obj instanceof String) {
			java.sql.Timestamp value1 = java.sql.Timestamp.valueOf((String) obj);
			value = value1.getTime();
		}
		return value;
	}
	
	Object toTimestampToLonToDecimal (Object obj) {
		BigDecimal value = null;
		if (obj instanceof String) {
			java.sql.Timestamp value1 = java.sql.Timestamp.valueOf((String) obj);
			long value2 = value1.getTime();
			
			value = BigDecimal.valueOf(value2);
		}
		return value;
	}
	
	String LongTimestampTo(Object obj) {
		String value = "??";
		if (obj instanceof Long) {
			java.sql.Timestamp value1 = new java.sql.Timestamp((long) obj);
			value = value1.toString();
		}
		return value;
	}
	String DecimaltoLongTimestampTo(Object obj) {
		String value = "";
		if (obj instanceof BigDecimal) {
			long value1 = ((BigDecimal)obj).longValue();
			java.sql.Timestamp value2 = new java.sql.Timestamp(value1);
			value = value2.toString();
		}
		return value;
	}

}
