package com.wowsanta.daemon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShutdownDaemonHookThread extends Thread {
	Logger logger = LoggerFactory.getLogger(ShutdownDaemonHookThread.class);
	private Process process;
//	
	public void attachShutDownHook(){	
		logger.info("SET SYSTEM SHUTDONW HOOK : ");
		Runtime.getRuntime().addShutdownHook(this);
	}
	public void run() {
		
		logger.info("SYSTEM SHUTDOWN RUN...");
		if(this.process != null) {
			this.process.destroy();
		}		
		logger.info("SYSTEM SHUTDOWN DISTROY...");
	}

	public void setProcess(Process process) {
		logger.info("SET SYSTEM PROCESS : {} ", process);
		this.process = process;
	}
	
	
}
