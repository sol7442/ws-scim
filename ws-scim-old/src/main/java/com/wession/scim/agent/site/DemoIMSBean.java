package com.wession.scim.agent.site;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Set;

import com.wession.scim.agent.DataMap;
import com.wession.scim.agent.site.intf.AbstractBean;
import com.wession.scim.agent.site.intf.BeanModel;

import net.minidev.json.JSONObject;

public class DemoIMSBean extends AbstractBean implements BeanModel {
	private String id;
	private String Name;
	private String title;
	private String telephone;
	private String companyID;
	private String scimID;
	
	public DemoIMSBean() {
		
	}
	
	public DemoIMSBean(DataMap user) {
		if (user == null) 
			return; 
		else
			setDataMap(user);
	}
	
	public DemoIMSBean(String id, String name, String title, String phone, String empno, String scimID) {
		this.id = id;
		this.Name = name;
		this.title = title;
		this.telephone = phone;
		this.companyID = empno;
		this.scimID = scimID;
	}
	
	public DemoIMSBean(String id, String name, String title, String phone, String empno) {
		this.id = id;
		this.Name = name;
		this.title = title;
		this.telephone = phone;
		this.companyID = empno;
	}
	
	public DemoIMSBean(String id, String name, String empno) {
		this.id = id;
		this.Name = name;
		this.companyID = empno;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getCompanyID() {
		return companyID;
	}

	public void setCompanyID(String companyID) {
		this.companyID = companyID;
	}
	
	public String getScimID() {
		return scimID;
	}

	public void setScimID(String scimID) {
		this.scimID = scimID;
	}


}
