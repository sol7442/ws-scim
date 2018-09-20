package com.wession.scim.agent.site;

import com.wession.scim.agent.DataMap;
import com.wession.scim.agent.site.intf.AbstractBean;

public class MyGWBean extends AbstractBean {
	
	private String email;
	private String Name;
	private String title;
	private String telephone;
	private String deptname;
	private String companyID;
	
	public MyGWBean() {
		
	}
	
	public MyGWBean(DataMap user) {
		if (user == null) 
			return; 
		else
			setDataMap(user);
	}
	
	public MyGWBean(String email, String name, String title, String phone, String deptname, String empno) {
		this.email = email;
		this.Name = name;
		this.title = title;
		this.telephone = phone;
		this.deptname = deptname;
		this.companyID = empno;
	}
	
	public MyGWBean(String email, String name, String empno) {
		this.email = email;
		this.Name = name;
		this.companyID = empno;
	}
	
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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
	

	public String getDeptname() {
		return deptname;
	}

	public void setDeptname(String deptname) {
		this.deptname = deptname;
	}

	public String getCompanyID() {
		return companyID;
	}

	public void setCompanyID(String companyID) {
		this.companyID = companyID;
	}

}
