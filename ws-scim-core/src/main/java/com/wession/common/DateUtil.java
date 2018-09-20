package com.wession.common;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class DateUtil {
	public static String getDateTimeUTC() {
		return ZonedDateTime.now( ZoneOffset.UTC ).withNano( 0 ).toString();
	}
}
