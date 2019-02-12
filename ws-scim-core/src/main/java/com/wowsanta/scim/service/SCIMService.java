package com.wowsanta.scim.service;

import com.wowsanta.scim.json.SCIMJsonObject;

public interface SCIMService {

	public SCIMJsonObject execute(SCIMJsonObject request);

}
