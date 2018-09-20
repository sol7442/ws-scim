package com.wession.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import spark.Request;
import spark.Response;

public class WessionLog {
	private static Logger systemLog = LoggerFactory.getLogger("SystemLog");
	private static Logger accessLog = LoggerFactory.getLogger("AccessLog");
	private static Logger processLog = LoggerFactory.getLogger("ProcessLog");
	private static Logger ProvisionLog = LoggerFactory.getLogger("ProvisionLog");
	private static Logger ScheduleLog = LoggerFactory.getLogger("ScheduleLog");
	private static Logger auditLog = LoggerFactory.getLogger("auditLog");
	
	public WessionLog() {
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        JoranConfigurator configurator = new JoranConfigurator();
        configurator.setContext(lc);
        lc.reset();

        try {
            configurator.doConfigure("D:\\DEV_05\\WessionIM\\wession_logback.xml");
        } catch (JoranException e) {
            e.printStackTrace();
        }
	}
	public Logger getSystemLog() { return systemLog; }
	public Logger getAccessLog() { return accessLog; }
	public Logger getProcessLog() { return processLog; }
	public Logger getProvisionLog() { return ProvisionLog; }
	public Logger getScheduleLog() { return ScheduleLog; }
	public Logger getAuditLog() { return auditLog; }
	
	public void accessLog(Request request, Response response) {
//		#Version: 1.0
//		#Date: 12-Jan-1996 00:00:00
//		#Fields: cs-method cs-uri cs-uri-query time-taken cs-ip
		long startTime = Long.parseLong((String)request.attribute("RequestTime"));
		String method = request.requestMethod();
		String cs_ip = request.ip();
		String cs_uri = request.uri();
		String cs_param = request.queryString();
		String time_taken = Long.toString(System.currentTimeMillis() - startTime);
//		String time_taken = "0";
		String bodyLength = response.body();
		if (bodyLength != null) {
			bodyLength = bodyLength.length() + "";
		} else {
			response.status(404);
		}

		String protocol = request.protocol();
		String log = cs_ip + " \"" + method + " " + cs_uri + " " + cs_param + " " + protocol + "\" " + time_taken + " " + response.status() + " " + bodyLength;
		accessLog.info(log);
		
	}
}
