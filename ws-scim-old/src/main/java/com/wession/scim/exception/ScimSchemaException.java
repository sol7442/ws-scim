package com.wession.scim.exception;

public class ScimSchemaException extends Exception {
	private int scim_code = 400;
	
    public ScimSchemaException(String message) {
        super(message);
    }
    
    public ScimSchemaException(int code, String message) {
        super(message);
        scim_code = code;
    }

    public ScimSchemaException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }
    
    public ScimSchemaException(String message, Throwable throwable) {
        super(message, throwable);
    }
    
    public int getCode(){
    	return scim_code;
    }
    
}
