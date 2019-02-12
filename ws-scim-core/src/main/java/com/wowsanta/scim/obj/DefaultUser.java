package com.wowsanta.scim.obj;

import java.util.ArrayList;
import java.util.List;


public class DefaultUser extends SCIMUser {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3021137249579613705L;

	private List<String> schemas = new ArrayList<String>();
	
	private String id;
	private String userName;
	private SCIMMeta meta = new DefaultUserMeta();
	
	public DefaultUser() {
		addSchema(SCIMConstants.USER_CORE_SCHEMA_URI);
	}
	
	@Override
	public void addSchema(String url) {
		this.schemas.add(url);
	}
	
	@Override
	public void setSchemas(List<String> schemas) {
		this.schemas = schemas;
	}

	@Override
	public List<String> getSchemas() {
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
		this.meta = meta;
	}

	@Override
	public SCIMMeta getMeta() {
		return this.meta;
	}

	@Override
	public String getUserName() {
		return userName;
	}

	@Override
	public void setUserName(String userName) {
		this.userName = userName;
	}


}
