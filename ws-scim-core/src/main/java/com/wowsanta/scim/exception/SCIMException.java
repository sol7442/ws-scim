package com.wowsanta.scim.exception;


public class SCIMException extends Exception{
	private static final long serialVersionUID = 6917484448134959239L;
	private int code;
	private SCIMError error;
//	private List<Exception> stack = new ArrayList<Exception>();
	
	
	public SCIMException(String msg) {
		super(msg);

		this.error 		= SCIMError.InternalServerError;
		this.code 		= SCIMError.InternalServerError.getStatus();
	}
	
	public SCIMException(String msg , Exception e) {
		super(msg,e);
		
		this.error 		= SCIMError.InternalServerError;
		this.code 		= SCIMError.InternalServerError.getStatus();
		
//		this.stack.add(e);
		
	}
	
	public SCIMException(SCIMError err, Exception e) {
		super(err.getDetail());
		
		this.error 		= err;
		this.code 		= this.error.getStatus();
		
//		this.stack.add(e);
	}
	
	
	
	public SCIMException(SCIMError err) {
		super(err.getDetail());
		
		this.error 		= err;
		this.code 		= this.error.getStatus();
	}

	
	public SCIMException(String msg ,SCIMError err) {
		super(msg);		
		this.error 		= err;
		this.error.addDetail(msg);
	}
	
	public SCIMException(String msg ,SCIMError err, Throwable e) {
		super(msg,e);		
		this.error 		= err;
		this.error.addDetail(e.getMessage());
	}

	public int getCode() {
		return this.code;
	}

	public SCIMError getError() {
		if(this.error == null) {
			return SCIMError.InternalServerError;
		}
		return error;
	}

	public void setError(SCIMError error) {
		this.error = error;
	}
}
