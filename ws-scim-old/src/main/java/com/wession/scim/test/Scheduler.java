package com.wession.scim.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.wession.common.RESTClient;

import net.minidev.json.JSONObject;

public class Scheduler {
	private static boolean stopped;
	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(10);
	private volatile ArrayList<ScheduledFuture<?>> lstScheduleFuture = new ArrayList <ScheduledFuture<?>> ();
	
	public static void main(String [] args) throws InterruptedException {
		System.out.println("Start");
		Scheduler sc = new Scheduler();
		sc.goes();
	}
	
	public void goes() {
		try {
			process(0, 60*10, 0, TimeUnit.SECONDS, 0);
			//process(10, 60*2, 0, TimeUnit.SECONDS, 1);
			
		} catch (Exception e) {
			e.printStackTrace();
			stopped = true;
		}
	}
	
	private static boolean stopped() {
		return stopped;
	}
	
	public void process(int delay, int duration, int endtime, TimeUnit unit, final int index) {
		
		final Runnable proc = new Runnable() {
			public void run() {
				//delegate.run();
                
                try {
//                	Account ac = new Account();
//                	ac.main(null);
                	long startTime = System.currentTimeMillis();
                	

                	System.out.println("============================================================");
                	System.out.println("====    Sync with HR " + new Date() + " ====");
                	System.out.println("============================================================");

//                	DemoHRSCIM demo = new DemoHRSCIM();
//                	demo.Sync();
                	RESTClient client = new RESTClient();
                	System.out.println("RESTClient=="+client);
                	String command = "sync";
                	String URL = "http://localhost:6001/command/v1.1/" + command;
                	JSONObject hr_json = client.get(URL);
                	
                	System.out.println(hr_json.toJSONString());
                	
                	System.out.println("============================================================");
                	System.out.println("\t" + (System.currentTimeMillis() - startTime) + " ms - " + new Date());
                	System.out.println("============================================================");
                	
                	
                	startTime = System.currentTimeMillis();
                	
                	System.out.println("============================================================");
                	System.out.println("====     Sync with Groupware " + new Date() + " ====");
                	System.out.println("============================================================");
                	

                	client = new RESTClient();
                	command = "sync";
                	URL = "http://localhost:5002/command/v1.1/" + command;
                	JSONObject gw_json = client.get(URL);
                	
                	System.out.println(gw_json.toJSONString());
                	
                	System.out.println();
                	System.out.println("============================================================");
                	System.out.println("\t" + (System.currentTimeMillis() - startTime) + " ms - " + new Date());
                	System.out.println("============================================================");
                	
                	
                	startTime = System.currentTimeMillis();


                	System.out.println("============================================================");
                	System.out.println("====     Sync with KMS " + new Date() + " ====");
                	System.out.println("============================================================");

                	client = new RESTClient();
                	command = "sync";
                	URL = "http://localhost:5001/command/v1.1/" + command;
                	JSONObject ims = client.get(URL);
                	
                	System.out.println(ims.toJSONString());
                	
                	System.out.println("============================================================");
                	System.out.println("\t" + (System.currentTimeMillis() - startTime) + " ms - " + new Date());
                	System.out.println("============================================================");
                	
                } catch (Exception e) {
                	e.printStackTrace();
                }
                System.out.println("====================[" + index + "] processing " + new Date() + "====================="); 
                
		        if(stopped() == true) {
		            boolean interrupted = false;
		            try {
		                System.out.println("Stoped Process " + index);
		                lstScheduleFuture.get(index).cancel(true);
		            } finally {
		                if(interrupted) {
		                    Thread.currentThread().interrupt();
		                }
		            }
		        }
				
			}
		};
		lstScheduleFuture.add(index, scheduler.scheduleAtFixedRate(proc, delay, duration, unit));
	}
}
