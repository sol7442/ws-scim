package com.wowsanta.scim.service.logger;

import java.io.File;
import java.io.OutputStream;
import java.nio.file.Files;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import spark.Request;
import spark.Response;
import spark.Route;

public class LoggerService {

	Logger logger = LoggerFactory.getLogger(LoggerService.class);
	
	public Route getSystemLogList() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				String log_path = System.getProperty("logback.path");
				logger.debug("log path : {}", log_path);
				File log_dir = new File(log_path); 
				
				return log_dir.listFiles();//gson.toJson(log_dir.listFiles());
			}
		};
	}

	public Route getSystemLogFile() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				String fileName = request.params(":fileName").replaceAll("\\+"," ");

				logger.debug("log fileName >> : {}", fileName);
				
				System.out.println("file fileName >> " + fileName);

				
				String log_path = System.getProperty("logback.path");
				File log_dir = new File(log_path); 
				File[] log_files = log_dir.listFiles();
				File request_file = null;
				for (File file : log_files) {
					//System.out.println("file list --> " + file.getName());
					if(file.getName().equals(fileName)) {
						System.out.println("find file --> " + file);
						request_file = file;
						break;
					}
				}
				
				System.out.println("file ?? >> " + request_file);
				
				if(request_file != null) {
					
					System.out.println("...write file...: " + fileName);
					
			        response.header("Content-Disposition", String.format("attachment; filename=", fileName));
			        response.raw().setContentType("application/octet-stream");
			        response.raw().setContentLength((int) request_file.length());
			        OutputStream outputStream = response.raw().getOutputStream();
			        outputStream.write(Files.readAllBytes(request_file.toPath()));
			        outputStream.flush();	
				}
				
				return response.raw();
			}
		};
	}

}
