package com.ehyundai.im;

import com.wowsanta.scim.obj.SCIMAdmin;

public class Admin implements SCIMAdmin{

	private String id;
	private String name;
	private String pw;
	private String type;
	
	
	public Admin(String id, String name, String pw, String type) {
		this.id = id;
		this.name = name;
		this.pw = pw;
		this.type = type;
	}
	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getType() {
		return this.type;
	}

	public String getPw() {
		return this.pw;
	}
}
