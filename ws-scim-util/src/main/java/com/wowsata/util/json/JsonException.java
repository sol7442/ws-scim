package com.wowsata.util.json;

public class JsonException extends Exception {
	private static final long serialVersionUID = -2928277151949349630L;
	public JsonException(String message, Throwable e){
		super(message, e);
	}
}
