package com.wowsanta.scim.exception;


public enum SCIMError {
	
//	public static final SCIMError e307 = new SCIMError("307","Temporary Redirect");
//	public static final SCIMError e308 = new SCIMError("308","Permanent Redirect");
//	public static final SCIMError e400 = new SCIMError("400","Bad Request");
//	public static final SCIMError e401 = new SCIMError("401","Unauthorized");
//	public static final SCIMError e403 = new SCIMError("403","Forbidden");
//	public static final SCIMError e404 = new SCIMError("404","Not Found");
//	public static final SCIMError e408 = new SCIMError("404","ERR_CONNECTION_TIMED_OUT");
//	public static final SCIMError e409 = new SCIMError("409","Conflict");
//	public static final SCIMError e412 = new SCIMError("412","Precondition Failed");
//	public static final SCIMError e413 = new SCIMError("412","Payload Too Large");
//	public static final SCIMError e500 = new SCIMError("412","Internal Server Error");
//	public static final SCIMError e501 = new SCIMError("412","Not Implemented");
	
	
	TemprorayRedirect(	307,"Temporary Redirect"),
	PermanentRedirect(	308,"Temporary Redirect"),
	BadRequest(			400,"Bad Request"),
	Unauthorized(		401,"Unauthorized"),
	Forbidden(			403,"Forbidden"),
	NotFound(			404,"Not Found"),
	Conflict(			409,"Conflict"),
	PreconditionFailed(	412,"Precondition Failed"),
	PayloadTooLarge(	413,"Payload Too Large"),
	InternalServerError(500,"Internal Server Error"),
	NotImplemented(		501,"Not Implemented"),
	
	
	invalidSyntax(	400,"Bad Request", "invalidSyntax"),
	invalidFilter(	400,"Bad Request", "invalidFilter"),
	tooMany(		400,"Bad Request", "tooMany"),
	uniqueness(		400,"Bad Request", "uniqueness"),
	mutability(		400,"Bad Request", "mutability"),
	invalidPath(	400,"Bad Request", "invalidPath"),
	noTarget(		400,"Bad Request", "noTarget"),
	invalidValue(	400,"Bad Request", "invalidValue"),
	invalidVers(	400,"Bad Request", "invalidVers"),
	sensitive(		400,"Bad Request", "sensitive");
		
	
	private int status;
	private String scimType;
	private String detail;
	
	private SCIMError(int status, String detail) {
		this.status = status;
		this.detail = detail;
	}
	
	private SCIMError(int status, String detail, String type) {
		this.status = status;		
		this.detail = detail;
		this.scimType = type;
	}
	
	public int getStatus() {return this.status;}
	public String getScimType() {return this.scimType;}
	public String getDetail() {return this.detail;}
	
}
