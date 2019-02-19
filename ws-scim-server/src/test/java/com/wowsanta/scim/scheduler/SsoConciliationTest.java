package com.wowsanta.scim.scheduler;

import java.util.List;

import org.junit.Test;

import com.ehyundai.gw.ReconciliationJob_GW;
import com.ehyundai.gw.ReconciliationJob_GW_All;
import com.ehyundai.sso.ConsiliationJob_SSO;
import com.wowsanta.scim.SCIMSystemManager;
import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.obj.SCIMUser;
import com.wowsanta.scim.resource.SCIMRepositoryManager;

public class SsoConciliationTest {

	private final String config_file = "../config/office_dev_sso_scim-service-provider.json";

	
	@Test
	public void run_conciliation_job_all_test() {
		System.out.println(">>>[]run_conciliation_job_all_test>>>>>>>>>>>>");
		load_manager(this.config_file);
		try {
			SCIMRepositoryManager.getInstance().initailze();
			SCIMSystemManager.getInstance().loadSchdulerManager();
			
			String schedulerId = "sch-con-sso";
			SCIMScheduler scheduler = SCIMSchedulerManager.getInstance().getScheduler(schedulerId);
			List<SCIMScheduler> sheduler_list =  SCIMSchedulerManager.getInstance().getSchedulerList();
			System.out.println(">>> ("+sheduler_list.size()+")" + scheduler);
		
			SCIMUser test_user = new SCIMUser();
			test_user.setId("sys-scim-tester");
			test_user.setUserName("SYSTEM-TESTER");
			
			
			ConsiliationJob_SSO job =  (ConsiliationJob_SSO) Class.forName(scheduler.getJobClass()).newInstance();
			job.doExecute(scheduler, test_user);
			
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
