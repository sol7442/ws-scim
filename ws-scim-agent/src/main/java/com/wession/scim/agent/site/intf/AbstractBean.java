package com.wession.scim.agent.site.intf;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.wession.scim.agent.DataMap;
import com.wession.scim.agent.site.DemoIMSBean;

import net.minidev.json.JSONObject;

public abstract class AbstractBean {
 
	public void compare(DemoIMSBean comp) {
		
	}
	
	public JSONObject toJSONObject() {
		JSONObject json = new JSONObject();
		Field fds [] = getClass().getDeclaredFields();
		for (int i=0; i<fds.length; i++) {
			try {
				String name = fds[i].getName();
				String methodName = "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
				Method m = getClass().getDeclaredMethod(methodName);
				Object valueObject = m.invoke(this, (Object[]) null);
				
				json.put(name, valueObject);
//				json.put(fds[i].getName(), (Object) fds[i].get(fds[i].getName()));
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		
		return json;
	}
	
	public DataMap toDataMap() {
		DataMap map = new DataMap();
		Field fds [] = getClass().getDeclaredFields();
		for (int i=0; i<fds.length; i++) {
			try {
				String name = fds[i].getName();
				String methodName = "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
				Method m = getClass().getDeclaredMethod(methodName);
				Object valueObject = m.invoke(this, (Object[]) null);
				
				map.put(name, valueObject);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		
		return map;
	}

	public void setDataMap(DataMap map) {
		Field fds[] = getClass().getDeclaredFields();
		
		for (int i = 0; i < fds.length; i++) {
			try {
				String fieldName = fds[i].getName();
				Object fieldValue = map.get(fieldName);
				String methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
				Method m;
//				System.out.println(i + ")" + fieldName + " : " + fieldValue);
				if (fieldValue != null) {
					if (fieldValue instanceof Integer) {
						m = getClass().getDeclaredMethod(methodName, int.class);
					} else {
						m = getClass().getDeclaredMethod(methodName, fieldValue.getClass());
					}
					m.invoke(this, fieldValue);
				}

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
