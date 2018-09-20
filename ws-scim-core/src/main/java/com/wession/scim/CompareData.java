package com.wession.scim;

public class CompareData {
	private String attr_name; 
	private String new_value_to_bean;
	private String old_value_to_scim;
	
	public CompareData() {
		
	}
	
	public CompareData(String attr_name, String new_value_to_bean, String old_value_to_scim) {
		this.setAttr_name(attr_name);
		this.setNew_value_to_bean(new_value_to_bean);
		this.setOld_value_to_scim(old_value_to_scim);
	}

	public String getAttr_name() {
		return attr_name;
	}

	public void setAttr_name(String attr_name) {
		this.attr_name = attr_name;
	}

	public String getNew_value_to_bean() {
		return new_value_to_bean;
	}

	public void setNew_value_to_bean(String new_value_to_bean) {
		this.new_value_to_bean = new_value_to_bean;
	}

	public String getOld_value_to_scim() {
		return old_value_to_scim;
	}

	public void setOld_value_to_scim(String old_value_to_scim) {
		this.old_value_to_scim = old_value_to_scim;
	}
	
	public String getAttrName() {
		return attr_name;
	}

	public void setAttrNme(String attr_name) {
		this.attr_name = attr_name;
	}

	public String getNewValue() {
		return new_value_to_bean;
	}

	public void setNewValue(String new_value_to_bean) {
		this.new_value_to_bean = new_value_to_bean;
	}

	public String getOldValue() {
		return old_value_to_scim;
	}

	public void setOldValue(String old_value_to_scim) {
		this.old_value_to_scim = old_value_to_scim;
	}
	
}
