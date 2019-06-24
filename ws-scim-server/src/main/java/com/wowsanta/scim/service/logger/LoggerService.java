package com.wowsanta.scim.service.logger;

import java.io.File;
import java.io.OutputStream;
import java.nio.file.Files;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wowsanta.scim.protocol.ClientReponse;
import com.wowsanta.scim.protocol.ResponseState;

import spark.Request;
import spark.Response;
import spark.Route;

public class LoggerService {

	Logger logger = LoggerFactory.getLogger(LoggerService.class);
	
	public Route getSystemLogList() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				ClientReponse client_response = new ClientReponse();
				try {
					String log_path = System.getProperty("logback.path");
					File log_dir = new File(log_path);
					
					logger.debug("log path : {}", log_path);

					client_response.setData(log_dir.list());
					client_response.setState(ResponseState.Success);
					
					logger.debug("log file size  : {}", log_dir.list().length);
					
				}catch (Exception e) {
					logger.error(e.getMessage(),e);
					client_response.setState(ResponseState.Fail);
					client_response.setMessage(e.getMessage());
				}
				
				return client_response;
			}
		};
	}

	public Route getSystemLogFile() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				String fileName = request.params(":fileName").replaceAll("\\+"," ");

				logger.debug("log fileName >> : {}", fileName);
				

				
				String log_path = System.getProperty("logback.path");
				File log_dir = new File(log_path); 
				File[] log_files = log_dir.listFiles();
				File request_file = null;
				for (File file : log_files) {
					if(file.getName().equals(fileName)) {
						request_file = file;
						break;
					}
				}
				
				if(request_file != null) {
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
