package com.wession.scim;

import java.util.ArrayList;


public class AccountCompare {
	private String method; 
	private ArrayList <CompareData> data = new ArrayList<CompareData>();
	
	public AccountCompare() {
		
	}
	
	public AccountCompare(String method) {
		this.setMethod(method);
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public ArrayList<CompareData> getData() {
		return data;
	}

	public void setData(ArrayList<CompareData> data) {
		this.data = data;
	}
	
	public void addData(CompareData obj) {
		data.add(obj);
	}
	
	public CompareData getData(String attrName) {
		if (attrName == null) return null;
		java.util.Iterator<CompareData> itor = data.iterator();
		while (itor.hasNext()) {
			CompareData c = itor.next();
			if (c.getAttr_name().equals(attrName)) {
				return c;
			}
		}
		return null;
	}
	
	public void removeData(String attrName) {
		if (attrName == null) return;
		java.util.Iterator<CompareData> itor = data.iterator();
		while (itor.hasNext()) {
			CompareData c = itor.next();
			if (c.getAttr_name().equals(attrName)) {
				itor.remove();
			}
		}
	}
	
	public void compare(String attr_name, String bean_data, String scim_data) {
//		System.out.println("compare : " + attr_name + ", " + bean_data + ", " + scim_data);
		
		if ((bean_data == null || "".equals(bean_data)) && (scim_data == null || "".equals(scim_data))) {
			return;
		} else if (bean_data == null || "".equals(bean_data)) {
			addData(new CompareData(attr_name, "", scim_data));
			System.out.println("compare (bean_date = null): " + attr_name + ", " + bean_data + "," + scim_data);
		} else if ((scim_data == null || "".equals(scim_data))) {
			addData(new CompareData(attr_name, bean_data, ""));
			System.out.println("compare (scim_data = null): " + attr_name + ", " + bean_data + "," + scim_data);
		} else if (!bean_data.equals(scim_data)) {
			addData(new CompareData(attr_name, bean_data, scim_data));
			System.out.println("compare (!bean_data.equals(scim_data)): " + attr_name + ", " + bean_data + "," + scim_data);
			
		}
	}

}
