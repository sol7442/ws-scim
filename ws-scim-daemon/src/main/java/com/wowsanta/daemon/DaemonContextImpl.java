package com.wowsanta.daemon;


import org.apache.commons.daemon.DaemonContext;
import org.apache.commons.daemon.DaemonController;

public class DaemonContextImpl implements DaemonContext{
	private final String[] args;
	
	public DaemonContextImpl(String[] args) {
		this.args = args;
	}
	@Override
	public DaemonController getController() {
		return null;
	}

	@Override
	public String[] getArguments() {
		return this.args;
	}
}