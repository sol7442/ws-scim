package com.wowsanta.scim.scheduler;

public class SCIMSchedulerDetailHistory extends SCIMSchedulerHistory {
	private String schedulerDesc;
	private String adminId;
	public String getSchedulerDesc() {
		return schedulerDesc;
	}
	public void setSchedulerDesc(String schedulerDesc) {
		this.schedulerDesc = schedulerDesc;
	}
	public String getAdminId() {
		return adminId;
	}
	public void setAdminId(String adminId) {
		this.adminId = adminId;
	}
	
}
