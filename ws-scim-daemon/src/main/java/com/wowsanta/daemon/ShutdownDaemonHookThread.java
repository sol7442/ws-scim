package com.wowsanta.daemon;

import java.util.Date;

public class ShutdownDaemonHookThread extends Thread {
	private Process process;
	public void run() {
		System.out.println(new Date().toString() + "daemon shutdown....");
		this.process.destroy();
	}

	public void setProcess(Process process) {
		this.process = process;
	}
}
