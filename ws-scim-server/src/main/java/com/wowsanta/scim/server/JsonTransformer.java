package com.wowsanta.scim.server;

import spark.ResponseTransformer;

public class JsonTransformer implements ResponseTransformer {

	@Override
	public String render(Object object) throws Exception {
		return JsonUtil.toJson(object);
	}

}
