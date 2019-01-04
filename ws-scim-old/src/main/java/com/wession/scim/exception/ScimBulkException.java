package com.wession.scim.exception;

public class ScimBulkException extends Exception {
	private int scim_code = 400;
	
    public ScimBulkException(String message) {
        super(message);
    }
    
    public ScimBulkException(int code, String message) {
        super(message);
        scim_code = code;
    }

    public ScimBulkException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }
    
    public ScimBulkException(String message, Throwable throwable) {
        super(message, throwable);
    }
    
    public int getCode(){
    	return scim_code;
    }
    
}
