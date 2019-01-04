package com.wowsanta.scim.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SCIMLogger {
	
	private static Logger sys = LoggerFactory.getLogger("system");
	private static Logger proc = LoggerFactory.getLogger("process");
	private static Logger error = LoggerFactory.getLogger("error");
	private static Logger audit = LoggerFactory.getLogger("audit");
	private static Logger access = LoggerFactory.getLogger("acccess");
	
	public static void sys(String msg, Object ... params) {
		sys.info(msg, params);
	}
	
	public static void proc(String msg, Object ... params) {
		proc.info(msg, params);
	}
	
	public static void error(String msg, Object ... params) {
		error.info(msg, params);
	}
	
	public static void audit(String msg, Object ... params) {
		audit.info(msg, params);
	}

	public static void access(String msg, Object ... params) {
		access.info(msg, params);
	}
}
