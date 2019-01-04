package com.wession.scim.exception;

public class ScimException extends Exception {
	private int scim_code = 400;
	
    public ScimException(String message) {
        super(message);
    }
    
    public ScimException(int code, String message) {
        super(message);
        scim_code = code;
    }

    public ScimException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }
    
    public ScimException(String message, Throwable throwable) {
        super(message, throwable);
    }
    
    public int getCode(){
    	return scim_code;
    }
    
}
