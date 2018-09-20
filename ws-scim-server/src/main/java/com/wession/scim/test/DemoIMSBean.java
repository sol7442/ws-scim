package com.wession.scim.test;

public class DemoIMSBean {
	private String id;
	private String Name;
	private String title;
	private String telephone;
	private String companyID;
	
	public DemoIMSBean() {
		
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

	public void compare(DemoIMSBean comp) {
		
	}

}
