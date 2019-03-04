package com.ehyundai.object;

import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wowsanta.scim.obj.JsonUtil;
import com.wowsanta.scim.obj.SCIMResource2;

public class Resource extends SCIMResource2 {
	
	private String userName;
	private boolean active;
	private String password;
	private String exernalId;
	
	private String employeeNumber;
	private String organization;
	private String division;
	private String department;
	
	private String companyCode;
	private String groupCode;
	private String groupName;
	
	private String positionCode;
	private String position;
	
	private String jobCode;
	private String job;
	
	private String rankCode;
	private String rank;
	
	private Date   joinDate;
	private Date   retireDate;
	
	private Date   lastAccessDate;
	
	private Date   created;
	private Date   lastModified;
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getExernalId() {
		return exernalId;
	}
	public void setExernalId(String exernalId) {
		this.exernalId = exernalId;
	}
	public String getEmployeeNumber() {
		return employeeNumber;
	}
	public void setEmployeeNumber(String employeeNumber) {
		this.employeeNumber = employeeNumber;
	}
	public String getOrganization() {
		return organization;
	}
	public void setOrganization(String organization) {
		this.organization = organization;
	}
	public String getDivision() {
		return division;
	}
	public void setDivision(String division) {
		this.division = division;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getCompanyCode() {
		return companyCode;
	}
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
	public String getGroupCode() {
		return groupCode;
	}
	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getPositionCode() {
		return positionCode;
	}
	public void setPositionCode(String positionCode) {
		this.positionCode = positionCode;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getJobCode() {
		return jobCode;
	}
	public void setJobCode(String jobCode) {
		this.jobCode = jobCode;
	}
	public String getJob() {
		return job;
	}
	public void setJob(String job) {
		this.job = job;
	}
	public String getRankCode() {
		return rankCode;
	}
	public void setRankCode(String rankCode) {
		this.rankCode = rankCode;
	}
	public String getRank() {
		return rank;
	}
	public void setRank(String rank) {
		this.rank = rank;
	}
	public Date getJoinDate() {
		return joinDate;
	}
	public void setJoinDate(Date joinDate) {
		this.joinDate = joinDate;
	}
	public Date getRetireDate() {
		return retireDate;
	}
	public void setRetireDate(Date retireDate) {
		this.retireDate = retireDate;
	}
	public Date getLastAccessDate() {
		return lastAccessDate;
	}
	public void setLastAccessDate(Date lastAccessDate) {
		this.lastAccessDate = lastAccessDate;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public Date getLastModified() {
		return lastModified;
	}
	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}
	
	public JsonObject parse(String json_str){
		JsonObject json_obj = super.parse(json_str);
		
		setId(JsonUtil.toString(json_obj.get("id")));
		
		this.userName 		= JsonUtil.toString(json_obj.get("userName"));
		this.active 		= JsonUtil.toBoolean(json_obj.get("active"));
		this.password 		= JsonUtil.toString(json_obj.get("password"));
		this.employeeNumber = JsonUtil.toString(json_obj.get("employeeNumber"));
		this.organization 	= JsonUtil.toString(json_obj.get("organization"));
		this.division 		= JsonUtil.toString(json_obj.get("division"));
		this.department 	= JsonUtil.toString(json_obj.get("department"));
		
		this.companyCode 	= JsonUtil.toString(json_obj.get("companyCode"));
		this.groupCode 		= JsonUtil.toString(json_obj.get("groupCode"));
		this.groupName 		= JsonUtil.toString(json_obj.get("groupName"));
		this.positionCode 	= JsonUtil.toString(json_obj.get("positionCode"));
		this.position 		= JsonUtil.toString(json_obj.get("position"));
		this.jobCode 		= JsonUtil.toString(json_obj.get("jobCode"));
		this.job 			= JsonUtil.toString(json_obj.get("job"));
		this.rankCode 		= JsonUtil.toString(json_obj.get("rankCode"));
		this.rank 			= JsonUtil.toString(json_obj.get("rank"));
		
		this.joinDate      	= JsonUtil.toDate(json_obj.get("joinDate"));
		this.retireDate 	= JsonUtil.toDate(json_obj.get("retireDate"));
		this.lastAccessDate = JsonUtil.toDate(json_obj.get("lastAccessDate"));
		this.created      	= JsonUtil.toDate(json_obj.get("created"));
		this.lastModified 	= JsonUtil.toDate(json_obj.get("lastModified"));
		
		return json_obj;
	}
	public JsonObject encode(){
		JsonObject super_json = super.encode();
		
		
		super_json.addProperty("id",getId());
		
		super_json.addProperty("userName",this.userName);
		super_json.addProperty("active",this.active);
		super_json.addProperty("password",this.password);
		super_json.addProperty("exernalId",this.exernalId);
		super_json.addProperty("employeeNumber",this.employeeNumber);
		super_json.addProperty("organization",this.organization);
		super_json.addProperty("division",this.division);
		super_json.addProperty("department",this.department);
		
		super_json.addProperty("companyCode",this.companyCode);
		super_json.addProperty("groupCode",this.groupCode);
		super_json.addProperty("groupName",this.groupName);
		super_json.addProperty("positionCode",this.positionCode);
		super_json.addProperty("position",this.position);
		super_json.addProperty("jobCode",this.jobCode);
		super_json.addProperty("job",this.job);
		super_json.addProperty("rankCode",this.rankCode);
		super_json.addProperty("rank",this.rank);
		
		super_json.addProperty("joinDate",JsonUtil.toString(this.joinDate));
		super_json.addProperty("retireDate",JsonUtil.toString(this.retireDate));
		super_json.addProperty("lastAccessDate",JsonUtil.toString(this.lastAccessDate));
		super_json.addProperty("created",JsonUtil.toString(this.created));
		super_json.addProperty("lastModified",JsonUtil.toString(this.lastModified));
	    
		return super_json;
	}
}
