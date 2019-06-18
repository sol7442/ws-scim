package com.wowsanta.scim;


import java.io.File;
import java.util.List;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.repository.SCIMRepositoryManager;
import com.wowsanta.scim.repository.system.SCIMSystemRepository;
import com.wowsanta.scim.scheduler.SCIMScheduler;
import com.wowsanta.scim.scheduler.SCIMSchedulerManager;
import com.wowsanta.scim.server.SparkConfiguration;

import spark.Spark;



public class SCIMServer  {

	Logger logger = LoggerFactory.getLogger(SCIMServer.class);
	
	private boolean initialize = false;
	public static void main(String[] args)  {
		try {
			Runtime.getRuntime().addShutdownHook(new ShutdownHookThread());
			new SCIMServer().init().start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public SCIMServer init() throws Exception{
		logger.info("SCIMServer INITIALIZE....................");
		
		initialize = false;
		try {
			String service_configFile    = System.getProperty("service.config");
			logger.info("CURRENT PATH      : {}", new File(System.getProperty("user.dir")).getCanonicalFile());
			logger.info("SERVICE CONFIG    : {}", service_configFile);
			
			SparkConfiguration service_config = SparkConfiguration.loadFromFile(service_configFile);
			logger.debug("SparkConfiguration \n{}",service_config.tojson(true));

			
			logger.info("SERVICE PROVIDER CONFIG    : {}", service_config.getServiceProviderConfig());
			SCIMSystemManager.getInstance().load(service_config.getServiceProviderConfig());
			
			
			loadRepository(service_config);
			loadScheduler();
			
			Spark.initExceptionHandler(new Consumer<Exception>() {
				@Override
				public void accept(Exception t) {
					logger.error("SPARK SERVER INITIALIZE FAIL ", t);
				}
			});
			
			Spark.port(service_config.getServicePort());
			Spark.threadPool(service_config.getMaxThreads(),service_config.getMinThreads(),service_config.getIdleTimeoutMills());
			if(service_config.getStaticFiles() != null) {
				logger.info("SPARK SERVER STATIC PATH    : {}", service_config.getStaticFiles());
				Spark.staticFiles.externalLocation(service_config.getStaticFiles());
			}
			
			try {
				if(service_config.getKeyStorePath() != null) {
					logger.info("SPARK SERVER SSL KEY STORE    : {}", service_config.getKeyStorePath());
					Spark.secure(service_config.getKeyStorePath(),service_config.getKeyStorePassword(),null,null);
				}
			}catch (Exception e) {
				logger.error("Spark Https Setting Failed : ",e);
			}
			
			logger.info("ROUTER CLASS : {} " , service_config.getRouterClass());
			ServiceRouter router = (ServiceRouter) Class.forName(service_config.getRouterClass()).newInstance();
			router.regist();
			
			initialize = true;
		} catch (Exception e) {
			logger.error("SCIMServer INIT FAILED ",e);
		}
		return this;
    }

	private void loadScheduler() {
		try {
			SCIMSystemRepository system_repository = SCIMRepositoryManager.getInstance().getSystemRepository();	
			if(system_repository != null) {
				List<SCIMScheduler> scheduler_list = system_repository.getSchdulerAll();
				for (SCIMScheduler scimScheduler : scheduler_list) {
					logger.info("SCHEDULER INITIALIZE : {} ", scimScheduler);
					SCIMSchedulerManager.getInstance().addScheduler(scimScheduler);
				}
				SCIMSchedulerManager.getInstance().initialize();
			}
		} catch (Exception e) {
			logger.error("Scheduler INITIALIZE FAILED ",e);
		}
	}

	private void loadRepository(SparkConfiguration service_config) {
		logger.info("- REPOSITORY CONFIG    : {}", service_config.getRepositoryConfig());
		try {
			SCIMRepositoryManager.load(service_config.getRepositoryConfig()).initailze();		
		}catch (Exception e) {
			logger.error(e.getMessage() + " : {} ",service_config, e);
		}
	}
    //@Override
	public void start() throws ServiceException{
		logger.info("SCIMServer START PORT : {} =========================================", Spark.port());
		if(initialize) {
			Spark.awaitInitialization();
		}else {
			throw new ServiceException("SERVICE NOT INITIALIZED");
		}
		
    }
   // @Override
	public void stop() throws Exception{
    	logger.info("SCIMServer STOP");
		Spark.stop();
    }
    //@Override
	public void destroy() {
    	logger.info("SCIMServer DESTORY START");
    	Spark.stop();
    	Spark.awaitStop();
    	logger.info("SCIMServer DESTORY FINISH");
    }
}
