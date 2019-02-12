package com.wowsanta.scim.obj;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.JsonElement;

public class JsonUtil {

	final static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
	
	public static String toString(JsonElement element){
		if(element == null) return null;
		if(element.isJsonNull()) return null;
		
		return element.getAsString();
	}
	
	public static String toString(Date date){
		if(date == null){
			return null;
		}else{
			return df.format(date);
		}
	}
	public static Date toDate(JsonElement element){
		if(element == null) return null;
		if(element.isJsonNull()) return null;
		
		Date date = null;
		try {
			date =  df.parse(element.getAsString());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static boolean toBoolean(JsonElement element) {
		if(element == null) return false;
		if(element.isJsonNull()) return false;
		
		return element.getAsBoolean();
	}
	

}
