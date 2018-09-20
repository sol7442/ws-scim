package com.wession.scim.agent.site.intf;

import com.wession.scim.agent.DataMap;
import net.minidev.json.JSONObject;

public interface BeanModel {
	public JSONObject toJSONObject();
	public DataMap toDataMap(); 
}
