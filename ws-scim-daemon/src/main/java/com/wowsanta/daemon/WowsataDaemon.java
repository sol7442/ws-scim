package com.wowsanta.daemon;

import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.daemon.Daemon;
import org.apache.commons.daemon.DaemonContext;
import org.apache.commons.daemon.DaemonInitException;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class WowsataDaemon implements Daemon {

	static Logger logger = LoggerFactory.getLogger(WowsataDaemon.class);
	
	private static ShutdownDaemonHookThread shutdownHook;
	private static WowsataDaemon daemon = new WowsataDaemon();
//	private ExecutorService executorService = Executors.newSingleThreadExecutor();
	private DaemonWatcher watcher;
	private DaemonContext context;

	private Thread processThread;
	private Process process;
	private List<String> command_list;
	
	private boolean restart = false;
	private boolean destroy = false;
	
	public static void main(String[] args) {
		shutdownHook = new ShutdownDaemonHookThread();
		shutdownHook.attachShutDownHook();
		//Runtime.getRuntime().addShutdownHook(shutdownHook);
		WowsataDaemon.start(args);
	}
	
	public static void start(String [] args){
		logger.info("DAEMON MAIN START");
        try {
        	daemon.init(new DaemonContextImpl(args));
        	daemon.start();
        } catch (Exception e) {
            logger.error("DEAMON MAIN ERROR ",e);
        }
    }
    
    public static void stop(String [] args){
        try {
        	daemon.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private WowsataDaemon() {
    	this.watcher = new DaemonWatcher();
    	this.watcher.setDaemon(this);
    	this.watcher.start();
    }
    
	@Override
	public void init(DaemonContext context) throws DaemonInitException, Exception {
		logger.info("DAEMON INITIALIZE");
		
		String configFile = System.getProperty("config.file");
		logger.info("DAEMON CONFIG FILE : {} ", configFile);
		File daemon_config_file = new File(configFile);
		logger.debug("daemon_config_file : {}",daemon_config_file.getCanonicalPath());
		
		try {
			this.context = context;
			logger.debug("daemon_config_file1 : {}",daemon_config_file.getCanonicalPath());

			DaemonConfiguration config = DaemonConfiguration.loadFromFile(configFile);
			
			watcher.addWatchPath(config.getDistPath());
			watcher.addWatchPath(config.getConfigPath());
			watcher.addWatchPath(config.getLibPath());
			
			logger.debug("daemon_config_file2 : {}",daemon_config_file.getCanonicalPath());

			logger.debug("system propertis.............");
			Set<Object> key_set = System.getProperties().keySet();
			for (Object sys_key : key_set) {
				logger.debug("- {} : {}",sys_key, System.getProperty(sys_key.toString()));
			}
			
			StringBuffer java_run = new StringBuffer();
			java_run.append(System.getProperty("java.home")).append(File.separator);
			java_run.append("bin").append(File.separator);
			java_run.append("java");
			
			logger.info("library cp : " + config.getLibraryClassPath());
			
			logger.debug("sesrvice run properties..........");
			command_list = new ArrayList<String>();
			command_list.add(java_run.toString());
			command_list.add("-cp");			
			command_list.add(System.getProperty("java.class.path") + System.getProperty("path.separator") + config.getLibraryClassPath() );
			
			command_list.add("-Dlogback.path=" + System.getProperty("logback.path"));
			command_list.add("-Dlogback.configurationFile=" + System.getProperty("logback.configurationFile"));
			command_list.add("-Dlogback.mode=" + System.getProperty("logback.mode"));
			
			command_list.add("-Dapp.name=" + System.getProperty("app.name"));
			command_list.add("-Dapp.dist=" + config.getDistPath());
			command_list.add("-Dapp.libs=" + config.getLibPath());
			
			command_list.add("-Dservice.config="+config.getServiceConfig());
			command_list.add(config.getServiceClass());
			
			for (String command : command_list) {
				logger.debug("-{}", command);
			}
			
			
		}catch (Exception e) {
			logger.error("DAEMON INITIALIZE FAILD : " + configFile ,e);
		}
	}

	@Override
	public void start() throws Exception {
		logger.info("DAEMON START");
		//
		processThread = new Thread(new Runnable() {
			@Override
			public void run() {
				ProcessBuilder builder = new ProcessBuilder();
				builder.command(command_list);
				builder.redirectOutput(Redirect.INHERIT);
			    builder.redirectError(Redirect.INHERIT);
			    
			    try {
					process = builder.start();
					if(shutdownHook != null) {
						shutdownHook.setProcess(process);
					}
			        
					logger.info("process start");
					int exitCode = process.waitFor();
					logger.info("process end : {}",exitCode);
					
				} catch (Exception e) {
					logger.error("Process Start Error "+e.getMessage(), e);
					e.printStackTrace();
				}
			}
		});
		processThread.start();
	}

	@Override
	public void stop() throws Exception {
		logger.info("DAEMON STOP");
		process.destroy();
	}

	@Override
	public void destroy() {
		logger.info("DAEMON DESTROY START");
		try {
			destroy = true;
			this.watcher.close();
			this.stop();
			this.processThread.join();
		} catch (Exception e) {
			logger.error("DAEMON DESTROY ERROR",e);
		}
		logger.info("DAEMON DESTROY FINISH");
	}
	
	public void restart() throws Exception {
		logger.debug("restart call...");
		if(!restart && !destroy) {
			logger.info("DAEMON RESTART START  .......");
			restart = true;
			
			Thread restar_thread = new Thread(new Runnable() {
				@Override
				public void run() {

					try {
						int stop_count = 0;
						while(stop_count < 2) {
							logger.info("DAEMON RESTART STOP PANDDING...{}", stop_count);
							Thread.sleep(1000);
							stop_count++;
						}
						
						stop();
						processThread.join();
						
						init(context);
						start();
						
						int start_count = 0; 
						while(start_count < 3) {
							logger.info("DAEMON RESTART START PANDDING...{}", start_count);
							Thread.sleep(1000);
							start_count++;
						}
					
						restart = false;
						logger.info("DAEMON RESTART FINISH ......");
					}catch (Exception e) {
						logger.error("DAEMON RESTART FAILED ",e);
					}
				}
			});
			restar_thread.start();
		}
	}
}
