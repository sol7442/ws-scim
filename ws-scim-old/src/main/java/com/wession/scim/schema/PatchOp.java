package com.wession.scim.schema;

import com.wession.scim.Const;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

public class PatchOp extends JSONObject {
	public PatchOp() {
		JSONArray schema = new JSONArray();
		schema.add(Const.schemas_v20_patch);
		this.put("schemas", schema);
		this.put("Operations", new JSONArray());
	}
	
	public PatchOp(JSONObject json) {
		this.merge(json);
	}
	
	public JSONObject getPatchOne(int idx) {
		JSONArray arry = (JSONArray) this.get("Operations");
		return (JSONObject) arry.get(idx);
	}
	
	public int size() {
		JSONArray arry = (JSONArray) this.get("Operations");
		return arry.size();
	}
	
	class operation extends JSONObject {
		public operation() {
			this.put("op", "");
			this.put("path", "");
			this.put("value", new JSONArray());
		}
	}
}

