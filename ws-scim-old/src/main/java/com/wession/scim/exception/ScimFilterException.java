package com.wession.scim.exception;

public class ScimFilterException extends Exception {
	private int scim_code = 400;
	
    public ScimFilterException(String message) {
        super(message);
    }
    
    public ScimFilterException(int code, String message) {
        super(message);
        scim_code = code;
    }

    public ScimFilterException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }
    
    public ScimFilterException(String message, Throwable throwable) {
        super(message, throwable);
    }
    
    public int getCode(){
    	return scim_code;
    }
    
}
