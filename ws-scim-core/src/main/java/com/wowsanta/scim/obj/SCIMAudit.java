package com.wowsanta.scim.obj;

import java.util.Date;

import com.wowsanta.scim.json.AbstractJsonObject;
import com.wowsanta.scim.util.Random;

public class SCIMAudit extends AbstractJsonObject{
	private String workId;
	private String workerId;
	private String workerType;
	private String userId;
	private String sourceSystemId;
	private String targetSystemId;
	private String action; // 동기화 - 배포           - 상신/승인
	private String method; // 생성 - 삭제 - 변경  - 요청/결제
	private String result = "FAILD";; // 결과 - 성공/실패     - 승인/거절
	private String detail; // 사유
	private Date   workDate;
	
	
	public SCIMAudit() {}
	public SCIMAudit(Date date) {
		this.workDate = date;
		this.workId = date.getTime() + "-" + Random.number(0, 100000000);
	}
	
	public String getWorkId() {
		return workId;
	}
	public void setWorkId(String workId) {
		this.workId = workId;
	}
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
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getSourceSystemId() {
		return sourceSystemId;
	}
	public void setSourceSystemId(String sourceSystemId) {
		this.sourceSystemId = sourceSystemId;
	}
	public String getTargetSystemId() {
		return targetSystemId;
	}
	public void setTargetSystemId(String targetSystemId) {
		this.targetSystemId = targetSystemId;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
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
}
