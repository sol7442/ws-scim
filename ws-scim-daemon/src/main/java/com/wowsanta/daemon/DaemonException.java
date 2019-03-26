package com.wowsanta.daemon;

public class DaemonException extends Exception {
	private static final long serialVersionUID = 5716010957648724804L;

	public DaemonException(String message, Throwable e) {
		super(message,e);
	}
}
