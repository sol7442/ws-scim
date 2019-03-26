package com.wowsanta.scim.scheduler;

import org.junit.Test;

import com.ehyundai.gw.ConciliationJob_GW;
import com.ehyundai.gw.ConciliationJob_GW;
import com.ehyundai.im.ProvisioningJob_SSO;
import com.wowsanta.scim.SCIMSystemManager;
import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.obj.SCIMUser;
import com.wowsanta.scim.repository.SCIMRepositoryManager;

public class IMProvisioningSsoTest {

	private final String config_file = "../config/home_dev_scim-service-provider.json";

	

	@Test
	public void run_provisionig_sso_job_test() {
		System.out.println(">>>[]run_provisioning_job_test>>>>>>>>>>>>");
		load_manager(this.config_file);
		try {
			SCIMRepositoryManager.getInstance().initailze();
			SCIMSystemManager.getInstance().loadSchdulerManager();
			
			String schedulerId = "sch-pro-sso";
			SCIMScheduler sheduler = SCIMSchedulerManager.getInstance().getScheduler(schedulerId);
			System.out.println(sheduler);
		
			SCIMUser test_user = new SCIMUser();
			test_user.setId("sys-scim-tester");
			test_user.setUserName("SYSTEM-TESTER");
			
//			ProvisioningJob_SSO job = new ProvisioningJob_SSO();
//			job.doExecute(sheduler, test_user);
			
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
