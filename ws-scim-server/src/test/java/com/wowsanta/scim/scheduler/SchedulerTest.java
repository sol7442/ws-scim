package com.wowsanta.scim.scheduler;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.Test;

import com.wowsanta.scim.SCIMSystemManager;
import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.repository.RepositoryManagerTest;
import com.wowsanta.scim.repository.SCIMRepositoryManager;
import com.wowsanta.scim.schema.SCIMDefinitions;
public class SchedulerTest extends RepositoryManagerTest{

	private final String config_file = "../config/home_dev_scim-service-provider.json";
	
	//@Test
	public void schduler_buil_test() {
		try {
			load_schduler(config_file, SampleSchedulerJob.class.getCanonicalName());
			SCIMSchedulerManager.getInstance().initialize();
			
			int count = SCIMSchedulerManager.getInstance().getSchedulerList().size();
			
			Thread.sleep(1000 * 60 * (count + 1));

			SCIMSchedulerManager.getInstance().close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void load_schduler(String config_file_path, String className) {
		load_repository_manager(config_file_path);
		try {
			SCIMRepositoryManager.getInstance().initailze();
			SCIMSystemManager.getInstance().loadSchdulerManager();
			
			
			Date nowDate = new Date();   		// given date
			Calendar calendar = GregorianCalendar.getInstance(); 
			calendar.setTime(nowDate);   		 
			
			int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY); 
			int minuteOfHour = calendar.get(Calendar.MINUTE);       
			
			// set sample job
			List<SCIMScheduler> schudler_list = SCIMSchedulerManager.getInstance().getSchedulerList();
			for (SCIMScheduler scimScheduler : schudler_list) {
				scimScheduler.setJobClass(className);
				scimScheduler.setTriggerType(SCIMDefinitions.TriggerType.DAY.toString());
				
				minuteOfHour++;
				if(minuteOfHour > 59) {
					minuteOfHour = 0;
					hourOfDay++;
				}
				if(hourOfDay > 23) {
					hourOfDay = 0;
				}
				
				scimScheduler.setHourOfDay(hourOfDay);
				scimScheduler.setMinuteOfHour(minuteOfHour);
				
				System.out.println(scimScheduler.toString(true));
			}
			
		} catch (SCIMException e) {
		
			e.printStackTrace();
		}
	}
	
	//public
//	SCIMSystemManager.getInstance().load(config_file);
//	SCIMRepositoryManager.getInstance().initailze();
//	SCIMSystemRepository system_repository = SCIMRepositoryManager.getInstance().getSystemRepository();
//
//	for (SCIMSystem system : system_repository.getSystemAll()) {
//		System.out.println(system.toString(true));
//	}
	
}
