package com.wowsanta.daemon;

import java.io.IOException;
import java.nio.file.ClosedWatchServiceException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DaemonWatcher extends Thread {

	Logger logger = LoggerFactory.getLogger(DaemonWatcher.class);
	
//	private ExecutorService executorService = Executors.newSingleThreadExecutor();
	private WatchService watchService;
	
	//private List<Path> watchList = new ArrayList<Path>();
	private Map<String,Path> watchMap = new HashMap<String,Path>();

	private WowsataDaemon daemon;
	private boolean watch = true;
	
  private final Map<WatchKey, Path> keys = new ConcurrentHashMap<WatchKey, Path>();

	  
	public DaemonWatcher() {
		try {
			watchService = FileSystems.getDefault().newWatchService();
		} catch (IOException e) {
			logger.error("Watcher create Failed ", e);
		}
	}
	
	public void addWatchPath(String path) {
		Path watch_path = Paths.get(path);
		if(watchMap.putIfAbsent(path, watch_path) == null) {
			logger.info("Add Watcher Path {} :", path );
			try {
				WatchKey watchKey= watch_path.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
				keys.put(watchKey, watch_path);
			} catch (IOException e) {
				logger.error("Watcher register Failed ", e);
			}
		}else {
			logger.info("Already Exist Watcher Path {} :", watchMap.get(path).toString() );
		}
	}

	public void close() {
		logger.info("DEAMON WATCHER STOP");
		try {			
			this.watch = false;
			keys.keySet().forEach(WatchKey::cancel);
			this.watchService.close();
		} catch (IOException e) {
			logger.error("Watcher stop failed", e);
		}
	}
	
	public void run() {
			
			watch = true;
	        while ( watch ) {
	        	WatchKey key = null; 
				try {
					logger.info("Waiting for key to be signalled...");
	                key = watchService.take();
	            }
	            catch (InterruptedException ex) {
	            	logger.error("Interrupted Exception", ex);
	                return;
	            }catch (ClosedWatchServiceException ex) {
	            	ex.printStackTrace();
	            	return;
	            }
				
	            List<WatchEvent<?>> eventList = key.pollEvents();
	            logger.debug("Process the pending events for the key: {} " , eventList.size());
	            
	            for (WatchEvent<?> genericEvent: eventList) {
	                WatchEvent.Kind<?> eventKind = genericEvent.kind();
	                logger.debug("Event kind: : {} " , eventKind);

	                if (eventKind == StandardWatchEventKinds.OVERFLOW) {
	                    continue; 
	                }
	 
	                WatchEvent<Path> pathEvent = (WatchEvent<Path>) genericEvent;
	                Path file = pathEvent.context();
					logger.info("File name: {} ", file);
	            } 
	 
	            boolean validKey = key.reset();
	            if (! validKey) {
	            	logger.error("Invalid key");
	                break; 
	            }
	            
	            try {
					daemon.restart();
				} catch (Exception e) {
					logger.error("DAEMOMN RESTAT FAILED : ", e);
					break;
				}
			}
			logger.debug("watcher thread.. finish");
	}

	public void setDaemon(WowsataDaemon daemon) {
		this.daemon = daemon;
	}



	
	
}
