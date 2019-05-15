package com.wowsanta.scim.repository;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class DataMapper {
	transient Logger logger = LoggerFactory.getLogger(DataMapper.class) ;
	
	private String id;
	private String className;
	private String methodName;
	private Object defaultData;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public Object getDefault() {
		return defaultData;
	}
	public void setDefaultData(Object object) {
		this.defaultData = object;
	}
	
	public String toString() {
		return toString(false);
	}
	public String toString(boolean pretty) {
		try {
			GsonBuilder builder = new GsonBuilder().disableHtmlEscaping();
			if(pretty) {
				builder.setPrettyPrinting();
			}
			Gson gson  = builder.create();
			return gson.toJson(this);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
	public Object convert(Object data) {
		Object convert_data = null;
		try {
			Class convert_class = Class.forName(getClassName());			
			Object covert_object = convert_class.newInstance();
			
			Class[] covert_params = {Object.class};
			Method method = convert_class.getMethod(getMethodName(), covert_params);
			
			if(data == null) {
				Object[] args = new Object[] {null};
				convert_data = method.invoke(covert_object, args );
			}else {
				convert_data = method.invoke(covert_object, data);	
			}
		}catch (Exception e) {
			logger.error("{} : {}",data, e.getMessage(), e);
		}finally {
			logger.debug("{} > {}", data, convert_data);
		}
		
		return convert_data;
	}
	
}
