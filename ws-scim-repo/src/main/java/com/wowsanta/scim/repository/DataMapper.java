package com.wowsanta.scim.repository;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class DataMapper {
	private String id;
	private String className;
	private String inMethod;
	private String outMethod;
	
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
	public String getInMethod() {
		return inMethod;
	}
	public void setInMethod(String inMethod) {
		this.inMethod = inMethod;
	}
	public String getOutMethod() {
		return outMethod;
	}
	public void setOutMethod(String outMethod) {
		this.outMethod = outMethod;
	}
}
