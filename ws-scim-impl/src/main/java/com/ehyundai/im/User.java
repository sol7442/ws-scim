package com.ehyundai.im;

import java.util.ArrayList;
import java.util.List;

import com.wowsanta.scim.resource.SCIMGroup;
import com.wowsanta.scim.resource.SCIMMeta;
import com.wowsanta.scim.resource.SCIMUser;

public class User implements SCIMUser {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6709617785615904142L;
	
	private List<String> schemas = new ArrayList<String>();
	private String id;
	private String userName;
	private Meta meta;
	
	@Override
	public void setSchemas(List<String> urls) {
		this.schemas = urls;
	}
	
	@Override
	public List<String> getSchemas(){
		return this.schemas;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public void setMeta(SCIMMeta meta) {
		this.meta = (Meta)meta;
	}

	@Override
	public SCIMMeta getMeta() {
		return this.meta;
	}

	@Override
	public void setGroup(List<SCIMGroup> groups) {
	}

	@Override
	public List<SCIMGroup> getGroups() {
		return null;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
