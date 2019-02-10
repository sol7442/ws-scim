package com.wowsanta.scim.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.quartz.JobExecutionException;

import com.ehyundai.gw.ReconciliationJob_GW;
import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.resource.SCIMRepository;
import com.wowsanta.scim.resource.SCIMRepositoryManager;
import com.wowsanta.scim.resource.SCIMResourceRepository;

public class ReconciliationTest extends ServiceProviderTest {

	
	@Test
	public void reconciliation_test() {
		System.out.println("------------------------");
		
		
		try {
			SCIMResourceRepository res_repo = SCIMRepositoryManager.getInstance().getResourceRepository();
			((SCIMRepository)res_repo).initialize();
		} catch (SCIMException e1) {
			// TODO Auto-generated catch block
		}
		
		ReconciliationJob_GW recon_gw = new ReconciliationJob_GW();
		try {
			recon_gw.execute(null);
		} catch (JobExecutionException e) {
			e.printStackTrace();
		}
		
		System.out.println("------------------------");
	}

	
	
	
}
