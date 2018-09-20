package com.wession.scim.test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

public class AutoSetTest {
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	private String name;
	String day;
	int count;
	
	public static void main(String [] args) {
	
		AutoSetTest at = new AutoSetTest();
		at.set();
		
		HashMap <String, Object> hm = new HashMap <String, Object>();
		hm.put("name", "이은준");
		hm.put("day", "Today");
		hm.put("count", 0);
		
		at.set(hm);
		System.out.println(at.name);
	}
	
	private void set() {
		Field [] fd = getClass().getDeclaredFields();
		System.out.println(fd.length);
		for (int i = 0; i<fd.length; i++) {
			System.out.println(fd[i].getName());
		}
	}
	
	public void set(HashMap map) {
		Field fds [] = getClass().getDeclaredFields();
		for (int i=0; i<fds.length; i++) {
			try {
				String fieldName = fds[i].getName();
				Object fieldValue = map.get(fieldName);
				String methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
				Method m;
				if (fieldValue instanceof Integer) {
					m = getClass().getDeclaredMethod(methodName, int.class);
				} else {
					m = getClass().getDeclaredMethod(methodName, fieldValue.getClass());
				}
				m.invoke(this, fieldValue);
				
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}
}
