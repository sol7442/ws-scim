package com.wowsanta.scim.scheduler;

import java.io.File;

import org.junit.Test;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;

import com.wowsanta.scim.SCIMSystemManager;
import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.obj.SCIMSystem;
import com.wowsanta.scim.resource.SCIMRepositoryManager;
import com.wowsanta.scim.resource.SCIMSystemRepository;

public class SchedulerTest {

	//private final String config_file = "../config/scim_scheduler.json";

	private final String config_file = "../config/home_dev_scim-service-provider.json";
	
	//@Test
	public void run_scheduler_manager_config_test() {
		try {

			SCIMSystemManager.getInstance().load(config_file);
			SCIMRepositoryManager.getInstance().initailze();
			SCIMSystemRepository system_repository = SCIMRepositoryManager.getInstance().getSystemRepository();

			for (SCIMSystem system : system_repository.getSystemAll()) {
				System.out.println(system.toString(true));
			}
			
			
//			SCIMSchedulerManager scheduler_mgr = SCIMSchedulerManager.getInstance();
//			scheduler_mgr.load(config_file);
//			//scheduler_mgr.load(config_file);
//			Thread.sleep(1000 * 60 * 3);
//			scheduler_mgr.close();
//			
//			Thread.sleep(1000 * 30 );
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	// @Test
	public void create_scheduler_manager_config_test() {
//		SCIMScheduler scheduler1 = new SCIMScheduler();
//
//		scheduler1.setName("TestScheduler1");
//		scheduler1.setDescription("Scheduler 1 Test");
//
//		SCIMDayTrigger trigger = new SCIMDayTrigger();
//		trigger.setHour(15);
//		trigger.setMinute(10);
//		scheduler1.setTrigger(trigger);
//
//		SampleSchedulerJob job = new SampleSchedulerJob();
//		scheduler1.setJob(job);
//
//		scheduler1.addSubJob(SampleSubJob1.class.getCanonicalName());
//		scheduler1.addSubJob(SampleSubJob2.class.getCanonicalName());
//
//		SCIMScheduler scheduler2 = new SCIMScheduler();
//
//		scheduler2.setName("TestScheduler2");
//		scheduler2.setDescription("Scheduler 2 Test");
//
//		SCIMDayTrigger trigger2 = new SCIMDayTrigger();
//		trigger2.setHour(15);
//		trigger2.setMinute(10);
//		scheduler2.setTrigger(trigger2);
//
//		SampleSchedulerJob job2 = new SampleSchedulerJob();
//		scheduler2.setJob(job2);
//
//		scheduler2.addSubJob(SampleSubJob1.class.getCanonicalName());
//		scheduler2.addSubJob(SampleSubJob2.class.getCanonicalName());
//
//		SCIMSchedulerManager scheduler_mgr = SCIMSchedulerManager.getInstance();
//		scheduler_mgr.addScheduler(scheduler1);
//		scheduler_mgr.addScheduler(scheduler2);
//
//		try {
//			scheduler_mgr.save(config_file);
//		} catch (SCIMException e) {
//			e.printStackTrace();
//		}
	}

	// @Test
	public SCIMScheduler load_scheduler_config_test() {
		SCIMScheduler scheduler = null;
		try {
			scheduler = (SCIMScheduler) SCIMScheduler.load(new File(config_file));
			System.out.println(scheduler.toString(true));
		} catch (SCIMException e) {
			e.printStackTrace();
		}
		return scheduler;
	}

	// @Test
	public void run_scheduler_test() {
		System.out.println("start test =======");
		try {
			SCIMScheduler scheduler = load_scheduler_config_test();

			Scheduler detailScheduler = scheduler.build();

			Thread.sleep(1000 * 60 * 2);

			try {
				detailScheduler.shutdown();
			} catch (SchedulerException e) {
				e.printStackTrace();
			}

		} catch (SCIMException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("end test =========");
	}
}
