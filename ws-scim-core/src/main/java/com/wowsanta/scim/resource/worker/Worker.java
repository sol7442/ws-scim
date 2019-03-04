package com.wowsanta.scim.resource.worker;

import com.wowsanta.scim.json.AbstractJsonObject;

public class Worker extends AbstractJsonObject{
	private String workerId;
	private String workerType;
	public String getWorkerId() {
		return workerId;
	}
	public void setWorkerId(String workerId) {
		this.workerId = workerId;
	}
	public String getWorkerType() {
		return workerType;
	}
	public void setWorkerType(String workerType) {
		this.workerType = workerType;
	}
}
