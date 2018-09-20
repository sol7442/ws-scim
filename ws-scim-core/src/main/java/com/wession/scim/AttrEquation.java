package com.wession.scim;

import java.util.Iterator;
import java.util.Vector;  

import com.wession.scim.exception.ScimFilterException;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

public class AttrEquation {
	private String attrName;
	private String operator;
	private String compareValue;
	private JSONArray Resources;
	
	private int startPoint=0;
	private String parentAttrName;
	
	private String logical_and_or;
	private String logical_not;

	public AttrEquation() {
		this.Resources = new JSONArray();
	}

	public AttrEquation(String attrName, String operator, String compareValue) throws ScimFilterException {
		if (!allowOperator(attrName))
			throw new ScimFilterException("Not Allowed operator");

		this.attrName = attrName;
		this.operator = operator;
		this.compareValue = compareValue;
		this.Resources = new JSONArray();
	}

	private boolean allowOperator(String operator) {
		return Const.OPERATOR.contains(operator);
	}

	public String getAttrName() {
		return attrName;
	}

	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) throws ScimFilterException {
		if (!allowOperator(operator))
			throw new ScimFilterException("Not Allowed operator");
		this.operator = operator;
	}

	public String getCompareValue() {
		return compareValue;
	}

	public void setCompareValue(String compareValue) {
		this.compareValue = compareValue;
	}

	public JSONArray getResult() {
		return Resources;
	}

	public void setResult(JSONArray result) {
		this.Resources = result;
	}

	public int getStartPoint() {
		return startPoint;
	}

	public void setStartPoint(int startPoint) {
		this.startPoint = startPoint;
	}

	public String getParentAttrName() {
		return parentAttrName;
	}

	public void setParentAttrName(String parentAttrName) {
		this.parentAttrName = parentAttrName;
	}

	public String getLogical_and_or() {
		return logical_and_or;
	}

	public void setLogical_and_or(String logical_and_or) {
		this.logical_and_or = logical_and_or;
	}

	public String getLogical_not() {
		return logical_not;
	}

	public void setLogical_not(String logical_not) {
		this.logical_not = logical_not;
	}

	public void clear() {
		this.attrName = null;
		this.operator = null;
		this.compareValue = null;
		this.Resources = null;
		this.Resources = new JSONArray();
	}

	public String toString() {
		return attrName + " " + operator + " " + compareValue;
	}

	public boolean isCorrect() {
		if (attrName != null && operator != null && compareValue != null)
			return true;
		else
			return false;
	}

	public void addResource(JSONObject json_target) {
		Resources.add(json_target);
	}

	public void addResource(String op, String v1, String v2, Object json) {

		if ("eq".equals(op)) {
			if (v1.equals(v2))
				Resources.add(json);
		} else if ("ne".equals(op)) {
			if (!v1.equals(v2))
				Resources.add(json);
		} else if ("co".equals(op)) {
			if (v1.contains(v2))
				Resources.add(json);
		} else if ("nc".equals(op)) { // not contains
			if (!v1.contains(v2))
				Resources.add(json);
		} else if ("sw".equals(op)) {
			if (v1.startsWith(v2))
				Resources.add(json);
		} else if ("ew".equals(op)) {
			if (v1.endsWith(v2))
				Resources.add(json);
		} else if ("ns".equals(op)) {
			if (!v1.startsWith(v2)) // not startWith
				Resources.add(json);
		} else if ("nw".equals(op)) {
			if (!v1.endsWith(v2))	// not endWith
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
				if ("eq".equals(operator)) {
					if (parts_value.equals(attrvalue))
						canResult = true;
				} else if ("ne".equals(operator)) {
					if (!parts_value.equals(attrvalue))
						canResult = true;
				} else if ("co".equals(operator)) {
					if (parts_value.contains(attrvalue))
						canResult = true;
				} else if ("sw".equals(operator)) {
					if (parts_value.startsWith(attrvalue))
						canResult = true;
				} else if ("ew".equals(operator)) {
					if (parts_value.endsWith(attrvalue))
						canResult = true;
				} else if ("nc".equals(operator)) { // not contains
					if (!parts_value.contains(attrvalue))
						canResult = true;
				} else if ("ns".equals(operator)) { // not startsWith
					if (!parts_value.startsWith(attrvalue))
						canResult = true;
				} else if ("nw".equals(operator)) { // not endsWith
					if (!parts_value.endsWith(attrvalue))
						canResult = true;
				}
			}
		}
		if (canResult)
			Resources.add(json);

	}

}
