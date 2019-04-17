package com.wowsanta.scim.object;

public class SCIM_User extends SCIM_Resource {
	private SCIM_Meta meta;

	public SCIM_Meta getMeta() {
		return meta;
	}

	public void setMeta(SCIM_Meta meta) {
		this.meta = meta;
	}
}
