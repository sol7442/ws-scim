package com.wession.scim.agent.site;

import com.wession.scim.agent.DataMap;
import com.wession.scim.agent.site.intf.AbstractBean;

public class MyHRBean extends AbstractBean {
	
	private String hr_id;
	private String userName;
	private String deptCode;
	private String deptName;
	private String title;
	private String phone;
	private String email;
	private String ims_id;

	public MyHRBean() {
		

	}
	public MyHRBean(DataMap user) {
		if (user == null) 
			return; 
		else
			setDataMap(user);
	}
	
	
	public String getHr_id() {
		return hr_id;
	}
	public void setHr_id(String hr_id) {
		this.hr_id = hr_id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getIms_id() {
		return ims_id;
	}
	public void setIms_id(String ims_id) {
		this.ims_id = ims_id;
	}
	
	public void compare() {
		
	}
	
	

}
