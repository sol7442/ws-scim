package com.wowsanta.scim.obj;

import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wowsanta.scim.json.AbstractJsonObject;

public class SCIMSchedulerHistory extends AbstractJsonObject {

	private String schedulerId;
	private String workId;
	private String workerId;
	private int successCount;
	private int failCount;
	private Date workDate;
	
	public SCIMSchedulerHistory() {}
	public SCIMSchedulerHistory(String schedulerId) {
		this.schedulerId = schedulerId;
	}
	public void addAudit(SCIMAudit audit) {
		if(audit.getResult().equals("SUCCESS")) {
			this.successCount++;
		}else{
			this.failCount++;
		};
	}
	public String getSchedulerId() {
		return schedulerId;
	}
	public void setSchedulerId(String schedulerId) {
		this.schedulerId = schedulerId;
	}
	public String getWorkerId() {
		return workerId;
	}
	public void setWorkerId(String workerId) {
		this.workerId = workerId;
	}
	public int getSuccessCount() {
		return successCount;
	}
	public void setSuccessCount(int successCount) {
		this.successCount = successCount;
	}
	public int getFailCount() {
		return failCount;
	}
	public void setFailCount(int failCount) {
		this.failCount = failCount;
	}
	public Date getWorkDate() {
		return workDate;
	}
	public void setWorkDate(Date workDate) {
		this.workDate = workDate;
	}
	public String getWorkId() {
		return workId;
	}
	public void setWorkId(String workId) {
		this.workId = workId;
	}
	 
	public String toString() {
		return toString(false);
	}
	public String toString(boolean pretty) {
		try {
			GsonBuilder builder = new GsonBuilder().disableHtmlEscaping();
			if(pretty) {
				builder.setPrettyPrinting();
			}
			Gson gson  = builder.create();
			return gson.toJson(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
