package com.wowsanta.scim.resource;

import java.io.Serializable;

public interface SCIMResult extends Serializable {
	public String getCode();
	public String getMessage();
	public Object getData();
}
