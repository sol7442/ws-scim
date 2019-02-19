package com.wowsanta.scim.scheduler;

import org.junit.Test;

import com.ehyundai.gw.ReconciliationJob_GW;
import com.ehyundai.gw.ReconciliationJob_GW_All;
import com.wowsanta.scim.SCIMSystemManager;
import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.obj.SCIMUser;
import com.wowsanta.scim.resource.SCIMRepositoryManager;

public class GwReconciliationTest {

	private final String config_file = "../config/home_dev_gw_scim-service-provider.json";

	
	//@Test
	public void run_reconciliation_job_all_test() {
		System.out.println(">>>[]run_reconciliation_job_all_test>>>>>>>>>>>>");
		load_manager(this.config_file);
		try {
			SCIMRepositoryManager.getInstance().initailze();
			SCIMSystemManager.getInstance().loadSchdulerManager();
			
			SCIMScheduler sheduler = SCIMSchedulerManager.getInstance().getSchedulerList().get(0);
			System.out.println(sheduler);
		
			SCIMUser test_user = new SCIMUser();
			test_user.setId("sys-scim-tester");
			test_user.setUserName("SYSTEM-TESTER");
			
			ReconciliationJob_GW_All job = new ReconciliationJob_GW_All();
			job.doExecute(sheduler, test_user);
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void run_reconciliation_job_test() {
		System.out.println(">>>[]run_reconciliation_job_test>>>>>>>>>>>>");
		load_manager(this.config_file);
		try {
			SCIMRepositoryManager.getInstance().initailze();
			SCIMSystemManager.getInstance().loadSchdulerManager();
			
			SCIMScheduler sheduler = SCIMSchedulerManager.getInstance().getSchedulerList().get(0);
			System.out.println(sheduler);
		
			SCIMUser test_user = new SCIMUser();
			test_user.setId("sys-scim-tester");
			test_user.setUserName("SYSTEM-TESTER");
			
			ReconciliationJob_GW job = new ReconciliationJob_GW();
			job.doExecute(sheduler, test_user);
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void load_manager(String config_file) {
		try {
			SCIMSystemManager.getInstance().load(config_file);
			SCIMSystemManager.getInstance().loadRepositoryManager();
			SCIMRepositoryManager.getInstance().initailze();
			
			SCIMSystemManager.getInstance().loadSchdulerManager();
			
		} catch (SCIMException e) {
			e.printStackTrace();
		}
	}
}
