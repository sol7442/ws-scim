package com.wession.scim.schema;

import com.wession.scim.Const;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

public class ListResponse extends JSONObject {
	public ListResponse() {
		JSONArray schema = new JSONArray();
		schema.add(Const.schemas_v20_list_response);
		this.put("schemas", schema);
		this.put("totalResults", 0);
		this.put("startIndex", 1);
		this.put("itemsPerPage", 200);
		this.put("Resources", new JSONArray());
	}

	public String toJSONString(int start) {
		return this.toJSONString(start, (int) this.getAsNumber("itemsPerPage"));
	}
	
	public String toJSONString(int start, int perpage) {
		//TODO 페이징 및 카운팅은 넘어오기전에 처리할 수 있도록  
		
		JSONArray resources = (JSONArray) this.get("Resources");
		JSONArray ranged = new JSONArray();
		if (start > resources.size()) start=resources.size()-perpage;
		if (start < 1) start = 1;
		int end = perpage+start-1;
		if (end > resources.size()) end = resources.size();
		for (int i=start; i<=end; i++) {
			ranged.add(resources.get(i-1));
		}
		this.put("startIndex", start);
		this.put("Resources", ranged);
		this.put("itemsPerPage", perpage);
		return this.toJSONString();
	}
	
	public void addResource(JSONObject result) {
		JSONArray resources = (JSONArray) this.get("Resources");
		resources.add(result);
		this.put("totalResults", resources.size());
	}
	
	public void addResource(String result) {
		JSONObject ro = (JSONObject) JSONValue.parse(result);
		addResource(ro);
	}
	
	public void setResources(JSONArray resources) {
		if (resources != null) {
			this.put("Resources", resources);
			this.put("totalResults", resources.size());
		}
	}

}
