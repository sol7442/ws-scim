package com.wowsanta.scim.scheduler;

import java.util.Date;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.json.AbstractJsonObject;

public class SCIMSchedulerHistory extends AbstractJsonObject {
	private String schedulerId;
	private String workId;
	
	private int reqPut;
	private int reqPost;
	private int reqPatch;
	private int reqDelete;
	
	
	private int resPut;
	private int resPost;
	private int resPatch;
	private int resDelete;
	
	private Date workDate;

	public String getSchedulerId() {
		return schedulerId;
	}

	public void setSchedulerId(String schedulerId) {
		this.schedulerId = schedulerId;
	}

	public String getWorkId() {
		return workId;
	}

	public void setWorkId(String workId) {
		this.workId = workId;
	}

	public int getReqPut() {
		return reqPut;
	}

	public void setReqPut(int reqPut) {
		this.reqPut = reqPut;
	}

	public int getReqPost() {
		return reqPost;
	}

	public void setReqPost(int reqPost) {
		this.reqPost = reqPost;
	}

	public int getReqPatch() {
		return reqPatch;
	}

	public void setReqPatch(int reqPatch) {
		this.reqPatch = reqPatch;
	}

	public int getReqDelete() {
		return reqDelete;
	}

	public void setReqDelete(int reqDelete) {
		this.reqDelete = reqDelete;
	}

	public int getResPut() {
		return resPut;
	}

	public void setResPut(int resPut) {
		this.resPut = resPut;
	}

	public int getResPost() {
		return resPost;
	}

	public void setResPost(int resPost) {
		this.resPost = resPost;
	}

	public int getResPatch() {
		return resPatch;
	}

	public void setResPatch(int resPatch) {
		this.resPatch = resPatch;
	}

	public int getResDelete() {
		return resDelete;
	}

	public void setResDelete(int resDelete) {
		this.resDelete = resDelete;
	}

	public Date getWorkDate() {
		return workDate;
	}

	public void setWorkDate(Date workDate) {
		this.workDate = workDate;
	}
	
}
