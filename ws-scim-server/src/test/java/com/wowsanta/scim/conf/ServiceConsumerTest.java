package com.wowsanta.scim.conf;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;

import com.wowsanta.scim.resource.Target;
import com.wowsanta.scim.server.ServiceConsumer;


public class ServiceConsumerTest {
	private final String config_file_name = "../config/service-consumer.json";
	
	@Test
	public void make() {
		ServiceConsumer targets = new ServiceConsumer();
		Target t1 = new Target();
		t1.setName("GroupWare");
		t1.setName("GW");
		
		Target t2 = new Target();
		t2.setName("업무포탈");
		t2.setCode("PT");
		Target t3 = new Target();
		t3.setName("재경시스템");
		t3.setCode("JLK");
		Target t31 = new Target();
		t31.setName("재정시스템");
		t31.setCode("JK1");
		Target t32 = new Target();
		t32.setName("경영시스템");
		t32.setCode("JK2");
		
		t3.putSubTarget(t31);
		t3.putSubTarget(t32);
		
		targets.putTarget(t1);
		targets.putTarget(t2);
		targets.putTarget(t3);
		try {
			System.out.println(targets);
			targets.save(this.config_file_name);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//@Test
	public void load() {
		try {
			ServiceConsumer targets = ServiceConsumer.load(this.config_file_name);
			System.out.println(targets);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
