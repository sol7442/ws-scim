package com.ehyundai.scheduler;

import java.io.File;
import java.util.List;

import javax.swing.plaf.synth.SynthScrollBarUI;

import org.junit.Test;

import com.ehyundai.im.ResourceSyncJob_Groupware;
import com.wowsanta.scim.SCIMSystemManager;
import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.repository.SCIMRepositoryManager;
import com.wowsanta.scim.repository.system.SCIMProviderRepository;
import com.wowsanta.scim.resource.worker.Worker;
import com.wowsanta.scim.scheduler.SCIMJob;
import com.wowsanta.scim.scheduler.SCIMScheduler;

public class ResourceSyncJob_SingleSignOn_Test {

	private final String repository_config_file = "../config/ehyundai_im_oracle_repository.json";
	
	private final String test_scheduler_id = "sch-sync-sso";
	
	@Test
	public void run_test() {
		load_manager(this.repository_config_file);
		try {
			SCIMProviderRepository system_repository = (SCIMProviderRepository) SCIMRepositoryManager.getInstance().getSystemRepository();
			SCIMScheduler scheduler  = system_repository.getSchdulerById(test_scheduler_id);
			
			Worker worker = new Worker();
			worker.setWorkerId("tester");
			worker.setWorkerType("TESTER");
			
			System.out.println(scheduler);
			try {
				SCIMJob job_class = (SCIMJob)Class.forName(scheduler.getJobClass()).newInstance();
				
				Object result =  job_class.run(scheduler, worker);
				
				System.out.println(result);
				
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
				e.printStackTrace();
			}
			//ResourceSyncJob_Groupware job = new ResourceSyncJob_Groupware();
			//Object result = job.run(scheduler, woker);
			//System.out.println(result);
			
		} catch (SCIMException e) {
			e.printStackTrace();
		}
	}
	
	//@Test
	public void load_from_db_test() {
		load_manager(this.repository_config_file);
		try {
			SCIMProviderRepository system_repository = (SCIMProviderRepository) SCIMRepositoryManager.getInstance().getSystemRepository();
			List<SCIMScheduler> scheduler_list = system_repository.getSchdulerAll();
			for (SCIMScheduler scimScheduler : scheduler_list) {
				System.out.println("scheduler : " + scimScheduler);
			}
			
		} catch (SCIMException e) {
			e.printStackTrace();
		}
		//ResourceSyncJob_Groupware job = new ResourceSyncJob_Groupware();
		
		
	}
	
	public void load_manager(String conf_file_path) {
		try {
			File config_file = new File(conf_file_path);
			SCIMSystemManager.getInstance().loadRepositoryManager(config_file);
			SCIMRepositoryManager.getInstance().initailze();
		} catch (SCIMException e) {
			e.printStackTrace();
		}
	}
}
