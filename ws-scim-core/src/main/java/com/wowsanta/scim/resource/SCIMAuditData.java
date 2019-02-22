package com.wowsanta.scim.resource;

import java.util.Date;

public class SCIMAuditData {
	private String adminId;
	private String sourceSystemId;
	private String directSystemId;
	private String method;
	private String result;
	private String detail;
	private Date   workDate;
	
	
	public String getAdminId() {
		return adminId;
	}
	public void setAdminId(String adminId) {
		this.adminId = adminId;
	}
	public String getSourceSystemId() {
		return sourceSystemId;
	}
	public void setSourceSystemId(String sourceSystemId) {
		this.sourceSystemId = sourceSystemId;
	}
	public String getDirectSystemId() {
		return directSystemId;
	}
	public void setDirectSystemId(String directSystemId) {
		this.directSystemId = directSystemId;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public Date getWorkDate() {
		return workDate;
	}
	public void setWorkDate(Date workDate) {
		this.workDate = workDate;
	}
	
//	
//		data.setAdminId(resultSet.getString("adminId"));
//		data.setSourceSystemId(resultSet.getString("sourceSystem"));
//		data.setDirectSystemId(resultSet.getString("directSystem"));
//		data.setMethod(resultSet.getString("method"));
//		data.setResult(resultSet.getString("result"));
//		data.setDetail(resultSet.getString("detail"));
//		data.setWorkDate(resultSet.getDate("workDate"));
}
