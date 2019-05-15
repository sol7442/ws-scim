package com.wowsanta.scim.client;

public class ClientException extends Exception{
	private static final long serialVersionUID = 2765561841620140174L;
	public ClientException(String message) {
		super(message);
	}
	public ClientException(String message, Exception e) {
		super(message,e);
	}

}
