package com.wowsanta.scim.resource;

public class ServiceAdmin implements SCIMAdmin{

	private String id;
	private String name;
	private String type;
	private String pw;
	
	public ServiceAdmin() {
		
	}
	public ServiceAdmin (String id, String name) {
		this.id = id;
		this.name = name;
	}
	
	@Override
	public String getId() {
		return this.id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Override
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getType() {
		return this.type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getPw() {
		return this.pw;
	}
	public void setPw(String pw) {
		this.pw = pw;
	}

}
