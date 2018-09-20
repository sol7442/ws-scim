package com.wession.scim.exception;

public class ScimUsersException extends Exception {
	private int scim_code = 400;
	
    public ScimUsersException(String message) {
        super(message);
    }
    
    public ScimUsersException(int code, String message) {
        super(message);
        scim_code = code;
    }

    public ScimUsersException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }
    
    public ScimUsersException(String message, Throwable throwable) {
        super(message, throwable);
    }
    
    public int getCode(){
    	return scim_code;
    }
    
}
