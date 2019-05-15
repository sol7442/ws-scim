package com.wowsanta.scim.schema;

public class SCIMDefinitions {
    public static enum DataType {
        STRING, BOOLEAN, DECIMAL, INTEGER, DATE_TIME, DATE, BINARY, REFERENCE, COMPLEX
    }
    public static enum Mutability {
        READ_WRITE, READ_ONLY, IMMUTABLE, WRITE_ONLY
    }
    public static enum Returned {
        ALWAYS, NEVER, DEFAULT, REQUEST
    }
    public static enum Uniqueness {
        NONE, SERVER, GLOBAL
    }
    public static enum ReferenceType {
        USER, GROUP, EXTERNAL, URI
    }
    public static enum ResoureType {
        USER, GROUP, Schema, ResourceType;
    }
    
    public static enum MethodType {
    	GET("SELECT"),
    	POST("UPDATE"),
    	PUT("CREATE"),
    	PATCH("UPDAT"),
    	DELETE("DELETE")
    	;
    	
    	private String method;
    	MethodType(String repository_method){
    		this.method = repository_method;
    	}
    	public String getMethod() {
    		return this.method;
    	}
    }
    public static enum AdminType{
    	SYS_SCHEDULER,SYS_ADMIN,SYS_OPERATOR
    }
    public static enum UserType{
    	IM_ADMIN,IM_OPERATOR,SYS_ADMIN,SYS_OPERATOR,USER,DummyUser
    }
	public static enum ErrorType{
		uniqueness, invalidSyntax
	}
	public static enum TriggerType{
		DAY,WEEK,MONTH
	}
}
