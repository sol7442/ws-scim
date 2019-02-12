package com.ehyundai.object;

import java.util.Date;

import com.google.gson.JsonObject;
import com.wowsanta.scim.obj.DefaultEnterpriseUser;
import com.wowsanta.scim.obj.JsonUtil;
import com.wowsanta.scim.obj.SCIMEnterpriseUser;
import com.wowsanta.scim.schema.SCIMConstants;

public class User extends SCIMEnterpriseUser{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6709617785615904142L;
	
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
	
	private String eMail;
	
	public static final String USER_URI = "urn:ehyundai:params:scim:schemas:extension:enterprise:2.0:User";
	public User() {
		super();
		addSchema(USER_URI);
	}
	
	
	@Override
	public JsonObject parse(String json_str) {
		JsonObject json_user = super.parse(json_str);
		JsonObject json_this = json_user.get(USER_URI).getAsJsonObject();
		if(!json_this.isJsonNull()){
			this.companyCode 	= JsonUtil.toString(json_this.get("companyCode"));
			this.groupCode		= JsonUtil.toString(json_this.get("groupCode"));
			this.groupName		= JsonUtil.toString(json_this.get("groupName"));
			
			this.positionCode 	= JsonUtil.toString(json_this.get("positionCode"));
			this.position		= JsonUtil.toString(json_this.get("position"));
			
			this.jobCode 		= JsonUtil.toString(json_this.get("jobCode"));
			this.job			= JsonUtil.toString(json_this.get("job"));
			
			this.rankCode 		= JsonUtil.toString(json_this.get("rankCode"));
			this.rank			= JsonUtil.toString(json_this.get("rank"));
			
			this.joinDate 		= JsonUtil.toDate(json_this.get("joinDate"));
			this.retireDate		= JsonUtil.toDate(json_this.get("retireDate"));
			
			this.eMail			= JsonUtil.toString(json_this.get("eMail"));
			
		}
		return json_user;
	}

	@Override
	public JsonObject encode() {
		JsonObject json_this = new JsonObject();
		json_this.addProperty("companyCode", this.companyCode);
		json_this.addProperty("groupCode"	, this.groupName);
		json_this.addProperty("groupName"	, this.groupName);
		
		json_this.addProperty("positionCode", this.positionCode);
		json_this.addProperty("position"	, this.position);
		
		json_this.addProperty("jobCode"		, this.jobCode);
		json_this.addProperty("job"			, this.job);
		
		json_this.addProperty("rankCode"	, this.rankCode);
		json_this.addProperty("rank"		, this.rank);
		
		json_this.addProperty("joinDate"	, JsonUtil.toString(this.joinDate));
		json_this.addProperty("retireDate"	, JsonUtil.toString(this.retireDate));
		
		json_this.addProperty("eMail"		, this.eMail);
		
		JsonObject user_json = super.encode();
		user_json.add(USER_URI, json_this);
		
		return user_json;
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

	public String geteMail() {
		return eMail;
	}

	public void seteMail(String eMail) {
		this.eMail = eMail;
	}

}
