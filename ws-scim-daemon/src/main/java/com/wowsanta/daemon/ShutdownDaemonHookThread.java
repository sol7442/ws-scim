package com.wowsanta.daemon;

public class ShutdownDaemonHookThread extends Thread {
	public void run() {
		System.out.println("daemon shutdown....");
	}
}
