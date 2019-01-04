package com.wession.scim;

import java.io.Serializable; 
import java.util.Iterator;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

public class Operater implements Serializable {
	private String attrName;
	private String operater;
	private String attrValue;

	private String subTarget;

	private JSONArray Resources = new JSONArray();

	public Operater() {

	}

	public Operater(String operater) {
		this.operater = operater;
	}

	public String getAttrName() {
		return attrName;
	}

	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}

	public String getOperater() {
		return operater;
	}

	public void setOperater(String operater) {
		this.operater = operater;
	}

	public String getAttrValue() {
		return attrValue;
	}

	public void setAttrValue(String attrValue) {
		this.attrValue = attrValue;
	}

	public String getSubTarget() {
		return subTarget;
	}

	public void setSubTarget(String subTarget) {
		this.subTarget = subTarget;
	}

	public void addResource(Object json) {
		Resources.add(json);
	}

	public void addResource(String op, String v1, String v2, Object json) {
		/*
		 * eq | equal ne | not equal co | contains sw | starts with ew | ends
		 * with pr | present(has value) gt | greater than ge | greater than or
		 * equal to lt | less than le | less than or equal to
		 */

		if ("eq".equals(op)) {
			if (v1.equals(v2))
				Resources.add(json);
		} else if ("ne".equals(op)) {
			if (!v1.equals(v2))
				Resources.add(json);
		} else if ("co".equals(op)) {
			if (v1.contains(v2))
				Resources.add(json);
		} else if ("sw".equals(op)) {
			if (v1.startsWith(v2))
				Resources.add(json);
		} else if ("ew".equals(op)) {
			if (v1.endsWith(v2))
				Resources.add(json);
		}
	}

	public void addResource(String op, boolean v1, boolean v2, Object json) {
		if ("eq".equals(op)) {
			if (v1 == v2)
				Resources.add(json);
		} else if ("ne".equals(op)) {
			if (v1 != v2)
				Resources.add(json);
		}
	}

	public void addResource(String op, Integer v1, Integer v2, Object json) {
		if ("eq".equals(op)) {
			if (v1 == v2)
				Resources.add(json);
		} else if ("ne".equals(op)) {
			if (v1 != v2)
				Resources.add(json);
		} else if ("gt".equals(op)) {
			if (v1 > v2)
				Resources.add(json);
		} else if ("ge".equals(op)) {
			if (v1 >= v2)
				Resources.add(json);
		} else if ("lt".equals(op)) {
			if (v1 < v2)
				Resources.add(json);
		} else if ("le".equals(op)) {
			if (v1 <= v2)
				Resources.add(json);
		}
	}
	
	public void addResource(String op, JSONArray j_arry, String attrname, String attrvalue, JSONObject json) {
		Iterator inner_itor = j_arry.iterator();
		boolean canResult = false;
		while (inner_itor.hasNext()) {
			JSONObject parts = (JSONObject) inner_itor.next();
			if (parts.containsKey(attrname)) {
				String parts_value = parts.getAsString(attrname);
				if ("eq".equals(operater)) {
					if (parts_value.equals(attrvalue))
						canResult = true;
				} else if ("ne".equals(operater)) {
					if (!parts_value.equals(attrvalue))
						canResult = true;
				} else if ("co".equals(operater)) {
					if (parts_value.contains(attrvalue))
						canResult = true;
				}
			}
		}
		if (canResult)
			Resources.add(json);

	}

	public JSONArray getResources() {
		return Resources;
	}

	public Integer toInteger() {
		try {
			return new Integer(attrValue);
		} catch (Exception e) {
			throw e;
		}
	}

	public Long toLong() {
		try {
			return new Long(attrValue);
		} catch (Exception e) {
			throw e;
		}
	}

	public Double toDouble() {
		try {
			return new Double(attrValue);
		} catch (Exception e) {
			throw e;
		}
	}

	public Boolean toBoolean() {
		try {
			return new Boolean(attrValue);
		} catch (Exception e) {
			throw e;
		}
	}

	public void clear() {
		this.attrName = "";
		this.operater = "";
		this.subTarget = "";
		this.Resources = new JSONArray();
	}


}
