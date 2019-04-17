package com.wowsanta.scim.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.quartz.JobExecutionException;

import com.ehyundai.gw.ConciliationJob_GW;
import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.repository.AbstractSCIMRepository;
import com.wowsanta.scim.repository.SCIMRepositoryManager;
import com.wowsanta.scim.repository.SCIMResourceRepository;

public class ReconciliationTest extends ServiceProviderTest {

	
	@Test
	public void reconciliation_test() {
		System.out.println("------------------------");
		
		
		try {
			SCIMResourceRepository res_repo = SCIMRepositoryManager.getInstance().getResourceRepository();
			((AbstractSCIMRepository)res_repo).initialize();
		} catch (SCIMException e1) {
			// TODO Auto-generated catch block
		}
		
		ConciliationJob_GW recon_gw = new ConciliationJob_GW();
		try {
			recon_gw.execute(null);
		} catch (JobExecutionException e) {
			e.printStackTrace();
		}
		
		System.out.println("------------------------");
	}

	
	
	
}
