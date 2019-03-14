package com.wowsanta.scim;


import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.daemon.Daemon;
import org.apache.commons.daemon.DaemonContext;
import org.apache.commons.daemon.DaemonController;
import org.apache.commons.daemon.DaemonInitException;

import com.wowsanta.scim.exception.SCIMError;
import com.wowsanta.scim.log.SCIMLogger;
import com.wowsanta.scim.resource.SCIMRepositoryManager;
import com.wowsanta.scim.resource.SCIMResouceFactory;
import com.wowsanta.scim.scheduler.SCIMSchedulerManager;



public class SCIMServer implements Daemon {

	private static final SCIMServer server = new SCIMServer();

	private ExecutorService executorService = Executors.newSingleThreadExecutor();
	
	public static void main(String[] args) throws DaemonInitException, Exception {
		try {
			server.init(new DaemonContextImpl(args));
			server.start();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void start(String [] args){
        try {
        	server.init(new DaemonContextImpl(args));
        	server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void stop(String [] args){
        try {
        	server.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	@Override
	public void init(DaemonContext context) throws DaemonInitException, Exception {
		//String log_path = System.getProperty("logback.path");
		File log_directory = new File(System.getProperty("logback.path"));
		if(!log_directory.exists()) {
			log_directory.mkdirs();
		}
		
		PrintStream out = new PrintStream(new FileOutputStream(log_directory + "/output.txt"));
		System.setOut(out);

		String config_file_path = "";
		if(context.getArguments().length == 0) {
			String instance_name = System.getProperty("scim.instance");
			if(instance_name == null) {
				instance_name = "";
			}else {
				instance_name = instance_name + "_";
			}
			
			File current_path  = new File(System.getProperty("user.dir"));
			config_file_path = current_path.getParent() + File.separator + "config" +File.separator + instance_name +"scim-service-provider.json"; 
		}
		System.out.println(config_file_path);
		System.out.println("server starting........");
		
		try {
			SCIMSystemManager.getInstance().load(config_file_path);
			
			SCIMSystemManager.getInstance().getServiceProvider().getServer().initialize();
			
			SCIMSystemManager.getInstance().loadRepositoryManager();
			SCIMRepositoryManager.getInstance().initailze();
			
			SCIMSystemManager.getInstance().loadSchdulerManager();
			SCIMSchedulerManager.getInstance().initialize();
		}catch (Exception e) {
			SCIMLogger.error("SYSTEM INITIALIZE FAILED : {}", e);
			throw new DaemonInitException(e.getMessage(),e);
		}	
	}
	@Override
	public void start() throws Exception {
		SCIMLogger.sys("SYSTEM START : {} ======== ",new Date());
		SCIMSystemManager.getInstance().getServiceProvider().getServer().start();
		this.executorService.execute(new Runnable() {
			CountDownLatch latch = new CountDownLatch(1);
			@Override
			public void run() {
				try {
					latch.await();
				} catch (InterruptedException e) {
					SCIMLogger.debug("Thread interrupted, probably means we're shutting down now.");
				}
			}
		});
	}
	@Override
	public void stop() throws Exception {
		SCIMSystemManager.getInstance().getServiceProvider().getServer().stop();
		this.executorService.shutdown();
		SCIMLogger.sys("SYSTEM SHUTDOWN : {} ======== ",new Date());
		System.exit(0);
	}
	@Override
	public void destroy() {
		SCIMLogger.sys("SYSTEM Destroying daemon....bye..");
	}
	
	private static final class DaemonContextImpl implements DaemonContext{
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
}
