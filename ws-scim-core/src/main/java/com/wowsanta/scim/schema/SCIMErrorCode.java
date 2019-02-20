package com.wowsanta.scim.schema;

import com.wowsanta.scim.message.SCIMError;

public class SCIMErrorCode {
	public static final SCIMError e307 = new SCIMError("307","Temporary Redirect");
	public static final SCIMError e308 = new SCIMError("308","Permanent Redirect");
	public static final SCIMError e400 = new SCIMError("400","Bad Request");
	public static final SCIMError e401 = new SCIMError("401","Unauthorized");
	public static final SCIMError e403 = new SCIMError("403","Forbidden");
	public static final SCIMError e404 = new SCIMError("404","Not Found");
	public static final SCIMError e408 = new SCIMError("404","ERR_CONNECTION_TIMED_OUT");
	public static final SCIMError e409 = new SCIMError("409","Conflict");
	public static final SCIMError e412 = new SCIMError("412","Precondition Failed");
	public static final SCIMError e413 = new SCIMError("412","Payload Too Large");
	public static final SCIMError e500 = new SCIMError("412","Internal Server Error");
	public static final SCIMError e501 = new SCIMError("412","Not Implemented");
	
	public static enum SCIMType{
		invalidSyntax,invalidFilter,tooMany,uniqueness,mutability,invalidPath,noTarget,invalidValue,invalidVers,sensitive,
		serverError,
	}
}
